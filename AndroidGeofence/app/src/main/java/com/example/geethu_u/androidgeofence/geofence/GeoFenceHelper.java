package com.example.geethu_u.androidgeofence.geofence;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;

/**
 * Created by Geethu_U on 11/13/2015.
 */
public class GeoFenceHelper {

    public GeofencingRequest getGeofencingRequest(ArrayList<Geofence> mCurrentGeofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mCurrentGeofences);
        return builder.build();
    }
    public PendingIntent getGeofencePendingIntent(Activity mActivity, PendingIntent mGeofencePendingIntent) {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mActivity, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(mActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public String getGeoFenceIds( ArrayList<Geofence> mCurrentGeofences){
        String geoFenceRequestIds="";
        for(Geofence geofence:mCurrentGeofences){
            geoFenceRequestIds+=geofence.getRequestId();
        }
        return geoFenceRequestIds;
    }


}
