package com.pointhouse.chiguan.l1_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.Application.SysApplication;
import com.pointhouse.chiguan.common.LoadingAlertDialog;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.l1.LoginDaoImpl;
import com.pointhouse.chiguan.l1.LoginNetWork;
import com.pointhouse.chiguan.l1.Login_activity;
import com.pointhouse.chiguan.l1.Vaildate;
import com.pointhouse.chiguan.l1_2.Protocol_Activity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-sunjb on 2017/6/23.
 */

public class Register_Activity extends Activity{
    //private TimeCount time;
    private UserInfo userInfo;
    private Boolean dataisok = false;
    private Boolean isClick = true;
    private Button sendMessage_btn = null;
    private Button nextBtn = null;
    private EditText ftext = null;
    private EditText stext = null;
    private EditText userphone = null;
    private EditText acode = null;
    private TextView ts = null;
    private UserInfo user;
    private String token;
    public LoadingAlertDialog dialog;
    static String fromid="";
    String str_acode="";
    Intent messageintent;
    static String timestr = "";
    TextView textView;
    Button regster_protocol;
    /**
     * 倒计时Handler
     */
    @SuppressLint("HandlerLeak")
    Handler mCodeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == com.pointhouse.chiguan.l1_1.TimeCount.IN_RUNNING) {// 正在倒计时
                timestr = msg.obj.toString();
                Register_Activity.this.ts.setText(timestr);
                Register_Activity.this.sendMessage_btn.setText("秒再次发送");
                Register_Activity.this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcode);
                Register_Activity.this.sendMessage_btn.setClickable(false);
            } else if (msg.what == com.pointhouse.chiguan.l1_1.TimeCount.END_RUNNING) {// 完成倒计时
                Register_Activity.this.sendMessage_btn.setText(msg.obj.toString());
                Register_Activity.this.ts.setText("");
                Register_Activity.this.ts.setVisibility(View.GONE);
                Register_Activity.this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcodeactive);
                Register_Activity.this.sendMessage_btn.setClickable(true);
                Log.v("123213123","1231232131232132");
                stopService(messageintent);
            }
        }
    };


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l1_1_activity_register);
        SysApplication.getInstance().addActivity(this);
        messageintent = new Intent(this,MessageService.class);
        if(fromid.equals("w1_7")){
            LinearLayout relativeLayout = (LinearLayout)findViewById(R.id.bottomLayout);
            relativeLayout.setVisibility(View.INVISIBLE);
        }
        init();
    }



    public void init(){
        MessageService.setHandler(mCodeHandler);
        Uri uri = getIntent().getData();
        if(uri!=null){
            String Url = uri.toString();
            Log.d("url:",Url);
            fromid = uri.getQueryParameter("id");
            Log.d("id:",fromid);
        }
        if(fromid==null){
            fromid = GlobalApplication.fromid;
        }
        this.sendMessage_btn =(Button) findViewById(R.id.sendMessage_btn);
        this.ts = (TextView) findViewById(R.id.timepick);
        this.ts.setVisibility(View.GONE);
        this.ftext = (EditText) findViewById(R.id.firstpsd);
        this.stext = (EditText) findViewById(R.id.secondpsd);
        this.nextBtn = (Button) findViewById(R.id.regster_btn);
        if(fromid.equals("w1_7")){
            this.textView =(TextView) findViewById(R.id.custom_title);
            this.textView.setText("手机绑定");
            this.regster_protocol = (Button)findViewById(R.id.regster_protocol);
            this.regster_protocol.setVisibility(View.INVISIBLE);
            this.nextBtn.setText("现在绑定");
        }
        this.nextBtn.setClickable(false);
        this.userphone = (EditText) findViewById(R.id.phoneText);
        this.acode = (EditText) findViewById(R.id.message_text);
        this.userInfo = new UserInfo();
        TextChange textChange = new TextChange();
        //添加edittext的输入监听
        this.ftext.addTextChangedListener(textChange);
        this.stext.addTextChangedListener(textChange);
        this.userphone.addTextChangedListener(textChange);
        this.acode.addTextChangedListener(textChange);
    }

    public void Reg_back(View view){
        Register_Activity.super.onBackPressed();
    }

    //提交注册信息
    public void regBtnClick(View view) throws IOException, JSONException, SQLException {
        this.userInfo = new UserInfo();
        if(!initUserInfo()){
            return;
        }
        String res = Vaildate.VaildateMobile(this.userphone.getText().toString());
        if(res!=null) {
            ToastUtil.getToast(this,res,"center",0,180).show();
            return;
        }
        String pres = Vaildate.VaildateisPassword(this.ftext.getText().toString());
        if(pres!=null) {
            ToastUtil.getToast(this,pres,"center",0,180).show();
            return;
        }
        if(str_acode.equals("")){
            ToastUtil.getToast(Register_Activity.this,"验证码不能为空","center",0,180).show();
            return;
        }

        this.nextBtn.setBackgroundResource(R.mipmap.resetpassword);
        this.nextBtn.setEnabled(false);
        dialog = new LoadingAlertDialog(this);
        dialog.show("提交中...");
        if(fromid.equals("w1_7")){
            String parm = JsonUtil.initGetRequestParm(this.userphone.getText().toString()+","+this.str_acode+","+this.ftext.getText().toString());
            String url = Constant.URL_BASE+"mobileBand/"+parm;
            RetrofitFactory.getInstance().getRequestServicesToken()
                    .UserRegister(url)
                    .subscribeOn(Schedulers.newThread())
                    .map(response ->{
                        String result = "";
                        String resultCode = response.getString("resultCode");
                        if(resultCode.equals("2")){
                            com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                            if(exceptions!=null&&exceptions.size()>0){
                                JSONObject exlist = exceptions.getJSONObject(0);
                                result = exlist.getString("message");
                            }
                        }else if(resultCode.equals("1")){
                            result="";
                        }else if(resultCode.equals("0")){
                            result = null;
                        }
                        return result;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result ->{
                        if(result==null){
                            ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                            Register_Activity.this.nextBtn.setEnabled(true);
                            this.dialog.dismiss();
                            return;
                        }else if(result!=null&&!result.equals("")){
                            ToastUtil.getToast(this,result,"center",0,180).show();
                            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                            Register_Activity.this.nextBtn.setEnabled(true);
                            this.dialog.dismiss();
                            return;
                        }
                        if(result.equals("")){
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(GlobalApplication.scheme_uri+fromid+"?id="+"l1_1"));
                            startActivity(intent);
                            GlobalApplication.user.setPassword(this.ftext.getText().toString());
                            GlobalApplication.user.setMobile(this.userphone.getText().toString());
                            new RegisterDaoImpl().updateUserInfo(this,GlobalApplication.user);
                            ToastUtil.getToast(this,"绑定成功","center",0,180).show();
                            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                            Register_Activity.this.nextBtn.setEnabled(true);
                            this.dialog.dismiss();
                            MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示

                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                        Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                        Register_Activity.this.nextBtn.setEnabled(true);
                        this.dialog.dismiss();
                    });
        }else{
            String Parm = JsonUtil.initGetRequestParm(this.userphone.getText().toString()+","+this.str_acode+","+this.ftext.getText().toString());
            String url = Constant.URL_BASE+"register/"+Parm;
            RetrofitFactory.getInstance().getRequestServices()
                    .UserRegister(url)
                    .subscribeOn(Schedulers.newThread())
                    .map(response ->{
                        String result = "";
                        String resultCode = response.getString("resultCode");
                        if(resultCode.equals("2")){
                            com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                            if(exceptions!=null&&exceptions.size()>0){
                                JSONObject exlist = exceptions.getJSONObject(0);
                                result = exlist.getString("message");
                            }
                        }else if(resultCode.equals("1")){
                            JSONObject resultObject = response.getJSONObject("resultObject");
                            JSONObject userInfo = resultObject.getJSONObject("user");
                            token = resultObject.getString("token");
                            String userstr = userInfo.toString();

                            Gson gson = new Gson();
                            user = gson.fromJson(userstr,UserInfo.class);
                            user.setPassword(this.ftext.getText().toString());
                        }else if(resultCode.equals("0")){
                            result = null;
                        }
                        return result;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response ->{
                        if(response==null){
                            ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                            Register_Activity.this.nextBtn.setEnabled(true);
                            this.dialog.dismiss();
                            return;
                        }
                        if(GlobalApplication.user!=null&&token!=null&&!token.equals("")){
                            GlobalApplication.LoginType=1;
                            if(fromid.equals("w1_11")){
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(GlobalApplication.scheme_uri+"t1?id=w1"));
                                ToastUtil.getToast(this,"注册成功","center",0,180).show();
                                Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                                Register_Activity.this.nextBtn.setEnabled(true);
                                this.dialog.dismiss();
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(GlobalApplication.scheme_uri+"t1"+"?id=w1_11"));
                                ToastUtil.getToast(this,"注册成功","center",0,180).show();
                                Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                                Register_Activity.this.nextBtn.setEnabled(true);
                                this.dialog.dismiss();
                                startActivity(intent);
                            }
                            SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(this,"userinfo");
                            sharedPreferences.edit().clear().commit();
                            GlobalApplication.user = user;
                            GlobalApplication.user.setNickname(user.getMobile());
                            LoginDaoImpl loginDao = new LoginDaoImpl();
                            loginDao.saveLoginUserinfo(this,user);
                            RetrofitFactory.resetRetrofitFactory(token);
                            SharedPreferencesUtil.saveToken(this,token);
                            SharedPreferencesUtil.saveSharedPreferences(this,"userinfo",user);
                            ToastUtil.getToast(this,"注册成功","center",0,180).show();
                            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                            Register_Activity.this.nextBtn.setEnabled(true);
                            MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
                            String rid = JPushInterface.getRegistrationID(getApplicationContext());
                            new LoginNetWork().uploadRegId(rid);
                        }else{
                            ToastUtil.getToast(this,response,"center",0,180).show();
                            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                            Register_Activity.this.nextBtn.setEnabled(true);
                            this.dialog.dismiss();
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                        Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                        Register_Activity.this.nextBtn.setEnabled(true);
                        this.dialog.dismiss();
                    });
        }
    }

    //发送验证短信
    public void sendShorMessageClick(View view){
        Log.v("点击:","验证短信");
        if(Vaildate.VaildateMobile(this.userphone.getText().toString())!=null){
            ToastUtil.getToast(this,Vaildate.VaildateMobile(this.userphone.getText().toString()),"center",0,180).show();
            return;
        }else{
            String parm = JsonUtil.initGetRequestParm(this.userphone.getText().toString());
            String url = Constant.URL_BASE+"sendAuthenticationCode/" + parm;
            RetrofitFactory.getInstance().getRequestServices().getStringRx(url)
                    .subscribeOn(Schedulers.newThread())
                    .map(response ->{
                        String result = "";
                        String resultCode = response.getString("resultCode");
                        if(resultCode.equals("2")){
                            JSONArray exceptions = response.getJSONArray("exceptions");
                            if(exceptions!=null&&exceptions.size()>0){
                                JSONObject jsmess = exceptions.getJSONObject(0);
                                result = jsmess.getString("message");
                            }
                        }else if(resultCode.equals("1")){

                        }
                        else if(resultCode.equals("0")){
                            result = null;
                        }
                        return result;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result->{
                        if(result==null){
                            ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                            return;
                        }
                        if(!result.equals("")){
//                            this.sendMessage_btn.setClickable(false);
//                            startService(messageintent);
//                            ts.setVisibility(View.VISIBLE);
                            ToastUtil.getToast(this,result,"center",0,180).show();
                        }else{
                            //this.sendMessage_btn.setClickable(false);
                            ts.setVisibility(View.VISIBLE);
                            startService(messageintent);
//                            time = new TimeCount(60000, 1000,new Handler());//构造CountDownTimer对象
//                            time.start();//开始计时
//                            ts.setVisibility(View.VISIBLE);
//                            this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcode);
//                            this.sendMessage_btn.setClickable(false);
//                            this.sendMessage_btn.setText("发送验证中");
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                    });
                }
    }

    //点击注册协议
    public void ClickProtocol(View view){
        Intent intent = new Intent(this, Protocol_Activity.class);
        startActivity(intent);
    }

    //勾选协议事件
    public void IsClickProtocol(View view){
        Button button = (Button)findViewById(R.id.clickRead);
        if(this.isClick == false){
            this.isClick = true;
            button.setBackgroundResource(R.mipmap.checkactive);
            if(this.dataisok){
                Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                Register_Activity.this.nextBtn.setClickable(true);
            }
        }else{
            this.isClick = false;
            button.setBackgroundResource(R.mipmap.check);
            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpassword);
            Register_Activity.this.nextBtn.setClickable(false);
        }
    }

    //封装请求参数
    public Boolean initUserInfo(){
        EditText phonenumber = (EditText)findViewById(R.id.phoneText);
        userInfo.setMobile(phonenumber.getText().toString());
        EditText message = (EditText)findViewById(R.id.message_text);
        this.str_acode = message.getText().toString();
        EditText firstpsd = (EditText)findViewById(R.id.firstpsd);
        String fd =  firstpsd.getText().toString();
        EditText secondpsd = (EditText)findViewById(R.id.secondpsd);
        String sd =  secondpsd.getText().toString();
        if(!fd.equals(sd)){
            ToastUtil.getToast(this,"密码不一致","center",0,180).show();
            return false;
        }
        userInfo.setPassword(fd);
        if(this.isClick==false){
            ToastUtil.getToast(this,"请勾选用户协议","center",0,180).show();
            return false;
        }
        return true;
    }


    //定义一个倒计时的内部类
    //View.VISIBLE--->可见
    //View.INVISIBLE--->不可见，但这个View仍然会占用在xml文件中所分配的布局空间，不重新layout
    //View.GONE---->不可见，但这个View在ViewGroup中不保留位置，会重新layout，不再占用空间，那后面的view就会取代他的位置，
//    class TimeCount extends CountDownTimer {
//
//        public TimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
//        }
//        @Override
//        public void onFinish() {//计时完毕时触发
//            Register_Activity.this.sendMessage_btn.setText("重新发送");
//            Register_Activity.this.ts.setVisibility(View.GONE);
//            Register_Activity.this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcodeactive);
//            Register_Activity.this.sendMessage_btn.setClickable(true);
//        }
//        @Override
//        public void onTick(long millisUntilFinished) {//计时过程显示

//            String str = millisUntilFinished / 1000+"";
//            Register_Activity.this.ts.setText(str);
//            Register_Activity.this.sendMessage_btn.setText("秒再次发送");
//        }
//    }

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
            if(Register_Activity.this.ftext.getText().toString().length()>=6 && Register_Activity.this.stext.getText().toString().length()>=6
                    &&Register_Activity.this.ftext.getText().toString().length()<=16&& Register_Activity.this.stext.getText().toString().length()<=16){
                Register_Activity.this.userInfo.setPassword(Register_Activity.this.ftext.getText().toString());
                Register_Activity.this.userInfo.setPassword(Register_Activity.this.stext.getText().toString());
            }else{
                Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpassword);
                Register_Activity.this.dataisok = false;
                return;
            }
            Register_Activity.this.userInfo.setMobile(Register_Activity.this.userphone.getText().toString());
            str_acode = Register_Activity.this.acode.getText().toString();
            Register_Activity.this.dataisok = true;
            if(Register_Activity.this.isClick==false){
                return;
            }
            Register_Activity.this.nextBtn.setBackgroundResource(R.mipmap.resetpasswordactive);
            Register_Activity.this.nextBtn.setClickable(true);
        }
    }


    //隐藏显示密码
    public void ShowPassword(View view){
//        ImageView img = (ImageView)view;
//        if(isShow ==false){
//            img.setImageResource(R.);
//        }
    }

    public void onDestory(){
        super.onDestroy();
        this.dialog.dismiss();
    }

    public void onStart(){
        super.onStart();
        if(fromid.equals("w1_7")){
            LinearLayout relativeLayout = (LinearLayout)findViewById(R.id.bottomLayout);
            relativeLayout.setVisibility(View.INVISIBLE);
        }
        if(isServiceExisted(this,"com.pointhouse.chiguan.l1_1.MessageService")){
            this.ts.setVisibility(View.GONE);
            this.sendMessage_btn.setClickable(false);
            Register_Activity.this.sendMessage_btn.setText("已发送验证码");
            this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcode);
        }
        MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
    }

    public void onStop(){
        super.onStop();
//        if(isServiceExisted(this,"com.pointhouse.chiguan.l1_1.MessageService")){
//            GlobalApplication.message_time = this.ts.getText().toString();
//        }
        fromid="";
    }



    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
