package com.damskuy.petfeedermobileapp.data.schedule;

import com.damskuy.petfeedermobileapp.data.model.response.GetDeviceScheduleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScheduleService {
    @GET("api/v1/schedule/{id}")
    Call<GetDeviceScheduleResponse> getDeviceSchedule(@Path("id") String deviceId);
}
