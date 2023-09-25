package com.damskuy.petfeedermobileapp.data.model.response;

import com.google.gson.annotations.SerializedName;

public class ScheduleFeedResponse {

    private Data data;

    private String error;

    public static class Data {

        @SerializedName("schedule_id")
        private int scheduleId;

        @SerializedName("created_at")
        private String createdAt;

        public int getScheduleId() {
            return scheduleId;
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
