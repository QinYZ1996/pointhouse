package com.pointhouse.chiguan.common.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import com.pointhouse.chiguan.Application.MediaService;

/**
 * MediaService帮助类
 * Created by Maclaine on 2017/7/20.
 */
public class MediaServiceHelper {
    //音频
    private static Intent mediaIntent=null;

    public static boolean hasRequestPermission;

    private static Intent getMediaIntent(Context context){
        if(mediaIntent==null){
            mediaIntent = new Intent(context, MediaService.class);
        }
        return mediaIntent;
    }

    public static void getService(Context context,CallBack callBack){
        context.bindService(getMediaIntent(context), new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    MediaService mediaService = ((MediaService.MediaBinder) service).getService();
                    callBack.accept(mediaService);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        context.unbindService(this);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    public interface CallBack{
        public void accept(MediaService mediaService);
    }

    public static boolean hasPermission(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (!Settings.canDrawOverlays(context)) {
                return false;
            }
        }
        return true;
    }
}
