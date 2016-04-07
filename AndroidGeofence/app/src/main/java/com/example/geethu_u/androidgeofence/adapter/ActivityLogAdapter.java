package com.example.geethu_u.androidgeofence.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geethu_u.androidgeofence.R;
import com.example.geethu_u.androidgeofence.beans.ActivityLog;

import java.util.List;

/**
 * Created by Geethu_U on 11/27/2015.
 */
public class ActivityLogAdapter extends  RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {
    private final List<ActivityLog> activities;

    public ActivityLogAdapter(List<ActivityLog> activities){
        this.activities= activities;
    }
    @Override
    public int getItemCount() {
        return activities.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View vi = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_activity_log_view, null);
        holder = new ViewHolder(vi);

        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            ActivityLog item = activities.get(position);
            holder.locationText.setText(item.getLocation());
            holder.date.setText(item.getDate());

            if ("Entered".equals(item.getTransitionType())) {
                holder.transition_img.setImageResource(R.drawable.enter);
            } else {
                holder.transition_img.setImageResource(R.drawable.exit);
            }
    }

        public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locationText;
        public TextView date;
        public ImageView transition_img;

        public ViewHolder(View itemView) {
            super(itemView);
            locationText=(TextView)itemView.findViewById(R.id.locationText);
            transition_img=(ImageView)itemView.findViewById(R.id.transition_img);
            date=(TextView) itemView.findViewById(R.id.date);
        }
    }
}
