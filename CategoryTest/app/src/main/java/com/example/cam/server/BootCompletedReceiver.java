package com.example.cam.server;

/**
 * Created by cam on 1/12/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.cam.categorytest.CategroyMain;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent newIntent = new Intent(context, CategroyMain.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意，必须添加这个标记，否则启动会失败
            context.startActivity(newIntent);
        }
    }
}
