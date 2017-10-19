package com.pointhouse.chiguan.welcome;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.l1.LoginDaoImpl;
import com.pointhouse.chiguan.l1.LoginNetWork;
import com.pointhouse.chiguan.t1.FatherActivity;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends FragmentActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000; // 两秒后进入系统

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,
                        FatherActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGTH);

        // 隐藏MINI播放器
        MediaServiceHelper.getService(getApplicationContext(), MediaService::backGroundHide);
        // 显示MINI播放器
        // MediaServiceHelper.getService(getApplicationContext(), MediaService::reFleshDisplay);

        //自动登陆
        try {
            try {
                autoLogin(this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void autoLogin(Activity context) throws SQLException, IOException, JSONException {
        UserInfo userInfo;
        userInfo = new LoginDaoImpl().getUserinfo(context);
        if(userInfo!=null){
            GlobalApplication.user = userInfo;
            //new LoginNetWork().UserLogin(context,userInfo.getMobile(),userInfo.getPassword(),null);
        }
    }

    //必须在第一个activity调用极光生命周期使其保持一致
    protected void onResume() {
        //极光推送的启动
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //极光推送的暂停
        JPushInterface.onPause(this);
        super.onPause();
    }
}
