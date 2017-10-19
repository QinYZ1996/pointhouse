package com.pointhouse.chiguan.common.base;

import android.app.Activity;
import android.view.MotionEvent;

import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;

/**
 * Created by gyh on 2017/8/10.
 */

public class MediaBaseActivity extends Activity {
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    protected boolean isIntercept = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isIntercept) {
            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
                y1 = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                //当手指移动的时候
                x2 = event.getX();
                y2 = event.getY();
                if (y1 - y2 > 50) {
                    MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
                } else if (y2 - y1 > 50) {
                    MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
