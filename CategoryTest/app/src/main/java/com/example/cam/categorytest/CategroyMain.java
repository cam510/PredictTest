package com.example.cam.categorytest;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.cam.MyApplication;
import com.example.cam.categoryUtil.PackageUtil;
import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.commonUtils.ActivityUtil;
import com.example.cam.commonUtils.CategroyUtil;
import com.example.cam.httpUtil.AsyncHttpClient;
import com.example.cam.httpUtil.AsyncHttpRequest;
import com.example.cam.httpUtil.BaseJsonHttpResponseHandler;
import com.example.cam.httpUtil.RequestHandle;
import com.example.cam.httpUtil.ResponseHandlerInterface;
import com.example.cam.httpUtil.SampleInterface;
import com.example.cam.httpUtil.SampleJSON;
import com.example.cam.server.AppCategroyServer;
import com.example.cam.server.GetCurrentAppServer;
import com.example.cam.server.LocationServer;
import com.example.cam.server.NotificationServer;

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

public class CategroyMain extends AppCompatActivity implements SampleInterface {

    private int mGlobalIndex = 0;
    private String LOG_TAG = "CategroyMain";
    private static final int GET_NEXT_CATEGROY = 0;
    private static final int DISSMISS_DIALOG = 1;

    private ArrayList<PackageVO> nullList;
    private Handler mHandler;
    private final List<RequestHandle> requestHandles = new LinkedList<RequestHandle>();

    private AlertDialog initDialog;

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient() {

        @Override
        protected AsyncHttpRequest newAsyncHttpRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, ResponseHandlerInterface responseHandler, Context context) {
            AsyncHttpRequest httpRequest = getHttpRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
            return httpRequest == null
                    ? super.newAsyncHttpRequest(client, httpContext, uriRequest, contentType, responseHandler, context)
                    : httpRequest;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categroy_main);

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GET_NEXT_CATEGROY) {
                    if (mGlobalIndex < nullList.size()) {
                        mGlobalIndex++;
                        onRunButtonPressed();
                    }
                } else if (msg.what == DISSMISS_DIALOG){
                    if (initDialog.isShowing()) {
                        initDialog.dismiss();
                    }
                }
            }
        };

        showProcessDialog();
        //解除注释 启动分类应用服务
        runDB();

//        startService(new Intent(this, NotificationServer.class));
//        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);
//        try {
//            if (ActivityUtil.hasSomePermission("android.permission.BIND_NOTIFICATION_LISTENER_SERVICE", getApplicationContext())) {
//                startService(new Intent(this, NotificationServer.class));
//            } else {
//                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//                startActivity(intent);
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    private void runDB() {
        MyApplication.getmDbHelper().insertAppTable(MyApplication.getmDbHelper().getWritableDatabase(),
                ((ArrayList<PackageVO>) PackageUtil.getLaunchableApps(getApplicationContext(), true)));
        System.out.println("db count -> " + MyApplication.getmDbHelper().getAppTableCount(MyApplication.getmDbHelper().getReadableDatabase()));
        nullList = MyApplication.getmDbHelper().getNullCategroyList(MyApplication.getmDbHelper().getReadableDatabase());
        if (nullList.size() > 0) {
            onRunButtonPressed();
        } else {
            MyApplication.getmDbHelper().queryCategroy(MyApplication.getmDbHelper().getReadableDatabase());
        }
//        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Context ctx() {
        return this;
    }

    @Override
    public List<RequestHandle> getRequestHandles() {
        return null;
    }

    @Override
    public void addRequestHandle(RequestHandle handle) {
        if (null != handle) {
            requestHandles.add(handle);
        }
    }

    @Override
    public void onRunButtonPressed() {
        System.out.println("mGlobalIndex -> " + mGlobalIndex);
        if (mGlobalIndex == nullList.size()) {
            showAllCategroy();
        } else {
            addRequestHandle(executeSample(getAsyncHttpClient(),
                    CategroyUtil._42_Matter_Heread + nullList.get(mGlobalIndex).pname + CategroyUtil.get_42_Matter_End,
                    getRequestHeaders(),
                    getRequestEntity(),
                    getResponseHandler()));
        }
    }

    @Override
    public void onCancelButtonPressed() {

    }

    @Override
    public Header[] getRequestHeaders() {
        List<Header> headers = getRequestHeadersList();
        return headers.toArray(new Header[headers.size()]);
    }

    @Override
    public HttpEntity getRequestEntity() {
        String bodyText;
        if (isRequestBodyAllowed() && (bodyText = null) != null) {
            try {
                return new StringEntity(bodyText);
            } catch (UnsupportedEncodingException e) {
                Log.e(LOG_TAG, "cannot create String entity", e);
            }
        }
        return null;
    }

    @Override
    public AsyncHttpClient getAsyncHttpClient() {
        return this.asyncHttpClient;
    }

    @Override
    public void setAsyncHttpClient(AsyncHttpClient client) {

    }

    @Override
    public AsyncHttpRequest getHttpRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, ResponseHandlerInterface responseHandler, Context context) {
        return null;
    }

    @Override
    public ResponseHandlerInterface getResponseHandler() {
        return new BaseJsonHttpResponseHandler<SampleJSON>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, SampleJSON response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, SampleJSON errorResponse) {
                if (statusCode == 0) {

                } else if (statusCode == 404) {

                } else {
                    System.out.println("rawJson Data -> " + rawJsonData);
                    Toast.makeText(CategroyMain.this, "StatusCode is " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected SampleJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                System.out.println("parseResponse " + rawJsonData);
                if (!isFailure) {
                    JSONTokener jsonParser = new JSONTokener(rawJsonData);
                    JSONObject jsonOb = (JSONObject) jsonParser.nextValue();
                    if (jsonOb.has("category")) {
                        nullList.get(mGlobalIndex).category = (String) jsonOb.get("category");
                    }
                }
                Message m = Message.obtain();
                m.what = GET_NEXT_CATEGROY;
                mHandler.sendEmptyMessage(m.what);
                return null;
            }

        };
    }

    private void showAllCategroy() {
        Message m = Message.obtain();
        m.what = DISSMISS_DIALOG;
        mHandler.sendEmptyMessage(m.what);
        MyApplication.getmDbHelper().updateAppCategroy(MyApplication.getmDbHelper().getWritableDatabase(), nullList);
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }

    public List<Header> getRequestHeadersList() {
        List<Header> headers = new ArrayList<Header>();
        String headersRaw = null;

        if (headersRaw != null && headersRaw.length() > 3) {
            String[] lines = headersRaw.split("\\r?\\n");
            for (String line : lines) {
                try {
                    int equalSignPos = line.indexOf('=');
                    if (1 > equalSignPos) {
                        throw new IllegalArgumentException("Wrong header format, may be 'Key=Value' only");
                    }

                    String headerName = line.substring(0, equalSignPos).trim();
                    String headerValue = line.substring(1 + equalSignPos).trim();
                    Log.d(LOG_TAG, String.format("Added header: [%s:%s]", headerName, headerValue));

                    headers.add(new BasicHeader(headerName, headerValue));
                } catch (Throwable t) {
                    Log.e(LOG_TAG, "Not a valid header line: " + line, t);
                }
            }
        }
        return headers;
    }

    @Override
    public String getDefaultURL() {
        return null;
    }

    @Override
    public String getDefaultHeaders() {
        return null;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return false;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public int getSampleTitle() {
        return 0;
    }

    @Override
    public boolean isCancelButtonAllowed() {
        return false;
    }

    @Override
    public RequestHandle executeSample(AsyncHttpClient client, String URL, Header[] headers, HttpEntity entity, ResponseHandlerInterface responseHandler) {
        return client.get(this, URL, headers, null, responseHandler);
    }

    private void showProcessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("init enterment plese wait")
                .setCancelable(false);
        initDialog = builder.create();
        initDialog.show();
    }

}
