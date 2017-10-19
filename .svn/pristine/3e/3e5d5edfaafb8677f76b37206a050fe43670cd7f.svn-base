package com.pointhouse.chiguan.k1_2;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.BroadcastServiceBase;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.t1.FatherActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljj on 2017/7/19.
 */

public class DownloadService extends BroadcastServiceBase {

    /**
     * 更新进度的回调接口
     */
    private OnProgressListener onProgressListener;
    private CourseDownloadInfoDao courseDownloadInfoDao;
    private Map<Integer,CourseDownloadInfo> waitDownloadList = new LinkedHashMap<>();
    private boolean canDownload1 = true;
    private boolean canDownload2 = true;
    private boolean canDownload3 = true;
    NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate(){
        //启动应用时所有未下载完成的变成暂停。
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);

        List<CourseDownloadInfo> courseDownloadInfos = null;
        try {
            courseDownloadInfos = courseDownloadInfoDao.queryAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(courseDownloadInfos != null && courseDownloadInfos.size() > 0 ){
            for (CourseDownloadInfo courseDownloadInfo :courseDownloadInfos)
            {
                if (courseDownloadInfo.getState() == 1 || courseDownloadInfo.getState() == 3) {
                    courseDownloadInfo.setState(2);
                    try {
                        courseDownloadInfoDao.update(courseDownloadInfo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        listenProgress();
        super.onCreate();
    }

    /**
     * 注册回调接口的方法，供外部调用
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    /**
     * 监听列队内是否还有需要下载的。
     */
    private void listenProgress(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    startDownload();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


    //下载
    public void addWaitDownload(List<CourseDownloadInfo> courseDownloadInfoList){
        canDownload1 = false;
        for(CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList){
            if(!waitDownloadList.containsKey(courseDownloadInfo.getId()) && courseDownloadInfo.getState() != 4){
                waitDownloadList.put(courseDownloadInfo.getId() ,courseDownloadInfo);
            }
        }
        canDownload1 = true;
    }

    //暂停
    public void stopDownload(List<CourseDownloadInfo> courseDownloadInfoList){
        canDownload2 = false;
        for(CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList){
            if(courseDownloadInfo.getState() == 1 || courseDownloadInfo.getState() == 5 || courseDownloadInfo.getState() == 3){
                DownloadManager.getInstance().cancel(courseDownloadInfo.getLessonId());
            }
            if(waitDownloadList.containsKey(courseDownloadInfo.getId())) {
                waitDownloadList.remove(courseDownloadInfo.getId());
            }
            if(courseDownloadInfo.getState() != 4){
                courseDownloadInfo.setState(2);
                try {
                    courseDownloadInfoDao.update(courseDownloadInfo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        canDownload2 = true;
    }

    private void startDownload(){
        if(!canDownload1  || !canDownload2 || !canDownload3) return;
        if(waitDownloadList.size() > 0) {
            Map.Entry<Integer, CourseDownloadInfo> entry = waitDownloadList.entrySet().iterator().next();
                CourseDownloadInfo courseDownloadInfo = entry.getValue();
                if (courseDownloadInfo != null) {
                    if(courseDownloadInfo.getState() == 2 || courseDownloadInfo.getState() == 4 || courseDownloadInfo.getState() == 5){
                        if(waitDownloadList.containsKey(courseDownloadInfo.getId())) {
                            waitDownloadList.remove(courseDownloadInfo.getId());
                            Log.e("waitDownloadList", "error");
                            return;
                        }
                    }
                    if (courseDownloadInfo.getState() == 3) {
                        courseDownloadInfo.setState(1);
                        canDownload3 = false;
                        DownloadManager.getInstance().download(courseDownloadInfo, new DownLoadObserver() {
                            @Override
                            public void onNext(CourseDownloadInfo value) {
                                super.onNext(value);
                                if(value.getState() == 5){
                                    try {
                                        List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(value.getLessonId());
                                        for(CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList){
                                            if(courseDownloadInfo.getState() == 3){
                                                courseDownloadInfo.setState(2);
                                            }
                                            courseDownloadInfoDao.update(courseDownloadInfo);
                                            if(waitDownloadList.containsKey(courseDownloadInfo.getId())) {
                                                waitDownloadList.remove(courseDownloadInfo.getId());
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    canDownload3 = true;
                                }
                                if(value.getState() == 2){
                                    canDownload3 = true;
                                }
                                onProgressListener.onNext(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                onProgressListener.onError(e);
                            }

                            @Override
                            public void onComplete() {
                                if(waitDownloadList.containsKey(courseDownloadInfo.getId())) {
                                    waitDownloadList.remove(courseDownloadInfo.getId());
                                }
                                onProgressListener.onComplete();
                                Log.e("onComplete", "errornot");
                                canDownload3 = true;
                            }
                        });
                    }
                }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    @Override
    public void onNetChange(int netMobile) {
        if(netMobile == 2 && waitDownloadList.size() > 0){
            Log.d("4G","4Gnet");
            stopDownload(new ArrayList<>(waitDownloadList.values()));
            RemoteViews view_custom = new RemoteViews(getPackageName(), R.layout.view_notificationsdownload);
            view_custom.setTextViewText(R.id.tv_custom_title,"移动网络下下载服务以停止");
            view_custom.setTextViewText(R.id.tv_custom_content,"进入下载页面点击开始可继续下载");
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setAutoCancel(true);
            mBuilder.setContent(view_custom);
            mBuilder.setWhen(System.currentTimeMillis());
            mBuilder.setCustomBigContentView(view_custom);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            //点击的意图ACTION是跳转到Intent
            Intent resultIntent = new Intent(this, FatherActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public DownloadService getService(){
            return DownloadService.this;
        }
    }

    /**
     * 更新进度的回调接口
     */
    public interface OnProgressListener {
        void onNext(CourseDownloadInfo downloadInfo);
        void onComplete();
        void onError(Throwable e);
    }

}
