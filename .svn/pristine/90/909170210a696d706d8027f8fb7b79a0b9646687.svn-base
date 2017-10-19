package com.pointhouse.chiguan.w1_12;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.LoadingAlertDialog;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.l1.Vaildate;
import com.pointhouse.chiguan.l1_1.RegisterDaoImpl;

import java.sql.SQLException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/7/19.
 */

public class EditPassWordActivity extends Activity{
    private EditText ftext;
    private EditText stext ;
    private EditText oldtext ;
    private Button resetpsd ;
    public LoadingAlertDialog dialog;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_12_activity_editpassword);
        init();
    }

    public void init(){
        this.stext = (EditText) findViewById(R.id.EditPsd_spsw);
        this.ftext = (EditText) findViewById(R.id.EditPsd_fpsw);
        this.oldtext = (EditText) findViewById(R.id.EditPsd_oldpsw);
        this.resetpsd = (Button) findViewById(R.id.EditPsd_resetpsd);
        this.resetpsd.setClickable(false);
        EditPassWordActivity.TextChange textChange = new EditPassWordActivity.TextChange();
        this.oldtext.addTextChangedListener(textChange);
        this.stext.addTextChangedListener(textChange);
        this.ftext.addTextChangedListener(textChange);
    }

    public void EditPass_back(View view){
        super.onBackPressed();
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

            if(Vaildate.VailDateEditPassWord(EditPassWordActivity.this.oldtext.getText().toString(),EditPassWordActivity.this.ftext.getText().toString()
            ,EditPassWordActivity.this.stext.getText().toString())!=null){
                EditPassWordActivity.this.resetpsd.setBackgroundResource(R.mipmap.resetpassword);
                EditPassWordActivity.this.resetpsd.setClickable(false);
                return;
            }else{
                EditPassWordActivity.this.resetpsd.setBackgroundResource(R.mipmap.resetpasswordactive);
                EditPassWordActivity.this.resetpsd.setClickable(true);
            }
        }
    }

    public void clickReset(View view) throws SQLException {

        if(!this.ftext.getText().toString().equals(this.stext.getText().toString())){
            ToastUtil.getToast(this,"前后密码不一致","center",0,180).show();
            return;
        }else {
            String result = "";
            result = Vaildate.VaildateisPassword(this.ftext.getText().toString());
            this.resetpsd.setBackgroundResource(R.mipmap.resetpassword);
            this.resetpsd.setEnabled(false);
            if (result == null) {
                String parm = JsonUtil.initGetRequestParm(this.oldtext.getText().toString() + "," + this.ftext.getText().toString());
                String url = Constant.URL_BASE+"changePassword/"+parm;
                dialog = new LoadingAlertDialog(this);
                dialog.show("处理中...");
                RetrofitFactory.getInstance().getRequestServicesToken()
                        .EditPassWord(url)
                        .subscribeOn(Schedulers.newThread())
                        .map(response ->{
                            String responses ="";
                            String resultCode = response.getString("resultCode");
                            if(resultCode.equals("2")){
                                com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                                if(exceptions!=null&&exceptions.size()>0){
                                    JSONObject exlist = exceptions.getJSONObject(0);
                                    responses = exlist.getString("message");
                                }
                            }else if(resultCode.equals("1")){

                            }else if(resultCode.equals("0")){
                                responses = null;
                            }
                            return responses;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responses-> {
                            if(responses == null){
                                ToastUtil.getToast(this,"系统异常，请稍后再试!","center",0,180).show();
                                this.resetpsd.setBackgroundResource(R.mipmap.resetpasswordactive);
                                this.resetpsd.setEnabled(true);
                                dialog.dismiss();
                                return;
                            }
                            if(responses!=null&&responses.equals("")){
                                GlobalApplication.user.setPassword(this.ftext.getText().toString());
                                new RegisterDaoImpl().updateUserInfo(this,GlobalApplication.user);
                                ToastUtil.getToast(this,"修改成功","center",0,180).show();
                                dialog.dismiss();
                                finish();
                            } else{
                                ToastUtil.getToast(this,responses,"center",0,180).show();
                                this.resetpsd.setBackgroundResource(R.mipmap.resetpasswordactive);
                                this.resetpsd.setEnabled(true);
                                dialog.dismiss();
                            }

                },throwable ->{
                            throwable.printStackTrace();
                            ToastUtil.getToast(this,"系统异常，请稍后再试!","center",0,180).show();
                            this.resetpsd.setBackgroundResource(R.mipmap.resetpasswordactive);
                            this.resetpsd.setEnabled(true);
                            dialog.dismiss();
                        });
                }else{
                ToastUtil.getToast(this,result,"center",0,180).show();
            }
        }
    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            //当手指移动的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) {
                MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
            }else if(y2 - y1 > 50) {
                MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
