package com.netease.nim.uikit.common.util.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Retrofit实例工厂
 * Created by Maclaine on 2017/6/26.
 */

public class RetrofitFactory {
    private static RetrofitFactory retrofitFactory;
    private static RequestServices requestServices;
    private static RequestServices requestServicesToken;

    private RetrofitFactory() {
        resetRetrofitFactory(null);
    }

    public static RetrofitFactory getInstance() {
        if (retrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofitFactory == null)
                    retrofitFactory = new RetrofitFactory();
            }
        }
        return retrofitFactory;
    }

    /**
     * @return 不带token的service
     */
    public RequestServices getRequestServices() {
        return requestServices;
    }

    /**
     * @return 带token的service.注意如果没有注入token,则会返回不带token的
     */
    public RequestServices getRequestServicesToken() {
        if(requestServicesToken==null){
            return requestServices;
        }
        return requestServicesToken;
    }

    /**
     * 注入token
     * @param token token
     */
    public static void resetRetrofitFactory(final String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        try {
                            if (token != null) {
                                builder.addHeader("token", token);
                            }
                            builder.addHeader("platform", "android")
                                    .addHeader("Connection", "close");//关闭连接，不让它保持连接
                            if(request.header("Content-Type")==null){
                                builder.addHeader("Content-Type","application/json; charset=utf-8");//编码
                            }
                        }catch (Exception e){
                            Log.e("RetrofitFactory",e.getMessage());
                        }
                        return chain.proceed(builder.build());
                    }
                })
                .retryOnConnectionFailure(false)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_BASE)
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())//fastjson转换器
//                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .build();
        if(token==null){
            requestServices = retrofit.create(RequestServices.class);
        }else{
            requestServicesToken= retrofit.create(RequestServices.class);
        }
    }

    public static RequestBody createRequestBody(String param){
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),param);
    }

    public static void ErrorMessage(Context context, JSONObject jsonObject,String defaultMsg){
        if(jsonObject.getInteger("resultCode")==2){
            Toast.makeText(context, jsonObject.getJSONArray("exceptions").getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, defaultMsg, Toast.LENGTH_SHORT).show();
        }
    }

}
