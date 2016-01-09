package com.example.cam;

import android.app.Application;

import com.example.cam.DB.DatabaseHelper;

/**
 * Created by cam on 1/7/16.
 */
public class MyApplication extends Application{

    private DatabaseHelper mDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mDbHelper = new DatabaseHelper(this);
        mDbHelper.onCreate(mDbHelper.getWritableDatabase());
    }

    public DatabaseHelper getmDbHelper() {
        return mDbHelper;
    }
}
