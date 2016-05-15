package com.example.cam.ruibin;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

public class DeamonService extends Service {

    private Binder mBinder = new Binder();

    public static void start(Context context) {
        Intent i = new Intent(context, DeamonService.class);
        i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startService(i);
    }

    public DeamonService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        L.i("DeamonService.onCreate");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(64324, new Notification());
        } else {
            startForeground(64324, new Notification());
            startService(new Intent(this, InnerService.class));
        }

        RecordService.start(this);
        AlarmReceiver.start(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("DeamonService.onStartCommand, flags=" + flags + ", START_STICKY=" + START_STICKY);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        L.i("DeamonService.onDestroy");
        super.onDestroy();
    }

    public static class InnerService extends Service {
        public IBinder onBind(Intent paramIntent) {
            return null;
        }

        public void onCreate() {
            super.onCreate();
            L.i("DeamonService$InnerService.onCreate");

            startForeground(64324, new Notification());
            stopSelf();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            L.i("DeamonService$InnerService.onStartCommand");
            return super.onStartCommand(intent, flags, startId);
        }

        public void onDestroy() {
            L.i("DeamonService$InnerService.onDestroy");
            stopForeground(true);
            super.onDestroy();
        }
    }
}
