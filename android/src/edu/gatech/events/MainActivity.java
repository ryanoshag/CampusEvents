package edu.gatech.events;

//import android.R;
import android.app.*;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    GoogleMap map;
    MapFragment mapFragment;
    AllEventsFragment eventsFragment;
    List<Location> buildings;
    List<Geofence> geofenceList;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        buildings = new ArrayList<Location>();

        geofenceList = new ArrayList<Geofence>();
        Geofence.Builder geofenceBuilder = new Geofence.Builder();
        geofenceBuilder.setRequestId("Phi Sigma Kappa");
        geofenceBuilder.setCircularRegion(33.777152, -84.391853, 200);
        geofenceBuilder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
        geofenceBuilder.setExpirationDuration(Geofence.NEVER_EXPIRE);
        geofenceList.add(geofenceBuilder.build());
        geofenceBuilder = new Geofence.Builder();
        geofenceBuilder.setRequestId("College of Computing");
        geofenceBuilder.setCircularRegion(33.777417, -84.397318, 200);
        geofenceBuilder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
        geofenceBuilder.setExpirationDuration(Geofence.NEVER_EXPIRE);
        geofenceList.add(geofenceBuilder.build());


        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.event_view_list, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft;
                switch (i) {
                    case 0:
                        ft = fm.beginTransaction();
                        ft.hide(mapFragment);
                        ft.show(eventsFragment);
                        ft.commit();
                        break;
                    case 1:
                        ft = fm.beginTransaction();
                        ft.hide(eventsFragment);
                        ft.show(mapFragment);
                        ft.commit();
                        break;
                    default:
                        break;
                }
                return true;
            }

        });

        eventsFragment = (AllEventsFragment) getFragmentManager().findFragmentById(R.id.events);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                intent.putExtra("location", marker.getTitle());
                startActivity(intent);
            }
        });

        new GetLocationsTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(result, this, 0);

            if (errorDialog != null) {
                //TODO display error dialog
            }
        }
    }
    
    /**
     * Adds an option menu to this activitiy.  I am going to use it 
     * eventually to allow the user to filter events by date.
     * 
     * The menu is stored under res/values/settings_menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.settings_menu, menu);//settings_menu has an xml file defining it.
    	return true;
    }
    
    /**
     * Handles which options from the option menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	//handles when the user selects an item from the option menu.
    	switch(item.getItemId()){
    	case R.id.settings:
    		//starts the settings activity because the user clicked on the filter results option
    		Intent settingIntent = new Intent(this,SettingsActivity.class);
    		startActivity(settingIntent);
    		return true;
    	default:
    		return true;
    	}
    }

    private class GetLocationsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            GeofenceHandler handler = new GeofenceHandler(MainActivity.this);
            Geofence.Builder geofenceBuilder;
            InputStream inStream = null;
            try {
                Log.d("Geofence", "getting locations");
                URL downloadUrl = new URL("http://wesley-crusher.firba1.com:8080/api/v1.0/location/getlocations");
                HttpURLConnection connect = (HttpURLConnection) downloadUrl.openConnection();
                connect.setReadTimeout(10000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("GET");

                Log.d("Geofence", "parsing locations");
                inStream = connect.getInputStream();
                Reader read = new InputStreamReader(inStream, "UTF-8");

                char [] buffers = new char[600];
                read.read(buffers);
                JSONObject json = new JSONObject(String.valueOf(buffers));
                Log.d("Geofence", json.toString());
                JSONArray eventArray = json.getJSONArray("locations");
                for (int i = 0; i < eventArray.length(); i++) {
                    buildings.add(new Location(eventArray.getString(i)));
                    Log.d("Geofence", buildings.get(i).toString());
                }
                Log.d("Geofence", "done parsing locations");
            } catch (Exception e) {
                Log.e("Geofence", "failed to download stuff", e);
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
            for (Location building : buildings) {
                geofenceBuilder = new Geofence.Builder();
                geofenceBuilder.setRequestId(building.name);
                try {
                    String encodedName = building.name.replaceAll(" ", "%20");
                    URL downloadUrl = new URL("http://wesley-crusher.firba1.com:8080/api/v1.0/location/nametocoordinates/" + encodedName);
                    HttpURLConnection connect = (HttpURLConnection) downloadUrl.openConnection();
                    connect.setReadTimeout(10000);
                    connect.setConnectTimeout(15000);
                    connect.setRequestMethod("GET");

                    inStream = connect.getInputStream();
                    Reader read = new InputStreamReader(inStream, "UTF-8");
                    char [] buffers = new char[600];
                    read.read(buffers);
                    JSONObject json = new JSONObject(String.valueOf(buffers));
                    Log.d("Geofence", json.toString());
                    building.latitude = json.getDouble("latitude");
                    building.longitude = json.getDouble("longitude");
                    geofenceBuilder.setCircularRegion(building.latitude, building.longitude, 200);
                    geofenceBuilder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
                    geofenceBuilder.setExpirationDuration(Geofence.NEVER_EXPIRE);
                    geofenceList.add(geofenceBuilder.build());
                } catch (Exception e) {
                    Log.e("Geofence", "failed to download stuff", e);
                } finally {
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
            handler.addGeofences(geofenceList);
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            for (Location building : buildings) {
                map.addMarker(new MarkerOptions().position(new LatLng(building.latitude, building.longitude)).title(building.name));
            }
        }
    }
}
