package com.example.geethu_u.androidgeofence.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.geethu_u.androidgeofence.R;
import com.example.geethu_u.androidgeofence.geofence.GeofenceBean;
import com.example.geethu_u.androidgeofence.geofence.GeofenceRequester;
import com.example.geethu_u.androidgeofence.geofence.GeofenceUtils;

import java.util.List;

/**
 * Created by Geethu_U on 1/21/2016.
 */

public class ViewGeofenceAdapter extends RecyclerView.Adapter<ViewGeofenceAdapter.ViewHolder> {
    private final Activity activity;
    private final List<GeofenceBean.GeofenceObj> mGeofenceList;

    @Override
    public int getItemCount() {
        return mGeofenceList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ViewGeofenceAdapter(List<GeofenceBean.GeofenceObj> mGeofenceList, Activity activity) {
        this.mGeofenceList = mGeofenceList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View vi = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_geofence_view, null);
        holder = new ViewHolder(vi, new ViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                new GeofenceUtils().removeGeofence(activity, mGeofenceList.get(pos).id);
                new GeofenceRequester(activity,"remove").removeGeofence(mGeofenceList.get(pos).id);
                mGeofenceList.remove(pos);
                notifyDataSetChanged();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GeofenceBean.GeofenceObj item = mGeofenceList.get(position);
        holder.locationText.setText(item.id);
        holder.latitude.setText("Latitude: " + item.lat);
        holder.longitude.setText("Longitude: " + item.lon);
        holder.radius.setText("Radius: " + item.radius + "m");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView locationText;
        public TextView latitude;
        public TextView longitude;
        public TextView radius;
        public Button btnRemove;
        public ItemClickListener mListener;

        public interface ItemClickListener {
            void onItemClick(View view, int pos);
        }
        @Override
        public void onClick(View view) {
            if(view==btnRemove) {
                int itemPosition = getAdapterPosition();
                mListener.onItemClick(view, itemPosition);
            }
        }
        public ViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            locationText = (TextView) itemView.findViewById(R.id.locationText);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            radius = (TextView) itemView.findViewById(R.id.radius);
            btnRemove= (Button)itemView.findViewById(R.id.btnRemove);
            btnRemove.setOnClickListener(this);
            mListener = listener;
        }

    }
}
