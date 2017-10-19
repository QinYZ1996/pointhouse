package com.pointhouse.chiguan.k1_2;

import android.util.Log;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 */

public class DownloadManager {

    private static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>();
    private HashMap<Integer, Call> downCalls;//用来存放各个下载的请求
    private OkHttpClient mClient;//OKHttpClient;
    private CourseDownloadInfoDao courseDownloadInfoDao;

    //获得一个单例类
    public static DownloadManager getInstance() {
        for (; ; ) {
            DownloadManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new DownloadManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private DownloadManager() {
        downCalls = new HashMap<>();
        mClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
    }

    /**
     * 开始下载
     *
     * @param courseDownloadInfo              下载请求的网址
     * @param downLoadObserver 用来回调的接口
     */
    public void download(CourseDownloadInfo courseDownloadInfo, DownLoadObserver downLoadObserver) {
            Observable.just(courseDownloadInfo)
                    .filter(s -> !downCalls.containsKey(s.getLessonId()))//call的map已经有了,就证明正在下载,则这次不下载
                    .flatMap(s -> Observable.just(createDownInfo(s)))
                    .map(this::getRealFileName)//检测本地文件夹,生成新的文件名
                    .flatMap(downloadInfo -> Observable.create(new DownloadSubscribe(downloadInfo)))//下载
                    .observeOn(AndroidSchedulers.mainThread())//在主线程回调
                    .subscribeOn(Schedulers.io())//在子线程执行
                    .subscribe(downLoadObserver);//添加观察者
    }

    public void cancel(Integer lessonId) {
        Call call = downCalls.get(lessonId);
        if (call != null) {
            call.cancel();//取消
        }
        downCalls.remove(lessonId);
    }

//    public void cancelAll() {
//        Iterator<Map.Entry<String, Call>> entries =downCalls.entrySet().iterator();
//
//        while (entries.hasNext()){
//            Map.Entry<String, Call> entry = entries.next();
//
//            Call call = entry.getValue();
//            if (call != null) {
//                call.cancel();//取消
//            }
//            downCalls.remove(entry.getKey());
//        }
//    }

    /**
     * 创建DownInfo
     *
     * @param courseDownloadInfo 请求网址
     * @return DownInfo
     */
    private CourseDownloadInfo createDownInfo(CourseDownloadInfo courseDownloadInfo) {

        return courseDownloadInfo;
    }

    private CourseDownloadInfo getRealFileName(CourseDownloadInfo downloadInfo) throws SQLException {
        long downloadLength = 0;
        File file = SDCardHelper.getSDExternalFilesDir(downloadInfo.getFilePath(), downloadInfo.getfileName());
        if (file != null && file.exists()) {
            //找到了文件,代表已经下载过,则获取其长度
            downloadLength = file.length();
            downloadInfo.setFileProgress(downloadLength);
        } else {
            downloadInfo.setFileProgress(downloadLength);
        }
        return downloadInfo;
    }

    private class DownloadSubscribe implements ObservableOnSubscribe<CourseDownloadInfo> {
        private CourseDownloadInfo downloadInfo;

        public DownloadSubscribe(CourseDownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void subscribe(ObservableEmitter<CourseDownloadInfo> e) {
            downloadInfo.setFileSize(getContentLength(downloadInfo,e));
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            boolean hasError = false;
            int packetLossRange = -1;
            try {
            String url = downloadInfo.geturl();
            long downloadLength = downloadInfo.getFileProgress();//已经下载好的长度
            long contentLength = downloadInfo.getFileSize();//文件的总长度
                if(contentLength == -1){
                    downloadInfo.setState(5);
                    downloadInfo.setErrorMessage("数据取得失败");
                    try {
                        courseDownloadInfoDao.update(downloadInfo);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.onNext(downloadInfo);
                    Log.e("Progress", "data = 0");
                    return;
                }
            // 初始进度信息
            e.onNext(downloadInfo);

            Request request = new Request.Builder()
                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                    .url(url)
                    .build();

            Call call = mClient.newCall(request);
            downCalls.put(downloadInfo.getLessonId(), call);//把这个添加到call里,方便取消

                Response response = call.execute();

                //新建文件夹
                File dirFirstFolder;
                if (SDCardHelper.isSDCardMounted()) {
                    if (GlobalApplication.sContext.getExternalFilesDir(null) != null) {
                        dirFirstFolder = new File(GlobalApplication.sContext.getExternalFilesDir(null).getPath() + "/" + downloadInfo.getFilePath());
                    } else {
                        dirFirstFolder = new File(GlobalApplication.sContext.getFilesDir().getPath() + "/" + downloadInfo.getFilePath());
                    }
                } else {
                    dirFirstFolder = new File(GlobalApplication.sContext.getFilesDir().getPath() + "/" + downloadInfo.getFilePath());
                }

                if (!dirFirstFolder.exists()) { //如果该文件夹不存在，则进行创建
                    dirFirstFolder.mkdirs();//创建文件夹
                }

                File file = SDCardHelper.getSDExternalFilesDir(downloadInfo.getFilePath(), downloadInfo.getfileName());

                ResponseBody responseBody = response.body();
                Log.e("Progresslong", String.valueOf(responseBody.contentLength()));
                is = responseBody.byteStream();
                fileOutputStream = new FileOutputStream(file, true);

                byte[] buffer = new byte[2048];//缓冲数组2kB
                int len;
                int count = 0;
                while ((len = is.read(buffer)) != -1) {
                    count++;

                    fileOutputStream.write(buffer, 0, len);
                    downloadLength += len;
                    packetLossRange = (int)(downloadInfo.getFileSize() - downloadLength);
                    //Log.d("Progress", downloadLength + "/" + downloadInfo.getFileSize());

                    if (count % 100 == 0 && !(downloadLength == downloadInfo.getFileSize())) {
                        // 更新下载进度
                        downloadInfo.setFileProgress(downloadLength);

                        downloadInfo.setProgress(((1000 / downloadInfo.getFileCount()) * downloadInfo.getIndex()) +
                                ((1000 / downloadInfo.getFileCount()) * downloadInfo.getFileProgress() / downloadInfo.getFileSize()));


                        try {
                            courseDownloadInfoDao.update(downloadInfo);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        e.onNext(downloadInfo);
                    }

                    if (downloadLength == downloadInfo.getFileSize()) {
                        //Log.d("Progress", "over");
                        // 更新下载进度
                        downloadInfo.setFileProgress(downloadLength);

                        downloadInfo.setProgress(((1000 / downloadInfo.getFileCount()) * downloadInfo.getIndex()) +
                                ((1000 / downloadInfo.getFileCount()) * downloadInfo.getFileProgress() / downloadInfo.getFileSize()));

                        //Log.d("Progress", downloadLength + "/" + downloadInfo.getFileSize());
                        downloadInfo.setState(4);
                        try {
                            courseDownloadInfoDao.update(downloadInfo);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        e.onNext(downloadInfo);
                        e.onComplete();//完成
                    }
                }
                fileOutputStream.flush();
                downCalls.remove(downloadInfo.getLessonId());
            }catch (SocketTimeoutException ext){
                    if(packetLossRange < 0){
                        downloadInfo.setState(4);
                        try {
                            courseDownloadInfoDao.update(downloadInfo);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        e.onNext(downloadInfo);
                        Log.e("Progress", "errornot");
                        e.onComplete();//完成
                    } else {
                        downloadInfo.setState(5);
                        downloadInfo.setErrorMessage(ext.toString());
                        try {
                            courseDownloadInfoDao.update(downloadInfo);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        e.onNext(downloadInfo);
                    }
            }catch (Exception ext) {
                try {
                    CourseDownloadInfo courseDownloadInfo = courseDownloadInfoDao.queryId(downloadInfo.getId());
                    if(courseDownloadInfo.getState() != 2){
                        downloadInfo.setState(5);
                        downloadInfo.setErrorMessage(ext.toString());
                        try {
                            courseDownloadInfoDao.update(downloadInfo);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        e.onNext(downloadInfo);
                    } else {
                        e.onNext(downloadInfo);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                //关闭IO流
                IOUtil.closeAll(is, fileOutputStream);
                downCalls.remove(downloadInfo.getLessonId());
                e.onComplete();//完成
            }
        }
    }

    /**
     * 获取下载长度
     *
     * @param courseDownloadInfo
     * @return
     */
    private long getContentLength(CourseDownloadInfo courseDownloadInfo,ObservableEmitter<CourseDownloadInfo> e2) {
        try {
            Request request = new Request.Builder()
                .url(courseDownloadInfo.geturl())
                .build();
            Response response = mClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? -1 : contentLength;
            }
        } catch (Exception e) {
            courseDownloadInfo.setState(5);
            courseDownloadInfo.setErrorMessage(e.toString());
            try {
                courseDownloadInfoDao.update(courseDownloadInfo);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e2.onNext(courseDownloadInfo);
            e.printStackTrace();
        }
        return -1;
    }


}
