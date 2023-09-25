package com.damskuy.petfeedermobileapp.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.damskuy.petfeedermobileapp.data.model.Schedule;

import java.util.ArrayList;

public class CacheRepository extends CacheDataSource {

    private static CacheRepository instance;
    private static Boolean SCHEDULE_CACHED = false;

    private CacheRepository(Context context) {
        super(context);
    }

    public static synchronized CacheRepository getInstance(Context context) {
        if (instance == null) instance = new CacheRepository(context);
        return instance;
    }

    public void cacheSchedules(ArrayList<Schedule> schedules) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Schedule schedule : schedules) {
                ContentValues values = new ContentValues();
                values.put("id", schedule.getId());
                values.put("day", schedule.getDay());
                values.put("schedule_time", schedule.getScheduledTime());
                values.put("feed_amount", schedule.getFeed().getFeedAmount());
                db.insert("schedules", null, values);
            }
            db.setTransactionSuccessful();
            SCHEDULE_CACHED = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void clearScheduleCache() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("schedules", null, null);
        db.close();
        SCHEDULE_CACHED = false;
    }

    public ArrayList<Schedule> getAllSchedulesFromCache() {
        ArrayList<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM schedules";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String day = cursor.getString(cursor.getColumnIndexOrThrow("day"));
                String scheduledTime = cursor.getString(cursor.getColumnIndexOrThrow("schedule_time"));
                int feedAmount = cursor.getInt(cursor.getColumnIndexOrThrow("feed_amount"));
                Schedule schedule = new Schedule()
                        .setId(id)
                        .setDay(day)
                        .setScheduleTime(scheduledTime)
                        .setFeed(feedAmount);
                schedules.add(schedule);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return schedules;
    }

    public boolean isScheduleCached() {
        return SCHEDULE_CACHED;
    }
}
