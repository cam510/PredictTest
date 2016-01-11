package com.example.cam.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.cam.MyApplication;

/**
 * Created by cam on 1/11/16.
 */
public class MyLocationListenner implements BDLocationListener {

    private LocationClient mLocationClient;

    public MyLocationListenner () {}

    private String curPackName = "";

    public MyLocationListenner(LocationClient locationClient) {
        mLocationClient = locationClient;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return ;
        }
//        StringBuffer sb = new StringBuffer(256);
//        sb.append("time : ");
//        sb.append(location.getTime());
//        sb.append("\nerror code : ");
//        sb.append(location.getLocType());
//        sb.append("\nlatitude : ");
//        sb.append(location.getLatitude());
//        sb.append("\nlontitude : ");
//        sb.append(location.getLongitude());
//        sb.append("\nradius : ");
//        sb.append(location.getRadius());
//        if (location.getLocType() == BDLocation.TypeGpsLocation){
//            System.out.println("Type Gps Location");
//            sb.append("\nspeed : ");
//            sb.append(location.getSpeed());
//            sb.append("\nsatellite : ");
//            sb.append(location.getSatelliteNumber());
//        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//            System.out.println("Type NetWork Location");
//            sb.append("\n省:");
//            System.out.println("省:" + location.getProvince());
//            sb.append(location.getProvince());
//            sb.append("\n市");
//            System.out.println("市" + location.getCity());
//            sb.append(location.getCity());
//            sb.append("\n区/县:");
//            System.out.println("区/县:" + location.getDistrict());
//            sb.append(location.getDistrict());
//            sb.append("\naddr : ");
//            System.out.println("全地址:" + location.getAddrStr());
//            sb.append(location.getAddrStr());
//        }
//        sb.append("\nsdk version : ");
//        sb.append(mLocationClient.getVersion());
//        System.out.println(sb.toString());
        String locationStr = "";
        if (location.getTime() == null || location.getTime().equals("")) {
            System.out.println("time is null");
        } else {
            System.out.println("location is -> " + location.getAddrStr());
            locationStr = location.getAddrStr();
        }
        MyApplication.getmDbHelper().insertSession(curPackName, locationStr);
        mLocationClient.stop();
    }

    public void onReceivePoi(BDLocation poiLocation) {
        if (poiLocation == null){
            return ;
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("Poi time : ");
        sb.append(poiLocation.getTime());
        sb.append("\nerror code : ");
        sb.append(poiLocation.getLocType());
        sb.append("\nlatitude : ");
        sb.append(poiLocation.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(poiLocation.getLongitude());
        sb.append("\nradius : ");
        sb.append(poiLocation.getRadius());
        if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\naddr : ");
            sb.append(poiLocation.getAddrStr());
        }
//			if(poiLocation.hasPoi()){
        if(poiLocation.hasAddr()){
            sb.append("\nPoi:");
//				sb.append(poiLocation.getPoi());
            sb.append(poiLocation.getAddrStr());
        }else{
            sb.append("noPoi information");
        }
        System.out.println(sb.toString());
    }

    public String getCurPackName() {
        return curPackName;
    }

    public void setCurPackName(String curPackName) {
        this.curPackName = curPackName;
    }
}
