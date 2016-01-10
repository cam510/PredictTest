package com.example.cam.server;

import android.app.IntentService;
import android.content.Intent;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.cam.MyApplication;

/**
 * Created by cam on 1/11/16.
 */
public class LocationServer  extends IntentService {

    private LocationClient mLocClient;

    private String packName = "";

    public LocationServer() {
        super("LocationServer");
    }

    public LocationServer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mLocClient.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocClient = MyApplication.mLocationClient;
        packName = intent.getStringExtra("packName");
        MyApplication.myListener.setCurPackName(packName);
        setLocationOption();
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);				//打开gps
//        option.setCoorType(mCoorEdit.getText().toString());		//设置坐标类型
        option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
        option.setScanSpan(0);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }
}
