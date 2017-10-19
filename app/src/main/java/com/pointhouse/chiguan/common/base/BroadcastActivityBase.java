package com.pointhouse.chiguan.common.base;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;

/**
 * Created by ljj on 2017/7/13.
 */

public abstract class BroadcastActivityBase extends HttpActivityBase implements NetBroadcastReceiverA.NetEvevt{
    public static NetBroadcastReceiverA.NetEvevt evevt;

    NetBroadcastReceiverA myReceiver;

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        evevt = this;
        //核心部分代码：
        myReceiver = new NetBroadcastReceiverA();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, itFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
