package com.example.cam.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cam.commonUtils.DateUtil;

/**
 * Created by cam on 1/7/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String LOG_TAG = "PredictDB";
    private final static String DB_NAME = "predict";
    private static final int DATABASE_VERSION = 1;

    private String CREATE_SESSION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            DateUtil.formatDateWithoutHour(System.currentTimeMillis()) +"_Session" + " ( "
            + TableIndex.Session.APP_NAME + " TEXT , "
            + TableIndex.Session.LOCATION + " TEXT DEFAULT NULL, "
            + TableIndex.Session.OPEN_TIME + " TEXT DEFAULT NULL, "
            + TableIndex.Session.TIME_PERIOD + " TEXT DEFAULT NULL"
            + " )";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean tabIsExist(String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tabName.trim() + "' ";
            cursor = getReadableDatabase().rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "" + e.toString());
        }
        return result;
    }

    public static String getSessionTableName() {
        return DateUtil.formatDateWithoutHour(System.currentTimeMillis()) + "_Session";
    }

    public static String getNotificationTableName() {
        return DateUtil.formatDateWithoutHour(System.currentTimeMillis()) + "_Notiication";
    }
}
