package com.damskuy.petfeedermobileapp.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDeviceScheduleResponse {

    @SerializedName("data")
    private ScheduleData data;

    @SerializedName("error")
    private String error;

    public ScheduleData getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public static class ScheduleData {

        @SerializedName("schedules")
        private List<Schedule> schedules;

        public List<Schedule> getSchedules() {
            return schedules;
        }
    }

    public static class Schedule {

        @SerializedName("id")
        private int id;

        @SerializedName("day_of_week")
        private String dayOfWeek;

        @SerializedName("feed_time")
        private String feedTime;

        @SerializedName("feed_amount")
        private int feedAmount;

        public int getId() {
            return id;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public String getFeedTime() {
            return feedTime;
        }

        public int getFeedAmount() {
            return feedAmount;
        }
    }
}