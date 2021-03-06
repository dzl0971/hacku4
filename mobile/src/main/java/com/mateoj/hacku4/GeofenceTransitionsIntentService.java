package com.mateoj.hacku4;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.mateoj.hacku4.events.EnterFence;
import com.mateoj.hacku4.events.ExitFence;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jose on 2/4/16.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    public static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    public GeofenceTransitionsIntentService() {
        super("geofenceservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "Geofence intent activated", Toast.LENGTH_SHORT).show();
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "" + geofencingEvent.getErrorCode();
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
//        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
//                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            EventBus.getDefault().post(new EnterFence(triggeringGeofences));

            // Get the transition details as a String.
            Log.d(TAG, "" + geofenceTransition);
//            String geofenceTransitionDetails = getGeofenceTransitionDetails(
//                    this,
//                    geofenceTransition,
//                    triggeringGeofences
//            );

            // Send notification and log the transition details.
//            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, "" + geofenceTransition);
        } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            EventBus.getDefault().post(new ExitFence(triggeringGeofences));

        } else {
            // Log the error.
            Log.e(TAG, "geofence error");
        }
    }
}