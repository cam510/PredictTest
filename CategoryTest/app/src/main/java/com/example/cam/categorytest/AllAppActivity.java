package com.example.cam.categorytest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cam.MyApplication;
import com.example.cam.adapter.AppAdapter;
import com.example.cam.categoryUtil.PackageUtil;
import com.example.cam.categoryUtil.PackageVO;

import java.util.ArrayList;

/**
 * Created by MoreSmart-PC007 on 2016/3/21.
 */
public class AllAppActivity extends Activity{

    private ArrayList<PackageVO> mAllAppList;
    private GridView mAllAppGridView;
    private AppAdapter mAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app);

        mAllAppGridView = (GridView) findViewById(R.id.gv_all_app);
        mAllAppList = (ArrayList<PackageVO>) PackageUtil.getLaunchableApps(this, true);
        mAppAdapter = new AppAdapter(mAllAppList, this);
        mAllAppGridView.setAdapter(mAppAdapter);
        mAllAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageVO p = mAppAdapter.getItem(position);
                PackageUtil.openPackageName(AllAppActivity.this, p.pname);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyApplication.getmDbHelper().getAllNewRecore();
    }
}
