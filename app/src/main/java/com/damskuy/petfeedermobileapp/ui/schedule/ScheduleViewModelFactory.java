package com.damskuy.petfeedermobileapp.ui.schedule;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.damskuy.petfeedermobileapp.data.cache.CacheRepository;
import com.damskuy.petfeedermobileapp.data.schedule.ScheduleRepository;

public class ScheduleViewModelFactory implements ViewModelProvider.Factory {

    private final ScheduleRepository scheduleRepository;
    private final CacheRepository cacheRepository;

    public ScheduleViewModelFactory(Context context) {
       this.scheduleRepository = ScheduleRepository.getInstance();
       this.cacheRepository = CacheRepository.getInstance(context);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ScheduleViewModel.class)) {
            return (T) new ScheduleViewModel(scheduleRepository, cacheRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
