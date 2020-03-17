/**
 * Broadcast Receiver to be triggered when users enter or exit a geofence
 */

package edu.wwu.csci412.SolveAndSnooze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private AlarmLocation alarmLocation;

    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;
        }

        alarmLocation = AlarmLocation.getInstance(context);

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Get the geofences that were triggered (A single event can trigger multiple geofences)
        List<Geofence> triggeringGeofences;

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            alarmLocation.updateActiveAlarms(triggeringGeofences, "true");
            System.out.println("ENTERED!");

        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            alarmLocation.updateActiveAlarms(triggeringGeofences, "false");
            System.out.println("EXITED!");

        }
    }
}
