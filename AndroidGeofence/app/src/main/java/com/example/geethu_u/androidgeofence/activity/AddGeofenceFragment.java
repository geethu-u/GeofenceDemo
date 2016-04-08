/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.geethu_u.androidgeofence.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.geethu_u.androidgeofence.R;
import com.example.geethu_u.androidgeofence.geofence.GeofenceRequester;
import com.example.geethu_u.androidgeofence.geofence.GeofenceUtils;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;


public class AddGeofenceFragment extends Fragment implements View.OnClickListener {

    protected static final String TAG = "AddGeofenceFragment";

    protected ArrayList<Geofence> mGeofenceList;

    public Activity mBaseactivity;
    public LinearLayout mainLayout;

    GeofenceUtils utility;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseactivity = getActivity();
        utility = new GeofenceUtils();
        mGeofenceList = new ArrayList<Geofence>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_geofences, container, false);
        view.findViewById(R.id.addButton).setOnClickListener(this);
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                String latitude = ((EditText) mainLayout.findViewById(R.id.editTextLatitude)).getText().toString();
                String longitude = ((EditText) mainLayout.findViewById(R.id.editTextLongitude)).getText().toString();
                String radius = ((EditText) mainLayout.findViewById(R.id.editTextRadius)).getText().toString();
                String name = ((EditText) mainLayout.findViewById(R.id.editTextName)).getText().toString();
                if ("".equals(latitude) || "".equals(longitude) || "".equals(radius) || "".equals(name)) {
                    Toast.makeText(mBaseactivity,
                            mBaseactivity.getString(R.string.add_validation_failed),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Geofence geofence = utility.buildGeofence(name,
                            Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT,
                            Double.valueOf(latitude),
                            Double.valueOf(longitude),
                            Integer.valueOf(radius),
                            Geofence.NEVER_EXPIRE);
                    utility.saveGeofences(mBaseactivity, latitude, longitude, radius, name);
                    mGeofenceList.add(geofence);
                    new GeofenceRequester(mBaseactivity,"add").addGeofences(mGeofenceList);
                    mBaseactivity.findViewById(R.id.addLayout).setVisibility(View.GONE);
                    mBaseactivity.findViewById(R.id.activityLogLayout).setVisibility(View.VISIBLE);                }
        }
    }

}
