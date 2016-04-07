package com.example.geethu_u.androidgeofence.geofence;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Geethu_U on 11/24/2015.
 */
public class Utility {
    public static String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("geofences.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
