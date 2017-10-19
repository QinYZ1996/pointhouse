package com.pointhouse.chiguan.common.base;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ljj on 2017/7/13.
 */

public abstract class BroadcastFragmentBase extends HttpFragmentBase implements NetBroadcastReceiver.NetEvevt {
    public static NetBroadcastReceiver.NetEvevt evevt;

    NetBroadcastReceiver myReceiver;

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        evevt = this;
        //核心部分代码：
        myReceiver = new NetBroadcastReceiver();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(myReceiver, itFilter);
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }
}
