package com.pointhouse.chiguan.l1_1;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * Created by P on 2017/8/11.
 */

public class TimeCount extends CountDownTimer {


    private static Handler mHandler;
    public static final int IN_RUNNING = 1001;
    public static int END_RUNNING = 1002;

    /**
     * @param millisInFuture
     *            // 倒计时的时长
     * @param countDownInterval
     *            // 间隔时间
     * @param handler
     *            // 通知进度的Handler
     */
    public TimeCount(long millisInFuture, long countDownInterval,
                             Handler handler) {
        super(millisInFuture, countDownInterval);
        mHandler = handler;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mHandler != null)
            mHandler.obtainMessage(IN_RUNNING,
                    (millisUntilFinished / 1000)+"").sendToTarget();
    }

    @Override
    public void onFinish() {
        if (mHandler != null)
            mHandler.obtainMessage(END_RUNNING, "重新发送").sendToTarget();
    }
}
