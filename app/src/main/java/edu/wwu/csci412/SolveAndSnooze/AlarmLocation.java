/**
 * Singleton class to store logic for location and geofences
 */
package edu.wwu.csci412.SolveAndSnooze;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
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

    // logic to set up Geofences
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(gfList);
        return builder.build();
    }

    // logic to set up Geofences
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

        db.updateHasGfById(Integer.valueOf(id), "true");
        return gf;
    }

    // delete a geofence with given id
    public void deleteGeofence(String id) {
        db.updateHasGfById(Integer.valueOf(id), "false");

    }


    /**
     * Updates which alarms are active based on their location
     * @param triggeringGeofences geofences that user has entered/exited
     * @param inRange whether they are in range of triggeringGeofences
     */
    public void updateActiveAlarms(List<Geofence> triggeringGeofences, String inRange) {
        for (Geofence gf : triggeringGeofences) {
            //Update alarms in database
            db.updateInRangeById(Integer.parseInt(gf.getRequestId()), inRange);
            AlarmData alarm = db.selectById(Integer.parseInt(gf.getRequestId())); //Enables or Cancels alarms since inRange will now be updated.
            alarmSetup(alarm);
        }
    }

    public void alarmSetup(AlarmData alarmData)
    {
        String daysString = alarmData.getDays();
        String[] days = daysString.split(" ");

        DatabaseManager db = new DatabaseManager(context);

        for(int i = 0; i < days.length; i++)
        {
            if(days[i].equals("M"))
            {
                setAlarms(Calendar.MONDAY, true, alarmData);
            }
            else if (days[i].equals("T"))
            {
                setAlarms(Calendar.TUESDAY, true, alarmData);
            }
            else if (days[i].equals("W"))
            {
                setAlarms(Calendar.WEDNESDAY, true, alarmData);
            }
            else if (days[i].equals("Th"))
            {
                setAlarms(Calendar.THURSDAY, true, alarmData);
            }
            else if (days[i].equals("F"))
            {
                setAlarms(Calendar.FRIDAY, true, alarmData);
            }
            else if (days[i].equals("Sa"))
            {
                setAlarms(Calendar.SATURDAY, true, alarmData);
            }
            else if (days[i].equals("Su"))
            {
                setAlarms(Calendar.SUNDAY, true, alarmData);
            }
        }

        db.updateById(alarmData.getid(),
                alarmData.getHour(),
                alarmData.getMinutes(),
                alarmData.getAM_PM(),
                alarmData.getDays(),
                alarmData.getChallenges(),
                Boolean.toString(alarmData.getActive()),
                alarmData.isInRange(),
                alarmData.getHasGf(),
                alarmData.getMemEnabled(),
                alarmData.getMathEnabled(),
                alarmData.getTiltEnabled(),
                alarmData.getChallengesCompleted());
    }

    /**
     * set an alarm
     * @param dayOfWeek day alarm will sound
     * @param active whether the alarm is enabled and in range
     * @param alarmData information about an alarm
     */
    public void setAlarms(int dayOfWeek, boolean active, AlarmData alarmData)
    {
        Intent intent = new Intent(context, MemoryPuzzle.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmData.getid(), intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // enable alarm
        if(active)
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            if(alarmData.getAM_PM().equals("PM"))
            {
                cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour()+12);
            }
            else
            {
                cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour());
            }
            cal.set(Calendar.MINUTE,alarmData.getMinutes());
            cal.set(Calendar.SECOND, 0);

            //Check that day is not in the past. If so set for next same day of week.
            if(cal.getTimeInMillis() < System.currentTimeMillis())
            {
                cal.add(Calendar.DAY_OF_YEAR,7);
            }

            alarmData.setActive(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }
        else
        {
            //disable alarm
            alarmData.setActive(false);
            am.cancel(pendingIntent);
        }
    }
}
