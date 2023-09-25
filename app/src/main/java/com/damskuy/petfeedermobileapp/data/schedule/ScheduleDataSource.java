package com.damskuy.petfeedermobileapp.data.schedule;

import com.damskuy.petfeedermobileapp.BuildConfig;
import com.damskuy.petfeedermobileapp.data.model.response.GetDeviceScheduleResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleDataSource {

    private final ScheduleService scheduleService;

    public ScheduleDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        scheduleService = retrofit.create(ScheduleService.class);
    }

    public void getDeviceSchedule(Callback<GetDeviceScheduleResponse> callback, String deviceId) {
        Call<GetDeviceScheduleResponse> call = scheduleService.getDeviceSchedule(deviceId);
        call.enqueue(callback);
    }
}
