package com.example.cam.server;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.ActivityCompat;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.cam.DB.DatabaseHelper;
import com.example.cam.MyApplication;
import com.example.cam.predict.PredictUtil;
import com.example.cam.utils.NetworkUtil;
import com.example.cam.utils.SensorUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MoreSmart-PC007 on 2016/3/25.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class RecoreServer extends NotificationListenerService {

    SensorManager sm;
    Sensor ligthSensor;
    Sensor accSensor;
    private float mLuc;
    private float[] mAcc = new float[3];
    SensorUtil.AccListener accListener = new SensorUtil.AccListener();
    SensorUtil.MySensorListener lightListener = new SensorUtil.MySensorListener();
//    private LocationManager locationManager;
    private String locationProvider;
    private double latitude;
    private double longtitude;

    private LocationClient mLocClient;

    private String lastApp = "";
    private String closeApp = "";
    private String lastNotificaApp = "";
    private boolean isStart = true;
    private boolean isScreenOn = true;
    private HashMap<String, ReceviceObject> myReceiveNotification = new HashMap<String, ReceviceObject>();

    public RecoreServer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(lightListener, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(accListener, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //获取地理位置管理器
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocClient = MyApplication.mLocationClient;
        setLocationOption();
        //启动线程，不断检测当前应用
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    try {
                        final String runningActivity = getRunningAppPackName();
                        if (!lastApp.equals(runningActivity)
                                && isScreenOn
                                && !runningActivity.contains("LAUNCHER")
                                && !runningActivity.contains("launcher")
                                && !runningActivity.contains("homescreen")
                                && !runningActivity.contains("systemui")) {
                            System.out.println("runningActivity -> " + runningActivity);
                            MyApplication.getmDbHelper().insertNewRecored(MyApplication.getmDbHelper().getWritableDatabase()
                                    , runningActivity, getApplicationContext(), lightListener.getLux(), accListener.getmAcc(), 0);
                            lastApp = runningActivity;
//                            if (mLocClient.isStarted()) {
//                                mLocClient.stop();
//                            }
                            mLocClient.start();
                        }
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(lightListener);
        sm.unregisterListener(accListener);
    }

//    public void getLocation() {
//        //获取所有可用的位置提供器
//        List<String> providers = locationManager.getProviders(true);
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            //如果是GPS
//            locationProvider = LocationManager.GPS_PROVIDER;
//        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//            //如果是Network
//            locationProvider = LocationManager.NETWORK_PROVIDER;
//        } else {
////                Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            System.out.println("get location return");
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(locationProvider);
//        if (location != null) {
//            latitude = location.getLatitude();
//            longtitude = location.getLongitude();
//            MyApplication.getmDbHelper().updateLocation(MyApplication.getmDbHelper().getWritableDatabase());
//            System.out.println("latitude -> " + latitude);
//            System.out.println("longtitude -> " + longtitude);
//        } else {
//            System.out.println("location is null");
//        }
//    }

    //通知栏收到通知
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("open" + "-----" + sbn.toString());
        if (lastNotificaApp.equals(sbn.getPackageName())) {
            //防止重复多次查询重复app
        } else {
            lastNotificaApp = sbn.getPackageName();
            //屏幕关闭时候收到通知判定是否需要预加载app
//            if (!isScreenOn && MyApplication.getmDbHelper().getIntimate(sbn.getPackageName()) > 70) {
//                System.out.println("intimate > 70 app " + lastNotificaApp);
////                ActivityUtil.launcherPredictApp(getApplicationContext(), mHandler, lastNotificaApp, lastApp);
//            }
            if (!sbn.getPackageName().contains("systemui")) {
                if (myReceiveNotification.get(sbn.getPackageName()) == null) {
                    ReceviceObject recevice = new ReceviceObject(sbn.getPackageName(), System.currentTimeMillis());
                    myReceiveNotification.put(sbn.getPackageName(), recevice);
                } else {
                    ReceviceObject recevice = myReceiveNotification.get(sbn.getPackageName());
                    int count = recevice.getReceviceCount();
                    count++;
                    recevice.setReceviceCount(count);
                    recevice.setReceviceTime(System.currentTimeMillis());
                    myReceiveNotification.put(sbn.getPackageName(), recevice);
                }
            }
        }

//        super.onNotificationPosted(sbn);
    }

    //通知栏移除通知
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        System.out.println("shut" + "-----" + sbn.toString());
        if (!sbn.getPackageName().contains("systemui")) {
            if (myReceiveNotification.get(sbn.getPackageName()) != null) {
                ReceviceObject recevice = myReceiveNotification.get(sbn.getPackageName());
                long currentTime = System.currentTimeMillis();
                int duration = (int) ((currentTime - recevice.getReceviceTime()) / 1000 / 60);
                //收到通知5分钟内打开亲密度+1
                if (duration <= 5
                        && getRunningAppPackName().equalsIgnoreCase(sbn.getPackageName())) {
//                    System.out.println("enter add 亲密度");
//                    MyApplication.getmDbHelper().updateIntimate(MyApplication.getmDbHelper().getWritableDatabase(), recevice, true);
                    MyApplication.getmDbHelper().insertNewRecored(MyApplication.getmDbHelper().getWritableDatabase()
                            , sbn.getPackageName(), this, lightListener.getLux(), accListener.getmAcc(), 1);
                } else if (duration <= 10){
//                    System.out.println("enter add 接收次数");
//                    MyApplication.getmDbHelper().updateIntimate(MyApplication.getmDbHelper().getWritableDatabase(), recevice, false);
                    MyApplication.getmDbHelper().insertNewRecored(MyApplication.getmDbHelper().getWritableDatabase()
                            , sbn.getPackageName(), this, lightListener.getLux(), accListener.getmAcc(), 2);
                } else {
                    MyApplication.getmDbHelper().insertNewRecored(MyApplication.getmDbHelper().getWritableDatabase()
                            , sbn.getPackageName(), this, lightListener.getLux(), accListener.getmAcc(), 3);
                }
                myReceiveNotification.remove(sbn.getPackageName());
            }
        }
//        super.onNotificationRemoved(sbn);
    }

    //获取当前运行应用
    private String getRunningAppPackName() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            return runningActivity;
        } else {
            ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
            return activityManager.getRunningAppProcesses().get(0).pkgList[0];
        }
    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);				//打开gps
        option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
        option.setScanSpan(0);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }
}
