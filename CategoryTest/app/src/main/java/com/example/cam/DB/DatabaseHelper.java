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

    private String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.App.TABLE_NAME + " ( "
            + TableIndex.App.APP_NAME + " TEXT , "
            + TableIndex.App.APP_PACKAGE + " TEXT DEFAULT NULL, "
            + TableIndex.App.APP_CATEGROY + " TEXT DEFAULT NULL, "
            + TableIndex.App.APP_ALL_LAUNCHER_COUNT + " INTEGER DEFAULT 0"
            + " )";

    private String CREATE_SESSION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "Session_" + DateUtil.formatDateWithoutHour(System.currentTimeMillis()) +" ( "
            + TableIndex.Session.APP_PACKAGE + " TEXT , "
            + TableIndex.Session.LOCATION + " TEXT DEFAULT NULL, "
            + TableIndex.Session.OPEN_TIME + " TEXT DEFAULT NULL, "
            + TableIndex.Session.TIME_PERIOD + " TEXT DEFAULT NULL"
            + " )";

    private String CREATE_NOTIICATION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.Notiication.TABLE_NAME + " ( "
            + TableIndex.Notiication.APP_PACKAGE + " TEXT , "
            + TableIndex.Notiication.RECEIVE_TIME + " TEXT DEFAULT NULL, "
            + TableIndex.Notiication.OPEN_TIME + " TEXT DEFAULT NULL "
            + " )";

    private String CREATE_INTIMATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.Intimate.TABLE_NAME + " ( "
            + TableIndex.Intimate.APP_PACKAGE + " TEXT , "
            + TableIndex.Intimate.INTIMACY + " INTEGER DEFAULT 0, "
            + TableIndex.Intimate.RECEIVE_COUNT + " INTEGER DEFAULT 0"
            + " )";

    private String CREATE_PERIOD_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "Period_" + DateUtil.formatDateWithoutHour(System.currentTimeMillis()) +" ( "
            + TableIndex.Period.APP_PACKAGE + " TEXT , "
            + TableIndex.Period._0_1 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._1_2 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._2_3 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._3_4 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._4_5 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._5_6 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._6_7 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._7_8 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._8_9 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._9_10 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._10_11 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._11_12 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._12_13 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._13_14 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._14_15 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._15_16 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._16_17 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._17_18 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._18_19 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._19_20 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._20_21 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._21_22 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._22_23 + " INTEGER DEFAULT 0, "
            + TableIndex.Period._23_24 + " INTEGER DEFAULT 0"
            + " )";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("enter DB onClick");
        db.execSQL(CREATE_APP_TABLE);
        db.execSQL(CREATE_SESSION_TABLE);
        db.execSQL(CREATE_NOTIICATION_TABLE);
        db.execSQL(CREATE_INTIMATE_TABLE);
        db.execSQL(CREATE_PERIOD_TABLE);
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

    public void createSession(SQLiteDatabase db) {
        db.execSQL(CREATE_SESSION_TABLE);
        db.close();
    }

}
