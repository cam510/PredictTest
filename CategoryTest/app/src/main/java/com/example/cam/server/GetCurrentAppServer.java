package com.example.cam.server;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.cam.DB.DatabaseHelper;
import com.example.cam.MyApplication;
import com.example.cam.broadcast.ScreenBroadcastReceiver;
import com.example.cam.commonUtils.ActivityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cam on 1/7/16.
 */
public class GetCurrentAppServer extends IntentService{

    private String LOG_TAG = "GetCurrentAppServer";

    private Handler mHandler = new Handler();
    private DatabaseHelper dbHelper = MyApplication.getmDbHelper();

    private boolean isStart = true;
    private String lastApp = "";
    private String closeApp = "";
    private boolean isScreenOn = true;

    private static GetCurrentAppServer mInstance = null;

    private LocationClient mLocClient;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public GetCurrentAppServer() {
        super("GetCurrentAppServer");
    }

    public GetCurrentAppServer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("onHandleIntent getcurrentAppServer");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    try {
                        String runningActivity = getRunningAppPackName();
                        System.out.println("runningActivity -> " + runningActivity);
                        if (!lastApp.equals(runningActivity)
                                && isScreenOn
                                && !runningActivity.contains("LAUNCHER")
                                && !runningActivity.contains("launcher")) {
                            lastApp = runningActivity;
                            MyApplication.getmDbHelper().updateAppLauncher(MyApplication.getmDbHelper().getWritableDatabase(), lastApp);
                            MyApplication.getmDbHelper().updatePeriod(MyApplication.getmDbHelper().getWritableDatabase(), lastApp);
                            MyApplication.myListener.setCurPackName(lastApp);
                            setLocationOption();
                            if (mLocClient.isStarted()) {
                                mLocClient.stop();
                            }
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
        System.out.println("getCurrentAppServer destory");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStart = intent.getBooleanExtra("isStart", true);
        flags = START_STICKY;
        mLocClient = MyApplication.mLocationClient;
        setLocationOption();
        registerScreenBroadcast();
        return super.onStartCommand(intent, flags, startId);
    }


    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public static GetCurrentAppServer getmInstance() {
        if (mInstance == null) {
            mInstance = new GetCurrentAppServer();
            return mInstance;
        } else {
            return mInstance;
        }
    }

//    private TimerTask myTimerTask = new TimerTask() {
//        @Override
//        public void run() {
//            System.out.println("run in server TimerTask");
//            ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
//            String runningActivity = activityManager.getRunningAppProcesses().get(0).pkgList[0];
//            System.out.println("runningActivity -> " + runningActivity);
//        }
//    };
//
//    private Timer mTimer = new Timer(true);

    private void registerScreenBroadcast() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);  // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);   // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_USER_PRESENT); // 屏幕解锁广播
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); //长按关机
        getApplicationContext().registerReceiver(ScreenBroadcastReceiverTest, filter);
    }

    private BroadcastReceiver ScreenBroadcastReceiverTest = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.i(LOG_TAG, "screen on");
                isScreenOn = true;
                if (!closeApp.equals(lastApp)) {
//                    ActivityUtil.launcherPredictApp(context, mHandler, closeApp);
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.i(LOG_TAG, "screen off");
                closeApp = getRunningAppPackName();
                isScreenOn = false;
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                Log.i(LOG_TAG, "screen unlock");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                Log.i(LOG_TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            }
        }
    };

    private String getRunningAppPackName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        return activityManager.getRunningAppProcesses().get(0).pkgList[0];
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
