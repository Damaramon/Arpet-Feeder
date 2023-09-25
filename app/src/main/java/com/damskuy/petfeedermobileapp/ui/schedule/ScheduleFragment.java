package com.damskuy.petfeedermobileapp.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.Schedule;
import com.damskuy.petfeedermobileapp.ui.feed.ScheduledFeedActivity;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel scheduleViewModel;
    private View fragmentView;
    private TextView txtFetchingSchedule;
    private RecyclerView scheduleRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false);
        bindViewModel();
        initUI();
        observeLiveData();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        scheduleViewModel.fetchSchedule("592f5ec4-c3dd-4d5c-93fe-4ee3db513ad7");
    }

    private void bindViewModel() {
        ScheduleViewModelFactory factory = new ScheduleViewModelFactory(requireActivity());
        scheduleViewModel = new ViewModelProvider(this, factory).get(ScheduleViewModel.class);
    }

    private void initUI() {
        scheduleRecyclerView = fragmentView.findViewById(R.id.rv_schedule);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtFetchingSchedule = fragmentView.findViewById(R.id.txt_fetching_device_schedule);
        updateScheduleRecyclerView(new ArrayList<>());
    }

    private void observeLiveData() {
        scheduleViewModel.getFetchScheduleResult().observe(getViewLifecycleOwner(), scheduleResult -> {
            if (scheduleResult instanceof Result.Success) {
                ArrayList<Schedule> schedules = ((Result.Success<ArrayList<Schedule>>)scheduleResult).getData();
                scheduleViewModel.saveScheduleToCache(schedules);
                updateScheduleRecyclerView(schedules);
                txtFetchingSchedule.setVisibility(View.GONE);
            } else {
                String error = ((Result.Error<ArrayList<Schedule>>)scheduleResult).getErrorMessage();
                txtFetchingSchedule.setText(error);
            }
        });
    }

    private void updateScheduleRecyclerView(ArrayList<Schedule> schedules) {
        ScheduleListAdapter scheduleListAdapter = new ScheduleListAdapter(schedules, getActivity());
        scheduleRecyclerView.setAdapter(scheduleListAdapter);
    }
}
