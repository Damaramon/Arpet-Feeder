package com.damskuy.petfeedermobileapp.ui.schedule;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.damskuy.petfeedermobileapp.data.cache.CacheRepository;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.Schedule;
import com.damskuy.petfeedermobileapp.data.model.response.GetDeviceScheduleResponse;
import com.damskuy.petfeedermobileapp.data.schedule.ScheduleRepository;

import java.util.ArrayList;

public class ScheduleViewModel extends ViewModel {

    private final ScheduleRepository scheduleRepository;
    private final CacheRepository cacheRepository;
    private final MutableLiveData<Result<ArrayList<Schedule>>> fetchScheduleResult = new MutableLiveData<>();

    public ScheduleViewModel(
            ScheduleRepository scheduleRepository,
            CacheRepository cacheRepository
    ) {
        this.scheduleRepository = scheduleRepository;
        this.cacheRepository = cacheRepository;
    }

    public LiveData<Result<ArrayList<Schedule>>> getFetchScheduleResult() {
        return fetchScheduleResult;
    }

    public void fetchSchedule(String deviceId) {
        if (!cacheRepository.isScheduleCached()) scheduleRepository.fetchDeviceSchedule(fetchScheduleResult, deviceId);
        else fetchScheduleResult.postValue(new Result.Success<>(cacheRepository.getAllSchedulesFromCache()));
    }

    public void saveScheduleToCache(ArrayList<Schedule> schedules) {
        if (cacheRepository.isScheduleCached()) return;
        AsyncTask.execute(() -> cacheRepository.cacheSchedules(schedules));
    }
}
