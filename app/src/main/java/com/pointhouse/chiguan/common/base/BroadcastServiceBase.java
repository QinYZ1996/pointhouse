package com.pointhouse.chiguan.common.base;

import android.app.NotificationManager;
import android.app.Service;
import android.content.IntentFilter;
import android.support.annotation.CallSuper;

/**
 * Created by ljj on 2017/8/4.
 */

public abstract class BroadcastServiceBase extends Service implements NetBroadcastReceiverService.NetEvevt {
    public static NetBroadcastReceiverService.NetEvevt evevt;

    NetBroadcastReceiverService myReceiver;

    @Override
    @CallSuper
    public void onCreate(){
        evevt = this;
        //核心部分代码：
        myReceiver = new NetBroadcastReceiverService();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, itFilter);
        initService();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }


    /** Notification管理 */
    public NotificationManager mNotificationManager;

    /**
     * 初始化要用到的系统服务
     */
    private void initService() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId){
        mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
//		mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     * */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }

}
