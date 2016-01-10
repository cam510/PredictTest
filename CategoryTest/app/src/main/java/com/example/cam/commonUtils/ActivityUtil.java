package com.example.cam.commonUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by cam on 1/8/16.
 */
public class ActivityUtil {

    private static Handler mHandler = new Handler();

    public static void startWeChat(final Context context, Handler handler) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    context.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void launcherPredictApp(final Context context, Handler handler, final String lastApp) {
        startWeChat(context, handler);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("launcherPredictApp --> " + lastApp);
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(lastApp);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 600);
    }
}
