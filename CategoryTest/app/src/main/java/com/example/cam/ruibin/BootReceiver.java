package com.example.cam.ruibin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        L.i("BootReceiver.onReceive: action=" + intent.getAction());

//        Intent i = new Intent(context, DeamonService.class);
//        context.startService(i);
        Intent i = new Intent(context, RecordService.class);
        context.startService(i);
    }
}
