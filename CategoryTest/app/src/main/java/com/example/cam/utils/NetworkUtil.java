package com.example.cam.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by MoreSmart-PC007 on 2016/3/25.
 */
public class NetworkUtil {

    public static int isGprsConnected(Context context) {
        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isGprsConnected=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ? true : false ;
        if (isGprsConnected) {
            return 1;
        }
        return 0;
    }

    public static int isWifiConnected(Context context) {
        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConnected=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true : false ;
        if (isWifiConnected) {
            return 1;
        }
        return 0;
    }

    public static String getWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    public static int getBluetoothState(Context context) {
        BluetoothAdapter blueadapter= BluetoothAdapter.getDefaultAdapter();
        if (blueadapter != null) {
            if (blueadapter.isEnabled()) {
                return 1;
            }
        }
        return 0;
    }
}
