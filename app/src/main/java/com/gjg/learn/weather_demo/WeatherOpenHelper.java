package com.gjg.learn.weather_demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： ${高俊光}
 * 时间： 2016/7/18   23：35.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {
    //建表语句
    private static final String CREATE_CITY_WEATHER = "create table city_weather_table("
            + "_id integer primary key autoincrement,"
            + "city_name text,"
            + "city_id text,"
            + "date text,"
            + "week text,"
            + "station text,"
            + "station_code text,"
            + "temperature text,"
            + "airqlty text,"
            + "windDir text,"
            + "windSc text,"
            + "pm text,"
            + "icon text"
            +")";

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
