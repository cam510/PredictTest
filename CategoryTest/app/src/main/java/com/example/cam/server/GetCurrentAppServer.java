package com.example.cam.server;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cam on 1/7/16.
 */
public class GetCurrentAppServer extends IntentService{

    private boolean isStart = true;

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
                        System.out.println("run in server thread");
                        ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
                        String runningActivity = activityManager.getRunningAppProcesses().get(0).pkgList[0];
                        System.out.println("runningActivity -> " + runningActivity);
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
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
        System.out.println("onStartCommand getcurrentAppServer");
        isStart = intent.getBooleanExtra("isStart", true);
        System.out.println("start ->" + isStart);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
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
}
