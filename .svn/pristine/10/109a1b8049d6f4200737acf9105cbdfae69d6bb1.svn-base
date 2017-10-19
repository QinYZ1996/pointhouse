package com.pointhouse.chiguan.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 请求接口
 * Created by Maclaine on 2017/6/26.
 */

public interface  RequestServices {
    @GET
    Observable<JSONObject> getStringRx(@Url String url);
    @GET
    Observable<JSONObject> getWXUserInfo(@Url String url);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> getDownload(@Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("Range") String bytes, @Url String url);


    @POST("orderSign")
    Observable<JSONObject> getOrderInfo(@Body RequestBody requestBody);

    @GET
    Observable<JSONObject> UserLogin(@Url String url,@Query("linked[]") String... linked);

    @GET
    Observable<ResponseBody> getByURL(@Url String url);

    @GET
    Observable<JSONObject> get(@Url String url);

    @POST
    Observable<JSONObject> post(@Url String url,@Body RequestBody requestBody);


    @POST
    Observable<JSONObject> weChatBand(@Url String url,@Body RequestBody requestBody);

    @GET
    Observable<JSONObject> UserRegister(@Url String url);

    @GET
    Observable<JSONObject> EditPassWord(@Url String url);

    @POST
    Observable<JSONObject> UploadImage(@Url String url,@Body RequestBody requestBody);

    //我的收藏
    @GET
    Observable<JSONObject> myFavoriteList(@Url String url);
    //删除收藏
    @GET
    Observable<JSONObject> removemyFavorite(@Url String url);

    //会员价格
    @GET
    Observable<JSONObject> GetPrice(@Url String url);

    @POST
    Observable<JSONObject> JPushSelected(@Url String url,@Body RequestBody requestBody);

    @GET
    Observable<JSONObject> uploadRegID(@Url String url);

    @POST
    Observable<JSONObject> wxlogin(@Url String url,@Body RequestBody requestBody);
}
