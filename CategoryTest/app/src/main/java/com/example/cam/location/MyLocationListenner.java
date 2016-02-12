package com.example.cam.location;

import android.content.Intent;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.cam.MyApplication;
import com.example.cam.categorytest.LocationTypeActivity;
import com.example.cam.predict.PredictUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by cam on 1/11/16.
 * 重写百度地图定位
 */
public class MyLocationListenner implements BDLocationListener {

    private LocationClient mLocationClient;

    public MyLocationListenner () {}

    private String curPackName = "";
    private String lastLocation = "unkonow";

    private static Map<String, Integer> mLocationMap = new HashMap<String, Integer>() ;

    public MyLocationListenner(LocationClient locationClient) {
        mLocationClient = locationClient;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return ;
        }
        String locationStr = "";
        if (location.getTime() == null || location.getTime().equals("")) {
            System.out.println("time is null");
            if (!lastLocation.equals("")) {
                locationStr = lastLocation;
            } else {
                locationStr = getMaxLocation();
            }
        } else {
            System.out.println("location is -> " + location.getAddrStr());
            locationStr = location.getAddrStr();
            lastLocation = locationStr;
            if (mLocationMap.get(locationStr) == null) {
                mLocationMap.put(locationStr, 1);
            } else {
                int count = mLocationMap.get(locationStr);
                count++;
                mLocationMap.put(locationStr, count);
            }
        }
//        MyApplication.getmDbHelper().insertSession(curPackName, locationStr);
        if (lastLocation == null || locationStr == null) {
            if (lastLocation == null) {
                lastLocation = "unknow";
            }
            if (locationStr == null) {
                locationStr = "";
            }
        }
        String getLocation = MyApplication.getmDbHelper().queryLocationType(locationStr);
        if (getLocation == null
                || getLocation.equals("")) {
            if (!MyApplication.getAppInstance().isDialogShow()) {
                MyApplication.getAppInstance().setIsDialogShow(true);
                Intent i = new Intent(MyApplication.getAppInstance(), LocationTypeActivity.class);
                i.putExtra("location", locationStr);
                i.putExtra("appname", curPackName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppInstance().startActivity(i);
            }
        } else {
            MyApplication.getmDbHelper().insertSession(curPackName, MyApplication.getLocationType());
//            PredictUtil.getmInstance(MyApplication.getAppInstance()).getSomeDataFromPackName(curPackName);
        }
        mLocationClient.stop();

    }

    public void onReceivePoi(BDLocation poiLocation) {
        if (poiLocation == null){
            return ;
        }
//        StringBuffer sb = new StringBuffer(256);
//        sb.append("Poi time : ");
//        sb.append(poiLocation.getTime());
//        sb.append("\nerror code : ");
//        sb.append(poiLocation.getLocType());
//        sb.append("\nlatitude : ");
//        sb.append(poiLocation.getLatitude());
//        sb.append("\nlontitude : ");
//        sb.append(poiLocation.getLongitude());
//        sb.append("\nradius : ");
//        sb.append(poiLocation.getRadius());
//        if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//            sb.append("\naddr : ");
//            sb.append(poiLocation.getAddrStr());
//        }
////			if(poiLocation.hasPoi()){
//        if(poiLocation.hasAddr()){
//            sb.append("\nPoi:");
////				sb.append(poiLocation.getPoi());
//            sb.append(poiLocation.getAddrStr());
//        }else{
//            sb.append("noPoi information");
//        }
//        System.out.println(sb.toString());
        String locationStr = "";
        if (poiLocation.getTime() == null || poiLocation.getTime().equals("")) {
            System.out.println("time is null");
            if (!lastLocation.equals("")) {
                locationStr = lastLocation;
            } else {
                locationStr = getMaxLocation();
            }
        } else {
            System.out.println("location is -> " + poiLocation.getAddrStr());
            locationStr = poiLocation.getAddrStr();
            lastLocation = locationStr;
            if (mLocationMap.get(locationStr) == null) {
                mLocationMap.put(locationStr, 1);
            } else {
                int count = mLocationMap.get(locationStr);
                count++;
                mLocationMap.put(locationStr, count);
            }
        }
        MyApplication.getmDbHelper().insertSession(curPackName, locationStr);
        mLocationClient.stop();
    }

    public String getCurPackName() {
        return curPackName;
    }

    public void setCurPackName(String curPackName) {
        this.curPackName = curPackName;
    }

    public static String getMaxLocation() {
        String maxLocation = "";
        int tempCount = 0;
        for(HashMap.Entry<String, Integer> entry : mLocationMap.entrySet()) {
            if (entry.getValue() > tempCount) {
                maxLocation = entry.getKey();
            }
        }
        return maxLocation;
    }

    public void showDialog() {
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
    }
}
