package edu.wwu.csci412.SolveAndSnooze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
//            String errorMessage = GeofenceStatusCodes.getErrorString(geofencingEvent.getErrorCode());
//            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Get the geofences that were triggered. A single event can trigger
        // multiple geofences.
        List<Geofence> triggeringGeofences;
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            //Update alarms in database


        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            //Update alarms in database


        } else {
                // Log the error.
    //            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
    //                    geofenceTransition));
            }
        }

}
