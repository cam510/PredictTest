package com.example.cam.categorytest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.cam.categoryUtil.PackageUtil;
import com.example.cam.categoryUtil.PackageVO;
import com.example.cam.httpUtil.AsyncHttpClient;
import com.example.cam.httpUtil.AsyncHttpRequest;
import com.example.cam.httpUtil.BaseJsonHttpResponseHandler;
import com.example.cam.httpUtil.RequestHandle;
import com.example.cam.httpUtil.ResponseHandlerInterface;
import com.example.cam.httpUtil.SampleInterface;
import com.example.cam.httpUtil.SampleJSON;

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

    private String LOG_TAG = "CategroyMain";

    private ArrayList<PackageVO> packages;
    private final List<RequestHandle> requestHandles = new LinkedList<RequestHandle>();
    private ResponseHandlerInterface responseHandler;

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
        packages = ((ArrayList<PackageVO>) PackageUtil.getLaunchableApps(ctx(), true));

        for (PackageVO p : packages) {
            System.out.println("p.name " + p.appname + " p.package " + p.pname);
        }

        onRunButtonPressed();
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
        addRequestHandle(executeSample(getAsyncHttpClient(),
                "https://42matters.com/api/1/apps/lookup.json?p=com.tencent.mobileqq&access_token=ec82419c1335a1605952ea4d8f9e1ee0961d27f8",
                getRequestHeaders(),
                getRequestEntity(),
                getResponseHandler()));
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
//                debugHeaders(LOG_TAG, headers);
//                debugStatusCode(LOG_TAG, statusCode);
//                if (response != null) {
//                    debugResponse(LOG_TAG, rawJsonResponse);
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, SampleJSON errorResponse) {
//                debugHeaders(LOG_TAG, headers);
//                debugStatusCode(LOG_TAG, statusCode);
//                debugThrowable(LOG_TAG, throwable);
//                if (errorResponse != null) {
//                    debugResponse(LOG_TAG, rawJsonData);
//                }
                System.out.println("no connet --> resultcode " + statusCode);
            }

            @Override
            protected SampleJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
//                System.out.println("raw Json Data -> " + rawJsonData);
//                JSONTokener jsonParser = new JSONTokener(rawJsonData);
//                JSONObject jsonOb = (JSONObject) jsonParser.nextValue();
//                System.out.println("hst category -> " + jsonOb.has("category"));
//                System.out.println("is Failure ? -> " + isFailure);
//                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), SampleJSON.class).next();
                return null;
            }

        };
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

    public List<Header> getRequestHeadersList() {
        List<Header> headers = new ArrayList<Header>();
//        String headersRaw = headersEditText.getText() == null ? null : headersEditText.getText().toString();
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
}
