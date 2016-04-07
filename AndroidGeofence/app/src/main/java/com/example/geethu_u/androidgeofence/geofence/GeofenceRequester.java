package com.example.geethu_u.androidgeofence.geofence;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import static android.location.LocationManager.*;


/**
 * Class for connecting to Location Services and requesting geofences.
 * <b>
 * Note: Clients must ensure that Google Play services is available before requesting geofences.
 * </b> Use GooglePlayServicesUtil.isGooglePlayServicesAvailable() to check.
 *
 *
 * To use a GeofenceRequester, instantiate it and call AddGeofence(). Everything else is done
 * automatically.
 *
 */
public class GeofenceRequester implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    protected static final String TAG = "GeofenceRequester";

    // Storage for a reference to the calling client
    private final Activity mActivity;

    // Stores the PendingIntent used to send geofence transitions back to the app
    private PendingIntent mGeofencePendingIntent;

    // Stores the current list of geofences
    private ArrayList<Geofence> mCurrentGeofences;

    // Stores the current instantiation of the location client
    private GoogleApiClient googleApiClient;

    /*
     * Flag that indicates whether an add or remove request is underway. Check this
     * flag before attempting to start a new request.
     */
    private boolean mInProgress;
    private GeoFenceHelper helper;

    public GeofenceRequester(Activity activityContext) {
        // Save the context
        mActivity = activityContext;
        // Initialize the globals to null
        mGeofencePendingIntent = null;
        googleApiClient = null;
        mInProgress = false;
        helper = new GeoFenceHelper();
    }

    /**
     * Set the "in progress" flag from a caller. This allows callers to re-set a
     * request that failed but was later fixed.
     *
     * @param flag Turn the in progress flag on or off.
     */
    public void setInProgressFlag(boolean flag) {
        // Set the "In Progress" flag.
        mInProgress = flag;
    }

    /**
     * Get the current in progress status.
     *
     * @return The current value of the in progress flag.
     */
    public boolean getInProgressFlag() {
        return mInProgress;
    }

    /**
     * Start adding geofences. Save the geofences, then start adding them by requesting a
     * connection
     *
     * @param geofences A List of one or more geofences to add
     */
    public void addGeofences(List<Geofence> geofences) throws UnsupportedOperationException {
        /*
         * Save the geofences so that they can be sent to Location Services once the
         * connection is available.
         */
        mCurrentGeofences = (ArrayList<Geofence>) geofences;
        // If a request is not already in progress
        if (!mInProgress) {
            // Toggle the flag and continue
            mInProgress = true;
            // Request a connection to Location Services
            requestConnection();
            // If a request is in progress
        } else {
            // Throw an exception and stop the request
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Get the current location client, or create a new one if necessary.
     *
     * @return A LocationClient object
     */
    private GoogleApiClient getLocationClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(mActivity.getBaseContext()).addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            Log.d("GeoFenceRequester", " Initialized google api client");
        }
        return googleApiClient;
    }

    /**
     * Once the connection is available, send a request to add the Geofences
     */
    private void continueAddGeofences() {

        // Send a request to add the current geofences
        if (mCurrentGeofences != null && mCurrentGeofences.size() > 0) {
            LocationServices.GeofencingApi
                    .addGeofences(googleApiClient, helper.getGeofencingRequest(mCurrentGeofences),
                            helper.getGeofencePendingIntent(mActivity, mGeofencePendingIntent))
                    .setResultCallback(this);
        }
    }

    /**
     * Returns the current PendingIntent to the caller.
     *
     * @return The PendingIntent used to create the current set of geofences
     */
    public PendingIntent getRequestPendingIntent() {
        return helper.getGeofencePendingIntent(mActivity, mGeofencePendingIntent);
    }

    /**
     * Request a connection to Location Services. This call returns immediately,
     * but the request is not complete until onConnected() or onConnectionFailure() is called.
     */
    private void requestConnection() {
        getLocationClient().connect();
    }

    /**
     * Get a location client and disconnect from Location Services
     */
    private void requestDisconnection() {
        // A request is no longer in progress
        mInProgress = false;
        getLocationClient().disconnect();
    }

    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     *
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
//            if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return;
//            }

            ((LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

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
            });


        }


            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            // setButtonsEnabledState();

//            Toast.makeText(
//                    mActivity,mActivity. getString(R.string.geofences_added ),
//                    Toast.LENGTH_SHORT
//            ).show();
         else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(mActivity,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }

    /*
     * Called by Location Services once the location client is connected.
     *
     * Continue by adding the requested geofences.
     */
    @Override
    public void onConnected(Bundle arg0) {
        // If debugging, log the connection
        Log.d(TAG, "Connected");
        // Continue adding the geofences
        continueAddGeofences();
    }
    /*
     * Implementation of OnConnectionFailedListener.onConnectionFailed
     * If a connection or disconnection request fails, report the error
     * connectionResult is passed in from Location Services
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended");

        // onConnected() will be called again automatically when the service reconnects
    }

}
