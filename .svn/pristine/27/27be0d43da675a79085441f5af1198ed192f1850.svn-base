package com.pointhouse.chiguan.w1_11;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.netease.nim.demo.common.util.sys.GroupMsgUtil;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.mydialog.MyDialogBuilder;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.db.PayInfo;
import com.pointhouse.chiguan.db.PayInfoDao;
import com.pointhouse.chiguan.db.StudyInfo;
import com.pointhouse.chiguan.db.StudyInfoDao;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.l1.LoginDaoImpl;
import com.pointhouse.chiguan.l1_1.Register_Activity;
import com.pointhouse.chiguan.w1.PersonalCenterActivity;
import com.pointhouse.chiguan.w1_12.EditPassWordActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by P on 2017/7/18.
 */

public class MySettingsActivity extends Activity {
    MysettingsListViewAdapter mysettingsListViewAdapter;
    List<HashMap<String, String>> list;
    Button out_btn;
    ListView listView;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.w1_11_activity_mysettings);
        testinit();
        init();
    }

    public void testinit() {
        this.list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "修改密码");
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("name", "接收推送");
        String token = SharedPreferencesUtil.readToken(this);
        if (token != null && !token.equals("")) {
            if (GlobalApplication.user.getPassword() != null && !GlobalApplication.user.getPassword().equals("")) {
                this.list.add(hashMap);
            }
        }
        this.list.add(hashMap1);
    }

    //初始化界面
    public void init() {
        this.listView = (ListView) findViewById(R.id.mysettings_listview);
        this.mysettingsListViewAdapter = new MysettingsListViewAdapter(this, this.list);
        this.listView.setAdapter(mysettingsListViewAdapter);
        this.out_btn = (Button) findViewById(R.id.mysettings_btn);

        if (SharedPreferencesUtil.readToken(this).equals("") || SharedPreferencesUtil.readToken(this) == null) {
            this.out_btn.setVisibility(View.INVISIBLE);
        } else {
            this.listView.setOnItemClickListener((parent, view, position, id) -> {
                switch (position) {
                    case 0: {
                        Log.d("click:", MySettingsActivity.this.list.get(position) + "");
                        Intent editintent = new Intent(MySettingsActivity.this, EditPassWordActivity.class);
                        MySettingsActivity.this.startActivity(editintent);
                        break;
                    }
                    default:
                        break;
                }
            });
        }
        this.out_btn.setOnClickListener(v -> {
            new MyDialogBuilder(this, "确认退出登录？", () -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "t1?id=w1_11"));
                new LoginDaoImpl().deleteUserinfo(MySettingsActivity.this);
                SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(MySettingsActivity.this, "userinfo");
                sharedPreferences.edit().clear().commit();
                startActivity(intent);
                RetrofitFactory.clearToken();
                try {
                    NIMClient.getService(AuthService.class).logout();
                    NimUIKit.clearCache();
                }catch (Exception e){
                    e.printStackTrace();
                }
                PersonalCenterActivity.Header_drawable = null;
                PersonalCenterActivity.Background_drawble = null;
                PersonalCenterActivity.HeaderIsOk = false;
                PersonalCenterActivity.Background = false;
                //GlobalApplication.fromid="";
                GlobalApplication.LoginType = 0;
                GlobalApplication.user = new UserInfo();
                GlobalApplication.fromid = "w1_11";

                StudyInfoDao studyInfoDao = new StudyInfoDao(GlobalApplication.sContext);
                try {
                    List<StudyInfo> studyInfoList = studyInfoDao.getAll();
                    if (studyInfoList != null) {
                        for (StudyInfo studyInfo : studyInfoList) {
                            studyInfoDao.delete(studyInfo);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    PayInfoDao payInfoDao = new PayInfoDao(GlobalApplication.sContext);
                    List<PayInfo> payInfoList = payInfoDao.queryAll();
                    if (payInfoList != null) {
                        for (PayInfo payInfo : payInfoList) {
                            payInfoDao.delete(payInfo);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                MediaServiceHelper.getService(this, MediaService::stopMedia);
                finish();
            }).show();
//            new AlertDialog.Builder(this).setTitle("确认退出登录？")
//                    .setPositiveButton("确定", (dialog, which) -> {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "t1?id=w1_11"));
//                        new LoginDaoImpl().deleteUserinfo(MySettingsActivity.this);
//                        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(MySettingsActivity.this, "userinfo");
//                        sharedPreferences.edit().clear().commit();
//                        startActivity(intent);
//                        PersonalCenterActivity.Header_drawable = null;
//                        PersonalCenterActivity.Background_drawble = null;
//                        PersonalCenterActivity.HeaderIsOk = false;
//                        PersonalCenterActivity.Background = false;
//                        //GlobalApplication.fromid="";
//                        GlobalApplication.LoginType = 0;
//                        GlobalApplication.user = new UserInfo();
//                        GlobalApplication.fromid = "w1_11";
//
//                        StudyInfoDao studyInfoDao = new StudyInfoDao(GlobalApplication.sContext);
//                        try {
//                            List<StudyInfo> studyInfoList = studyInfoDao.getAll();
//                            if (studyInfoList != null) {
//                                for (StudyInfo studyInfo : studyInfoList) {
//                                    studyInfoDao.delete(studyInfo);
//                                }
//                            }
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            PayInfoDao payInfoDao = new PayInfoDao(GlobalApplication.sContext);
//                            List<PayInfo> payInfoList = payInfoDao.queryAll();
//                            if (payInfoList != null) {
//                                for (PayInfo payInfo : payInfoList) {
//                                    payInfoDao.delete(payInfo);
//                                }
//                            }
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                        MediaServiceHelper.getService(this, MediaService::stopMedia);
//                        finish();
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
        });
    }

    public void Setting_back(View view) {
        super.onBackPressed();
    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
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

}
