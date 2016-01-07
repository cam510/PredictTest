package com.example.cam.server;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.cam.categoryUtil.PackageUtil;
import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.commonUtils.CategroyUtil;
import com.example.cam.httpUtil.AsyncHttpClient;
import com.example.cam.httpUtil.AsyncHttpRequest;
import com.example.cam.httpUtil.AsyncHttpResponseHandler;
import com.example.cam.httpUtil.BaseJsonHttpResponseHandler;
import com.example.cam.httpUtil.LogHandler;
import com.example.cam.httpUtil.RequestHandle;
import com.example.cam.httpUtil.ResponseHandlerInterface;
import com.example.cam.httpUtil.SampleInterface;
import com.example.cam.httpUtil.SampleJSON;
import com.example.cam.httpUtil.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * Created by cam on 1/5/16.
 */
public class AppCategroyServer extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private int mGlobalIndex = 0;
    private String LOG_TAG = "CategroyServer";

    private ArrayList<PackageVO> mAppList;

    private final AsyncHttpClient aClient = new SyncHttpClient();

    public static AppCategroyServer mInstance = null;

    public AppCategroyServer() {
        super("");
//        mHandler = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (mGlobalIndex < mAppList.size()) {
//                    mGlobalIndex++;
//                    onRunButtonPressed();
//                }
//            }
//        };
    }

    public AppCategroyServer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("onHandleIntent");
        onRunButtonPressed();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onstart server");
        int flag = START_STICKY;
        mAppList = ((ArrayList<PackageVO>) PackageUtil.getLaunchableApps(getApplication().getApplicationContext(), true));
        return super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy server");
        super.onDestroy();
    }

    public void onRunButtonPressed() {
//        System.out.println("mGlobalIndex -> " + mGlobalIndex);
        if (mGlobalIndex == mAppList.size()) {
            showAllCategroy();
        } else {
            aClient.get(this, CategroyUtil._42_Matter_Heread + mAppList.get(mGlobalIndex).pname + CategroyUtil.get_42_Matter_End, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    System.out.println("responseBody -> " + new String(responseBody));
                    try {
                        JSONTokener jsonParser = new JSONTokener(new String(responseBody));
                        JSONObject jsonOb = (JSONObject) jsonParser.nextValue();
                        if (jsonOb.has("category")) {
                            mAppList.get(mGlobalIndex).category = (String) jsonOb.get("category");
                        }
                    } catch (JSONException ex) {
                        Log.e(LOG_TAG, ex.toString());
                    }

                    mGlobalIndex++;
                    onRunButtonPressed();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        System.out.println("responseBody -> " + new String(responseBody));
                    }
                    if (statusCode == 0) {

                    } else if (statusCode == 404){
                        mGlobalIndex++;
                        onRunButtonPressed();
                    } else {
                        System.out.println("statusCode -> " + statusCode);
                    }
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onRetry(int retryNo) {
                }

                @Override
                public void onFinish() {
                }
            });
        }
    }

    private void showAllCategroy() {
//        for (PackageVO p : mAppList) {
//            System.out.println("p.name " + p.appname + " p.package " + p.pname + " p.category " + p.category);
//        }
    }

    public static AppCategroyServer getmInstance() {
        if (mInstance == null) {
            mInstance = new AppCategroyServer("CategroyServer");
            return mInstance;
        } else {
            return mInstance;
        }
    }
}
