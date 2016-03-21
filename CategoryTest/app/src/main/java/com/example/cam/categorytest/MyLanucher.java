package com.example.cam.categorytest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.cam.categoryUtil.PackageVO;

import java.util.ArrayList;

/**
 * Created by MoreSmart-PC007 on 2016/3/21.
 */
public class MyLanucher extends Activity implements View.OnClickListener {
    private ArrayList<PackageVO> mPredictList = new ArrayList<PackageVO>();
    private ArrayList<PackageVO> mAssicoList = new ArrayList<PackageVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViewById(R.id.tv_all_app).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
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
}
