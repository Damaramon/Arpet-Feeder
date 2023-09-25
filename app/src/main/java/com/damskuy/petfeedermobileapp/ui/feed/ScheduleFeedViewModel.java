package com.damskuy.petfeedermobileapp.ui.feed;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.damskuy.petfeedermobileapp.data.cache.CacheRepository;
import com.damskuy.petfeedermobileapp.data.feed.FeedRepository;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.Schedule;
import com.damskuy.petfeedermobileapp.data.model.request.ScheduleFeedRequest;

public class ScheduleFeedViewModel extends AndroidViewModel {

    private final FeedRepository feedRepository;
    private final CacheRepository cacheRepository;
    private final MutableLiveData<Result<Schedule>> addScheduleResult = new MutableLiveData<>();

    public ScheduleFeedViewModel(
            @NonNull Application application,
            FeedRepository feedRepository,
            CacheRepository cacheRepository
    ) {
        super(application);
        this.feedRepository = feedRepository;
        this.cacheRepository = cacheRepository;
    }

    public LiveData<Result<Schedule>> getAddScheduleFeedResult() {
        return addScheduleResult;
    }

    public void addNewSchedule(String deviceId, Schedule schedule) {
        ScheduleFeedRequest request = new ScheduleFeedRequest(
                deviceId,
                schedule.getDay(),
                schedule.getScheduledTime(),
                schedule.getFeed().getFeedAmount()
        );
        feedRepository.scheduleFeed(addScheduleResult, request);
    }

    public void clearCache() {
        if (!cacheRepository.isScheduleCached()) return;
        AsyncTask.execute(cacheRepository::clearScheduleCache);
    }
}
