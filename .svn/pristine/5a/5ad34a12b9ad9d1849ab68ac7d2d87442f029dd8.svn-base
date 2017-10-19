package com.pointhouse.chiguan.common.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by PC-sunjb on 2017/6/29.
 */

public class ToastUtil {

    public static final String center = "center";
    public static final String left = "left";
    public static final String right = "right";
    public static final String bottom = "bottom";


    public static Toast toast = null;

    public ToastUtil(){

    }

    public static Toast getInstance(){
        synchronized (ToastUtil.class){
            if(toast==null){
                toast = new Toast(null);
            }
        }
        return toast;
    }


    public static Toast getToast(Context context,String info, String aspect,int x,int y){
        toast = Toast.makeText(context,info,Toast.LENGTH_SHORT);
        if(aspect.equals(center)){
            toast.setGravity(Gravity.CENTER, x, y);
        }else if(aspect.equals(left)){
            toast.setGravity(Gravity.LEFT, x, y);
        }else if(aspect.equals(right)){
            toast.setGravity(Gravity.RIGHT, x, y);
        }else if(aspect.equals(bottom)){
            toast.setGravity(Gravity.BOTTOM, x, y);
        }
        return toast;
    }
}
