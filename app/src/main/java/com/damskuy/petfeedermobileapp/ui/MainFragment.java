package com.damskuy.petfeedermobileapp.ui;

import androidx.fragment.app.Fragment;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.ui.devices.DevicesFragment;
import com.damskuy.petfeedermobileapp.ui.history.HistoryFragment;
import com.damskuy.petfeedermobileapp.ui.home.HomeFragment;
import com.damskuy.petfeedermobileapp.ui.schedule.ScheduleFragment;

public class MainFragment {

    private final FragmentType fragmentType;
    private final Fragment fragment;
    private final String fragmentTitle;

    public enum FragmentType {
        HOME_FRAGMENT,
        SCHEDULE_FRAGMENT,
        HISTORY_FRAGMENT,
        DEVICE_FRAGMENT
    }

    public MainFragment(int fragmentIconId) {
        if (fragmentIconId == R.id.item_home) {
            fragment = new HomeFragment();
            fragmentType = FragmentType.HOME_FRAGMENT;
            fragmentTitle = "Home";
        } else if (fragmentIconId == R.id.item_schedule) {
            fragment = new ScheduleFragment();
            fragmentType = FragmentType.SCHEDULE_FRAGMENT;
            fragmentTitle = "My Schedules";
        }  else if (fragmentIconId == R.id.item_history) {
            fragment = new HistoryFragment();
            fragmentType = FragmentType.HISTORY_FRAGMENT;
            fragmentTitle = "Feed History";
        } else if (fragmentIconId == R.id.item_devices) {
            fragment = new DevicesFragment();
            fragmentType = FragmentType.DEVICE_FRAGMENT;
            fragmentTitle = "Add Devices";
        }  else {
            throw new IllegalArgumentException("Not a valid id");
        }
    }

    public FragmentType getFragmentType() {
        return fragmentType;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getFragmentTitle() {
        return fragmentTitle;
    }
}
