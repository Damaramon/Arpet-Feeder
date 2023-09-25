package com.damskuy.petfeedermobileapp.data.model.request;

import com.google.gson.annotations.SerializedName;

public class ScheduleFeedRequest {

   @SerializedName("device_id")
   private final String deviceId;

   @SerializedName("day_of_week")
   private final String dayOfWeek;

   @SerializedName("feed_time")
   private final String feedTime;

   @SerializedName("feed_amount")
   private final int feedAmount;

   public ScheduleFeedRequest(
           String deviceId,
           String dayOfWeek,
           String feedTime,
           int feedAmount
   ) {
      this.deviceId = deviceId;
      this.dayOfWeek = dayOfWeek;
      this.feedTime = feedTime;
      this.feedAmount = feedAmount;
   }

   public String getDeviceId() {
      return deviceId;
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
