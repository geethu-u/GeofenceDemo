package com.example.geethu_u.androidgeofence.beans;

/**
 * Created by Geethu_U on 11/27/2015.
 */
public class ActivityLog {
    String transitionType;
    String location;
    String date;

    public ActivityLog(String transitionType, String location, String date) {
        this.transitionType = transitionType;
        this.location = location;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTransitionType() {
        return transitionType;
    }

    public String getLocation() {
        return location;
    }
}

