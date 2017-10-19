package com.pointhouse.chiguan.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by P on 2017/7/12.
 */

public class PermisionUtils {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 检查应用程序是否有写入设备存储的权限。
     * 如果应用程序没有权限，那么将提示用户
     * 授予权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // 检查我们是否有写权限
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 我们没有权限，提示用户
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
