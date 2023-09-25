package com.damskuy.petfeedermobileapp.ui.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Schedule;

import java.util.ArrayList;
import java.util.Locale;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleHolder> {

    private final ArrayList<Schedule> schedules;
    private final Context context;

    public ScheduleListAdapter(ArrayList<Schedule> schedules, Context context) {
        this.schedules = schedules;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_schedule, parent, false);
        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.txtTime.setText(schedule.getScheduledTime());
        holder.txtDayOfWeek.setText(schedule.getDay());
        holder.txtFeedAmount.setText(String.format(Locale.US, "%d Rounds", schedule.getFeed().getFeedAmount()));
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public static class ScheduleHolder extends RecyclerView.ViewHolder {

        private final TextView txtTime;
        private final TextView txtDayOfWeek;
        private final TextView txtFeedAmount;

        public ScheduleHolder(@NonNull View holderView) {
            super(holderView);
            txtTime = holderView.findViewById(R.id.txt_schedule_time);
            txtDayOfWeek = holderView.findViewById(R.id.txt_schedule_day_of_week);
            txtFeedAmount = holderView.findViewById(R.id.txt_schedule_feed_amount);
        }
    }
}
