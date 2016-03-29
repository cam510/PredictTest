package com.example.cam.categorytest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.cam.MyApplication;
import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.server.NotificationServer;
import com.example.cam.server.RecoreServer;
import com.example.cam.utils.SensorUtil;

import java.util.ArrayList;

/**
 * Created by MoreSmart-PC007 on 2016/3/21.
 */
public class MyLanucher extends Activity implements View.OnClickListener {
    private ArrayList<PackageVO> mPredictList = new ArrayList<PackageVO>();
    private ArrayList<PackageVO> mAssicoList = new ArrayList<PackageVO>();

    private LocationClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViewById(R.id.tv_all_app).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);

        //测试启动位置
//        RecoreServer recoreServer = new RecoreServer();
//        startService(new Intent(this, recoreServer.getClass()));

        startService(new Intent(getApplicationContext(), RecoreServer.class));
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);

        mLocClient = MyApplication.mLocationClient;
        setLocationOption();
        mLocClient.start();

        MyApplication.getmDbHelper().getAllNewRecore();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent i;
        switch (id) {
            case R.id.tv_setting:
                System.out.println("enter setting");
                i = new Intent(Settings.ACTION_SETTINGS);
                startActivity(i);
                break;
            case R.id.tv_all_app:
                System.out.println("enter all app");
                i = new Intent(this, AllAppActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);				//打开gps
        option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
        option.setScanSpan(0);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
