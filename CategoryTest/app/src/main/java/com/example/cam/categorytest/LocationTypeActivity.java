package com.example.cam.categorytest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

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
        System.out.println("enter location type activity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        location = getIntent().getStringExtra("location");
        curApp = getIntent().getStringExtra("appname");
        showDialog();
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
                        finish();
                    }
                });
        mDialog = builder.create();
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        if(mDialog !=null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

}
