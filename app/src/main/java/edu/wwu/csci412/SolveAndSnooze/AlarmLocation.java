package edu.wwu.csci412.SolveAndSnooze;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class AlarmLocation {

    private static AlarmLocation instance;

    private static final int GEOFENCE_RADIUS = 100; // Radius for geofence in meters

    private GeofencingClient gfClient;
    private List<Geofence> gfList;
    private PendingIntent geofencePendingIntent;
    private LocationProvider locationProvider;
    private LocationManager locationManager;
    private LocationListener listener;
    private Location currentLocation;
    private Context context;
    DatabaseManager db;

    public static AlarmLocation getInstance(Context context) {
        if (instance == null) {
            instance = new AlarmLocation(context);
        }


        return instance;
    }

    private AlarmLocation(Context context) {

        this.context = context;
        db = new DatabaseManager(context);

        // Set up geofence client
        if (gfClient == null) {
            gfClient = LocationServices.getGeofencingClient(context);
        }

        if (gfList == null) {
            gfList = new ArrayList<>();
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                // A new location update is received
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

    }

    public boolean locationServicesEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(gfList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    // create a geofence with given id (Should match the id of corresponding alarm)
    @SuppressLint("MissingPermission") // Will be checked in MainActivity
    public Geofence createGeofence(String id) {
        if (currentLocation == null) {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        Geofence gf = new Geofence.Builder()
                //Set ID
                .setRequestId(id)
                //Set expiration
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                // Get the users location and set the region from it
                .setCircularRegion(currentLocation.getLatitude(), currentLocation.getLongitude(), GEOFENCE_RADIUS)

                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                .build();


        gfList.add(gf);
        gfClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
        return gf;
    }

    // delete a geofence with given id
    public void deleteGeofence(String id) {
    }

    public void updateActiveAlarms(List<Geofence> triggeringGeofences, String inRange) {
        for (Geofence gf : triggeringGeofences) {
            //Update alarms in database
            db.updateInRangeById(Integer.parseInt(gf.getRequestId()), inRange);
            db.selectById(Integer.parseInt(gf.getRequestId())).alarmSetup(); //Enables or Cancels alarms since inRange will now be updated.
        }
    }
}
