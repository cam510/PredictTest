package com.example.cam.DB;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.categorytest.MyLanucher;
import com.example.cam.commonUtils.DateUtil;
import com.example.cam.predict.DataBean;
import com.example.cam.predict.PredictBean;
import com.example.cam.predict.PredictUtil;
import com.example.cam.server.ReceviceObject;
import com.example.cam.utils.NetworkUtil;

import java.sql.SQLData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by cam on 1/7/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String LOG_TAG = "PredictDB";
    private final static String DB_NAME = "predict";
    private static final int DATABASE_VERSION = 1;

    //创建app表
    private String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.App.TABLE_NAME + " ( "
            + TableIndex.App.APP_PACKAGE + " TEXT PRIMARY KEY, "
            + TableIndex.App.APP_NAME + " TEXT DEFAULT NULL, "
            + TableIndex.App.APP_CATEGROY + " TEXT DEFAULT NULL, "
            + TableIndex.App.APP_ALL_LAUNCHER_COUNT + " INTEGER DEFAULT 0"
            + " )";

    //创建session表
    private String CREATE_SESSION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "Session_" + " ( "
            + TableIndex.Session.ID + " INTEGER PRIMARY KEY,"
            + TableIndex.Session.NOW_APP + " TEXT DEFAULT NULL, "
            + TableIndex.Session.NEXT_APP + " TEXT DEFAULT NULL, "
            + TableIndex.Session.LOCATION + " TEXT DEFAULT NULL, "
            + TableIndex.Session.OPEN_TIME + " TEXT DEFAULT NULL, "
            + TableIndex.Session.TIME_PERIOD + " TEXT DEFAULT NULL"
            + " )";

    //创建notification表
    private String CREATE_NOTIICATION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.Notiication.TABLE_NAME + " ( "
            + TableIndex.Notiication.APP_PACKAGE + " TEXT , "
            + TableIndex.Notiication.RECEIVE_TIME + " TEXT DEFAULT NULL, "
            + TableIndex.Notiication.OPEN_TIME + " TEXT DEFAULT NULL "
            + " )";

    //创建亲密度表
    private String CREATE_INTIMATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.Intimate.TABLE_NAME + " ( "
            + TableIndex.Intimate.APP_PACKAGE + " TEXT PRIMARY KEY, "
            + TableIndex.Intimate.INTIMACY + " INTEGER DEFAULT 0, "
            + TableIndex.Intimate.RECEIVE_COUNT + " INTEGER DEFAULT 0"
            + " )";

    //创建地理位置表
    private String CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.Location.TABLE_NAME + " ( "
            + TableIndex.Location.ADDRESS + " TEXT PRIMARY KEY, "
            + TableIndex.Location.ADDRESS_TYPE + " TEXT DEFAULT NULL"
            + " )";

    //创建使用区间表
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

    //创建session表
    private String CREATE_NEWRECORE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TableIndex.NewRecore.TABLE_NAME + " ( "
            + TableIndex.NewRecore.ID + " INTEGER PRIMARY KEY,"
            + TableIndex.NewRecore.PACKAGE_NAME + " TEXT DEFAULT NULL, "
            + TableIndex.NewRecore.USE_TIME + " TEXT DEFAULT NULL, "
            + TableIndex.NewRecore.USE_PERIOD + " TEXT DEFAULT NULL, "
            + TableIndex.NewRecore.LOCATION_LA + " DOUBLE DEFAULT 0, "
            + TableIndex.NewRecore.LOCATION_LO + " DOUBLE DEFAULT 0,"
            + TableIndex.NewRecore.IS_WORK + " INTEGER DEFAULT 0,"
            + TableIndex.NewRecore.GPRS + " INTEGER DEFAULT 0,"
            + TableIndex.NewRecore.WIFI + " TEXT DEFAULT NULL,"
            + TableIndex.NewRecore.BLUETOOTH + " INTEGER DEFAULT 0,"
            + TableIndex.NewRecore.LIGHT_SENSOR + " Float DEFAULT 0,"
            + TableIndex.NewRecore.ACC_SENSOR_X + " Float DEFAULT 0,"
            + TableIndex.NewRecore.ACC_SENSOR_Y + " Float DEFAULT 0,"
            + TableIndex.NewRecore.ACC_SENSOR_Z + " Float DEFAULT 0,"
            + TableIndex.NewRecore.Notification + " INTEGER DEFAULT 0"
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
//        db.execSQL(CREATE_NOTIICATION_TABLE);
        db.execSQL(CREATE_INTIMATE_TABLE);
        db.execSQL(CREATE_PERIOD_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_NEWRECORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //判断是否存在表
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
        return "Session_";
    }

    public static String getPeriodTableName() {
        return "Period_" + DateUtil.formatDateWithoutHour(System.currentTimeMillis()) ;
    }



    public void createSession(SQLiteDatabase db) {
        db.execSQL(CREATE_SESSION_TABLE);
//        db.close();
    }

    //插入应用到app表
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
//            db.close();
        }
    }

    //unuse
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
//            db.close();
        }
        return 0;
    }

    public void deleteAppTable(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE " + TableIndex.App.TABLE_NAME);
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
//            db.close();
        }
    }

    //获取未分类应用
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
//            db.close();
        }
        System.out.println("null size -> " + nullCategroyList.size());
        return nullCategroyList;
    }

    //更新应用分类
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
//            db.close();
        }
    }

    //更新应用打开次数
    public void updateAppLauncher (SQLiteDatabase db, String packName) {
        System.out.println("enter update " + packName);
        try {
            int count = 0;
            Cursor cursor = db.query(TableIndex.App.TABLE_NAME,
                    new String[] {TableIndex.App.APP_ALL_LAUNCHER_COUNT},
                    TableIndex.App.APP_PACKAGE + " = ?" ,
                    new String[] {packName}, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    if (cursor.getColumnCount() > 0) {
                        count = cursor.getInt(0);
                        count++;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put(TableIndex.App.APP_ALL_LAUNCHER_COUNT, count);
            db.update(TableIndex.App.TABLE_NAME, cv, TableIndex.App.APP_PACKAGE + " = ?", new String[]{packName});
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
//            db.close();
        }
    }

    //查询并应用分类
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
//            db.close();
        }
    }

    //更新应用使用时区
    public void updatePeriod (SQLiteDatabase db, String packName) {
//        try {
//            if (!tabIsExist(getPeriodTableName())) {
//                db.execSQL(CREATE_PERIOD_TABLE);
//            }
//            Cursor cursor = db.query(getPeriodTableName(),
//                    null,
//                    TableIndex.App.APP_PACKAGE + " = ?" ,
//                    new String[] {packName}, null, null, null);
//            ContentValues cv = new ContentValues();
//            if (cursor == null) {
//                cv.put(TableIndex.Period.APP_PACKAGE, packName);
//                cv.put(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())], 1);
//                db.insert(getPeriodTableName(), null, cv);
//            } else {
//                if (cursor.getCount() == 0) {
//                    cv.put(TableIndex.Period.APP_PACKAGE, packName);
//                    cv.put(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())], 1);
//                    db.insert(getPeriodTableName(), null, cv);
//                } else {
//                    cursor.moveToFirst();
//                    int count = cursor.getInt(cursor.getColumnIndex(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]));
//                    count++;
//                    cv.put(DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())], count);
//                    db.update(getPeriodTableName(), cv, TableIndex.Period.APP_PACKAGE + " = ?", new String[]{packName});
//                }
//            }
//        } catch (android.database.SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            db.close();
//        }
    }

    //插入到session表
    public void insertSession( String packName, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("enter insertSession " + packName);
        try {
            if (!tabIsExist(getSessionTableName())) {
                db.execSQL(CREATE_SESSION_TABLE);
            }
            ContentValues cv = new ContentValues();
            cv.put(TableIndex.Session.NOW_APP, packName);
            cv.put(TableIndex.Session.LOCATION, location);
            cv.put(TableIndex.Session.TIME_PERIOD, DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]);
            cv.put(TableIndex.Session.OPEN_TIME, DateUtil.formatDateWithHourMin(System.currentTimeMillis()));
            db.insert(getSessionTableName(), null, cv);
            Cursor cr = db.query(getSessionTableName(), null, null, null, null, null, null);
            //update last id NextApp
            while (cr != null && cr.getCount() > 1) {
                cr.moveToLast();
                int id = cr.getInt(cr.getColumnIndex(TableIndex.Session.ID));
                id--;
                cv = new ContentValues();
                cv.put(TableIndex.Session.NEXT_APP, packName);
                getWritableDatabase().update(getSessionTableName(), cv,
                        " " + TableIndex.Session.ID + " = ? ", new String[] {String.valueOf(id)});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            db.close();
        }
    }

    //更新亲密度
    public void updateIntimate (SQLiteDatabase db, ReceviceObject recevice, boolean add) {
        try {
            if (!tabIsExist(TableIndex.Intimate.TABLE_NAME)) {
                db.execSQL(CREATE_NOTIICATION_TABLE);
            }
            Cursor cursor = db.query(TableIndex.Intimate.TABLE_NAME,
                    null,
                    TableIndex.Intimate.APP_PACKAGE + " = ?" ,
                    new String[] {recevice.getPackName()}, null, null, null);
            ContentValues cv = new ContentValues();
            if (cursor == null) {
                cv.put(TableIndex.Intimate.APP_PACKAGE, recevice.getPackName());
                cv.put(TableIndex.Intimate.INTIMACY, 1);
                cv.put(TableIndex.Intimate.RECEIVE_COUNT, 1);
                db.insert(TableIndex.Intimate.TABLE_NAME, null, cv);
            } else {
                if (cursor.getCount() == 0) {
                    cv.put(TableIndex.Intimate.APP_PACKAGE, recevice.getPackName());
                    cv.put(TableIndex.Intimate.INTIMACY, 1);
                    cv.put(TableIndex.Intimate.RECEIVE_COUNT, 1);
                    db.insert(TableIndex.Intimate.TABLE_NAME, null, cv);
                } else {
                    cursor.moveToFirst();
                    if (add) {
                        int intimate = cursor.getInt(cursor.getColumnIndex(TableIndex.Intimate.INTIMACY));
                        intimate++;
                        cv.put(TableIndex.Intimate.INTIMACY, intimate);
                    }
                    int receviceCount = cursor.getInt(cursor.getColumnIndex(TableIndex.Intimate.RECEIVE_COUNT));
                    receviceCount++;
                    cv.put(TableIndex.Intimate.RECEIVE_COUNT, receviceCount);
                    db.update(TableIndex.Intimate.TABLE_NAME, cv, TableIndex.Intimate.APP_PACKAGE + " = ?", new String[]{recevice.getPackName()});
                }
            }
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
//            db.close();
        }
    }

    /**
     * 暂定
     * 获取过去一天的数据
     */
    public HashMap<String, Integer> getLastDayApp() {
        HashMap<String, Integer> dbData = new HashMap<String, Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(getSessionTableName(),
                new String[] {TableIndex.Session.NEXT_APP},
//                " " + TableIndex.Session.NOW_APP + " = ?", new String[] {appName},
                null, null,
                null, null, TableIndex.Session.ID + " desc", "100");

        dbData.clear();
        if (c != null && c.getCount() > 0) {
            dbData.put("all", c.getCount());
            while (c.moveToNext()) {
                String packName = c.getString(0);
                if (packName != null && packName.equals("screenon")) {
                    continue;
                }
                if (dbData.containsKey(packName)) {
                    int count = dbData.get(packName);
                    count++;
                    dbData.put(packName, count);
                } else {
                    dbData.put(packName, 1);
                }
            }
        }
//        db.close();
        return dbData;
    }

    /**
     * 暂定
     * 获取过去一天该时段的数据
     */
    public HashMap<String, Integer> getLastDayPeriodApp() {
        HashMap<String, Integer> dbData = new HashMap<String, Integer>();

//        Cursor c = this.getReadableDatabase().query(getSessionTableName(),
//                new String[] {TableIndex.Session.NEXT_APP},
//                " " + TableIndex.Session.TIME_PERIOD + " = ? ",
//                new String[] {DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]},
//                null, null, null);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(getSessionTableName(),
                new String[]{TableIndex.Session.NEXT_APP},
                " " + TableIndex.Session.TIME_PERIOD + " = ? ",
                new String[]{DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]},
                null, null, TableIndex.Session.ID, "100");

        dbData.clear();
        if (c != null && c.getCount() > 0) {
            dbData.put("all", c.getCount());
            while (c.moveToNext()) {
                String packName = c.getString(0);
                if (packName != null && packName.equals("screenon")) {
                    continue;
                }
                if (dbData.containsKey(packName)) {
                    int count = dbData.get(packName);
                    count++;
                    dbData.put(packName, count);
                } else {
                    dbData.put(packName, 1);
                }
            }
        }
//        db.close();
        return dbData;
    }

    /**
     * 获取当前app的下一个app
     * */
    public HashMap<String, Integer> getNextApp(String appName) {
        HashMap<String, Integer> dbData = new HashMap<String, Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = this.getReadableDatabase().query(getSessionTableName(),
                new String[]{TableIndex.Session.NEXT_APP},
                " " + TableIndex.Session.NOW_APP + " = ?", new String[]{appName},
                null, null, TableIndex.Session.ID + " desc", "100");

        dbData.clear();
        if (c != null && c.getCount() > 0) {
            dbData.put("all", c.getCount());
            while (c.moveToNext()) {
                String packName = c.getString(0);
                if (packName != null && packName.equals("screenon")) {
                    continue;
                }
                if (dbData.containsKey(packName)) {
                    int count = dbData.get(packName);
                    count++;
                    dbData.put(packName, count);
                } else {
                    dbData.put(packName, 1);
                }
            }
        }
//        db.close();
        return dbData;
    }

    /**
     * 获取当前app该时间段的的下一个app
     * */
    public HashMap<String, Integer> getNextAppThisPerion(String appName) {
        HashMap<String, Integer> dbData = new HashMap<String, Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(getSessionTableName(),
                new String[]{TableIndex.Session.NEXT_APP},
                " " + TableIndex.Session.NOW_APP + " = ? and " + TableIndex.Session.TIME_PERIOD + " = ?"
                , new String[]{appName, DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]},
                null, null, TableIndex.Session.ID + " desc", "100");

        dbData.clear();
        if (c != null && c.getCount() > 0) {
            dbData.put("all", c.getCount());
            while (c.moveToNext()) {
                String packName = c.getString(0);
                if (packName != null && packName.equals("screenon")) {
                    continue;
                }
                if (dbData.containsKey(packName)) {
                    int count = dbData.get(packName);
                    count++;
                    dbData.put(packName, count);
                } else {
                    dbData.put(packName, 1);
                }
            }
        }
//        db.close();
        return dbData;
    }
    /**
     * 获取亲密度比例
     */
    public int getIntimate(String appName) {
        int temp = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TableIndex.Intimate.TABLE_NAME,
                new String[] {TableIndex.Intimate.INTIMACY, TableIndex.Intimate.RECEIVE_COUNT},
                " " + TableIndex.Intimate.APP_PACKAGE + " = ? ",
                new String[] {appName},
                null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int receviceCount = c.getInt(c.getColumnIndex(TableIndex.Intimate.RECEIVE_COUNT));
            int intimate = c.getInt(c.getColumnIndex(TableIndex.Intimate.INTIMACY));
            if (receviceCount > 0) {
                temp = intimate * 100 / receviceCount;
            }
        }
//        db.close();
        return temp;
    }

    public void insertToLocation(String address, String addressType) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(TableIndex.Location.ADDRESS, address);
            cv.put(TableIndex.Location.ADDRESS_TYPE, addressType);
            db.insert(TableIndex.Location.TABLE_NAME, null, cv);
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
//            db.close();
        }
    }

    public String queryLocationType(String address) {
        if (address == null) {
            return null;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query(TableIndex.Location.TABLE_NAME,
                    null, " " + TableIndex.Location.ADDRESS + " = ?",
                    new String[]{address}
                    , null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToLast();
                return c.getString(c.getColumnIndex(TableIndex.Location.ADDRESS_TYPE));
            } else {
                return null;
            }
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
//            db.close();
        }
        return null;
    }

    /**
     * 获取当前app的下一个app
     * */
    public ArrayList<DataBean> getNextAppNew(String appName) {
        ArrayList<DataBean> dbData = new ArrayList<DataBean>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = this.getReadableDatabase().query(getSessionTableName(),
//                new String[] {TableIndex.Session.NEXT_APP}
                null,
                " " + TableIndex.Session.NOW_APP + " = ?", new String[] {appName},
                null, null, TableIndex.Session.ID + " desc", "100");

        dbData.clear();
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String packName = c.getString(c.getColumnIndex(TableIndex.Session.NEXT_APP));
                if (packName == null || packName.equals("screenon")) {
                    continue;
                }
                if (packName.equals("")) {
                    continue;
                }
//                if (dbData.containsKey(packName)) {
//                    DataBean s = dbData.get(packName);
//                    int count = s.getCount();
//                    count++;
//                    s.setCount(count);
//                    dbData.put(packName,s);
//                } else {
                    DataBean a
                            = new DataBean(c.getString(c.getColumnIndex(TableIndex.Session.TIME_PERIOD)),
                                                          c.getString(c.getColumnIndex(TableIndex.Session.LOCATION)),
                                                          c.getString(c.getColumnIndex(TableIndex.Session.NEXT_APP)),
                                                          1);
                    dbData.add(a);
//                }
            }
        }
//        db.close();
        return dbData;
    }

    /**
     * 获取当前app的下一个app
     * */
    public ArrayList<DataBean> getNextAppNew70(String appName) {
        ArrayList<DataBean> dbData = new ArrayList<DataBean>();
        SQLiteDatabase db = this.getReadableDatabase();

        Random r = new Random(24);
        int time = r.nextInt();

        Cursor c = this.getReadableDatabase().query(getSessionTableName(),
//                new String[] {TableIndex.Session.NEXT_APP}
                null,
                " " + TableIndex.Session.NOW_APP + " = ? and "
                        + TableIndex.Session.TIME_PERIOD + " != ? and "
                        + TableIndex.Session.TIME_PERIOD + " != ? and "
                        + TableIndex.Session.TIME_PERIOD + " != ?"
                , new String[] {appName, ""+ (time%24) , "" + ((time+1)%24),  "" + ((time+2)%24)},
                null, null, TableIndex.Session.ID + " desc", "100");

        dbData.clear();
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String packName = c.getString(c.getColumnIndex(TableIndex.Session.NEXT_APP));
                if (packName == null || packName.equals("screenon")) {
                    continue;
                }
                if (packName.equals("")) {
                    continue;
                }
                DataBean a
                        = new DataBean(c.getString(c.getColumnIndex(TableIndex.Session.TIME_PERIOD)),
                        c.getString(c.getColumnIndex(TableIndex.Session.LOCATION)),
                        c.getString(c.getColumnIndex(TableIndex.Session.NEXT_APP)),
                        1);
                dbData.add(a);
            }
        }
//        db.close();
        return dbData;
    }

    public void updateLocation(SQLiteDatabase db) {
        try {
            Cursor cursor = db.query(TableIndex.NewRecore.TABLE_NAME,
                    null,null,null, null, null, null);
            if (cursor != null) if (cursor.getCount() > 0) {
                cursor.moveToLast();
                int id = cursor.getInt(cursor.getColumnIndex(TableIndex.NewRecore.ID));
                ContentValues cv = new ContentValues();
                cv.put(TableIndex.NewRecore.LOCATION_LA, MyLanucher.mLastLatitude);
                cv.put(TableIndex.NewRecore.LOCATION_LO, MyLanucher.mLastLongtitude);
                db.update(TableIndex.NewRecore.TABLE_NAME, cv, TableIndex.NewRecore.ID + " = ?", new String[]{"" + id});
            }
        } catch (android.database.SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void insertNewRecored(SQLiteDatabase db, String packName, Context context, float lux
    , float[] acc, int notification) {
        ContentValues cv = new ContentValues();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int isWork = c.get(Calendar.DAY_OF_WEEK);
        if (isWork == 6 || isWork == 7) {
            isWork = 1;
        } else {
            isWork = 0;
        }
        cv.put(TableIndex.NewRecore.PACKAGE_NAME, packName);
        cv.put(TableIndex.NewRecore.USE_TIME, DateUtil.formatDateWithHourMinSecond(System.currentTimeMillis()));
        cv.put(TableIndex.NewRecore.USE_PERIOD, DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())]);
        cv.put(TableIndex.NewRecore.LOCATION_LA, MyLanucher.mLastLatitude);
        cv.put(TableIndex.NewRecore.LOCATION_LO, MyLanucher.mLastLongtitude);
        cv.put(TableIndex.NewRecore.IS_WORK, isWork);
        cv.put(TableIndex.NewRecore.GPRS, NetworkUtil.isGprsConnected(context));
        if (NetworkUtil.isWifiConnected(context) == 1) {
            cv.put(TableIndex.NewRecore.WIFI, NetworkUtil.getWifiSSID(context));
        }
        cv.put(TableIndex.NewRecore.BLUETOOTH, NetworkUtil.getBluetoothState(context));
        cv.put(TableIndex.NewRecore.LIGHT_SENSOR, lux);
        cv.put(TableIndex.NewRecore.ACC_SENSOR_X, acc[0]);
        cv.put(TableIndex.NewRecore.ACC_SENSOR_Y, acc[1]);
        cv.put(TableIndex.NewRecore.ACC_SENSOR_Z, acc[2]);
        cv.put(TableIndex.NewRecore.Notification, notification);
        db.insert(TableIndex.NewRecore.TABLE_NAME, null, cv);
    }

    public void getAllNewRecore() {
        Cursor cursor = this.getReadableDatabase().query(TableIndex.NewRecore.TABLE_NAME,
                null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                System.out.println("the data -> "
                + cursor.getString(cursor.getColumnIndex(TableIndex.NewRecore.PACKAGE_NAME ))
                + cursor.getString(cursor.getColumnIndex(TableIndex.NewRecore.USE_TIME ))
                + " " + cursor.getString(cursor.getColumnIndex(TableIndex.NewRecore.USE_PERIOD))
                + " " + cursor.getFloat(cursor.getColumnIndex(TableIndex.NewRecore.LOCATION_LA))
                + " " + cursor.getFloat(cursor.getColumnIndex(TableIndex.NewRecore.LOCATION_LO))
                + " " + cursor.getInt(cursor.getColumnIndex(TableIndex.NewRecore.IS_WORK))
                + " " + cursor.getInt(cursor.getColumnIndex(TableIndex.NewRecore.GPRS))
                + " " + cursor.getString(cursor.getColumnIndex(TableIndex.NewRecore.WIFI))
                + " " + cursor.getInt(cursor.getColumnIndex(TableIndex.NewRecore.BLUETOOTH))
                + " " + cursor.getFloat(cursor.getColumnIndex(TableIndex.NewRecore.LIGHT_SENSOR))
                + " " + cursor.getFloat(cursor.getColumnIndex(TableIndex.NewRecore.ACC_SENSOR_X))
                + " " + cursor.getFloat(cursor.getColumnIndex(TableIndex.NewRecore.ACC_SENSOR_Y))
                + " " + cursor.getFloat(cursor.getColumnIndex(TableIndex.NewRecore.ACC_SENSOR_Z))
                + " " + cursor.getInt(cursor.getColumnIndex(TableIndex.NewRecore.Notification))
                );
            }

        }
    }
}
