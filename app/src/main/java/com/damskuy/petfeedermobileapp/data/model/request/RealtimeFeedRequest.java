package com.damskuy.petfeedermobileapp.data.model.request;

import com.damskuy.petfeedermobileapp.data.model.Feed;
import com.google.gson.annotations.SerializedName;

public class RealtimeFeedRequest {

    @SerializedName("device_id")
    private final String deviceId;

    @SerializedName("feed_amount")
    private final Integer feedAmount;

    public RealtimeFeedRequest(String deviceId, Feed feed) {
        this.deviceId = deviceId;
        this.feedAmount = feed.getFeedAmount();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getFeedAmount() {
        return feedAmount;
    }
}
