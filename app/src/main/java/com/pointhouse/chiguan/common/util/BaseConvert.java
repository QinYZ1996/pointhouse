package com.pointhouse.chiguan.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by P on 2017/8/7.
 */

public class BaseConvert {

    public String bitmaptoString(Bitmap bitmap){
        String localChartSet = System.getProperty("file.encoding");
        System.out.println("localChartSet>>>>"+localChartSet);   //查看本地默认字符集
        //将Bitmap转换成字符串
        String base=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        base=Base64.encodeToString(bytes,Base64.DEFAULT);
        return base;
    }
}
