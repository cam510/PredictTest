package com.example.cam.categorytest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.example.cam.DB.TableIndex;
import com.example.cam.MyApplication;
import com.example.cam.predict.PredictUtil;


/**
 * Created by cam on 2/6/16.
 */
public class LocationTypeActivity extends Activity {

    private AlertDialog mDialog;
    private String type = "";

    private String location = "";
    private String curApp = "";

    private RadioGroup group;
    private RadioButton rHome, rSchool, rCompany, rOnWay, rElse;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("enter location type activity");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.location_type);
        location = getIntent().getStringExtra("location");
        curApp = getIntent().getStringExtra("appname");
//        showDialog();

        group = (RadioGroup) findViewById(R.id.locationgroup);
        rHome = (RadioButton) findViewById(R.id.home_btn);
        rSchool = (RadioButton) findViewById(R.id.school_btn);
        rCompany = (RadioButton) findViewById(R.id.company_btn);
        rOnWay = (RadioButton) findViewById(R.id.on_way_to_work_btn);
        rElse = (RadioButton) findViewById(R.id.else_btn);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rHome.getId()) {
                    MyApplication.setLocationType(rHome.getText().toString());
                } else if (checkedId == rSchool.getId()) {
                    MyApplication.setLocationType(rSchool.getText().toString());
                } else if (checkedId == rCompany.getId()) {
                    MyApplication.setLocationType(rCompany.getText().toString());
                } else if (checkedId == rOnWay.getId()) {
                    MyApplication.setLocationType(rOnWay.getText().toString());
                } else if (checkedId == rElse.getId()) {
                    MyApplication.setLocationType(rElse.getText().toString());
                }
                MyApplication.getmDbHelper().insertSession(curApp, MyApplication.getLocationType());
                MyApplication.getmDbHelper().insertToLocation(location, type);
                MyApplication.getAppInstance().setIsDialogShow(false);
                PredictUtil.getmInstance(MyApplication.getAppInstance()).getSomeDataFromPackName(curApp);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationTypeActivity.this)
                .setTitle("select location type")
                .setItems(TableIndex.S_ADDRESS_TYPE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type = TableIndex.S_ADDRESS_TYPE[which];
                        System.out.println("location -> " + location + " curapp -> " + curApp + " type -> " + type);
                        MyApplication.setLocationType(type);
                        //insert two ,1 location , 2 session
                        MyApplication.getmDbHelper().insertSession(curApp, MyApplication.getLocationType());
                        MyApplication.getmDbHelper().insertToLocation(location, type);
                        MyApplication.getAppInstance().setIsDialogShow(false);
                        PredictUtil.getmInstance(MyApplication.getAppInstance()).getSomeDataFromPackName(curApp);
//                        end();
                    }
                });
        mDialog = builder.create();
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                end();
            }
        });
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

    private void end() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
//        Intent i = new Intent(this, CategroyMain.class);
        finish();
    }

}
