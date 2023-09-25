package com.damskuy.petfeedermobileapp.ui.history;

public class HistoryData{
    private final String day;
    private final String time;

    public HistoryData(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

}
