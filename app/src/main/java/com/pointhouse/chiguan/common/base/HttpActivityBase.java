package com.pointhouse.chiguan.common.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.MotionEvent;
import android.widget.Toast;

import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.common.http.HttpBase;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ljj on 2017/7/13.
 */

public abstract class HttpActivityBase extends PermissionActivityBase {
    protected boolean updateFlg;
    private static OkHttpClient mOkHttpClient = null;

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstance();
    }

    //获取okhttpclient单例对象
    public void getInstance() {
        if (mOkHttpClient == null) {
            synchronized (HttpBase.class) {
                if (mOkHttpClient == null)
                    initOkHttpClient();
            }
        }
    }

    private void initOkHttpClient() {
        File sdcache = SDCardHelper.getSDCacheDir(this);

        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }

    /**
     * get异步请求
     */
    public void getAsynHttp(String url,String json,String token,int flg) {
        String encodeJson = "";
        try {
            encodeJson = URLEncoder.encode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request.Builder requestBuilder = new Request.Builder().url(url +"/"+ encodeJson);
        requestBuilder.method("GET", null);
        Request request;
        if(token == null || token.isEmpty())
        {
            request = requestBuilder
                    .addHeader("platform","android")
                    .addHeader("Content-Type","application/json; charset=utf-8")
                    .build();
        } else {
            request = requestBuilder
                    .addHeader("platform","android")
                    .addHeader("token",token)
                    .addHeader("Content-Type","application/json; charset=utf-8")
                    .build();
        }
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络请求失败，请检查您的网络设置" , Toast.LENGTH_SHORT).show();
                        getAsynHttpResponse(null, null, flg, -1);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonbody =  response.body().string();
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONTokener jsonParser = new JSONTokener(jsonbody);
                            JSONObject jSONObject = null;
                            try {
                                jSONObject = (JSONObject) jsonParser.nextValue();
                                if(jSONObject.getInt("resultCode") == 0){
                                    Toast.makeText(getApplicationContext(), "系统异常，请稍后再试！" , Toast.LENGTH_SHORT).show();
                                    getAsynHttpResponse(call, jsonbody, flg, -1);
                                    return;
                                }

                                if(jSONObject.getInt("resultCode") == 2){
                                    Toast.makeText(getApplicationContext(), jSONObject.getJSONArray("exceptions").getJSONObject(0).getString("message") , Toast.LENGTH_SHORT).show();
                                    getAsynHttpResponse(call, jsonbody, flg, -1);
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            getAsynHttpResponse(call, jsonbody, flg, 1);
                        }
                    });
            }
        });
    }

    /**
     * get异步请求
     */
    public void getAsynHttpTest(String url,String json,int flg) {

        Request.Builder requestBuilder = new Request.Builder().url(url + json);
        requestBuilder.method("GET", null);
        Request request = requestBuilder
                .build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求失败" , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json =  response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAsynHttpResponse(call, json, flg,1);
                    }
                });
            }
        });
    }

    public abstract void getAsynHttpResponse(Call call, String json,int flg,int error);

    /**
     * post异步请求
     */
    public void postAsynHttp111(String url, Map<String, String> mapParams, int flg) {
        updateFlg = false;

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            builder.add(key, mapParams.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求失败" , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json =  response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postAsynHttpResponse(call, json, flg);
                        }
                    });
                }
        });
    }

    public abstract void postAsynHttpResponse(Call call, String json,int flg) ;


    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            //当手指移动的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) {
                MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
            }else if(y2 - y1 > 50) {
                MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
