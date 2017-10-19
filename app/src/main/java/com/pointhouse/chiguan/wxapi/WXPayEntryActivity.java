package com.pointhouse.chiguan.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.db.PayInfo;
import com.pointhouse.chiguan.db.PayInfoDao;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.sql.SQLException;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private PayInfoDao payInfoDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        payInfoDao = new PayInfoDao(GlobalApplication.sContext);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
                if(GlobalApplication.isCourse != -1){
                    PayInfo payInfo = new PayInfo();
                    payInfo.setIsCourse(GlobalApplication.isCourse);
                    payInfo.setCourseId(GlobalApplication.waitPayId);
                    //payInfo.setTid(GlobalApplication.tid);
                    payInfo.setIsBuy(true);
                    payInfo.setIsPrompt(false);
                    try {
                        payInfoDao.save(payInfo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();

            }
            finish();
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
        }
    }
}