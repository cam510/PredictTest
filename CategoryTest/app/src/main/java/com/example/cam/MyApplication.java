package com.example.cam;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import com.baidu.location.LocationClient;
import com.example.cam.DB.DatabaseHelper;
import com.example.cam.categoryUtil.PackageUtil;
import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.location.MyLocationListenner;
import com.example.cam.server.AppCategroyServer;

import java.util.ArrayList;

/**
 * Created by cam on 1/7/16.
 */
public class MyApplication extends Application{

    private static DatabaseHelper mDbHelper;
    private static MyApplication AppInstance = null;

    public static LocationClient mLocationClient = null;
    public static MyLocationListenner myListener = null;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        AppInstance = this;
        mDbHelper = new DatabaseHelper(this);
        mDbHelper.onCreate(mDbHelper.getWritableDatabase());

        mLocationClient = new LocationClient(this);
        myListener = new MyLocationListenner(mLocationClient);
        mLocationClient.registerLocationListener(myListener);

//        runDB();
    }

    private void runDB() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDbHelper.insertAppTable(mDbHelper.getWritableDatabase(),
                        ((ArrayList<PackageVO>) PackageUtil.getLaunchableApps(AppInstance, true)));
                System.out.println("db count -> " + mDbHelper.getAppTableCount(mDbHelper.getReadableDatabase()));
                ArrayList<PackageVO> nullList = MyApplication.getmDbHelper().getNullCategroyList(MyApplication.getmDbHelper().getReadableDatabase());
//                if (nullList.size() > 0) {
//                    Intent categroyIntent = new Intent(getApplicationContext(), AppCategroyServer.class);
//                    categroyIntent.putStringArrayListExtra("nullList", nullList);
//                    startService(categroyIntent);
//                }
            }
        });
    }

    public static DatabaseHelper getmDbHelper() {
        return mDbHelper;
    }
}
