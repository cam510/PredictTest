package com.example.cam.ruibin;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.cam.MyApplication;
import com.example.cam.commonUtils.DateUtil;
import com.example.cam.server.ReceviceObject;
import com.example.cam.utils.NetworkUtil;
import com.example.cam.utils.SensorUtil;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordService extends Service {
    SensorManager sm;
    Sensor ligthSensor;
    Sensor accSensor;
    SensorUtil.AccListener accListener = new SensorUtil.AccListener();
    SensorUtil.MySensorListener lightListener = new SensorUtil.MySensorListener();
    private LocationClient mLocClient;

    private String lastApp = "";
    private String closeApp = "";
    private String lastNotificaApp = "";

    private boolean isStart = true;
    private boolean isScreenOn = true;
    private HashMap<String, ReceviceObject> myReceiveNotification = new HashMap<String, ReceviceObject>();
    private long lastTime = 0L;

    private boolean needUpload = false;

    public static void start(Context context) {
        Intent i = new Intent(context, RecordService.class);
        i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startService(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("RecordService.onCreate");

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(lightListener, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(accListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mLocClient = MyApplication.mLocationClient;
        setLocationOption();
        lastTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    try {
                        final String runningActivity = getRunningAppPackName();
//                        && !runningActivity.contains("LAUNCHER")
//                                && !runningActivity.contains("launcher")
//                                && !runningActivity.contains("homescreen")
//                                && !runningActivity.contains("systemui")
                        if (!lastApp.equals(runningActivity)
                                && isScreenOn
                                ) {
                            System.out.println("runningActivity -> " + runningActivity);
                            MyApplication.getmDbHelper().insertNewRecored(MyApplication.getmDbHelper().getWritableDatabase()
                                    , runningActivity, getApplicationContext(), lightListener.getLux(), accListener.getmAcc(), 0);
                            MyApplication.getmDbHelper().updateSecond(MyApplication.getmDbHelper().getWritableDatabase()
                                    , (System.currentTimeMillis() - lastTime) / 1000);
                            lastTime = System.currentTimeMillis();
                            lastApp = runningActivity;
//                            if (mLocClient.isStarted()) {
//                                mLocClient.stop();
//                            }
                            mLocClient.start();
                        } else if (!isScreenOn) {
                            MyApplication.getmDbHelper().updateSecond(MyApplication.getmDbHelper().getWritableDatabase()
                                    , (System.currentTimeMillis() - lastTime) / 1000);
                            lastTime = System.currentTimeMillis();
                        }
                        int hour = DateUtil.toHour(System.currentTimeMillis());
                        if (hour == 11 || hour == 23) {
                            if (needUpload) {
                                if (NetworkUtil.isGprsConnected(MyApplication.getAppInstance()) == 1
                                        || NetworkUtil.isWifiConnected(MyApplication.getAppInstance()) == 1) {
                                    new uploadThread().start();
                                }
                            }
//                            needUpload = false;
                        } else {
                            needUpload = true;
                        }
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();

        Intent intent = new Intent(this, DeamonService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.i("RecordService.onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.i("RecordService.onServiceDisconnected");
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("RecordService.onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        L.i("RecordService.onDestroy");
        super.onDestroy();

        sm.unregisterListener(lightListener);
        sm.unregisterListener(accListener);
        unbindService(mConnection);
    }

    //获取当前运行应用
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getRunningAppPackName() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String runApp = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            return runApp;
        } else {
            //method 1
//            ActivityManager activityManager = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
//            return activityManager.getRunningAppProcesses().get(0).pkgList[0];

            //method 2
//            final int PROCESS_STATE_TOP = 2;
//            try {
//                Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
//                List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
//                for (ActivityManager.RunningAppProcessInfo process : processes) {
//                    System.out.println("process -> " + process.pkgList[0] + " state -> " + processStateField.getInt(process) );
//                    if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                        int state = processStateField.getInt(process);
//                        if (state == PROCESS_STATE_TOP) {
//                            String[] packname = process.pkgList;
//                            return packname[0];
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }

            //method 3
            class RecentUseComparator implements Comparator<UsageStats> {

                @Override
                public int compare(UsageStats lhs, UsageStats rhs) {
                    return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
                }
            }
            RecentUseComparator mRecentComp = new RecentUseComparator();
            long ts = System.currentTimeMillis();
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000 * 10, ts);
            if (usageStats == null || usageStats.size() == 0) {
                if (!havaPermissionForTest(this)) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                    Looper looper = Looper.getMainLooper();
                    Handler handler = new Handler(looper);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RecordService.this,
                                    "权限不够\n请打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return lastApp;
            }
            Collections.sort(usageStats, mRecentComp);
            String currentTopPackage = usageStats.get(0).getPackageName();
            return currentTopPackage;
        }
//        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//        return runningActivity;
    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);				//打开gps
        option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
        option.setScanSpan(3000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }

    class uploadThread extends Thread {

        @Override
        public void run() {
            super.run();
            System.out.println("thread begin upload -> ");
            String phone = android.os.Build.MANUFACTURER;
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            String data = MyApplication.getmDbHelper().outputAllNewRecore();
//            String url = "http://120.24.65.236:8080/JCtest/index.jsp";
            String url = "http://120.24.65.236:8080/JCtest/saveLogAction.action";
            List<BasicNameValuePair> uploadParams = new ArrayList<BasicNameValuePair>();
            uploadParams.add(new BasicNameValuePair("phone", phone));
            uploadParams.add(new BasicNameValuePair("mac", deviceId));
            uploadParams.add(new BasicNameValuePair("log", data));

            try {
                org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
                org.apache.http.client.methods.HttpPost postMethod = new org.apache.http.client.methods.HttpPost(url);
                postMethod.setEntity(new UrlEncodedFormEntity(uploadParams, HTTP.UTF_8)); //将参数填入POST Entity中
                org.apache.http.HttpResponse response = httpClient.execute(postMethod); //执行POST方法
                System.out.println("result -> " + response.getStatusLine());
                if (response.getStatusLine().toString().contains("200")) {
                    needUpload = false;

                    HttpEntity httpEntity = response.getEntity();
                    try
                    {
                        InputStream inputStream = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                inputStream));
                        String result = "";
                        String line = "";
                        while (null != (line = reader.readLine()))
                        {
                            result += line;

                        }

                        System.out.println(result);
                        System.out.println("Response Content from server: " + result);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    needUpload = true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (org.apache.http.client.ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class uploadThread2 extends Thread {
        String multipart_form_data = "multipart/form-data";
        String twoHyphens = "--";
        String boundary = "****************fD4fH3gL0hK7aI6";    // 数据分隔符
        String lineEnd = System.getProperty("line.separator");

        @Override
        public void run() {
            super.run();
            HttpURLConnection conn = null;
            DataOutputStream output = null;
            BufferedReader input = null;

            String phone = android.os.Build.MANUFACTURER;
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            String data = MyApplication.getmDbHelper().outputAllNewRecore();
            Map<String, String> allParams = new HashMap<String, String>();
            allParams.put("phone", phone);
            allParams.put("mac", phone);
            allParams.put("log", phone);

            try {
                URL url = new URL("http://120.24.65.236:8080/JCtest/saveLogAction.action");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(120000);
                conn.setDoInput(true);        // 允许输入
                conn.setDoOutput(true);        // 允许输出
                conn.setUseCaches(false);    // 不使用Cache
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "keep-alive");
                conn.setRequestProperty("Content-Type", multipart_form_data + "; boundary=" + boundary);

                conn.connect();
                output = new DataOutputStream(conn.getOutputStream());

                StringBuilder sb = new StringBuilder();

                for(Map.Entry<String, String> param : allParams.entrySet()) {
                    sb.append(twoHyphens + boundary + lineEnd);
                    sb.append("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + lineEnd);
                    sb.append(lineEnd);
                    sb.append(param.getValue() + lineEnd);
                }
                try {
                    output.writeBytes(sb.toString());// 发送表单字段数据
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
                output.flush();

                int code = conn.getResponseCode();
                if(code != 200) {
                    throw new RuntimeException("请求‘" + url +"’失败！");
                }

                input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String oneLine;
                while((oneLine = input.readLine()) != null) {
                    response.append(oneLine + lineEnd);
                }
                System.out.println("result -> " + response.toString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                // 统一释放资源
                allParams.clear();
                try {
                    if(output != null) {
                        output.close();
                    }
                    if(input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if(conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean havaPermissionForTest(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
