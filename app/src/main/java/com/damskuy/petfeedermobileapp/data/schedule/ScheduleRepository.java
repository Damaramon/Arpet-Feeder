package com.damskuy.petfeedermobileapp.data.schedule;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.damskuy.petfeedermobileapp.data.cache.CacheDataSource;
import com.damskuy.petfeedermobileapp.data.cache.CacheRepository;
import com.damskuy.petfeedermobileapp.data.model.response.GetDeviceScheduleResponse;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.Schedule;
import com.damskuy.petfeedermobileapp.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepository {

    private final ScheduleDataSource scheduleDataSource;
    private static ScheduleRepository instance;

    private ScheduleRepository() {
        scheduleDataSource = new ScheduleDataSource();
    }

    public static synchronized ScheduleRepository getInstance() {
        if (instance == null) instance = new ScheduleRepository();
        return instance;
    }

    public void fetchDeviceSchedule(
            MutableLiveData<Result<ArrayList<Schedule>>> result,
            String deviceId
    ) {
        scheduleDataSource.getDeviceSchedule(new Callback<GetDeviceScheduleResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<GetDeviceScheduleResponse> call,
                    @NonNull Response<GetDeviceScheduleResponse> response
            ) {
                if (!response.isSuccessful()) {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody == null) {
                        result.postValue(new Result.Error<>(new Exception("Failed to fetch schedule data")));
                        return;
                    }
                    try {
                        String errorBodyString = errorBody.string();
                        String errorMessage = JsonUtils.getJsonErrorField(errorBodyString);
                        result.postValue(new Result.Error<>(new Exception(errorMessage)));
                    } catch (IOException e) {
                        Log.e("JsonError", e.getMessage());
                        result.postValue(new Result.Error<>(new Exception("Failed to fetch schedule data")));
                    }
                    return;
                }
                GetDeviceScheduleResponse responseData = response.body();
                if (responseData == null) {
                    result.postValue(new Result.Error<>(new Exception("Unable to get response data")));
                    return;
                }
                ArrayList<Schedule> schedules = new ArrayList<>();
                for (GetDeviceScheduleResponse.Schedule sch : responseData.getData().getSchedules()) {
                    Schedule schedule = new Schedule()
                            .setId(sch.getId())
                            .setFeed(sch.getFeedAmount())
                            .setDay(sch.getDayOfWeek())
                            .setScheduleTime(sch.getFeedTime());
                    schedules.add(schedule);
                }
                result.postValue(new Result.Success<>(schedules));
            }

            @Override
            public void onFailure(
                    @NonNull Call<GetDeviceScheduleResponse> call,
                    @NonNull Throwable t
            ) {
                result.postValue(new Result.Error<>(new Exception("Failed to fetch schedule data")));
            }
        }, deviceId);
    }
}
