package com.example.cam.categoryUtil;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Package information utility class
 * 
 * @author : Daehee Han(@daniel_booknara)
 * @version : 1.0.0
 */
public class PackageUtil {
    private static String TAG = PackageUtil.class.getSimpleName();

    private PackageUtil() { }
    
	static String version = "1.0.0";
	static String selfPackage;
	
	public static String version(Context ctx) {
		if(ctx == null)
			return version;
		
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Logger.e(TAG, ExceptionUtil.exception(e));
		}	
	
		return version;
	}
	
	public static boolean isPackageInstalled(Context ctx, String pkgName) {
		try {
			ctx.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			Logger.e(TAG, ExceptionUtil.exception(e));

			return false;
		}

		return true;
	}
	
	public static Drawable getApplicationIcon(Context ctx, String packageName) {
		if(ctx == null || StringUtil.isEmpty(packageName))
			return null;
		
		try {
			return ctx.getPackageManager().getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			Logger.e(TAG, ExceptionUtil.exception(e));
		}	
	
		return null;
	}

	public static Drawable getApplicationIcon(Context ctx, ApplicationInfo appInfo) {
		if(ctx == null || appInfo == null)
			return null;
		
		return ctx.getPackageManager().getApplicationIcon(appInfo);
	}

	public static boolean checkifThisIsActive(Context ctx, RunningAppProcessInfo target){
	    boolean result = false;
	    ActivityManager.RunningTaskInfo info;

	    if(target == null)
	        return false;

	    final ActivityManager activityManager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);

	    List<ActivityManager.RunningTaskInfo> l = activityManager.getRunningTasks(9999);
	    Iterator<ActivityManager.RunningTaskInfo> i = l.iterator();

	    while(i.hasNext()){
	        info=i.next();
	        if(info.baseActivity.getPackageName().equals(target.processName)) {
	            result = true;
	            break;
	        }
	    }

	    return result;
	}
	
//	public static String getForegroundApp(Context ctx) {
//		final ActivityManager activityManager  =  (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
//		
//		return ((android.app.ActivityManager.RunningTaskInfo)activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
//	}
	
	public static boolean openPackageName(Context context, String packageName) {
		Log.d(TAG, "Open Package Name : " + packageName);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null)
            return false;

        intent.addCategory("android.intent.category.LAUNCHER");
        context.startActivity(intent);
        return true;
    }
	
	public static boolean isAppExist(Context context, String packageName) {
		try{
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch( NameNotFoundException e ) {
            return false;
        }
	}

	public static RunningAppProcessInfo getForegroundApp(Context ctx) {
	    RunningAppProcessInfo result = null, info = null;

	    final ActivityManager activityManager  =  (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);

	    List<RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
	    Iterator<RunningAppProcessInfo> i = l.iterator();
	    while(i.hasNext()) {
	        info = i.next();
	        if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && !isRunningService(ctx, info.processName)) {
	            result = info;
	            break;
	        }
	    }
	    return result;
	}

	public static boolean isRunningService(Context ctx, String processName) {
	    if(processName == null)
	        return false;

	    RunningServiceInfo service;

	    final ActivityManager activityManager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);

	    List<RunningServiceInfo> l = activityManager.getRunningServices(9999);
	    Iterator<RunningServiceInfo> i = l.iterator();
	    while(i.hasNext()){
	        service = i.next();
	        if(service.process.equals(processName))
	            return true;
	    }
	    return false;
	}

	public static List<PackageVO> searchPackages(Context ctx, String query, boolean system) {
		List<PackageVO> apps = getInstalledApps(ctx, system); /* false = no system packages */
		List<PackageVO> result = new ArrayList<PackageVO>();
	    for (PackageVO vo: apps) {
	    	if (vo.appname.matches("(.*)" + query + "(.*)")) {
	    		result.add(vo);
	    	}
	    }

	    return result;
	}


	public static List<PackageVO> getInstalledApps(Context ctx, boolean getSysPackages) {
	    List<PackageVO> res = new ArrayList<PackageVO>();
	    List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(0);

	    for (int i=0; i < packs.size();i++) {
	        PackageInfo p = packs.get(i);
	        if (!getSysPackages && isSystemPackage(p)) {
	            continue;
	        }

	        final PackageVO newInfo = new PackageVO();
	        newInfo.appname = p.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
	        newInfo.pname = p.packageName;
	        newInfo.versionName = p.versionName;
	        newInfo.versionCode = p.versionCode;
	        newInfo.firstInstallTime = p.firstInstallTime;
	        newInfo.lastUndateTime = p.lastUpdateTime;
	        newInfo.dataDir = p.applicationInfo.dataDir;
	        newInfo.targetSdkVersion = p.applicationInfo.targetSdkVersion;
	        newInfo.systemApp = isSystemPackage(p);

	        res.add(newInfo);
	    }
	    return res;
	}

    public static List<PackageVO> getLaunchableApps(Context ctx, boolean getSysPackages) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> draftApps = ctx.getPackageManager().queryIntentActivities(mainIntent, 0);

        List<PackageVO> res = new ArrayList<PackageVO>();
        for (int i=0; i < draftApps.size();i++) {
            ResolveInfo p = draftApps.get(i);

            final PackageVO newInfo = new PackageVO();
            try {
                newInfo.appname = (String)ctx.getPackageManager().getApplicationLabel(ctx.getPackageManager().getApplicationInfo(p.activityInfo.packageName, PackageManager.GET_UNINSTALLED_PACKAGES));
            } catch (NameNotFoundException e) {
                Log.e(TAG, "NameNotFoundException");
                CharSequence label = p.activityInfo.loadLabel(ctx.getPackageManager());
                if (StringUtil.isEmpty(label)) {
                    newInfo.appname = "";
                } else {
                    newInfo.appname = StringUtil.trim(label.toString());
                }
            }

            newInfo.pname = p.activityInfo.packageName;

            // Package Information
            PackageInfo packageInfo;
            try {
                packageInfo = ctx.getPackageManager().getPackageInfo(p.activityInfo.packageName, PackageManager.GET_PERMISSIONS);
                if (!getSysPackages && isSystemPackage(packageInfo)) {
                    continue;
                }

                newInfo.versionName = packageInfo.versionName == null ? "" : packageInfo.versionName;
                newInfo.versionCode = packageInfo.versionCode;
                newInfo.firstInstallTime = packageInfo.firstInstallTime;
                newInfo.lastUndateTime = packageInfo.lastUpdateTime;
                newInfo.dataDir = packageInfo.applicationInfo.dataDir;
                newInfo.targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
                newInfo.systemApp = isSystemPackage(packageInfo);
                newInfo.requestedPermissions = packageInfo.requestedPermissions;
				newInfo.icon = packageInfo.applicationInfo.loadIcon(ctx.getPackageManager());

            } catch (NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            res.add(newInfo);
        }

        return ListUtil.removeDuplicates(res);
    }

    public static PackageVO getLaunchableApp(Context ctx, String packageName) {
        PackageInfo p;
        try {
            p = ctx.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final PackageVO newInfo = new PackageVO();

        try {
            newInfo.appname = (String)ctx.getPackageManager().getApplicationLabel(ctx.getPackageManager().getApplicationInfo(p.packageName, PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundException");
            newInfo.appname = p.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
        }

        newInfo.pname = p.packageName;
        newInfo.versionName = p.versionName;
        newInfo.versionCode = p.versionCode;
        newInfo.firstInstallTime = p.firstInstallTime;
        newInfo.lastUndateTime = p.lastUpdateTime;
        newInfo.dataDir = p.applicationInfo.dataDir;
        newInfo.targetSdkVersion = p.applicationInfo.targetSdkVersion;
        newInfo.systemApp = isSystemPackage(p);

        return newInfo;
    }

	public static long getTotalApplicationStorageSize(PackageStats stat) {
		if (stat == null)
			return 0;

		long totalSize = 0;
		totalSize = getTotalInternalSize(stat) + getTotalExternalSize(stat);

	    return totalSize;
	}

//	private static long getTotalApplicationStorageSize(Context ctx, String packageName) {
//		long totalSize = 0;
//		PackageStats stat = new PackageStats(packageName);
//		if (stat == null)
//			return 0;
//
//		totalSize = getTotalInternalSize(stat) + getTotalExternalSize(stat);
//
//	    return totalSize;
//	}

	private static long getTotalInternalSize(PackageStats ps) {
        if (ps != null) {
            return ps.codeSize + ps.dataSize;
//            return ps.cacheSize + ps.codeSize + ps.dataSize;
        }
        return 0;
    }

	private static long getTotalExternalSize(PackageStats ps) {
        if (ps != null) {
            return ps.externalCodeSize + ps.externalDataSize
                    + ps.externalMediaSize + ps.externalObbSize;
        }
        return 0;
    }


	/**
	 * Return whether the given PackgeInfo represents a system package or not.
	 * User-installed packages (Market or otherwise) should not be denoted as
	 * system packages.
	 *
	 * @param pkgInfo
	 * @return
	 */
	private static boolean isSystemPackage(PackageInfo pkgInfo) {
	    return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	private static boolean isSelfPackage(PackageInfo pkgInfo) {
        return pkgInfo.packageName.equalsIgnoreCase(BuildConfig.APPLICATION_ID);

    }

	public static Drawable getFullResIcon(Context ctx, ActivityInfo info) {
	    Resources resources;
	    try {
	        resources = ctx.getPackageManager().getResourcesForApplication(info.applicationInfo);
	    } catch (NameNotFoundException e) {
	        resources = null;
	    }
	    if (resources != null) {
	        int iconId = info.getIconResource();
	        if (iconId != 0) {
	            return getFullResIcon(ctx, resources, iconId);
	        }
	    }
	    return getFullResDefaultActivityIcon(ctx);
	}

	public static Drawable getFullResIcon(Context ctx, Resources resources, int iconId) {
	    Drawable d;
	    try {
	        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
	        int iconDpi = activityManager.getLauncherLargeIconDensity();
	        d = resources.getDrawableForDensity(iconId, iconDpi);
	    } catch (Resources.NotFoundException e) {
	        d = null;
	    }

	    return (d != null) ? d : getFullResDefaultActivityIcon(ctx);
	}

	public static Drawable getFullResIcon(Context ctx, String packageName, int iconId) {
	    Resources resources;
	    try {
	        resources = ctx.getPackageManager().getResourcesForApplication(packageName);
	    } catch (NameNotFoundException e) {
	        resources = null;
	    }
	    if (resources != null) {
	        if (iconId != 0) {
	            return getFullResIcon(ctx, resources, iconId);
	        }
	    }
	    return getFullResDefaultActivityIcon(ctx);
	}

	public static Drawable getFullResDefaultActivityIcon(Context ctx) {
	    return getFullResIcon(ctx, Resources.getSystem(), android.R.mipmap.sym_def_app_icon);
	}

    public static int getPid(Context ctx, String packageName) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        int pid = -1;

        if (runningAppProcesses != null) {

            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                String[] pkgList = runningAppProcessInfo.pkgList;

                if(pkgList == null)
                    continue;

                for (String string : pkgList) {
                    if (string.equals(packageName)) {
                        pid = runningAppProcessInfo.pid;
                        break;
                    }
                }
            }
        }

        return pid;
    }

    private static final int NUMBER_OF_KILL_ATTEMPTS = 10;

    public static boolean killApplicationProcess(Context ctx, String packageName) {
        int pidToKill = getPid(ctx, packageName);

        Log.i(TAG, "killProcess(" + pidToKill + ")");

        try {
            Thread.sleep(1100); //give Launcher a time to get foreground after activateLauncherApplication() call above
        } catch (InterruptedException ignored) {
        }

        long timestamp = System.currentTimeMillis();
        int attempt = 0;
        while (attempt++ <= NUMBER_OF_KILL_ATTEMPTS) {

            ActivityManager manager = (ActivityManager) ctx.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

            // TODO: remove task first.

            List<RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();

            boolean killed = false;
            boolean found = false;

            if (runningAppProcesses == null) {
                return true;
            }

            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
//                Log.i(TAG, "Trying to kill app " + runningAppProcessInfo.processName);
                if (runningAppProcessInfo.pid == pidToKill) {

                    Log.i(TAG, "Trying to kill app " + runningAppProcessInfo.processName + ", importance = " + runningAppProcessInfo.importance);

                    List<RunningAppProcessInfo> oldProcesses = manager.getRunningAppProcesses();
                    int oldAppsCount = oldProcesses == null ? 0 : oldProcesses.size(); //TODO: simplify

                    Log.d(TAG, "runningAppsCount BEFORE: " + oldAppsCount);

                    manager.killBackgroundProcesses(packageName);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}

                    android.os.Process.sendSignal(pidToKill, android.os.Process.SIGNAL_KILL);

                    int newAppsCount = manager.getRunningAppProcesses().size();
                    Log.d(TAG, "runningAppsCount AFTER: " + newAppsCount);

                    if (newAppsCount != oldAppsCount) {

                        Log.d(TAG, "Killed application: " + packageName);
                        killed = true;

//                                    if(messageToShow != null) {
//                                        coreContext.getMessenger().broadcast(new ShowToastMessage(messageToShow));
//                                    }
                    }

                    found = true;
                    break;
                }
            }

            if (found && killed) {
                return true;
            }

        }

        return false;
    }

    public static boolean killProcess(Context context, String packageName) {
        int pid = getPid(context, packageName);

        ActivityManager manager  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        Method method = null;
        try {
            // Since API_LEVEL 8 : v2.2
            method = manager.getClass().getMethod("killBackgroundProcesses", new Class[] { String.class});
        } catch (NoSuchMethodException e) {
            // less than 2.2
            try {
                method = manager.getClass().getMethod("restartPackage", new Class[] { String.class });
            } catch (NoSuchMethodException ee) {
                Log.e(TAG, ExceptionUtil.exception(ee));
                return false;
            }
        }

        try {
            method.invoke(manager, packageName);
            Log.i(TAG, "kill method  " + method.getName() + " invoked " + packageName);
        } catch (Exception e) {
            Log.e(TAG, ExceptionUtil.exception(e));
            return false;
        }

        android.os.Process.killProcess(pid);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(TAG, ExceptionUtil.exception(e));
        }

        return isProcessRunning(context, packageName);

    }

    public static boolean isProcessRunning(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++){
            if(procInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }

}
