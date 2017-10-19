package com.pointhouse.chiguan.w1_13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.l1.LoginDaoImpl;
import com.pointhouse.chiguan.w1_4.MyCollectionAdapter;
import com.pointhouse.chiguan.w1_8.VIPPayActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/7/24.
 */

public class MyMemberActivity extends Activity{
    ListView mymember_listview;
    MyMemberAdapter myMemberAdapter;
    Button paybtn;
    static String price="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_13_activity_mymember);
        getPrice();
        //initListview();
        //initBtn();
    }

    public void getPrice(){
        String parm = JsonUtil.initGetRequestParm("vip");
        String url =Constant.URL_BASE+"systemConfig/"+parm;
        RetrofitFactory.getInstance().getRequestServicesToken()
                .GetPrice(url)
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
                        price = resultObject.getString("vip");
                        result = "";
                    }
                    else if (resultCode.equals("0")){
                        result = null;
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->{
                    if(result==null){
                        ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                        price="";
                        return;
                    }else if(result!=null&&!result.equals("")){
                        ToastUtil.getToast(this, result, "center", 0, 180).show();
                        price="";
                    }
                    initListview();

                },throwable ->{
                    initListview();
                    ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }

    public void initBtn(){
        this.paybtn = (Button)findViewById(R.id.mymember_paybtn);
        if(GlobalApplication.user.getVip()==0){
            this.paybtn.setText("立即购买");
        }else{
            this.paybtn.setText("续费");
        }
        paybtn.setOnClickListener(view ->{
//            Intent intent = IntentUriUtil.getIntent("w1_8","w1_13");
            Intent intent = new Intent(this, VIPPayActivity.class);
            intent.putExtra("price",Double.valueOf(price));
            startActivity(intent);
        });
    }

    public void Member_back(View view){
        super.onBackPressed();
    }

    public void initListview(){
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name","会员名");
        list.add(hashMap);
        HashMap<String,String> hashMap1 = new HashMap<>();
        hashMap1.put("name","到期日");
        list.add(hashMap1);
        HashMap<String,String> hashMap2 = new HashMap<>();
        hashMap2.put("name","价格");
        list.add(hashMap2);
        String date = GlobalApplication.user.getVipEndDate();

        List textlist = new ArrayList();
        HashMap<String, String> hashMaps1 = new HashMap<>();
        if(GlobalApplication.user.getNickname()==null||GlobalApplication.user.getNickname().equals("")){
            hashMaps1.put("name", GlobalApplication.user.getMobile());
        }else{
            hashMaps1.put("name", GlobalApplication.user.getNickname());
        }
        textlist.add(hashMaps1);

        HashMap<String, String> hashMaps2 = new HashMap<>();
        if(GlobalApplication.user.getVip()==0){
            hashMaps2.put("name","未开通");
        }else{
            if(date==null){
                date="";
            }
            hashMaps2.put("name",date);
        }
        textlist.add(hashMaps2);
        HashMap<String, String> hashMaps3 = new HashMap<>();
        if(price==null){
            price="";
        }
        hashMaps3.put("name", "¥"+ ToolUtil.doubleToString(Double.valueOf(price))+"/年");
        textlist.add(hashMaps3);
        this.mymember_listview = (ListView) findViewById(R.id.mymember_listview);
        this.myMemberAdapter = new MyMemberAdapter(this,list);
        this.myMemberAdapter.initModel(textlist);
        this.mymember_listview.setAdapter(this.myMemberAdapter);
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

    public void ReUserinfo(){
        String url = Constant.URL_BASE+"userInfo/";
        RetrofitFactory.getInstance().getRequestServicesToken()
                .removemyFavorite(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    if(response.getString("resultCode").equals("2")){
                        JSONArray jsonArray = response.getJSONArray("exceptions");
                        JSONObject js = jsonArray.getJSONObject(0);
                        String message = js.getString("message");
                        ToastUtil.getToast(this,message,"center",0,180).show();
                    }else if(response.getString("resultCode").equals("0")){
                        ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                    }else if(response.getString("resultCode").equals("1")){
                        JSONObject result = response.getJSONObject("resultObject");
                        JSONObject userInfo = result.getJSONObject("user");
                        String userstr = userInfo.toString();
                        Gson gson = new Gson();
                        UserInfo userinfo = gson.fromJson(userstr, UserInfo.class);
                        String psw = GlobalApplication.user.getPassword();
                        GlobalApplication.user.setVip(userinfo.getVip());
                        GlobalApplication.user.setVipEndDate(userinfo.getVipEndDate());
                        GlobalApplication.user.setPassword(psw);
                        new LoginDaoImpl().saveLoginUserinfo(GlobalApplication.sContext,GlobalApplication.user);
                    }
                    initListview();
                    initBtn();
                },throwable -> {
                    ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                    initListview();
                    initBtn();
                });
    }

    public void onStart(){
        super.onStart();
        ReUserinfo();
    }

}
