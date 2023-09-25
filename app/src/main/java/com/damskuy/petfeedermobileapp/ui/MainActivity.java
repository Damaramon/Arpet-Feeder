package com.damskuy.petfeedermobileapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;
import com.damskuy.petfeedermobileapp.ui.feed.ScheduledFeedActivity;
import com.damskuy.petfeedermobileapp.ui.login.LoginActivity;
import com.damskuy.petfeedermobileapp.ui.schedule.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private ActionBar actionBar;
    private BottomNavigationView bottomNavigationView;
    private MainFragment.FragmentType activeFragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViewModel();
        initUI();
        initEventHandlers();
        observeLiveData();
    }

    private void bindViewModel() {
        ViewModelProvider mainViewModelProvider =
                new ViewModelProvider(this, new MainViewModelFactory(getApplication()));
        mainViewModel = mainViewModelProvider.get(MainViewModel.class);
    }

    private void initUI() {
        actionBar = getSupportActionBar();
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        mainViewModel.changeFragment(R.id.item_home);
    }

    private void initEventHandlers() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            mainViewModel.changeFragment(item.getItemId());
            return true;
        });
    }

    private void observeLiveData() {
       mainViewModel.getActiveFragment().observe(this, mainFragment -> {
           if (mainFragment.getFragmentType() == activeFragmentType) return;
           activeFragmentType = mainFragment.getFragmentType();
           replaceFragmentLayout(mainFragment.getFragment());
           actionBar.setTitle(mainFragment.getFragmentTitle());
           invalidateOptionsMenu();
       });
    }

    private void replaceFragmentLayout(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (activeFragmentType == MainFragment.FragmentType.SCHEDULE_FRAGMENT) {
            getMenuInflater().inflate(R.menu.top_schedule_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.top_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.user_profile) {
            Toast.makeText(this, "user profile clicked", Toast.LENGTH_SHORT).show();
        }
        if (itemId == R.id.logout) {
            mainViewModel.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if (itemId == R.id.add_schedule) {
            startActivity(new Intent(this, ScheduledFeedActivity.class));
        }
        return true;
    }
}