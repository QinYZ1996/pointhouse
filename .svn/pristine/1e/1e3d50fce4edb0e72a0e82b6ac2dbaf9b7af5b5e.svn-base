package com.pointhouse.chiguan.w1_14;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.Application.SysApplication;
import com.pointhouse.chiguan.common.LoadingAlertDialog;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.l1.LoginNetWork;
import com.pointhouse.chiguan.l1.Vaildate;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/7/11.
 */

public class FindPsw_activity  extends Activity{
    private UserInfo userInfo;
    private FindPsw_activity.TimeCount time;
    private Button sendMessage_btn = null;
    private EditText ftext = null;
    private TextView ts = null;
    private EditText stext = null;
    private EditText userphone = null;
    private EditText acode = null;
    public  Button loginbtn = null;
    public LoadingAlertDialog dialog;

    String str_acode = null;
    String fromid;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_14_activity_findpsd);
        SysApplication.getInstance().addActivity(this);

        Uri uri = getIntent().getData();
        if(uri!=null){
            String url = uri.toString();
            Log.d("url:",url);
            fromid = uri.getQueryParameter("id");
            Log.d("id:",fromid);
        }
        if(fromid==null){
            fromid = GlobalApplication.fromid;
        }
        init();
    }

    public void init(){
        //this.ceshi = (TextView)findViewById(R.id.ceshi);
        this.ts = (TextView) findViewById(R.id.find_timepick);
        this.ts.setVisibility(View.GONE);
        this.userInfo = new UserInfo();
        this.sendMessage_btn =(Button) findViewById(R.id.findsendMessage_btn);
        this.stext = (EditText) findViewById(R.id.findfirstpsd);
        this.ftext = (EditText) findViewById(R.id.findsecondpsd);
        this.acode = (EditText) findViewById(R.id.findmessage_text);
        this.userphone = (EditText) findViewById(R.id.findphoneText);
        this.loginbtn = (Button) findViewById(R.id.resetpsd);
        this.loginbtn.setClickable(false);
        TextChange textChange = new TextChange();
        this.stext.addTextChangedListener(textChange);
        this.ftext.addTextChangedListener(textChange);
        this.userphone.addTextChangedListener(textChange);
        this.acode.addTextChangedListener(textChange);
    }

    public void findPsw_back(View view){
        super.onBackPressed();
    }

    //发送验证短信
    public void sendShorMessageClick(View view){
        if(Vaildate.VaildateMobile(this.userphone.getText().toString())!=null){
            ToastUtil.getToast(this,Vaildate.VaildateMobile(this.userphone.getText().toString()),"center",0,180).show();
            return;
        }else{
            String parm = JsonUtil.initGetRequestParm(this.userphone.getText().toString());
            String url = Constant.URL_BASE+"sendAuthenticationCode/"+parm;
            RetrofitFactory.getInstance().getRequestServices().getStringRx(url)
                    .subscribeOn(Schedulers.newThread())
                    .map(response ->{
                        String result = "";
                        String resultCode = response.getString("resultCode");
                        if(resultCode.equals("2.0")){
                            JSONArray exceptions = response.getJSONArray("exceptions");
                            if(exceptions!=null&&exceptions.size()>0){
                                JSONObject jsmess = exceptions.getJSONObject(0);
                                result = jsmess.getString("message");
                            }
                        }else if(resultCode.equals("1.0")){

                        }
                        return result;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result->{
                        if(!result.equals("")){
                            ToastUtil.getToast(this,result,"center",0,180).show();
                        }else{
                            ts.setVisibility(View.VISIBLE);
                            time = new TimeCount(60000, 1000);//构造CountDownTimer对象
                            time.start();//开始计时
                            this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcode);
                            this.sendMessage_btn.setClickable(false);
                            this.sendMessage_btn.setText("发送验证中");
                        }
                    },throwable -> {
                        throwable.printStackTrace();
                        //ToastUtil.getToast(this,"请检查网络情况","center",0,180).show();
                    });
        }
    }

    //定义一个倒计时的内部类
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            FindPsw_activity.this.sendMessage_btn.setText("重新发送");
            //View.VISIBLE--->可见
            //View.INVISIBLE--->不可见，但这个View仍然会占用在xml文件中所分配的布局空间，不重新layout
            //View.GONE---->不可见，但这个View在ViewGroup中不保留位置，会重新layout，不再占用空间，那后面的view就会取代他的位置，
            FindPsw_activity.this.ts.setVisibility(View.GONE);
            FindPsw_activity.this.sendMessage_btn.setBackgroundResource(R.mipmap.verificationcodeactive);
            FindPsw_activity.this.sendMessage_btn.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            String str = millisUntilFinished / 1000+"";
            FindPsw_activity.this.ts.setText(str);
            FindPsw_activity.this.sendMessage_btn.setText("秒再次发送");
        }

    }


    //设置新密码事件
    public void clickReset(View view) throws IOException, JSONException, SQLException {
        this.userInfo = new UserInfo();
        if(!initUserInfo()){
            return;
        }

        String res = Vaildate.VaildateMobile(this.userphone.getText().toString());
        if(res!=null) {
            ToastUtil.getToast(this,res,"center",0,180).show();
            return;
        }
        String pres = Vaildate.VaildateisPassword(this.stext.getText().toString());
        if(pres!=null) {
            ToastUtil.getToast(this,pres,"center",0,180).show();
            return;
        }

        loginbtn.setBackgroundResource(R.mipmap.resetpassword);
        loginbtn.setEnabled(false);

        String parm = JsonUtil.initGetRequestParm(this.userphone.getText().toString()+","+this.str_acode+","+this.ftext.getText().toString());
        //验证成功后的集中返回情况
        dialog = new LoadingAlertDialog(this);
        dialog.show("处理中...");
        String url = Constant.URL_BASE+"findPassword/"+parm;
        RetrofitFactory.getInstance().getRequestServices()
                .UserRegister(url)
                .subscribeOn(Schedulers.newThread())
                .map(response ->{
//                    RetrofitFactory.ErrorMessage(FindPsw_activity.this,response,"");
                    String result = "";
                    String resultCode = response.getString("resultCode");
                    if(resultCode.equals("2")){
                        com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                        if(exceptions!=null&&exceptions.size()>0){
                            JSONObject exlist = exceptions.getJSONObject(0);
                            result = exlist.getString("message");
                        }
                    }else if(resultCode.equals("1")){
                        result  = response.getString("resultObject");
                    }
                    else if(resultCode.equals("0")){
                        result  = null;
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result ->{
                    if(result==null){
                        ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                        loginbtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                        loginbtn.setEnabled(true);
                        this.dialog.dismiss();
                        return;
                    }
                    if(result.equals("success")){
                        new LoginNetWork().UserLogin(this,this.userphone.getText().toString(),this.ftext.getText().toString(),fromid);
                        loginbtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                        loginbtn.setEnabled(true);
                        return;
                    }else{
                        ToastUtil.getToast(this,result,"center",0,180).show();
                        loginbtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                        loginbtn.setEnabled(true);
                        this.dialog.dismiss();
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                    loginbtn.setBackgroundResource(R.mipmap.resetpasswordactive);
                    loginbtn.setEnabled(true);
                    this.dialog.dismiss();
                });
    }

    public Boolean initUserInfo(){
        userInfo.setMobile(this.userphone.getText().toString());
        String fd =  this.ftext.getText().toString();
        String sd =  this.stext.getText().toString();
        str_acode = acode.getText().toString();
        if(!fd.equals(sd)){
            ToastUtil.getToast(this,"密码不一致","center",0,180).show();
            return false;
        }
        userInfo.setPassword(fd);
        return true;
    }

    //设置edittext的输入监听
    private class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(FindPsw_activity.this.ftext.getText().toString().length()>=6 && FindPsw_activity.this.stext.getText().toString().length()>=6
                    &&FindPsw_activity.this.ftext.getText().toString().length()<=16&& FindPsw_activity.this.stext.getText().toString().length()<=16){
                FindPsw_activity.this.userInfo.setPassword(FindPsw_activity.this.ftext.getText().toString());
                FindPsw_activity.this.userInfo.setPassword(FindPsw_activity.this.stext.getText().toString());
            }else{
                FindPsw_activity.this.loginbtn.setBackgroundResource(R.mipmap.resetpassword);
                FindPsw_activity.this.loginbtn.setClickable(false);
                return;
            }

            FindPsw_activity.this.userInfo.setMobile(FindPsw_activity.this.userphone.getText().toString());

            FindPsw_activity.this.loginbtn.setBackgroundResource(R.mipmap.resetpasswordactive);
            FindPsw_activity.this.loginbtn.setClickable(true);
        }
    }

    public void onDestory(){
        super.onDestroy();
        this.dialog.dismiss();
    }

    public void onStart(){
        super.onStart();
        MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
    }
}
