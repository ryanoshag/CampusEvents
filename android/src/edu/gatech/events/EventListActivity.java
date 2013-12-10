package edu.gatech.events;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends ListActivity {
    List<Event> events;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String location = getIntent().getStringExtra("location");
        setTitle(location + "Events");
        new WebserviceManager().execute("http://wesley-crusher.firba1.com:8080/api/v1.0/location/geteventbylocation/" + location.replaceAll(" ", "%20"));
    }

    private class WebserviceManager extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            try{
                return downloadUrl(params[0]);//will make a web service call
            }
            catch(IOException e){
                Log.d("Webservice-connect", "downloadUrl failed" + e.getMessage());
            }

            return "";
        }

        String webServiceResults;

        /**
         * Overrides the onPostExecute function for AsyncTask so that we can populate the list after
         * the web service call returns. Executes after the doInBackground function executes.
         */
        @Override
        protected void onPostExecute(String result){
            events = new ArrayList<Event>();
            webServiceResults = result;
            try {
                JSONObject json = new JSONObject(result);
                JSONArray eventArray = json.getJSONArray("events");
                for(int i=0;i<eventArray.length();i++){
                    JSONObject temp = (JSONObject) eventArray.get(i);
                    Event tempEvent = new Event();
                    tempEvent.title = temp.getString("event_name");
                    tempEvent.time = temp.getString("start_date");
                    tempEvent.location = temp.getString("building");
                    //TODO time and date for time.
                    events.add(tempEvent);
                }
            } catch (JSONException e) {
                Log.e("Geofence", "fail" , e);
            }
            setListAdapter(new EventListAdapter(EventListActivity.this, events));
        }

        /**
         * Downloads the contents of a url with an http request.
         * @param url the string representation of the url to
         * @return
         * @throws IOException
         */
        public String downloadUrl(String url) throws IOException {
            InputStream inStream = null;
            try {
                URL downloadUrl = new URL(url);
                HttpURLConnection connect = (HttpURLConnection)downloadUrl.openConnection();
                connect.setReadTimeout(10000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("GET");

                Log.d("Webservice-connect","connected");
                inStream = connect.getInputStream();
                Log.d("Webservice-connect","inStream:"+inStream.toString());
                Reader read = new InputStreamReader(inStream, "UTF-8");
                char [] buffers = new char[2000];
                read.read(buffers);
                Log.d("webservice-connect",buffers.toString());
                return new String(buffers);
            }
            catch(Exception e) {
                Log.d("webservice-connect-error",e.getMessage());
            }
            finally {
                if(inStream != null) {
                    inStream.close();
                }
            }
            return "";
        }
    }
}