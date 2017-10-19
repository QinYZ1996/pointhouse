package com.pointhouse.chiguan.w1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.common.http.RetrofitFactory;

import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.l1.LoginDaoImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/7/31.
 */

public class PersonalCenterNetWork {

    public void getWXUserInfo(Context context, String url){
        RetrofitFactory.getInstance().getRequestServices()
                .getWXUserInfo(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    if(response!=null&&!response.equals("")){
                        String headimgurl = response.getString("headimgurl");
                        if(GlobalApplication.user!=null){
                            GlobalApplication.user.setAvatar(headimgurl);
                            new LoginDaoImpl().saveLoginUserinfo(context,GlobalApplication.user);
                            if(GlobalApplication.user.getAvatar()!=null&&!GlobalApplication.user.getAvatar().equals("")){
                                getWXUserHeader();
                            }
                        }
                    }else {
                        ToastUtil.getToast(context,"返回数据异常，获取头像地址失败","center",0,180).show();
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(context,"网络不稳定，获取头像地址失败","center",0,180).show();
                });
    }

    public void getWXUserName(Context context, String url){
        RetrofitFactory.getInstance().getRequestServices()
                .getWXUserInfo(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                    if(response!=null&&!response.equals("")){
                        String nickname = response.getString("nickname");
                        if(GlobalApplication.user!=null){
                            //GlobalApplication.user.setNickname(nickname);
                            //new LoginDaoImpl().saveLoginUserinfo(context,GlobalApplication.user);
                            PersonalCenterActivity.logintbn.setText(GlobalApplication.user.getNickname());
                        }
                    }else {
                        ToastUtil.getToast(context,"返回数据异常，获取用户名异常","center",0,180).show();
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(context,"网络不稳定，获取用户名异常","center",0,180).show();
                });
    }

    public void getWXUserHeader(){
        Log.v("headurl",GlobalApplication.user.getAvatar());
        RetrofitFactory.getInstance().getRequestServices()
                .getDownload(GlobalApplication.user.getAvatar())
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

                        //Bitmap bitmap = BitmapFactory.decodeByteArray(bmp_buffer, 0, bmp_buffer.length);
                        Bitmap bitmap = byteToBitmap(bmp_buffer);
                        PersonalCenterActivity.Header_drawable = new BitmapDrawable(bitmap);
                    }
                    return PersonalCenterActivity.Header_drawable;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(drawable -> {
                    if(drawable!=null){
                        PersonalCenterActivity.UserHeadView.setImageDrawable(drawable);
                        PersonalCenterActivity.HeaderIsOk=true;
                    }
                }, throwable -> {
                    ToastUtil.getToast(GlobalApplication.sContext,"系统异常，请稍后再试！","center",0,180).show();
                });
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
