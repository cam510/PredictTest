package com.example.cam.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.cam.commonUtils.ActivityUtil;

/**
 * Created by cam on 1/8/16.
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {

    private String LOG_TAG = "ScreenBroadcastReceiver";

    private Handler mHandler = new Handler();

    public ScreenBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.i(LOG_TAG, "screen on");
            ActivityUtil.startWeChat(context, mHandler);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.i(LOG_TAG, "screen off");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            Log.i(LOG_TAG, "screen unlock");
        } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
            Log.i(LOG_TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
        }
    }
}
