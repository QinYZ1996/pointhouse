package com.pointhouse.chiguan.common.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gyh on 2017/9/1.
 * 如果不想转换成javabean可以指定泛型为JsonObject，然后把请求对象直接返回
 */

public class RequestBuilder<T> {

    private Class<T> tClazz;
    private OnSystemErrorListener mOnSystemErrorListener;
    private OnBusinessErrorListener mOnBusinessErrorListener;

    public void setOnSystemErrorListener(OnSystemErrorListener mOnSystemErrorListener) {
        this.mOnSystemErrorListener = mOnSystemErrorListener;
    }

    public void setOnBusinessErrorListener(OnBusinessErrorListener mOnBusinessErrorListener) {
        this.mOnBusinessErrorListener = mOnBusinessErrorListener;
    }

    public RequestBuilder(Class<T> tClazz) {
        this.tClazz = tClazz;
    }

    public void getRequest(String method, String[] params, Context context, OnSuccessListener<T> onSuccessListener) {
        getRequest(method, params, null, context, onSuccessListener, null);
    }

    public void getRequest(String method, String[] params, Context context, OnSuccessListener<T> onSuccessListener, OnFailureListener onFailureListener) {
        getRequest(method, params, null, context, onSuccessListener, onFailureListener);
    }

    public void getRequest(String method, String[] params, String tag, Context context, OnSuccessListener<T> onSuccessListener) {
        getRequest(method, params, tag, context, onSuccessListener, null);
    }

    /**
     * 发送带token的get请求
     *
     * @param method            方法名
     * @param params            参数，没有则设为null
     * @param tag               log用tag
     * @param context
     * @param onSuccessListener 请求成功回调
     * @param onFailureListener 请求失败回调
     */
    public void getRequest(String method, String[] params, String tag, Context context, OnSuccessListener<T> onSuccessListener, OnFailureListener onFailureListener) {
        String url = JsonUtil.getURLWithArrayParamIfExists(method, params);
        RetrofitFactory.getInstance().getRequestServicesToken().get(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    JSONArray exceptions;
                    String errMsg;
                    switch (jsonObject.getIntValue("resultCode")) {
                        case 0: // 系统异常
                            exceptions = jsonObject.getJSONArray("exceptions");
                            if (tag != null) {
                                if (exceptions != null && exceptions.size() > 0) {
                                    errMsg = exceptions.getJSONObject(0).getString("message");
                                    Log.e(tag, errMsg);
                                }
                            }
                            ToastUtil.getToast(context, context.getString(R.string.req_sys_err_msg), "center", 0, 180).show();
                            if (mOnSystemErrorListener != null) {
                                mOnSystemErrorListener.onSystemError();
                            }
                            break;
                        case 1: // 正常
                            T t = JSON.parseObject(jsonObject.toString(), tClazz);
                            onSuccessListener.onSuccess(t);
                            break;
                        case 2: // 业务异常
                            exceptions = jsonObject.getJSONArray("exceptions");
                            if (exceptions != null && exceptions.size() > 0) {
                                errMsg = exceptions.getJSONObject(0).getString("message");
                                if (tag != null) {
                                    Log.e(tag, errMsg);
                                }
                                ToastUtil.getToast(context, errMsg, "center", 0, 180).show();
                            }
                            if (mOnBusinessErrorListener != null) {
                                mOnBusinessErrorListener.onBusinessError();
                            }
                            break;
                        default:
                            break;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (tag != null) {
                        String msg = throwable.getMessage();
                        if (msg != null && !msg.isEmpty()) {
                            Log.e(tag, msg);
                        }
                    }

                    if (onFailureListener == null) {
                        ToastUtil.getToast(context, context.getString(R.string.req_fail_msg), "center", 0, 180).show();
                    } else {
                        onFailureListener.onFailure(throwable);
                    }
                });
    }

    /**
     * 发送带token的get请求
     *
     * @param method            方法名
     * @param params            参数，没有则设为null
     * @param tag               log用tag
     * @param context
     * @param onSuccessListener 请求成功回调
     * @param onFailureListener 请求失败回调
     */
    public void postRequest(String method, String[] params, String tag, Context context, OnSuccessListener<T> onSuccessListener, OnFailureListener onFailureListener) {
        String url = JsonUtil.getURLWithArrayParamIfExists(method);
        org.json.JSONArray jsonArray = new org.json.JSONArray();
        if (params != null && params.length > 0) {
            for (String param : params) {
                jsonArray.put(param);
            }
        }
        RetrofitFactory.getInstance().getRequestServicesToken().post(url, RetrofitFactory.createRequestBody(jsonArray.toString()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    JSONArray exceptions;
                    String errMsg;
                    switch (jsonObject.getIntValue("resultCode")) {
                        case 0: // 系统异常
                            exceptions = jsonObject.getJSONArray("exceptions");
                            if (tag != null) {
                                if (exceptions != null && exceptions.size() > 0) {
                                    errMsg = exceptions.getJSONObject(0).getString("message");
                                    Log.e(tag, errMsg);
                                }
                            }
                            ToastUtil.getToast(context, context.getString(R.string.req_sys_err_msg), "center", 0, 180).show();
                            if (mOnSystemErrorListener != null) {
                                mOnSystemErrorListener.onSystemError();
                            }
                            break;
                        case 1: // 正常
                            T t = JSON.parseObject(jsonObject.toString(), tClazz);
                            onSuccessListener.onSuccess(t);
                            break;
                        case 2: // 业务异常
                            exceptions = jsonObject.getJSONArray("exceptions");
                            if (exceptions != null && exceptions.size() > 0) {
                                errMsg = exceptions.getJSONObject(0).getString("message");
                                if (tag != null) {
                                    Log.e(tag, errMsg);
                                }
                                ToastUtil.getToast(context, errMsg, "center", 0, 180).show();
                            }
                            if (mOnBusinessErrorListener != null) {
                                mOnBusinessErrorListener.onBusinessError();
                            }
                            break;
                        default:
                            break;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (tag != null) {
                        Log.e(tag, throwable.getMessage());
                    }

                    if (onFailureListener == null) {
                        ToastUtil.getToast(context, context.getString(R.string.req_fail_msg), "center", 0, 180).show();
                    } else {
                        onFailureListener.onFailure(throwable);
                    }
                });
    }

    public interface OnSuccessListener<D> {
        void onSuccess(D d);
    }

    public interface OnFailureListener {
        void onFailure(Throwable throwable);
    }

    public interface OnSystemErrorListener {
        void onSystemError();
    }

    public interface OnBusinessErrorListener {
        void onBusinessError();
    }
}
