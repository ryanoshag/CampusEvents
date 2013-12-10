package edu.gatech.events;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

public class GeofenceHandler implements LocationClient.OnAddGeofencesResultListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    LocationClient locationClient;

    PendingIntent geofenceServicePendingIntent;

    Activity activityContext;

    List<Geofence> geofences;

    public GeofenceHandler(Activity activityContext) {
        this.activityContext = activityContext;
    }

    public void addGeofences(List<Geofence> geofences) {
        Log.d("GeofenceHandler", "Starting geofence adding");

        this.geofences = geofences;

        getLocationClient().connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Geofence Handler", "Actually adding geofences");

        getLocationClient().addGeofences(geofences, getGeofenceServicePendingIntent(), this);

        getLocationClient().disconnect();
    }

    private PendingIntent getGeofenceServicePendingIntent() {
        if (geofenceServicePendingIntent != null) {
            return geofenceServicePendingIntent;
        }

        geofenceServicePendingIntent = PendingIntent.getService(activityContext, 0, new Intent(activityContext, GeofenceIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        return geofenceServicePendingIntent;
    }

    private LocationClient getLocationClient() {
        if (locationClient == null) {
            locationClient = new LocationClient(activityContext, this, this);
        }
        return locationClient;
    }

    @Override
    public void onDisconnected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
