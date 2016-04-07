package com.example.geethu_u.androidgeofence.geofence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geethu_U on 11/25/2015.
 */
public class GeofenceBean {
    public List<GeofenceObj> geofences = new ArrayList<GeofenceObj>() ;
    public class GeofenceObj{
        public String id;
        public String lat;
        public String lon;
        public String radius;
        public GeofenceObj(String id,String lat,String lon,String radius){
            this.id=id;
            this.lat=lat;
            this.lon=lon;
            this.radius=radius;

        }
    }
}
