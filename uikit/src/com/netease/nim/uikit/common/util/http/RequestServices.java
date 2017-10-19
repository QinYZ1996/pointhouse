package com.netease.nim.uikit.common.util.http;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 请求接口
 * Created by Maclaine on 2017/6/26.
 */

public interface  RequestServices {
    @GET
    Observable<JSONObject> get(@Url String url);

    @POST
    Observable<JSONObject> post(@Url String url, @Body RequestBody requestBody);
}
