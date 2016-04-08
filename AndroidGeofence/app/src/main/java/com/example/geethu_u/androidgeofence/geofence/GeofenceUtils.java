package com.example.geethu_u.androidgeofence.geofence;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geethu_U on 11/24/2015.
 */
public class GeofenceUtils {

    private SharedPreferences mSharedPreferences;
    private static GeofenceBean geofenceBean;

    public ArrayList<Geofence> populateGeofences(String geofenceJson){
        ArrayList<Geofence> geoList = new ArrayList<Geofence>();

        JSONArray jArray = null;
        try {
            JSONObject obj = new JSONObject(geofenceJson);
            jArray = obj.getJSONArray("geofences");
        } catch (JSONException e) {
        }

        if(jArray != null && jArray.length()>0){
            Geofence geoObj = null;
            for (int i=0; i<jArray.length(); i++){
                try {
                    JSONObject jObject = jArray.getJSONObject(i);
                    if(jObject.getInt("radius")!= 0){
                        geoObj=buildGeofence(jObject.getString("id"),
                                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT,
                                jObject.getDouble("lat"),
                                jObject.getDouble("lon"),
                                jObject.getInt("radius"),
                                Geofence.NEVER_EXPIRE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                if( geoObj!=null){
                    geoList.add(geoObj);
                }
            }
        }
        return geoList;
    }
    public Geofence buildGeofence(String id,int transitionType,double latitude, double longitude, int radius, long expiry){
        return new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(transitionType)
                .setCircularRegion(latitude,longitude,radius)
                .setExpirationDuration(expiry)
                .build();
    }
    public List<Geofence> getSavedGeofences(Activity activity){
        String savedGeofences = getmSharedPreferences(activity).getString("geofences", null);
        List<Geofence> geofences = new ArrayList<Geofence>();
        if(savedGeofences == null){
            geofences = populateGeofenceList(activity);
        }

        GeofenceBean bean = new Gson().fromJson(savedGeofences, GeofenceBean.class);
        this.geofenceBean = bean;
        if(bean !=null) {
            for (GeofenceBean.GeofenceObj obj : bean.geofences) {
                geofences.add(buildGeofence(obj.id,
                        Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT,
                        Double.valueOf(obj.lat), Double.valueOf(obj.lon), Integer.valueOf(obj.radius), Geofence.NEVER_EXPIRE));
            }
        }
        return geofences;
    }
    public List<Geofence> populateGeofenceList(Activity activity) {

        String savedGeofences = Utility.loadJSONFromAsset(activity);
        List<Geofence> mGeofenceList = new GeofenceUtils().populateGeofences(savedGeofences);
        SharedPreferences.Editor editor = getmSharedPreferences(activity).edit();
        editor.putString(Constants.SAVED_GEOFENCES, savedGeofences);
        editor.apply();
        return  mGeofenceList;
    }
    public void saveGeofences(Activity activity,String lat,String lon,String radius,String name){

        String savedGeofences = getmSharedPreferences(activity).getString("geofences", null);
        Gson gson = new Gson();
        GeofenceBean bean = gson.fromJson(savedGeofences, GeofenceBean.class);
        GeofenceBean.GeofenceObj obj = new GeofenceBean().new GeofenceObj(name ,lat,lon,radius);
        bean.geofences.add(obj);
        this.geofenceBean = bean;
        savedGeofences = gson.toJson(bean);
        mSharedPreferences.edit().putString(Constants.SAVED_GEOFENCES, savedGeofences).apply();
    }
    public void removeGeofence(Activity activity,String name){

        String savedGeofences = getmSharedPreferences(activity).getString(Constants.SAVED_GEOFENCES, null);
        Gson gson = new Gson();
        GeofenceBean bean = gson.fromJson(savedGeofences, GeofenceBean.class);
        for(int i=0; i<bean.geofences.size();i++){
            GeofenceBean.GeofenceObj obj = bean.geofences.get(i);
            if(obj.id.equals(name)){
                bean.geofences.remove(obj);
            }
        }
        this.geofenceBean = bean;
        savedGeofences = gson.toJson(bean);
        mSharedPreferences.edit().putString(Constants.SAVED_GEOFENCES, savedGeofences).apply();
    }
    public GeofenceBean getGeofenceUIList(){
        return geofenceBean;
    }
    public SharedPreferences getmSharedPreferences(Activity activity) {
        mSharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                activity.MODE_PRIVATE);
        return mSharedPreferences;
    }
}
