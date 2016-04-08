package com.example.geethu_u.androidgeofence.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geethu_u.androidgeofence.R;
import com.example.geethu_u.androidgeofence.adapter.ViewGeofenceAdapter;
import com.example.geethu_u.androidgeofence.geofence.GeofenceUtils;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;

/**
 * Created by Geethu_U on 1/14/2016.
 */
public class ViewGeofencesFragment extends Fragment{
    private RecyclerView geofenceslist;
    private ArrayList<Geofence> mGeofenceList;
    GeofenceUtils util ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.geofencelist, container, false);
        geofenceslist = (RecyclerView) v.findViewById(R.id.geofences_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        geofenceslist.setLayoutManager(linearLayoutManager);
        util = new GeofenceUtils();
        if(util.getGeofenceUIList()!=null) {
            ViewGeofenceAdapter adapter = new ViewGeofenceAdapter(util.getGeofenceUIList().geofences, getActivity());
            geofenceslist.setAdapter(adapter);
        }
        return v;
    }
}
