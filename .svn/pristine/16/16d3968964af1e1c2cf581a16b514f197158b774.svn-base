package com.pointhouse.chiguan.common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.pointhouse.chiguan.common.util.NetWorkCheck;

/**
 * Created by ljj on 2017/7/13.
 */

public class NetBroadcastReceiverA extends BroadcastReceiver {
    public NetEvevt evevt = BroadcastActivityBase.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // 接口回调传过去状态的类型
            int type = NetWorkCheck.getAPNType(context);
            evevt.onNetChange(type);
        }
    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(int netMobile);
    }
}
