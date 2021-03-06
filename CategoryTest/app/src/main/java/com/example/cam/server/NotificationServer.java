package com.example.cam.server;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.cam.MyApplication;
import com.example.cam.categorytest.LocationTypeActivity;
import com.example.cam.commonUtils.ActivityUtil;
import com.example.cam.predict.PredictUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cam on 1/27/16.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationServer extends NotificationListenerService {

    private String LOG_TAG = "NotificationServer";

    private LocationClient mLocClient;

    private String lastApp = "";
    private String closeApp = "";
    private String lastNotificaApp = "";
    private boolean isStart = true;
    private boolean isScreenOn = true;

    private HashMap<String, ReceviceObject> myReceiveNotification = new HashMap<String, ReceviceObject>();
    private Handler mHandler = new Handler();
    private PredictUtil predictUtil;

    @Override
    public void onCreate() {
        super.onCreate();
//        registerScreenBroadcast();
        System.out.println("onCreate NotificationServer");

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
                            lastApp = runningActivity;
                            MyApplication.getmDbHelper().updateAppLauncher(MyApplication.getmDbHelper().getWritableDatabase(), lastApp);
                            MyApplication.getmDbHelper().updatePeriod(MyApplication.getmDbHelper().getWritableDatabase(), lastApp);
                            MyApplication.myListener.setCurPackName(lastApp);
//                            PredictUtil.getmInstance(MyApplication.getAppInstance()).getSomeDataFromPackName(runningActivity);
                            PredictUtil.getmInstance(MyApplication.getAppInstance()).getSomeDataFromPackName("mm");
                            setLocationOption();
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
//        predictUtil = PredictUtil.getmInstance((MyApplication)getApplication());
    }

    //通知栏收到通知
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("open" + "-----" + sbn.toString());
        if (lastNotificaApp.equals(sbn.getPackageName())) {
            //防止重复多次查询重复app
        } else {
            lastNotificaApp = sbn.getPackageName();
            //屏幕关闭时候收到通知判定是否需要预加载app
            if (!isScreenOn && MyApplication.getmDbHelper().getIntimate(sbn.getPackageName()) > 70) {
                System.out.println("intimate > 70 app " + lastNotificaApp);
//                ActivityUtil.launcherPredictApp(getApplicationContext(), mHandler, lastNotificaApp, lastApp);
            }
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
                    System.out.println("enter add 亲密度");
                    MyApplication.getmDbHelper().updateIntimate(MyApplication.getmDbHelper().getWritableDatabase(), recevice, true);
                } else {
                    System.out.println("enter add 接收次数");
                    MyApplication.getmDbHelper().updateIntimate(MyApplication.getmDbHelper().getWritableDatabase(), recevice, false);
                }
            }
        }
//        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy NotificationServer");
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

    //屏幕开光广播
    private BroadcastReceiver ScreenBroadcastReceiverTest = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.i(LOG_TAG, "screen on");
                MyApplication.getmDbHelper().insertSession("screenon", "");
//                isScreenOn = true;
                if (!closeApp.equals(lastApp)
                        && !lastApp.equalsIgnoreCase(getApplicationContext().getPackageName())
                        && !closeApp.equalsIgnoreCase(getApplicationContext().getPackageName())) {
//                    ActivityUtil.launcherPredictApp(context, mHandler, predictUtil.predictNextApp(), closeApp);
//                    System.out.println("predict NextApp is -> " + predictUtil.predictNextApp());
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.i(LOG_TAG, "screen off");
                isScreenOn = false;
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                isScreenOn = true;
                Log.i(LOG_TAG, "screen unlock");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                Log.i(LOG_TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            }
        }
    };

    private void registerScreenBroadcast() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);  // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);   // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_USER_PRESENT); // 屏幕解锁广播
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); //长按关机
        getApplicationContext().registerReceiver(ScreenBroadcastReceiverTest, filter);
    }

    private boolean ServiceIsRunning(String className) {
        ActivityManager myManager = (ActivityManager)getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);				//打开gps
        option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
        option.setScanSpan(0);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }

    public static void startLocationType(String locationStr, String curPackName) {
        Intent i = new Intent(MyApplication.getAppInstance(), LocationTypeActivity.class);
        i.putExtra("location", locationStr);
        i.putExtra("appname", curPackName);
        MyApplication.getAppInstance().startActivity(i);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, START_STICKY, startId);
    }
}
