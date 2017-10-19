package com.pointhouse.chiguan.common.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pointhouse.chiguan.common.http.HttpBase;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;

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

public abstract class HttpFragmentBase extends PermissionFragmentBase {
    protected boolean updateFlg;
    private static OkHttpClient mOkHttpClient = null;

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        getInstance();
        return null;
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
        File sdcache = SDCardHelper.getSDCacheDir(getContext());

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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), "网络请求失败，请检查您的网络设置", Toast.LENGTH_SHORT).show();
                            getAsynHttpResponse(null, null, flg, -1);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonbody =  response.body().string();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONTokener jsonParser = new JSONTokener(jsonbody);
                            JSONObject jSONObject = null;
                            try {
                                jSONObject = (JSONObject) jsonParser.nextValue();
                                if(jSONObject.getInt("resultCode") == 0){
                                    Toast.makeText(getContext(), "系统异常，请稍后再试！" , Toast.LENGTH_SHORT).show();
                                    getAsynHttpResponse(call, jsonbody, flg, -1);
                                    return;
                                }

                                if(jSONObject.getInt("resultCode") == 2){
                                    Toast.makeText(getContext(), jSONObject.getJSONArray("exceptions").getJSONObject(0).getString("message") , Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    /**
     * get异步请求
     */
    public void getAsynHttpTest(String url,String json,int flg) {
        updateFlg = false;

        Request.Builder requestBuilder = new Request.Builder().url(url + json);
        requestBuilder.method("GET", null);
        Request request = requestBuilder
                .build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json =  response.body().string();
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAsynHttpResponse(call, json, flg,1);
                        }
                    });
                }
            }
        });
    }

    public abstract void getAsynHttpResponse(Call call, String json,int flg,int error);

    /**
     * post异步请求
     */
    public void postAsynHttp(String url,Map<String, String> mapParams,int flg) {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json =  response.body().string();
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postAsynHttpResponse(call, json, flg);
                        }
                    });
                }
            }
        });
    }

    public abstract void postAsynHttpResponse(Call call, String json,int flg);
}
