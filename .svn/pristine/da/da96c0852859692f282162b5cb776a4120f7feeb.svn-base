package com.pointhouse.chiguan.l1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.Application.SysApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.LoadingAlertDialog;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.k1_9.ShareArticleActivity;
import com.pointhouse.chiguan.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by PC-sunjb on 2017/6/23.
 */

public class Login_activity extends Activity {
    public static String k1_2id = "";
    public static String paramId = "";
    public Button loginbtn = null;
    private EditText unameEd;
    private EditText upswdEd;
    private UserInfo userInfo = new UserInfo();
    private ImageView wechat_btn;
    public static String fromid;
    String valdateResult;

    public Login_activity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l1_activity_login);
        SysApplication.getInstance().addActivity(this);

        init();
        RelativeLayout backbtn = (RelativeLayout) findViewById(R.id.logBackBtn);
        backbtn.setOnClickListener((View view) -> {
            if (fromid.equals("k1_2")) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + k1_2id));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                finish();
            } else if (fromid.equals("k1_9")) {
                Intent it = new Intent(this, ShareArticleActivity.class);
                it.putExtra("articleId", Login_activity.paramId);
                startActivity(it);
                finish();
            } else {
                super.onBackPressed();
            }
        });

        Button allback_btn = (Button) findViewById(R.id.login_allback_btn);
        allback_btn.setOnClickListener((View view) -> {
            if (fromid.equals("k1_2")) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + k1_2id));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                finish();
            } else if (fromid.equals("k1_9")) {
                Intent it = new Intent(this, ShareArticleActivity.class);
                it.putExtra("articleId", Login_activity.paramId);
                startActivity(it);
                finish();
            } else {
                MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
                super.onBackPressed();
            }
        });
    }

    public void init() {
        wechat_btn = (ImageView) findViewById(R.id.wechat_btn);
        wechat_btn.setEnabled(false);

        this.unameEd = (EditText) findViewById(R.id.login_edit_account);
        this.upswdEd = (EditText) findViewById(R.id.login_edit_pwd);
        this.loginbtn = (Button) findViewById(R.id.login_btn_login);
        this.loginbtn.setClickable(false);

        TextChange textChange = new TextChange();
        //添加edittext的输入监听
        this.unameEd.addTextChangedListener(textChange);
        this.upswdEd.addTextChangedListener(textChange);

        Uri uri = getIntent().getData();
        if (uri != null) {
            String url = uri.toString();
            Log.d("url:", url);
            fromid = uri.getQueryParameter("id");
            Log.d("id:", fromid);
        }
        if (fromid == null) {
            fromid = GlobalApplication.fromid;
            return;
        }
        if (fromid != null) {
            String[] str = fromid.split(",");
            fromid = str[0];
            GlobalApplication.fromid = fromid;
            if (fromid.equals("k1_9")) {
                paramId = str[1];
            } else if (str.length == 2) {
                k1_2id = str[1];
            }
        }

    }

    public LoadingAlertDialog dialog;

    //账号登录事件
    public void LoginClick(View view) throws IOException, JSONException, SQLException {

        String vname = Vaildate.VaildateMobile(this.unameEd.getText().toString());
        if (vname != null) {
            ToastUtil.getToast(this, vname, "center", 0, 180).show();
            return;
        }
        String vpas = Vaildate.VaildateisPassword(this.upswdEd.getText().toString());
        if (vpas != null) {
            ToastUtil.getToast(this, vpas, "center", 0, 180).show();
            return;
        }
        dialog = new LoadingAlertDialog(this);
        dialog.show("登录中...");

        String res = this.unameEd.getText().toString();
        String pres = this.upswdEd.getText().toString();
        LoginNetWork loginNetWork = new LoginNetWork();

        Login_activity.this.loginbtn.setBackgroundResource(R.mipmap.verificationcode);
        Login_activity.this.loginbtn.setEnabled(false);

        loginNetWork.UserLogin(this, res, pres, GlobalApplication.fromid);
    }

    //注册按钮事件
    public void GotoRegister(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "l1_1?id=" + fromid));
        startActivity(intent);
        Log.v("注册按钮:", "点击成功");
    }

    //判断是否安装卫微信
    public boolean isWeixinAvilible() {
        final PackageManager packageManager = this.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    //微信授权登录事件
    public void LoginWXClick(View view) {
        if (!isWeixinAvilible()) {
            ToastUtil.getToast(this, "请安装最新版本微信", "center", 0, 180).show();
            return;
        } else {
            GlobalApplication.BandweChat = 0;
            wechat_btn.setEnabled(false);
            //loginService.RegisterWx(this);
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            WXEntryActivity.login_activity = this;
            GlobalApplication.wxapi.sendReq(req);
        }
        Log.v("点击:", "微信登录");
    }

    //找回密码事件
    public void FindPassword(View view) {
        Intent intent = IntentUriUtil.getIntent("w1_14", fromid);
        startActivity(intent);
        Log.v("忘记密码:", "点击成功");
    }

    //设置edittext的输入监听
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            if(delayRun!=null){
//                //每次editText有变化的时候，则移除上次发出的延迟线程
//                handler.removeCallbacks(delayRun);
//            }
            Login_activity.this.userInfo.setMobile(Login_activity.this.unameEd.getText().toString());
            Login_activity.this.userInfo.setPassword(Login_activity.this.upswdEd.getText().toString());
            Login_activity.this.valdateResult = Vaildate.vaildateUserLogin(userInfo);
            if (Login_activity.this.valdateResult == null) {
                Login_activity.this.loginbtn.setBackgroundResource(R.mipmap.signin);
                Login_activity.this.loginbtn.setClickable(true);
                return;
            } else {
                //延迟3000ms，如果不再输入字符，则执行该线程的run方法
                Login_activity.this.loginbtn.setBackgroundResource(R.mipmap.verificationcode);
                Login_activity.this.loginbtn.setClickable(false);
                //handler.postDelayed(delayRun, 5000);
            }
        }
    }

    public void onStart() {
        super.onStart();
        wechat_btn.setEnabled(true);
        MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
    }

    public void onDestory() {
        super.onDestroy();
        this.dialog.dismiss();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (fromid.equals("k1_2")) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + k1_2id));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                finish();
            } else if (fromid.equals("k1_9")) {
                Intent it = new Intent(this, ShareArticleActivity.class);
                it.putExtra("articleId", Login_activity.paramId);
                startActivity(it);
                finish();
            } else {
                super.onBackPressed();
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
