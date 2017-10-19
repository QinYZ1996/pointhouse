package com.pointhouse.chiguan.common.sd;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SdBase {

    public static String SDCardRoot = Environment.getExternalStorageDirectory().toString()+ File.separator;

    public SdBase(){}

    /**
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    //判断文件是否存在
    public static boolean isFileExist(String fileName){
        File file=new File(SDCardRoot+fileName);
        return file.exists();
    }

}
