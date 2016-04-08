package com.example.geethu_u.androidgeofence.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.geethu_u.androidgeofence.R;
import com.example.geethu_u.androidgeofence.adapter.ActivityLogAdapter;
import com.example.geethu_u.androidgeofence.beans.ActivityLog;
import com.example.geethu_u.androidgeofence.geofence.Constants;
import com.example.geethu_u.androidgeofence.geofence.GeofenceRequester;
import com.example.geethu_u.androidgeofence.geofence.GeofenceUtils;
import com.example.geethu_u.androidgeofence.utils.FragmentsTags;
import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ADD_REQ = 1;
    private ActivityLogAdapter adapter;
    private SharedPreferences mSharedPreferences;
    private GeofenceEventReceiver mBroadcastReceiver;
    private List<ActivityLog> bean;
    private GeofenceRequester mGeofenceRequester;

    protected ArrayList<Geofence> mGeofenceList;
    private GeofenceUtils utils;
    private RecyclerView activityLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        utils = new GeofenceUtils();

        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        findViewById(R.id.addButton).setOnClickListener(this);
        findViewById(R.id.clearButton).setOnClickListener(this);
        findViewById(R.id.viewButton).setOnClickListener(this);

        activityLog = (RecyclerView) findViewById(R.id.activity_log_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityLog.setLayoutManager(linearLayoutManager);

        String savedActivityLog = mSharedPreferences.getString(Constants.SAVED_ACTIVITY_LOG, "");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ActivityLog>>() {}.getType();
        bean = (List<ActivityLog>) gson.fromJson(savedActivityLog, listType);
        if (bean != null) {
            setAdapter();
        }

        mGeofenceRequester = new GeofenceRequester(this,"add");
        mGeofenceList = (ArrayList<Geofence>) utils.getSavedGeofences(this);
        mGeofenceRequester.addGeofences(mGeofenceList);

        mBroadcastReceiver = new GeofenceEventReceiver();
        // Create an intent filter for the broadcast receiver
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.GEOFENCE_EVENT);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver, mIntentFilter);

    }

    public void setAdapter() {
        adapter = new ActivityLogAdapter(bean);
        activityLog.setAdapter(adapter);    }

    @Override
    protected void onResume() {
        super.onResume();

      registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.GEOFENCE_EVENT));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGeofenceRequester.requestDisconnection();
        unregisterReceiver(mBroadcastReceiver);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addButton:
                showFragment(0);
                break;
            case R.id.viewButton:
                showFragment(1);
                break;
            case R.id.clearButton:
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.SAVED_ACTIVITY_LOG, "");
                editor.apply();
                bean.clear();
                adapter.notifyDataSetChanged();
//                setAdapter();

        }
    }
    private void showFragment(int fragment) {
        Fragment fragmentView = null;
        int layout = 0;
        String tag= "";
        switch (fragment){
            case 0:
                fragmentView  = new AddGeofenceFragment();
                layout = R.id.addLayout;
                tag = FragmentsTags.ADD_SCREEN;
                break;
            case 1:
                fragmentView= new ViewGeofencesFragment();
                layout = R.id.viewLayout;
                tag = FragmentsTags.VIEW_SCREEN;
                break;
        }
        findViewById(layout).setVisibility(View.VISIBLE);
        findViewById(R.id.activityLogLayout).setVisibility(View.GONE);

       FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.add(layout, fragmentView, tag);
        fragmentTransaction.commit();
    }
    class GeofenceEventReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String savedActivityLog = mSharedPreferences.getString(Constants.SAVED_ACTIVITY_LOG, "");
            Type listType = new TypeToken<List<ActivityLog>>() {}.getType();
            bean=(List<ActivityLog>) new Gson().fromJson(savedActivityLog, listType);
            setAdapter();
        }
    }
}
