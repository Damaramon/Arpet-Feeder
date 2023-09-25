package com.damskuy.petfeedermobileapp.data.model.response;

import com.google.gson.annotations.SerializedName;

public class RealtimeFeedResponse {

    private Data data;

    private String error;

    public static class Data {

        @SerializedName("device_id")
        private String deviceId;

        @SerializedName("feed_amount")
        private Integer feedAmount;

        @SerializedName("created_at")
        private String createdAt;

        public String getDeviceId() {
            return deviceId;
        }

        public Integer getFeedAmount() {
            return feedAmount;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    public Data getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
