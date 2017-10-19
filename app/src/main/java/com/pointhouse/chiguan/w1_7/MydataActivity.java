package com.pointhouse.chiguan.w1_7;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.HttpActivityBase;
import com.pointhouse.chiguan.common.base.PermissionActivityBase;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.sd.SdBase;
import com.pointhouse.chiguan.common.util.BaseConvert;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.l1.LoginDaoImpl;
import com.pointhouse.chiguan.l1.Login_activity;
import com.pointhouse.chiguan.w1.PersonalCenterActivity;
import com.pointhouse.chiguan.w1_9.EditNicknameActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by PC-sunjb on 2017/7/5.
 */

public class MydataActivity extends HttpActivityBase {
    private ListView listView;
    private ImageView imageBtn;
    private ArrayList list;
    private View headerview;
    private int count = 0;

//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
    TakePhotoPopWin takePhotoPopWin;

    public static MydataListViewAdapter mListViewAdapter;
    //头像名称
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private Uri imageUri = null;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private int IMAGE_TYPE = 0;//1 头像,2 背景


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_activity_mydata);
        InitListModel();
        initListview();
//        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    this,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
    }

    @Override
    public void getAsynHttpResponse(Call call, String json, int flg, int error) {

    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {

    }

    public void InitListModel(){
        this.list = new ArrayList();

        HashMap<String, String> hash1 = new HashMap<String, String>();
        hash1.put("name", "背景");
        list.add(hash1);

        HashMap<String, String> hash2 = new HashMap<String, String>();
        hash2.put("name", "昵称");
        list.add(hash2);
        HashMap<String, String> hash3 = new HashMap<String, String>();
        HashMap<String, String> hash4 = new HashMap<String, String>();
        hash3.put("name", "绑定微信");
        list.add(hash3);

        hash4.put("name", "绑定手机号");
        list.add(hash4);

    }

    public void Data_back(View view){
        super.onBackPressed();
    }

    public void initListview() {
        HashMap<String, String> hmap = (HashMap<String, String>) this.list.get(0);
        String headerstr = "头像";

        this.listView = (ListView) findViewById(R.id.MyDataList);
        this.headerview = getLayoutInflater().inflate(R.layout.w1_header_mydata, null, true);

        if (PersonalCenterActivity.Header_drawable != null) {
            ImageView imageView = (ImageView) this.headerview.findViewById(R.id.mydateheader_btn);
            imageView.setImageDrawable(PersonalCenterActivity.Header_drawable);
        }

        count++;
        if(count<2){
            this.listView.addHeaderView(this.headerview);
            TextView textView = (TextView) this.headerview.findViewById(R.id.mydata_headtext);
            textView.setText(headerstr);
        }

        mListViewAdapter = new MydataListViewAdapter(this, this.list);

        this.listView.setAdapter(mListViewAdapter);

        this.listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = null;
            if (SharedPreferencesUtil.readToken(this) == null || SharedPreferencesUtil.readToken(this).equals("")) {
                ToastUtil.getToast(MydataActivity.this, "请登录后再操作", "center", 0, 180).show();
                intent = IntentUriUtil.getIntent("l1","w1_7");
                MydataActivity.this.startActivity(intent);
            } else {
                switch (position) {
                    case 0: {
                        Log.v("点击了:", "更换头像");
                        IMAGE_TYPE = 1;
                        imageBtn = (ImageView) findViewById(R.id.mydateheader_btn);
                        //创建弹出框页面对象
                        takePhotoPopWin = new TakePhotoPopWin(MydataActivity.this, itemsOnClick);
                        takePhotoPopWin.showAtLocation(MydataActivity.this.findViewById(R.id.upview), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    }
                    case 1: {
                        Log.v("点击了:", "更换背景");
                        IMAGE_TYPE =2;
                        imageBtn = (ImageView) findViewById(R.id.mydateheader_btn);
                        //创建弹出框页面对象
                        takePhotoPopWin = new TakePhotoPopWin(MydataActivity.this, itemsOnClick);
                        takePhotoPopWin.showAtLocation(MydataActivity.this.findViewById(R.id.upview), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    }
                    case 2: {
                        Log.v("点击了:", "更换昵称");
                        intent = new Intent();
                        intent.setClass(MydataActivity.this, EditNicknameActivity.class);
                        TextView nickname = (TextView) view.findViewById(R.id.mydate_text);
                        intent.putExtra("nickname", nickname.getText().toString());
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        if(GlobalApplication.user.getStatus().equals("2")||GlobalApplication.LoginType==2){
                            return;
                        }
                        if(!isWeixinAvilible()){
                            ToastUtil.getToast(this,"请安装最新版本微信","center",0,180).show();
                            return;
                        }
                        Log.v("点击了:", "绑定微信");
                        HashMap map = (HashMap) list.get(2);
                        String content = map.get("name").toString();
                        //if (content.equals("绑定微信")) {
                            GlobalApplication.BandweChat=1;
                            final SendAuth.Req req = new SendAuth.Req();
                            req.scope = "snsapi_userinfo";
                            req.state = "diandi_wx_login";
                            GlobalApplication.wxapi.sendReq(req);
                            finish();
//                        } else {
//                            intent = new Intent(IntentUriUtil.getIntent("l1_1", "w1_7"));
//                            GlobalApplication.fromid = "w1_7";
//                            startActivity(intent);
//                        }
                        break;
                    }
                    case 4: {
                        if(GlobalApplication.user.getStatus().equals("1")){
                            return;
                        }
                        Log.v("点击了:", "绑定手机");
                        intent = new Intent(IntentUriUtil.getIntent("l1_1", "w1_7"));
                        GlobalApplication.fromid = "w1_7";
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
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

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");//选择图片
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intentFromGallery, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 裁剪原始的图片
     */
    String cropName = "";
    File tempfile;

    public void cropRawPhoto(Uri uri) {
        cropName = SdBase.SDCardRoot + System.currentTimeMillis() + ".jpg";
        tempfile = new File(cropName);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        if(IMAGE_TYPE==2){
            intent.putExtra("aspectX", 2);
        }else{
            intent.putExtra("aspectX", 1);
        }
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputY", 750);
        intent.putExtra("outputX", 750);
        intent.putExtra("scale", false);
        imageUri = Uri.fromFile(tempfile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("return-data", false);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private Handler handler = new Handler();
    String base = "";
    String backbase="";
    Bitmap bitmap = null;
    String url = "";
    Drawable drawable;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY://如果是来自本地的
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
//                if(IMAGE_TYPE==1){
                    cropRawPhoto(data.getData());//直接裁剪图片
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 4;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(data.getData()),null,options);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    drawable = new BitmapDrawable(bitmap);
//                    ClipviewActivity.drawable = drawable;
//                    Intent intent = new Intent(this,ClipviewActivity.class);
//                    startActivity(intent);
//                }else{
//                    try {
//                        if(data==null){
//                            return;
//                        }
//                        if(data.getExtras()==null&&data.getData()==null){
//                            return;
//                        }
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 4;
//                        bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(data.getData()),null,options);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    drawable = new BitmapDrawable(bitmap);
//                    url = "uploadBg/";
//                    if (bitmap == null) {
//                        return;
//                    }
//                    new Thread(() -> handler.post(() -> {
//                        backbase = Bitmap2StrByBase64(bitmap);
//                        UploadImage(url);
//                    })).start();
//                }
                break;
            case PHOTO_REQUEST_CAREMA: {
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                Uri u = imageUri;
                cropRawPhoto(u);
//                try {
////                       if(data==null){
////                           return;
////                       }
////                       if(data.getExtras()==null&&data.getData()==null){
////                           return;
////                       }
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 4;
//                    bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(u),null,options);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                    drawable = new BitmapDrawable(bitmap);
//                    url = "uploadBg/";
//                    if (bitmap == null) {
//                        return;
//                    }
//                    new Thread(() -> handler.post(() -> {
//                        backbase = Bitmap2StrByBase64(bitmap);
//                        UploadImage(url);
//                    })).start();
                    break;
                }
            case PHOTO_REQUEST_CUT: {
//                if(data.getExtras()==null){
//                    return;
//                }
                try {
                    if(data==null){
                        return;
                    }
                    if(data.getExtras()==null&&data.getData()==null){
                        return;
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri),null,options);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Bitmap bitmap = data.getParcelableExtra("data");
                drawable = new BitmapDrawable(bitmap);
                if (IMAGE_TYPE == 1) {
                    url = "uploadAvatar/";
                }
                else {
                    url = "uploadBg/";
                }
                if (bitmap == null) {
                    return;
                }
                new Thread(() -> handler.post(() -> {
                    if(IMAGE_TYPE==1){
                        base = Bitmap2StrByBase64(bitmap);
                    }
                   else{
                        backbase = Bitmap2StrByBase64(bitmap);
                    }
                    UploadImage(url);
                })).start();
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //进行 BASE64 编码
    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }



    String strImgPath = "";
    //拍照
    public void takePhoto() {
        Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        strImgPath = Environment.getExternalStorageDirectory()
                .toString() + "/Xiong_PIC/";
        String fileName = System.currentTimeMillis() + PHOTO_FILE_NAME;
        File out = new File(strImgPath);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(strImgPath, fileName);
        strImgPath = strImgPath + fileName;// 该照片的绝对路径
        imageUri = Uri.fromFile(out);
        getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//根据uri保存照片
        getPhoto.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//保存照片的质量
        startActivityForResult(getPhoto, PHOTO_REQUEST_CAREMA);//启动相机拍照
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            takePhotoPopWin.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo: {
                    MydataActivity.this.requestPermissions(PermissionActivityBase.PermissionGroup.CAMERA, true, new PermissionActivityBase.CallbackListener() {
                        @Override
                        public void onPermissionsOK(int requestCode, List<String> perms) {
                            MydataActivity.this.requestPermissions(PermissionActivityBase.PermissionGroup.STORAGE, true, new PermissionActivityBase.CallbackListener() {
                                @Override
                                public void onPermissionsOK(int requestCode, List<String> perms) {
                                    takePhoto();//调用具体方法
                                }

                                @Override
                                public void onPermissionsNG(int requestCode, List<String> perms) {
                                    //Toast.makeText(MydataActivity.this,"授权失败",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {
                                    //Toast.makeText(MydataActivity.this,"授权取消",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                        @Override
                        public void onPermissionsNG(int requestCode, List<String> perms) {
                            //Toast.makeText(MydataActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {
                            //Toast.makeText(MydataActivity.this,"授权取消",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
                case R.id.btn_pick_photo:
                    MydataActivity.this.requestPermissions(PermissionActivityBase.PermissionGroup.STORAGE, true, new PermissionActivityBase.CallbackListener() {
                        @Override
                        public void onPermissionsOK(int requestCode, List<String> perms) {
                            choseHeadImageFromGallery();//调用具体方法
                        }

                        @Override
                        public void onPermissionsNG(int requestCode, List<String> perms) {
                            Toast.makeText(MydataActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {
                            Toast.makeText(MydataActivity.this,"授权取消",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public void UploadImage(String url) {
        String URL = Constant.URL_BASE+url;
        String strr="";
        try {
            if(IMAGE_TYPE==1){
                strr = URLEncoder.encode(base,"UTF-8");
            }else{
                strr = URLEncoder.encode(backbase,"UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = "[\""+strr+"\"]";
        RetrofitFactory.getInstance().getRequestServicesToken()
                .UploadImage(URL, RetrofitFactory.createRequestBody(str))
                .subscribeOn(Schedulers.newThread())
                .map(response -> {
                    String result = "";
                    String resultCode = response.getString("resultCode");
                    if (resultCode.equals("2")) {
                        com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                        if (exceptions != null && exceptions.size() > 0) {
                            JSONObject exlist = exceptions.getJSONObject(0);
                            result = exlist.getString("message");
                        }
                    } else if (resultCode.equals("1")) {
                        if (IMAGE_TYPE == 1) {
                            PersonalCenterActivity.Header_drawable = drawable;
                            PersonalCenterActivity.HeaderIsOk = true;
                            GlobalApplication.user.setAvatar(base);
                        } else if (IMAGE_TYPE == 2) {
                            PersonalCenterActivity.Background_drawble = drawable;
                            PersonalCenterActivity.Background = true;
                            GlobalApplication.user.setBackground(backbase);
                        }
                        new LoginDaoImpl().saveLoginUserinfo(this, GlobalApplication.user);
                    } else if (resultCode.equals("0")) {
                        result = null;
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response == null) {
                        ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                        return;
                    }
                    if (response.equals("")) {
                        ToastUtil.getToast(this, "上传成功", "center", 0, 180).show();
                        if(IMAGE_TYPE==1){
                            this.imageBtn.setImageDrawable(drawable);
                        }
                    } else {
                        ToastUtil.getToast(this, response, "center", 0, 180).show();
                    }
                    if(tempfile!=null){
                        if (tempfile.exists()) {
                            tempfile.delete();
                        }
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                    if(tempfile!=null){
                        if (tempfile.exists()) {
                            tempfile.delete();
                        }
                    }
                });
    }


    public void weChatBand(Activity context){
        String parm = JsonUtil.initGetRequestParm(GlobalApplication.openid+","+GlobalApplication.wxnickname);
        String url = Constant.URL_BASE+"weChatBand/";
        RetrofitFactory.getInstance().getRequestServicesToken()
                .weChatBand(url,RetrofitFactory.createRequestBody(parm))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    if(response.getString("resultCode").equals("1")){
                        GlobalApplication.user.setOpenid(GlobalApplication.openid);
                        new LoginDaoImpl().saveLoginUserinfo(this,GlobalApplication.user);
                        mListViewAdapter.notifyDataSetChanged();
                        ToastUtil.getToast(GlobalApplication.sContext,"绑定成功","center",0,180).show();
                    }else if(response.getString("resultCode").equals("2")){
                        JSONArray jsonArray = response.getJSONArray("exceptions");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String message = jsonObject.getString("message");
                        ToastUtil.getToast(GlobalApplication.sContext,message,"center",0,180).show();
                        finish();
                        context.finish();
                    }else if(response.getString("resultCode").equals("0")){
                        ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                        finish();
                        context.finish();
                    }

                },throwable -> {
                    ToastUtil.getToast(this,"系统异常，请稍后再试！","center",0,180).show();
                    finish();
                    context.finish();
                });
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

    public void onResume(){
        super.onResume();
        initListview();
    }
}
