package com.pointhouse.chiguan.common.sdcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.pointhouse.chiguan.Application.GlobalApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ljj on 2017/7/18.
 */

public class SDCardHelper {
    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

    //getSDCacheDir
    public static File getSDCacheDir(Context context){
        File sdcache;

        if (SDCardHelper.isSDCardMounted()){
            if(context.getExternalCacheDir() != null){
                sdcache = context.getExternalCacheDir();
            } else {
                sdcache = context.getCacheDir();
            }
        } else {
            sdcache = context.getCacheDir();
        }

        return sdcache;
    }


    //getSDExternalFilesDir
    public static File getSDExternalFilesDir(String filePath, String fileNameAndSlash){
        File file = null;
        if (SDCardHelper.isSDCardMounted()) {
            if (GlobalApplication.sContext.getExternalFilesDir(null) != null) {
                file = new File(GlobalApplication.sContext.getExternalFilesDir(null).getPath() + "/" + filePath + "/", fileNameAndSlash);
            } else {
                file = new File(GlobalApplication.sContext.getFilesDir().getPath() + "/" + filePath + "/", fileNameAndSlash);
            }
        } else {
            file = new File(GlobalApplication.sContext.getFilesDir().getPath() + "/" + filePath + "/", fileNameAndSlash);
        }
        return file;
    }

    //checkSize
    public static boolean checkSize(File file, long fileSize){
        if (file == null){
            return  false;
        }

        long availableSize = 0;

        if(SDCardHelper.isSDCardMounted()) {

            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                long blockSize = sf.getBlockSizeLong();
                long availCount = sf.getAvailableBlocksLong();
                availableSize = blockSize * availCount;
            } else {
                long blockSize = sf.getBlockSize();
                long availCount = sf.getAvailableBlocks();
                availableSize = blockSize * availCount;
            }
        }

        if (!file.exists() && fileSize > availableSize){
            return  false;
        }
        return  true;
    }


    // 获取SD卡的根目录
//    public static String getSDCardBaseDir() {
//        if (isSDCardMounted()) {
//            return Environment.getExternalStorageDirectory().getAbsolutePath();
//        }
//        return null;
//    }

//    // 获取SD卡的完整空间大小，返回MB
//    public static long getSDCardSize() {
//        if (isSDCardMounted()) {
//            StatFs fs = new StatFs(getSDCardBaseDir());
//            long count = fs.getBlockCountLong();
//            long size = fs.getBlockSizeLong();
//            return count * size / 1024 / 1024;
//        }
//        return 0;
//    }
//
//    // 获取SD卡的剩余空间大小
//    public static long getSDCardFreeSize() {
//        if (isSDCardMounted()) {
//            StatFs fs = new StatFs(getSDCardBaseDir());
//            long count = fs.getFreeBlocksLong();
//            long size = fs.getBlockSizeLong();
//            return count * size / 1024 / 1024;
//        }
//        return 0;
//    }
//
//    // 获取SD卡的可用空间大小
//    public static long getSDCardAvailableSize() {
//        if (isSDCardMounted()) {
//            StatFs fs = new StatFs(getSDCardBaseDir());
//            long count = fs.getAvailableBlocksLong();
//            long size = fs.getBlockSizeLong();
//            return count * size / 1024 / 1024;
//        }
//        return 0;
//    }

    // 往SD卡的公有目录下保存文件
    public static boolean saveFileToSDCardPublicDir(byte[] data, String type, String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的自定义目录下保存文件
//    public static boolean saveFileToSDCardCustomDir(byte[] data, String dir, String fileName) {
//        BufferedOutputStream bos = null;
//        if (isSDCardMounted()) {
//            File file = new File(getSDCardBaseDir() + File.separator + dir);
//            if (!file.exists()) {
//                file.mkdirs();// 递归创建自定义目录
//            }
//            try {
//                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
//                bos.write(data);
//                bos.flush();
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    bos.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        return false;
//    }

    // 往SD卡的私有Files目录下保存文件
    public static boolean saveFileToSDCardPrivateFilesDir(byte[] data, String type, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Cache目录下保存文件
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 保存bitmap图片到SDCard的私有Cache目录
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap, String fileName, Context context) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            // 获取私有的Cache缓存目录
            File file = context.getExternalCacheDir();

            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                if (fileName != null && (fileName.contains(".png") || fileName.contains(".PNG"))) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // 从SD卡获取文件
    public static byte[] loadFileFromSDCard(String fileDir) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            bis = new BufferedInputStream(new FileInputStream(new File(fileDir)));
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            while ((c = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 从SDCard中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath) {
        byte[] data = loadFileFromSDCard(filePath);
        if (data != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bm != null) {
                return bm;
            }
        }
        return null;
    }

    // 获取SD卡公有目录的路径
    public static String getSDCardPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }

    // 获取SD卡私有Cache目录的路径
    public static String getSDCardPrivateCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

    // 获取SD卡私有Files目录的路径
    public static String getSDCardPrivateFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    // 从sdcard中删除文件
    public static boolean removeFileFromRom(String filePath) {
        String dirFirstFolder;
        if (SDCardHelper.isSDCardMounted()) {
            if (GlobalApplication.sContext.getExternalFilesDir(null) != null) {
                dirFirstFolder = GlobalApplication.sContext.getExternalFilesDir(null) + "/" + filePath;
            } else {
                dirFirstFolder = GlobalApplication.sContext.getFilesDir() + "/" + filePath;
            }
        } else {
            dirFirstFolder = GlobalApplication.sContext.getFilesDir() + "/" + filePath;
        }

        File file = new File(dirFirstFolder);
        if (file.exists()) {
            try {
                file.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }


}
