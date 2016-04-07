package com.example.geethu_u.androidgeofence.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;


public class LocationUpdater{

    static double currentLat;
    static double currentLon;

    private static Location cLocation;


    Context mContext;

    public LocationUpdater(Context mContext, Activity mActivity){
        this.mContext = mContext;

    }


    public boolean shouldUpdateLocation(Location nLocation){
        Log.i("cLocation", "" + cLocation);
        if (cLocation == null) {
            return true;
        } else if (getDistance(cLocation.getLatitude(),cLocation.getLongitude(),nLocation.getLatitude(),nLocation.getLongitude())>500){
            return true;
        }

        return false;

    }

    private double getDistance(double currentLat, double currentLon, double referenceLat, double referenceLon) {
        float[] distance = { 0 };
        Location.distanceBetween(currentLat, currentLon, referenceLat,
                referenceLon, distance);
        return (double) distance[0];
    }


    public static double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
        Log.i("LOCATION", "Current Latitude"+getCurrentLat());
    }

    public static double getCurrentLon() {
        return currentLon;
    }

    public void setCurrentLon(double currentLon) {
        this.currentLon = currentLon;
        Log.i("LOCATION", "Current Longitude" + getCurrentLon());
    }

    public static Location getcLocation() {
        return cLocation;
    }

    public static void setcLocation(Location cLocation) {
        LocationUpdater.cLocation = cLocation;
    }
}


