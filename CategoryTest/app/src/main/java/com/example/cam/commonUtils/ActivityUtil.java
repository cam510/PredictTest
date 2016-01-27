package com.example.cam.commonUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    public static void launcherPredictApp(final Context context, Handler handler, final String predictApp, final String lastApp) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(predictApp);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 500);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(lastApp);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 600);
    }

    public static boolean hasSomePermission(String permission, Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
//        boolean hasPermission = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission(permission, context.getPackageName()));
//        if (hasPermission) {
//            System.out.println("有权限");
//        } else {
//            System.out.println("木有权限");
//        }
        boolean hasPermission = false;
        PackageInfo pack = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        String[] permissionStrings = pack.requestedPermissions;
        if (permissionStrings.length > 0) {
            for (String per : permissionStrings) {
                System.out.println(per);
                if (per.equalsIgnoreCase(permission)) {
                    hasPermission = true;
                    System.out.println("有权限");
                    break;
                }
            }
        }
        return hasPermission;
    }
}
