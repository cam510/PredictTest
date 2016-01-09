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

    private boolean isStart = true;

    private ScreenBroadcastReceiver mScreenBroadcastReceiver;

    private String lastApp = "";

    private String closeApp = "";

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("run in server handler");
                    ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
                    String runningActivity = activityManager.getRunningAppProcesses().get(0).pkgList[0];
                    System.out.println("runningActivity -> " + runningActivity);
                }
            }, 5000);
            sendEmptyMessage(0);
        }
    };

    private static GetCurrentAppServer mInstance = null;

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
                        ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
                        String runningActivity = activityManager.getRunningAppProcesses().get(0).pkgList[0];
                        System.out.println("runningActivity -> " + runningActivity);
                        lastApp = runningActivity;
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.startWeChat(getApplicationContext(), handler);
            }
        }, 50);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.launcherPredictApp(getApplicationContext(), handler, "com.example.cam.categorytest");
            }
        }, 50);

//        handler.sendEmptyMessage(0);
//        if (isStart) {
//            mTimer.schedule(myTimerTask,5000,10000);
//        }
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
        registerScreenBroadcast();
        return super.onStartCommand(intent, flags, startId);
    }

//    @Override
//    public void onStart(Intent intent, int startId) {
//        System.out.println("onStart getcurrentAppServer");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (isStart) {
//                    try {
//                        System.out.println("run in server thread");
//                        ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
//                        String runningActivity = activityManager.getRunningAppProcesses().get(0).pkgList[0];
//                        System.out.println("runningActivity -> " + runningActivity);
//                        Thread.sleep(5000);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//        super.onStart(intent, startId);
//    }

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

    private TimerTask myTimerTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println("run in server TimerTask");
            ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
            String runningActivity = activityManager.getRunningAppProcesses().get(0).pkgList[0];
            System.out.println("runningActivity -> " + runningActivity);
        }
    };

    private Timer mTimer = new Timer(true);

    private void registerScreenBroadcast() {
        mScreenBroadcastReceiver = new ScreenBroadcastReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);  // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);   // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_USER_PRESENT); // 屏幕解锁广播
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); //长按关机
        getApplicationContext().registerReceiver(mScreenBroadcastReceiver, filter);
    }

    private BroadcastReceiver ScreenBroadcastReceiverTest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.i(LOG_TAG, "screen on");
//                ActivityUtil.launcherPredictApp(context, mHandler, closeApp);
                ActivityUtil.startWeChat(getApplicationContext(), mHandler);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.i(LOG_TAG, "screen off");
                closeApp = lastApp;
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                Log.i(LOG_TAG, "screen unlock");
                ActivityUtil.launcherPredictApp(getApplicationContext(), mHandler, closeApp);
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                Log.i(LOG_TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            }
        }
    };
}
