package edu.gatech.events;

import android.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class GeofenceIntentService extends IntentService {

    public static final String PREFS_NAME = "settings";

    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Geofence Service", "Handling Geofence transition");

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean notifWhenBusy = settings.getBoolean("notifWhenBusy", true);

        boolean isBusy = isBusy();

        if(notifWhenBusy && !isBusy) {
            if(LocationClient.hasError(intent)) {
                //TODO handle error
                return;
            }

            int transition = LocationClient.getGeofenceTransition(intent);
            List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
            InputStream inStream = null;
            Event event = null;
            try {
                URL downloadUrl = new URL("http://wesley-crusher.firba1.com:8080/api/v1.0/location/geteventsfornexthours/24");
                HttpURLConnection connect = (HttpURLConnection) downloadUrl.openConnection();
                connect.setReadTimeout(10000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("GET");

                inStream = connect.getInputStream();
                Reader read = new InputStreamReader(inStream, "UTF-8");
                char [] buffers = new char[600];
                read.read(buffers);
                JSONObject json = new JSONObject(String.valueOf(buffers));
                JSONArray eventArray = json.getJSONArray("events");
                JSONObject temp = (JSONObject) eventArray.get(0);
                event = new Event();
                event.title = temp.getString("event_name");
                event.time = temp.getString("start_date");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }

            Intent notificationIntent = new Intent(getApplicationContext(), EventDetailActivity.class);
            notificationIntent.putExtra("Event", event);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSmallIcon(R.drawable.stat_notify_chat);
            builder.setContentTitle(event.title);
            builder.setContentText(event.location);
            builder.setContentIntent(notificationPendingIntent);
            builder.setAutoCancel(true);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(event.location));
            Intent calendarIntent = null;
            calendarIntent = new Intent(Intent.ACTION_INSERT);
            calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
            calendarIntent.putExtra(CalendarContract.Events.TITLE, event.title);
            calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.description);
            calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.location);
            //TODO event date sent to calendar
            PendingIntent calendarPI = PendingIntent.getActivity(this, 0, calendarIntent, 0);
            builder.addAction(R.drawable.stat_notify_error, "Add to Calendar", calendarPI);
            Intent directionsIntent = null;
            try {
                directionsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.ENGLISH, "geo:0,0?q=%s", URLEncoder.encode(event.getAddress(), "UTF-8"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            PendingIntent directionsPI = PendingIntent.getActivity(this, 0, directionsIntent, 0);
            builder.addAction(R.drawable.stat_notify_chat, "Get Directions", directionsPI);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, builder.build());

        }
    }

    public boolean isBusy() {
        Uri uri = Uri.parse("content://com.android.calendar/events");

        String[] projection = new String[] {
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_LOCATION
        };

        String selection = "((" + CalendarContract.Events.DTSTART + " < ?) AND ("
                + CalendarContract.Events.DTEND + " > ?))";

        String[] selectionArgs = new String[] {String.valueOf(Calendar.getInstance().getTimeInMillis()),
                String.valueOf(Calendar.getInstance().getTimeInMillis())};

        Cursor cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs, null);

        return (cursor != null) && (cursor.getCount() >= 1);
    }
}
