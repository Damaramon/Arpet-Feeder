package com.damskuy.petfeedermobileapp.ui.feed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Feed;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.Schedule;
import com.damskuy.petfeedermobileapp.utils.ViewUtils;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ScheduledFeedActivity extends AppCompatActivity {

    private NumberPicker npHour, npMinute, npAmPm, npServings;
    private Button btnCancel, btnSave;
    private RadioGroup rgDay;
    private String chosenDayAbv = "";
    private ScheduleFeedViewModel scheduleFeedViewModel;
    private SweetAlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_feed);
        bindViewModel();
        initUI();
        initHandlers();
        observeLiveData();
    }

    private void bindViewModel() {
        ViewModelProvider scheduleFeedViewModelProvider =
                new ViewModelProvider(this, new ScheduleFeedViewModelFactory(getApplication()));
        scheduleFeedViewModel = scheduleFeedViewModelProvider.get(ScheduleFeedViewModel.class);
    }

    private void initUI() {
        npHour = findViewById(R.id.np_hour_schedule);
        npMinute = findViewById(R.id.np_minute_schedule);
        npAmPm = findViewById(R.id.np_am_pm_schedule);
        npServings = findViewById(R.id.np_servings_schedule);
        btnCancel = findViewById(R.id.btn_cancel_schedule);
        btnSave = findViewById(R.id.btn_save_schedule);
        rgDay = findViewById(R.id.rg_day_schedule);
        numberPickerConfig();
    }

    private void numberPickerConfig() {
        npHour.setMinValue(1);
        npHour.setMaxValue(12);

        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);

        npAmPm.setMinValue(0);
        npAmPm.setMaxValue(1);
        npAmPm.setDisplayedValues(new String[]{"AM", "PM"});

        npHour.setFormatter(value -> String.format(Locale.US, "%02d", value));
        npMinute.setFormatter(value -> String.format(Locale.US, "%02d", value));

        npServings.setMinValue(Feed.MIN_FEED_AMOUNT);
        npServings.setMaxValue(Feed.MAX_FEED_AMOUNT);
    }
    
    private void initHandlers() {
        btnCancel.setOnClickListener(v -> onBackPressed());

        rgDay.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            chosenDayAbv = radioButton.getText().toString();
        });

        btnSave.setOnClickListener(v -> {
            if (chosenDayAbv.isEmpty()) {
                Toast.makeText(this, "Please choose the day!!", Toast.LENGTH_SHORT).show();
                ViewUtils.vibratePhone(this, (Vibrator) getSystemService(Context.VIBRATOR_SERVICE), findViewById(R.id.day_input_container_schedule), 500);
                return;
            }
            String amPm = npAmPm.getValue() == 0 ? "AM" : "PM";
            Schedule scheduledFeed = new Schedule()
                    .setFeed(npServings.getValue())
                    .setDayAbv(chosenDayAbv)
                    .setScheduleTime(npHour.getValue(), npMinute.getValue(), amPm);
            scheduleFeedViewModel.addNewSchedule("592f5ec4-c3dd-4d5c-93fe-4ee3db513ad7", scheduledFeed);
            loadingDialog = ViewUtils.showLoadingDialog(ScheduledFeedActivity.this);
        });
    }

    private void observeLiveData() {
        scheduleFeedViewModel.getAddScheduleFeedResult().observe(this, scheduleFeedResult -> {
            if (loadingDialog != null) loadingDialog.dismissWithAnimation();
            if (scheduleFeedResult instanceof Result.Success) {
                ViewUtils.fireSuccessAlert(ScheduledFeedActivity.this, "Added new schedule!");
                scheduleFeedViewModel.clearCache();
            } else {
                String errorMsg = ((Result.Error<Schedule>) scheduleFeedResult).getErrorMessage();
                ViewUtils.fireErrorAlert(ScheduledFeedActivity.this, errorMsg);
            }
        });
    }
}