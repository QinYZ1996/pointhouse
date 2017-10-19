package com.pointhouse.chiguan.common.api;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.AliPayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付工具类
 * Created by Maclaine on 2017/6/28.
 */

public class PayUtil {
    /**
     *
     * @param activity 当前Activity 支付宝必须在Activity中支付
     * @param productType 购买内容：1：课程专辑  2:文章  3：VIP
     * @param productID 购买目标：课程专辑ID/文章ID/0(当购买为VIP时购买目标ID设为0)
     * @param money 金额：购买目标金额（用于后台校验，防止前后台数据不匹配造成纠纷）
     * @return 观察者对象, 回调参数为AliPayResult
     * <br>判断resultStatus 为9000则代表支付成功
     * <br>该笔订单是否真实支付成功，需要依赖服务端的异步通知
     */
    public static Observable<AliPayResult> aliPayV2(Activity activity,String productType, String productID,Double money) {
        return RetrofitFactory.getInstance().getRequestServicesToken()
                .getOrderInfo(RetrofitFactory.createRequestBody(JSONObject.toJSON(new String[]{"1",productType,productID,String.valueOf(money)}).toString()))//向我方服务器请求订单签名信息
                .subscribeOn(Schedulers.newThread())//新建线程
                .map(jsonObject -> {
                    Log.d("PAY",jsonObject.toJSONString());
                    if (!(jsonObject.getInteger("resultCode")==1)) {
                        throw new Exception(jsonObject.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                    }
                    PayTask alipay = new PayTask(activity);//必须独立线程
                    Map<String, String> map = alipay.payV2(jsonObject.getJSONObject("resultObject").getString("orderStr"), true);
                    return new AliPayResult(map);
                })
                ;
    }


    public static Observable<PayReq> WXPay(String productType, String productID,Double money) {
        return RetrofitFactory.getInstance().getRequestServicesToken()
                .getOrderInfo(RetrofitFactory.createRequestBody(JSONObject.toJSON(new String[]{"2",productType,productID,String.valueOf(money)}).toString()))//向我方服务器请求订单签名信息
                .subscribeOn(Schedulers.newThread())//新建线程
                .map(jsonObject -> {
                    Log.d("PAY",jsonObject.toJSONString());
                    if (!(jsonObject.getInteger("resultCode")==1)) {
                        throw new Exception(jsonObject.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                    }
                    PayReq request=new PayReq();
                    JSONObject param=jsonObject.getJSONObject("resultObject").getJSONObject("orderParam");
                    request.appId=param.getString("appid");
                    request.partnerId=param.getString("partnerid");
                    request.prepayId=param.getString("prepay_id");
                    request.packageValue=param.getString("packageValue");
                    request.nonceStr=param.getString("nonceStr");
                    request.timeStamp=param.getString("timeStamp");
                    request.sign=param.getString("sign");
                    return request;
                })
                ;
    }
}
