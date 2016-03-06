package com.example.aswin.nitcmesschronicles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NITC_CHRONICLES";
    public static final Integer DATABASE_VERSION = 1;

    public static String TABLE_NAME;
    public static String CREATE_TABLE_NAME;

    public static final String ROW_ID = "_id";
    public static final String DATE_AND_TIME = "date";
    public static final String EXTRA = "extra";
    public static final String DAY = "day";

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
