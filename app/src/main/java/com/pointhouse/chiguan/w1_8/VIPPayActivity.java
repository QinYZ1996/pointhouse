package com.pointhouse.chiguan.w1_8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.annotation.ViewId;
import com.pointhouse.chiguan.common.api.PayUtil;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.AliPayResult;
import com.pointhouse.chiguan.common.util.AutoInject;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.alipay.sdk.app.statistic.c.v;
import static com.netease.nimlib.sdk.msg.constant.SystemMessageStatus.init;

/**
 * 会员购买
 * Created by Maclaine on 2017/7/25.
 */

public class VIPPayActivity extends Activity {
    private static final String TAG = "VIPPayActivity";

    public static final int ALIPAY = 0x001;
    public static final int WXPAY = 0x002;

    @ViewId(R.id.imgAliCheck)
    private ImageView imgAliCheck;
    @ViewId(R.id.imgWxCheck)
    private ImageView  imgWxCheck;
    @ViewId(R.id.txtPay)
    private TextView txtPay;
    @ViewId(R.id.viewAliCheck)
    private View viewAliCheck;
    @ViewId(R.id.viewWXCheck)
    private View viewWXCheck;
    @ViewId(R.id.txtVIP)
    private TextView txtVIP;
    @ViewId(R.id.imgBack)
    private ImageView imgBack;
    @ViewId(R.id.txtBuy)
    private TextView txtBuy;

    private int payType = ALIPAY;

    private Double price;
    private Integer vip=0;

    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_8_activity_vip);
        Intent intent=getIntent();
        price=intent.getExtras().getDouble("price");
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指移动的时候
            x2 = event.getX();
            y2 = event.getY();
            if (y1 - y2 > 50) {
                MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
            } else if (y2 - y1 > 50) {
                MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
            }
        }
        return super.dispatchTouchEvent(event);
    }

    //初始化
    private void init() {
        AutoInject.injectAll(this);
        initStyle();
        initListener();
    }


    //初始化样式
    private void initStyle() {
        txtVIP.setText("￥"+ ToolUtil.doubleToString(price)+"/年");
        try{
            vip= GlobalApplication.user.getVip();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        if(vip==0){
            txtBuy.setText(this.getString(R.string.vip_describe_content_buy));
        }else{
            txtBuy.setText(this.getString(R.string.vip_describe_content_continue));
        }
    }

    //初始化监听
    private void initListener() {
        imgBack.setOnClickListener(v->{
            super.onBackPressed();
        });
        viewAliCheck.setOnClickListener(v -> {
            payType = ALIPAY;
            imgAliCheck.setImageResource(R.mipmap.gouxuangoumaifangshi);
            imgWxCheck.setImageResource(R.mipmap.weigouxuangoumaifangshi);
        });
        viewWXCheck.setOnClickListener(v -> {
            payType = WXPAY;
            imgWxCheck.setImageResource(R.mipmap.gouxuangoumaifangshi);
            imgAliCheck.setImageResource(R.mipmap.weigouxuangoumaifangshi);
        });
        txtPay.setOnClickListener(v -> {
            switch (payType) {
                case ALIPAY:
                    PayUtil.aliPayV2(this,"3", "0", price)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                Log.d(TAG,"ALIPAY");
                                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                                if("9000".equals(result.getResultStatus())){
                                    Toast.makeText(VIPPayActivity.this, getResources().getString(R.string.vip_pay_success), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(VIPPayActivity.this, getResources().getString(R.string.vip_pay_fail), Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            },throwable -> {
                                Log.e(TAG,throwable.getMessage());
                                Toast.makeText(VIPPayActivity.this, getResources().getString(R.string.vip_pay_fail), Toast.LENGTH_SHORT).show();
                                finish();
                            })
                    ;
                    break;
                case WXPAY:
                    if(!msgApi.isWXAppInstalled()){
                        Toast.makeText(VIPPayActivity.this, getResources().getString(R.string.vip_pay_WX_uninstall), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PayUtil.WXPay("3","0",price)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result->{
                                Log.d(TAG,"WXPAY");
                                msgApi.sendReq(result);
                                finish();
                            },throwable -> {
                                Log.e(TAG,throwable.getMessage());
                                Toast.makeText(VIPPayActivity.this, getResources().getString(R.string.vip_pay_fail), Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            ;
                    break;

            }
        });
    }


}
