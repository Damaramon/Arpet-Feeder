package com.damskuy.petfeedermobileapp.ui.feed;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.damskuy.petfeedermobileapp.data.cache.CacheRepository;
import com.damskuy.petfeedermobileapp.data.feed.FeedRepository;
import com.damskuy.petfeedermobileapp.data.schedule.ScheduleRepository;

public class ScheduleFeedViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final FeedRepository feedRepository;
    private final CacheRepository cacheRepository;

    public ScheduleFeedViewModelFactory(Application application) {
       this.application = application;
       this.feedRepository = new FeedRepository();
       this.cacheRepository = CacheRepository.getInstance(application.getApplicationContext());
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ScheduleFeedViewModel.class)) {
            return (T) new ScheduleFeedViewModel(application, feedRepository, cacheRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
