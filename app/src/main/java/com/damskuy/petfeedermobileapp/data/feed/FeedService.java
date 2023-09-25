package com.damskuy.petfeedermobileapp.data.feed;

import com.damskuy.petfeedermobileapp.data.model.request.RealtimeFeedRequest;
import com.damskuy.petfeedermobileapp.data.model.response.RealtimeFeedResponse;
import com.damskuy.petfeedermobileapp.data.model.request.ScheduleFeedRequest;
import com.damskuy.petfeedermobileapp.data.model.response.ScheduleFeedResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface FeedService {
    @POST("api/v1/realtime")
    Call<RealtimeFeedResponse> realtimeFeed(@Body RealtimeFeedRequest request);

    @POST("api/v1/schedule")
    Call<ScheduleFeedResponse> scheduleFeed(@Body ScheduleFeedRequest request);
}
