package com.damskuy.petfeedermobileapp.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDataSource extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cache";

    public CacheDataSource(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createScheduleTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropScheduleTable(db);
        onCreate(db);
    }

    private void createScheduleTable(SQLiteDatabase db) {
        String query = "CREATE TABLE schedules ("
                + "id INTEGER PRIMARY KEY,"
                + "day TEXT,"
                + "schedule_time TEXT,"
                + "feed_amount INTEGER"
                + ")";
        db.execSQL(query);
    }

    private void dropScheduleTable(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS schedules";
        db.execSQL(query);
    }
}
