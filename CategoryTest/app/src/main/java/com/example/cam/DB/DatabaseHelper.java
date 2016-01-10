package com.example.cam.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.commonUtils.DateUtil;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by cam on 1/7/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String LOG_TAG = "PredictDB";
    private final static String DB_NAME = "predict";
    private static final int DATABASE_VERSION = 1;

    private String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.App.TABLE_NAME + " ( "
            + TableIndex.App.APP_PACKAGE + " TEXT PRIMARY KEY, "
            + TableIndex.App.APP_NAME + " TEXT DEFAULT NULL, "
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
            + TableIndex.Intimate.APP_PACKAGE + " TEXT PRIMARY KEY, "
            + TableIndex.Intimate.INTIMACY + " INTEGER DEFAULT 0, "
            + TableIndex.Intimate.RECEIVE_COUNT + " INTEGER DEFAULT 0"
            + " )";

    private String CREATE_PERIOD_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "Period_" + DateUtil.formatDateWithoutHour(System.currentTimeMillis()) +" ( "
            + TableIndex.Period.APP_PACKAGE + " TEXT PRIMARY KEY, "
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
        return "Session_" + DateUtil.formatDateWithoutHour(System.currentTimeMillis()) ;
    }

    public static String getPeriodTableName() {
        return "Period_" + DateUtil.formatDateWithoutHour(System.currentTimeMillis()) ;
    }



    public void createSession(SQLiteDatabase db) {
        db.execSQL(CREATE_SESSION_TABLE);
        db.close();
    }

    public void insertAppTable(SQLiteDatabase db, ArrayList<PackageVO> appList) {
        try {
            if (!tabIsExist(TableIndex.App.TABLE_NAME)) {
                db.execSQL(CREATE_APP_TABLE);
                for (PackageVO p : appList) {
                    ContentValues cv = new ContentValues();
                    cv.put(TableIndex.App.APP_NAME, p.appname);
                    cv.put(TableIndex.App.APP_PACKAGE, p.pname);
                    db.insert(TableIndex.App.TABLE_NAME, null, cv);
                }
            } else {
                for (PackageVO p : appList) {
                    ContentValues cv = new ContentValues();
                    cv.put(TableIndex.App.APP_NAME, p.appname);
                    cv.put(TableIndex.App.APP_PACKAGE, p.pname);
                    db.insert(TableIndex.App.TABLE_NAME, null, cv);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public int getAppTableCount(SQLiteDatabase db) {
        try {
            if (!tabIsExist(TableIndex.App.TABLE_NAME)) {
                db.execSQL(CREATE_APP_TABLE);
                return 0;
            } else {
                Cursor cr = db.query(TableIndex.App.TABLE_NAME, null, null, null, null, null, null);
                if (cr != null) {
                    return  cr.getCount();
                } else {
                    return 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return 0;
    }

    public void deleteAppTable(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE " + TableIndex.App.TABLE_NAME);
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public ArrayList<PackageVO> getNullCategroyList(SQLiteDatabase db) {
        ArrayList<PackageVO> nullCategroyList = new ArrayList<PackageVO>();
        try {
            Cursor c = db.query(TableIndex.App.TABLE_NAME,
                    new String[] {TableIndex.App.APP_PACKAGE, TableIndex.App.APP_NAME},
                    TableIndex.App.APP_CATEGROY + " is null ",
                    null, null, null, null);
            if (c != null) {
                while (c.moveToNext()) {
                    PackageVO p = new PackageVO();
                    String packName = c.getString(c.getInt(0));
                    String appName = c.getString(c.getInt(1));
                    p.pname = packName;
                    p.appname = appName;
                    nullCategroyList.add(p);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            db.close();
        }
        System.out.println("null size -> " + nullCategroyList.size());
        return nullCategroyList;
    }

    public void updateAppCategroy (SQLiteDatabase db, ArrayList<PackageVO> appList) {
        System.out.println("enter update");
        try {
            if (appList.size() == 0) {
                return;
            } else {
                for (PackageVO p : appList) {
                    ContentValues cv = new ContentValues();
                    cv.put(TableIndex.App.APP_CATEGROY, p.category);
                    db.update(TableIndex.App.TABLE_NAME, cv, TableIndex.App.APP_PACKAGE + " = ?", new String[]{p.pname});
                }
            }
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void updateAppLauncher (SQLiteDatabase db, String packName) {
        System.out.println("enter update");
        try {
            int count = 0;
            Cursor cursor = db.query(TableIndex.App.TABLE_NAME,
                    new String[] {TableIndex.App.APP_ALL_LAUNCHER_COUNT},
                    TableIndex.App.APP_PACKAGE + " = ?" ,
                    new String[] {packName}, null, null, null);
            if (cursor != null) {
                cursor.moveToNext();
                count = cursor.getInt(0);
                count++;
            } else {
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put(TableIndex.App.APP_ALL_LAUNCHER_COUNT, count);
            db.update(TableIndex.App.TABLE_NAME, cv, TableIndex.App.APP_PACKAGE + " = ?", new String[]{packName});
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void queryCategroy (SQLiteDatabase db) {
        try {
            Cursor cr = db.query(TableIndex.App.TABLE_NAME, null, null, null, null, null, null, null);
            if(cr != null) {
                while (cr.moveToNext()) {
                    System.out.println("" + cr.getString(cr.getColumnIndex(TableIndex.App.APP_CATEGROY)));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void updatePeriod (SQLiteDatabase db, String packName) {
        try {
            if (!tabIsExist(getPeriodTableName())) {
                db.execSQL(CREATE_PERIOD_TABLE);
            }
            Cursor cursor = db.query(getPeriodTableName(),
                    null,
                    TableIndex.App.APP_PACKAGE + " = ?" ,
                    new String[] {packName}, null, null, null);
            ContentValues cv = new ContentValues();
            if (cursor == null) {
                cv.put(TableIndex.Period.APP_PACKAGE, packName);
                cv.put(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())], 1);
                db.insert(getPeriodTableName(), null, cv);
            } else {
                if (cursor.getCount() == 0) {
                    cv.put(TableIndex.Period.APP_PACKAGE, packName);
                    cv.put(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())], 1);
                    db.insert(getPeriodTableName(), null, cv);
                } else {
                    cursor.moveToFirst();
                    int count = cursor.getInt(cursor.getColumnIndex(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]));
                    count++;
                    cv.put(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())], count);
                    db.update(getPeriodTableName(), cv, TableIndex.Period.APP_PACKAGE + " = ?", new String[]{packName});
                }
            }
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void insertSession(SQLiteDatabase db, String packName, String location) {
        try {
            if (!tabIsExist(getSessionTableName())) {
                db.execSQL(CREATE_SESSION_TABLE);
            }
            ContentValues cv = new ContentValues();
            cv.put(TableIndex.Session.APP_PACKAGE, packName);
            cv.put(TableIndex.Session.LOCATION, location);
            cv.put(TableIndex.Session.TIME_PERIOD, DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]);
            db.insert(getSessionTableName(), null, cv);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

}