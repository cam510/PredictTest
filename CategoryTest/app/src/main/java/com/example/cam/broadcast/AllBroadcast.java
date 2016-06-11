package com.example.cam.broadcast;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

import com.example.cam.MyApplication;
import com.example.cam.location.MyLocationListenner;

/**
 * Created by cam on 6/11/16.
 */
public class AllBroadcast extends BroadcastReceiver{

    public static String ACTION_CONNECTIVE = ConnectivityManager.CONNECTIVITY_ACTION;
    public static String ACTION_HEADPHONE = "android.intent.action.HEADSET_PLUG";
    public static String ACTION_CHARGE = Intent.ACTION_BATTERY_CHANGED;
    public static String ACTION_BLUETHOOD = BluetoothDevice.ACTION_ACL_CONNECTED;

    public static final String EVENT_WIFI = "WiFiConnected";
    public static final String EVENT_GPRS = "DataConnected";
    public static final String EVENT_BLUETHOOD = "BluetoothConnected";
    public static final String EVENT_HEADPHONE = "AudioCable";
    public static final String EVENT_CHARGE = "ChargeCable";
    public static final String EVENT_LOCATION = "LocationChanged";

    public AllBroadcast() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

            if(netInfo != null && netInfo.isAvailable()) {
                /////////////网络连接
                System.out.println("enter the network");
                String name = netInfo.getTypeName();
                if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
                    /////WiFi网络
                    WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    MyApplication.getmDbHelper().insertTarger(EVENT_WIFI, ssid, MyLocationListenner.lastLa, MyLocationListenner.lastLo);
                }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
                    /////有线网络

                }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                    /////////3g网络
                    String data = "gprs";
                    MyApplication.getmDbHelper().insertTarger(EVENT_GPRS, data, MyLocationListenner.lastLa, MyLocationListenner.lastLo);
                }
            } else {
                ////////网络断开

            }

        } else if (action.equals(ACTION_HEADPHONE)) {
            if(intent.hasExtra("state")){
                if(intent.getIntExtra("state", 0)==0){
                }
                else if(intent.getIntExtra("state", 0)==1){
                    System.out.println("enter the headphone");
                    MyApplication.getmDbHelper().insertTarger(EVENT_HEADPHONE, "", MyLocationListenner.lastLa, MyLocationListenner.lastLo);
                }
            }
        } else if (action.equals(ACTION_CHARGE)) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            if (isCharging) {
                System.out.println("enter the charge");
                MyApplication.getmDbHelper().insertTarger(EVENT_CHARGE, "", MyLocationListenner.lastLa, MyLocationListenner.lastLo);
            }
        } else if (action.equals(ACTION_BLUETHOOD)) {
            System.out.println("enter connect the bluethood");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = "";
            if (device != null) {
                name = device.getName();
                MyApplication.getmDbHelper().insertTarger(EVENT_BLUETHOOD, name, MyLocationListenner.lastLa, MyLocationListenner.lastLo);
            }
        }
    }
}
