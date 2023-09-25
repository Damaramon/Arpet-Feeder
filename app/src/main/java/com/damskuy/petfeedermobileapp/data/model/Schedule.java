package com.damskuy.petfeedermobileapp.data.model;

import java.util.HashMap;
import java.util.Locale;

public class Schedule {

    private int id;
    private String day;
    private String scheduledTime;
    private Feed feed;
    private final HashMap<String, String> dayAbvMap;

    public Schedule() {
        dayAbvMap = new HashMap<>();
        dayAbvMap.put("Mon", "Monday");
        dayAbvMap.put("Tue", "Tuesday");
        dayAbvMap.put("Wed", "Wednesday");
        dayAbvMap.put("Thu", "Thursday");
        dayAbvMap.put("Fri", "Friday");
        dayAbvMap.put("Sat", "Saturday");
        dayAbvMap.put("Sun", "Sunday");
    }

    public Schedule setScheduleTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public Schedule setScheduleTime(int hour, int minute, String amPm) {
        if (amPm.equals("PM")) hour += 12;
        if (amPm.equals("AM") && hour == 12) hour = 0;
        this.scheduledTime = String.format(Locale.US, "%02d:%02d", hour, minute);
        return this;
    }

    public Schedule setDay(String day) {
        this.day = day;
        return this;
    }

    public Schedule setDayAbv(String dayAbv) {
        if (!dayAbvMap.containsKey(dayAbv)) return null;
        this.day = dayAbvMap.get(dayAbv);
        return this;
    }

    public Schedule setFeed(int amount) {
        this.feed = new Feed().setFeedAmount(amount);
        return this;
    }

    public Schedule setId(int id) {
        this.id = id;
        return this;
    }

    public String getDay() {
        return day;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public Feed getFeed() {
        return feed;
    }

    public int getId() {
        return id;
    }
}
