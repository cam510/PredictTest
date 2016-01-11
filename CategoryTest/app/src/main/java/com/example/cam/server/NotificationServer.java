package com.example.cam.server;

/**
 * Created by cam on 1/11/16.
 */


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.cam.MyApplication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationServer extends NotificationListenerService {

    private HashMap<String, ReceviceObject> myReceiveNotification = new HashMap<String, ReceviceObject>();
    private Handler mHandler = new Handler();
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate NotificationServer");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("open" + "-----" + sbn.toString());
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

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        System.out.println("shut"+"-----"+sbn.toString());
        if (myReceiveNotification.get(sbn.getPackageName()) != null) {
            ReceviceObject recevice = myReceiveNotification.get(sbn.getPackageName());
            long currentTime = System.currentTimeMillis();
            int duration = (int) ((currentTime - recevice.getReceviceTime())/1000/60);
            //收到通知5分钟内打开亲密度+1
            if (duration <= 5
                    && getRunningAppPackName().equalsIgnoreCase(sbn.getPackageName())) {
                System.out.println("enter add 亲密度");
                MyApplication.getmDbHelper().updateIntimate(MyApplication.getmDbHelper().getWritableDatabase(), recevice);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy NotificationServer");
    }

    private String getRunningAppPackName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        return activityManager.getRunningAppProcesses().get(0).pkgList[0];
    }

}


