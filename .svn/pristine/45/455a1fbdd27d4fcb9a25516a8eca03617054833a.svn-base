package com.pointhouse.chiguan.w1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.w1_1.MyPurchaseActivity;
import com.pointhouse.chiguan.w1_10.MyMessageActivity;
import com.pointhouse.chiguan.w1_11.MySettingsActivity;
import com.pointhouse.chiguan.w1_13.MyMemberActivity;
import com.pointhouse.chiguan.w1_2.MyGroupActivity;
import com.pointhouse.chiguan.w1_3.MyStudyActivity;
import com.pointhouse.chiguan.w1_4.MyCollectionActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ljj on 2017/6/30.
 */

public class PersonalCenterActivity extends Fragment {
    static View header_view;
    static TextView logintbn;
    static ImageView UserHeadView;
    public static Boolean HeaderIsOk=false;
    public static Boolean Background=false;
    public static Drawable Header_drawable;
    public static Drawable Background_drawble;
    ImageView personalcenter_vipimage;
    ListView listView;
    List<HashMap<String,String>> list;
    ListViewAdapter mListViewAdapter;
    View view;

    RelativeLayout layout;
    String token;
    TextView ftextview;
    LayoutInflater inflater;
    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.w1_activity_personalcenter, container, false);
        token = SharedPreferencesUtil.readToken(getContext());
        initModelview();
        //列表数据
        ListviewModelInit();
        //设置背景
        if(!Background){
            initBackGround();
        }
        //设置头像
        if (!HeaderIsOk){
            initheadView();
        }
        //列表数据加载
        initListview(inflater);
        //初始化按钮事件
        initBtn(view);
        return view;
    }

    public void initModelview(){
        ftextview = (TextView) this.view.findViewById(R.id.Ftext);
        layout = (RelativeLayout) this.view.findViewById(R.id.background_imageview);
        UserHeadView =(ImageView) this.view.findViewById(R.id.UserHeadView);
        layout =(RelativeLayout ) this.view.findViewById(R.id.background_imageview);
        //background_black.getBackground().setAlpha(200);
    }

    public void initBackGround(){
        if(SharedPreferencesUtil.readToken(getContext())!=null&&!SharedPreferencesUtil.readToken(getContext()).equals("")){
            ftextview.setText("");
        }

        String backurl = "";
        if(token==null&&token.equals("")){
            layout.setBackgroundResource(R.drawable.mine_background_default);
            return;
        }

        if(GlobalApplication.LoginType==1||GlobalApplication.LoginType==2){
            if(GlobalApplication.user!=null) {
                if (GlobalApplication.user.getBackground() != null && !GlobalApplication.user.getBackground().equals("")) {
                    Log.v("backurl", backurl);
                    backurl = GlobalApplication.user.getBackground();
                    downloadBackImage(backurl);
                } else {
                    return;
                }
            }
        }else if(GlobalApplication.LoginType==0){
                if(GlobalApplication.user!=null){
                    if(GlobalApplication.user.getBackground()!=null&&!GlobalApplication.user.getBackground().equals("")){
                        Log.v("backurl",backurl);
                        String str = GlobalApplication.user.getBackground();
                        Bitmap bitmap = base64ToBitmap(str);
                        if(bitmap==null){
                            backurl = GlobalApplication.user.getBackground();
                            downloadBackImage(backurl);
                        }else{
                            Drawable drawable= new BitmapDrawable(bitmap);
                            Background_drawble = drawable;
                            layout.setBackground(Background_drawble);
                            Background=true;
                        }
                    }else{
                        return;
                    }
                }
            }
    }


    public void downloadBackImage(String backurl){RetrofitFactory.getInstance().getRequestServices()
            .getDownload(backurl)
            .subscribeOn(Schedulers.newThread())
            .map(response->{
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    byte[] bmp_buffer;
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    outStream.close();
                    inputStream.close();
                    bmp_buffer = outStream.toByteArray();
                    Bitmap bitmap = PersonalCenterNetWork.byteToBitmap(bmp_buffer);
                    Background_drawble = new BitmapDrawable(bitmap);
                }
                return Background_drawble;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(drawable -> {
                if(drawable!=null){
                    layout.setBackgroundDrawable(Background_drawble);
                    Background=true;
                }
            }, throwable -> {
                Log.d("1","4");
                ToastUtil.getToast(getContext(),"网络异常","center",0,180).show();
                layout.setBackgroundDrawable(Background_drawble);
            });
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = new byte[0];
        try {
            bytes = Base64.decode(base64Data, Base64.DEFAULT);
        } catch (Exception e){
            e.printStackTrace();
        }
        return PersonalCenterNetWork.byteToBitmap(bytes);
    }


    public void downloadHeadImage(String headurl){
        RetrofitFactory.getInstance().getRequestServices()
            .getDownload(headurl)
            .subscribeOn(Schedulers.newThread())
            .map(response->{
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    byte[] bmp_buffer;
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    outStream.close();
                    inputStream.close();
                    bmp_buffer = outStream.toByteArray();
                    Bitmap bitmap = PersonalCenterNetWork.byteToBitmap(bmp_buffer);
                    Header_drawable = new BitmapDrawable(bitmap);
                }
                return Header_drawable;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(drawable -> {
                if(drawable!=null){
                    UserHeadView.setImageDrawable(Header_drawable);
                    HeaderIsOk=true;
                }
            }, throwable -> {
                throwable.printStackTrace();
                ToastUtil.getToast(getContext(),"网络异常","center",0,180).show();
            });
    }

    public void initheadView(){
        PersonalCenterNetWork personalCenterNetWork = new PersonalCenterNetWork();
        String headurl = "";
        if(token==null&&token.equals("")){
            return;
        }
        if(GlobalApplication.LoginType==1) {
            Log.v("headurl",GlobalApplication.user.getAvatar());

            if(GlobalApplication.user!=null){
                if (GlobalApplication.user.getAvatar() != null && !GlobalApplication.user.getAvatar().equals("")) {
                    headurl = GlobalApplication.user.getAvatar();
                    downloadHeadImage(headurl);
                } else {
                    return;
                }
            }
        }else if(GlobalApplication.LoginType==2){
            Log.v("headurl",GlobalApplication.user.getAvatar());
            if (GlobalApplication.user.getAvatar()!= null && !GlobalApplication.user.getAvatar().equals("")) {
                headurl = GlobalApplication.user.getAvatar();
                downloadHeadImage(headurl);
            }else{
                String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                        + GlobalApplication.access_token
                        + "&openid="
                        + GlobalApplication.openid;
                personalCenterNetWork.getWXUserInfo(getContext(),path);
            }
        }else {
            if(GlobalApplication.user!=null){
                if (GlobalApplication.user.getAvatar() != null && !GlobalApplication.user.getAvatar().equals("")) {
                    String str = GlobalApplication.user.getAvatar();
                    Bitmap bitmap = base64ToBitmap(str);
                    if(bitmap==null){
                        headurl = GlobalApplication.user.getAvatar();
                        downloadHeadImage(headurl);
                    }else{
                        Drawable drawable = new BitmapDrawable(bitmap);
                        Header_drawable=drawable;
                        UserHeadView.setImageDrawable(Header_drawable);
                        HeaderIsOk=true;
                    }

                } else {
                    return;
                }
            }
        }
    }



    public void initListview(LayoutInflater inflater){
        String header_str = list.get(0).get("name");
        list.remove(0);
        mListViewAdapter = new ListViewAdapter(getActivity(),this.list);

        header_view = inflater.inflate(R.layout.w1_item_personcalheader, null, true);
        TextView rightview =(TextView) header_view.findViewById(R.id.item_personcal_righttext);
        if(SharedPreferencesUtil.readToken(GlobalApplication.sContext)!=null&&!SharedPreferencesUtil.readToken(GlobalApplication.sContext).equals("")){
            if(GlobalApplication.user.getVip()==0){
                rightview.setText("立即开通");
            }
        }
        TextView textView = (TextView) header_view.findViewById(R.id.pcheaderText);
        textView.setText(header_str);

        this.listView = (ListView)this.view.findViewById(R.id.MyListView);
        this.listView.addHeaderView(header_view);
        this.listView.setAdapter(mListViewAdapter);

        /**
         * 直接监听listview中的item事件
         */
        this.listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = null;
            if(token==null||token.equals("")){
                ToastUtil.getToast(PersonalCenterActivity.this.getActivity(),"请登录后再操作","center",0,180).show();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"l1?id=w1"));
                PersonalCenterActivity.this.startActivity(intent);
                GlobalApplication.fromid = "w1";
            }else{
                switch (position){
                    case 0:{
                        Log.v("点击了","我的会员");
                        intent = new Intent();
                        intent.setClass(PersonalCenterActivity.this.getContext(), MyMemberActivity.class);
                        PersonalCenterActivity.this.startActivity(intent);
                        break;
                    }
                    case 1:{
                        Log.v("点击了","我的学习");
                        intent = new Intent();
                        intent.setClass(PersonalCenterActivity.this.getContext(), MyStudyActivity.class);
                        PersonalCenterActivity.this.startActivity(intent);
                        break;
                    }
                    case 2:{
                        Log.v("点击了","我的购买");
                        intent = new Intent();
                        intent.setClass(PersonalCenterActivity.this.getContext(), MyPurchaseActivity.class);
                        PersonalCenterActivity.this.startActivity(intent);
                        break;
                    }
                   case 3:{
                        Log.v("点击了","我的互动群");
                        intent = new Intent();
                        intent.setClass(PersonalCenterActivity.this.getContext(), MyGroupActivity.class);
                        PersonalCenterActivity.this.startActivity(intent);
                        break;
                    }
                    case 4:{
                        Log.v("点击了","我的收藏");
                        intent = new Intent();
                        intent.setClass(PersonalCenterActivity.this.getContext(), MyCollectionActivity.class);
                        PersonalCenterActivity.this.startActivity(intent);
                        break;
                    }
                    case 5:
                        Log.v("点击了","我的消息");
                        intent = new Intent();
                        intent.setClass(PersonalCenterActivity.this.getContext(), MyMessageActivity.class);
                        PersonalCenterActivity.this.startActivity(intent);
                        break;
                }

            }
        });
    }

    public void ListviewModelInit(){
        this.list= new ArrayList<>();
        HashMap<String,String> hash = new HashMap<>();
        if(token!=null&&!token.equals("")&&GlobalApplication.user!=null){
            if(GlobalApplication.user.getVip()==0){
                hash.put("name","开通会员");
            }else{
                hash.put("name","我的会员");
            }
        }else{
            hash.put("name","我的会员");
        }
        list.add(hash);
        HashMap<String,String> hash1 = new HashMap<>();
        hash1.put("name","我的学习");
        list.add(hash1);

        HashMap<String,String> hash2 = new HashMap<>();
        hash2.put("name","我的购买");
        list.add(hash2);

        HashMap<String,String> hash3 = new HashMap<>();
        hash3.put("name","我的互动群");
        list.add(hash3);

        HashMap<String,String> hash4 = new HashMap<>();
        hash4.put("name","我的收藏");
        list.add(hash4);

        HashMap<String,String> hash6 = new HashMap<>();
        hash6.put("name","我的消息");
        list.add(hash6);
    }


    public void initBtn(View view){
        logintbn = (TextView) view.findViewById(R.id.loginBtn);
        personalcenter_vipimage = (ImageView) view.findViewById(R.id.personalcenter_vipimage);
        //PersonalCenterNetWork personalCenterNetWork = new PersonalCenterNetWork();
        if(token!=null&&!token.equals("")&&GlobalApplication.user!=null){
            if(GlobalApplication.LoginType==1){
                if(GlobalApplication.user.getNickname()==null||GlobalApplication.user.getNickname().equals("")){
                    logintbn.setText(GlobalApplication.user.getMobile());
                }else{
                    logintbn.setText(GlobalApplication.user.getNickname());
                }
            }else if(GlobalApplication.LoginType==2){
                if(GlobalApplication.user.getNickname()==null||GlobalApplication.user.getNickname().equals("")){
//                    String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
//                            + GlobalApplication.access_token
//                            + "&openid="
//                            + GlobalApplication.openid;
//                    personalCenterNetWork.getWXUserName(getContext(),path);
                    logintbn.setText(GlobalApplication.wxnickname);
                }
            }else{
                if(GlobalApplication.user.getNickname()==null||GlobalApplication.user.getNickname().equals("")){
                    logintbn.setText(GlobalApplication.user.getMobile());
                }else{
                    logintbn.setText(GlobalApplication.user.getNickname());
                }
            }
        }else {
            logintbn.setText("点击登录");
        }
        logintbn.setOnClickListener(v -> {
            Intent intent = null;
            if(token!=null&&!token.equals("")&&GlobalApplication.user!=null){
                intent = IntentUriUtil.getIntent("w1_7","w1");
            }else{
                intent = IntentUriUtil.getIntent("l1","w1");
                GlobalApplication.fromid = "w1";
            }
            PersonalCenterActivity.this.startActivity(intent);
        });

        RelativeLayout gotoMydate = (RelativeLayout)view.findViewById(R.id.FloginBtns);
        gotoMydate.setOnClickListener(v -> {
            Intent intent = null;
            if(SharedPreferencesUtil.readToken(getContext())!=null&&!SharedPreferencesUtil.readToken(getContext()).equals("")){
                intent = IntentUriUtil.getIntent("w1_7","w1");
            }else{
                intent = IntentUriUtil.getIntent("l1","w1");
                ToastUtil.getToast(getActivity(),"请先登录","center",0,180).show();
            }
            PersonalCenterActivity.this.startActivity(intent);

        });

        RelativeLayout gosettings = (RelativeLayout)view.findViewById(R.id.go_btn);
        gosettings.setOnClickListener(v -> {
            Intent intent = null;
            intent = new Intent(PersonalCenterActivity.this.getActivity(), MySettingsActivity.class);
            PersonalCenterActivity.this.startActivity(intent);
        });
    }

    public void freshenview(){
        if(SharedPreferencesUtil.readToken(getContext())!=null&&!SharedPreferencesUtil.readToken(getContext()).equals("")){
            if(GlobalApplication.user!=null){
                if(GlobalApplication.user.getVip()==0){
                    this.personalcenter_vipimage.setVisibility(View.INVISIBLE);
                }else if(GlobalApplication.user.getVip()==1){
                    this.personalcenter_vipimage.setVisibility(View.VISIBLE);
                }
            }
        }else{
            this.personalcenter_vipimage.setVisibility(View.INVISIBLE);
        }
        if(Header_drawable!=null){
            UserHeadView.setImageDrawable(Header_drawable);
        }else{
            UserHeadView.setImageDrawable(null);
            UserHeadView.setImageResource(R.mipmap.headportrait);
        }

        if(Background_drawble!=null){
            layout.setBackgroundDrawable(Background_drawble);
        }else{
            layout.setBackgroundDrawable(null);
            layout.setBackgroundResource(R.drawable.mine_background_default);
        }

        if(SharedPreferencesUtil.readToken(getContext())!=null&&!SharedPreferencesUtil.readToken(getContext()).equals("")&&GlobalApplication.user!=null){
            logintbn = (TextView) this.view.findViewById(R.id.loginBtn);
            if(GlobalApplication.user.getNickname()!=null&&!GlobalApplication.user.getNickname().equals("")){
                logintbn.setText(GlobalApplication.user.getNickname());
            }else{
                logintbn.setText(GlobalApplication.user.getMobile());
            }

            ftextview.setText("");
        } else{
            logintbn.setText("点击登录");
            ftextview.setText("快速登录,专享池馆更好服务");
        }
    }

    private final String TAG = "sjb";

    public void onStart() {
        super.onStart();
        token = SharedPreferencesUtil.readToken(getActivity());

        this.listView.removeHeaderView(header_view);
        this.listView = null;
        this.mListViewAdapter = null;
        this.list= null;
        //列表数据
        ListviewModelInit();
        initListview(inflater);
        this.mListViewAdapter.notifyDataSetChanged();
        freshenview();
        Log.d(TAG, "onStart()");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
