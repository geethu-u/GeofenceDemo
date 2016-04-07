package com.example.geethu_u.androidgeofence;

import com.example.geethu_u.androidgeofence.beans.ActivityLog;
import com.example.geethu_u.androidgeofence.geofence.GeofenceBean;

import java.util.List;

/**
 * Created by Geethu_U on 1/21/2016.
 */
public class Singleton {

    private static Singleton singleton = new Singleton();
    private GeofenceBean bean;
    private List<ActivityLog> activityLogs;

    /* A private Constructor prevents any other
         * class from instantiating.
         */
    private Singleton() {
    }

    /* Static 'instance' method */
    public static Singleton getInstance() {
        return singleton;
    }

    public GeofenceBean getGeofences() {
        return bean;
    }

    public List<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public void setBean(GeofenceBean bean) {
        this.bean = bean;
    }
}
