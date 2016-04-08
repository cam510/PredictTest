package com.example.cam.ruibin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        L.i("AlarmReceiver.onReceive: action=" + intent.getAction());

        DeamonService.start(context.getApplicationContext());

        Intent i = new Intent(context, AlarmReceiver.class);
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(context, PendingIntent.FLAG_UPDATE_CURRENT, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager localAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        localAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, 300000, pi);
    }
}
