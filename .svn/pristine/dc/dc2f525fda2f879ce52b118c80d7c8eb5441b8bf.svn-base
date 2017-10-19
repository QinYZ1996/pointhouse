package com.pointhouse.chiguan.l1_1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * Created by P on 2017/8/11.
 */

public class MessageService extends Service{

    private static Handler mHandler;
    private static TimeCount mCodeTimer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        mCodeTimer = new TimeCount(60000, 1000, mHandler);
        mCodeTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    /**
     * 设置Handler
     */
    public static void setHandler(Handler handler) {
        mHandler = handler;
    }
}
