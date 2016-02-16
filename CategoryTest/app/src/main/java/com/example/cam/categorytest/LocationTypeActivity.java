package com.example.cam.categorytest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("enter location type activity");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.location_type);
        location = getIntent().getStringExtra("location");
        curApp = getIntent().getStringExtra("appname");
//        showDialog();
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
        if(mDialog !=null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

    private void end() {
        if(mDialog !=null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
//        Intent i = new Intent(this, CategroyMain.class);
        finish();
    }

}
