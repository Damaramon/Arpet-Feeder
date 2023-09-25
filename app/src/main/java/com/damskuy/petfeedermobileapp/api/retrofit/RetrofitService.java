package com.damskuy.petfeedermobileapp.api.retrofit;

import com.damskuy.petfeedermobileapp.data.model.request.RealtimeFeedRequest;
import com.damskuy.petfeedermobileapp.data.model.response.RealtimeFeedResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("api/v1/realtime")
    Call<RealtimeFeedResponse> realtimeFeed(@Body RealtimeFeedRequest request);
}
