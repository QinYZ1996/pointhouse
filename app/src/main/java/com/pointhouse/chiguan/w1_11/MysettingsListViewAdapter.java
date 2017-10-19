package com.pointhouse.chiguan.w1_11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.l1.LoginDaoImpl;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/7/18.
 */

public class MysettingsListViewAdapter extends BaseAdapter {
    static String selectedid = "1";
    // 上下文
    private Context context;
    // List表数据
    private List<HashMap<String, String>> mList;
    ViewCache viewCache;
    String text;
    // 构造函数
    public MysettingsListViewAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        mList = list;
        if(SharedPreferencesUtil.readToken(context)!=null&&!SharedPreferencesUtil.readToken(context).equals("")&&GlobalApplication.user!=null){
            selectedid = GlobalApplication.user.getPushStatus();
        }
    }

    //设置某行不可点击
    public boolean isEnabled(int position) {
        return position != 1;
    }

    // List表单的总数
    @Override
    public int getCount() {
        return mList.size();
    }
    // 位于position处的List表单的一项
    @Override
    public Object getItem(int position) {
        return  mList.get(position);
    }
    // List表单的各项的索引
    @Override
    public long getItemId(int position) {
        return position;
    }
    // 最重要的获得视图内容方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView==null){
            viewCache = new ViewCache();

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.w1_11_item_mysettings, null);

            viewCache.rightimage = (ImageView) convertView.findViewById(R.id.mysettings_item_rightimageview);
            viewCache.lefttext =(TextView) convertView.findViewById(R.id.mysettings_item_lefttext);
            convertView.setTag(viewCache);
//        }else{
            // 根据标记找到viewCache，达到缓存的目的（软引用）
            viewCache = (ViewCache) convertView.getTag();
//        }
        viewCache.lefttext.setText(MysettingsListViewAdapter.this.mList.get(position).get("name"));
        text = MysettingsListViewAdapter.this.mList.get(position).get("name");
        if(viewCache.lefttext.getText().toString().equals("接收推送")){
            viewCache.rightimage.setBackgroundDrawable(null);
            if(selectedid.equals("0")){
                viewCache.rightimage.setImageResource(R.mipmap.tuisongbujieshou);
            }else{
                viewCache.rightimage.setImageResource(R.mipmap.tuisongjieshou);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(viewCache.rightimage.getLayoutParams());
            lp.width=86;
            lp.height=60;
            lp.rightMargin=36;
            viewCache.rightimage.setLayoutParams(lp);
            viewCache.rightimage.setOnClickListener(v -> {
                ImageView imageView = (ImageView) v;
                if(selectedid.equals("0")){
                    selectedid="1";
                }else{
                    selectedid="0";
                }
                HashMap hashMap = new HashMap();
                hashMap.put("pushStatus",selectedid);
                JSONObject jsonObject = new JSONObject(hashMap);
                JSONArray jarray = new JSONArray();
                jarray.add(jsonObject);
                String url = Constant.URL_BASE+"updateUserInfo/";
                RetrofitFactory.getInstance().getRequestServicesToken()
                        .JPushSelected(url,RetrofitFactory.createRequestBody(jarray.toString()))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response->{
                            if(response.getString("resultCode").equals("2")){
                                JSONArray jsonArray = response.getJSONArray("exceptions");
                                JSONObject js = jsonArray.getJSONObject(0);
                                String message = js.getString("message");
                                ToastUtil.getToast(context,message,"center",0,180).show();
                            }else if(response.getString("resultCode").equals("0")){
                                ToastUtil.getToast(context,"系统异常，请稍后再试！","center",0,180).show();
                            }else if(response.getString("resultCode").equals("1")){
                                String result = response.getString("resultObject");
                                if(result.equals("success")){
                                    if(selectedid.equals("0")){
                                        imageView.setImageResource(R.mipmap.tuisongbujieshou);
                                    }else{
                                        imageView.setImageResource(R.mipmap.tuisongjieshou);
                                    }
                                    LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(imageView.getLayoutParams());
                                    lpp.width=86;
                                    lpp.height=60;
                                    lpp.rightMargin=36;
                                    imageView.setLayoutParams(lpp);
                                    GlobalApplication.user.setPushStatus(selectedid+"");
                                    new LoginDaoImpl().saveLoginUserinfo(context,GlobalApplication.user);
                                }
                               }
                        },throwable -> {
                            if(selectedid.equals("0")){
                                selectedid="1";
                            }else{
                                selectedid="0";
                            }
                            ToastUtil.getToast(context,"系统异常，请稍后再试！","center",0,180).show();
                        });
            });
        }else{
            viewCache.rightimage.setImageResource(R.mipmap.forwardgray);
            viewCache.rightimage.setEnabled(false);
        }
        return convertView;
    }

    // 辅助缓存类
    class ViewCache {
        TextView lefttext;
        ImageView rightimage;
    }
}
