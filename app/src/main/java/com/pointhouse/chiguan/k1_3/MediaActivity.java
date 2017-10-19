package com.pointhouse.chiguan.k1_3;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.pointhouse.chiguan.Application.CommonMedia;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.RoundProgressBar.RoundProgressBar;
import com.pointhouse.chiguan.common.base.PermissionActivityBase;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.jsonObject.CourseDetailGetBean;
import com.pointhouse.chiguan.common.util.CollectionUtil;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.db.CourseDetailInfo;
import com.pointhouse.chiguan.db.CourseDetailInfoDao;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.db.ImgUrlInfo;
import com.pointhouse.chiguan.db.ImgUrlInfoDao;
import com.pointhouse.chiguan.k1_12.ExerciseListActivity;
import com.pointhouse.chiguan.k1_2.DownloadService;
import com.pointhouse.chiguan.k1_2.DownloadStateUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 视频测试类
 * Created by Maclaine on 2017/7/10.
 */

public class MediaActivity extends PermissionActivityBase {
    private static final String TAG = "MediaActivity";
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private CourseDownloadInfoDao courseDownloadInfoDao;
    private CourseDetailInfoDao courseDetailInfoDao;
    private ImgUrlInfoDao imgUrlInfoDao;
    //控件定义
    TabHost tabHost;
    View viewCourseList, viewTextOnly, viewContent, viewBack, viewPop, viewMain, viewContainerR, divider;
    LinearLayout viewSVContainer;
    SurfaceView sv;
    PopupWindow popWindow;
    ObservableScrollView viewScroll;
    ImageView imgExercise, imgBack2;
    RoundProgressBar imgDownload;
    TextView txtTitle, txtDownload;
    CommonMedia commonMedia;
    CommonMediaOption option;
    Bundle savedInstanceState;
    WebView txtContentTextOnly, txtInfo, txtContent, txtChat;
    private DownloadService mDownloadService;
    private ServiceConnection mDownloadServiceConnection;

    private CallBack callBack;

    boolean hasCommonInit = false;

    boolean firstStart = true;
    boolean startExercise = false;
    int snapPlayStatus = MediaService.PAUSE;

    Disposable disposable1, disposable2;

    Point out = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (commonMedia != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
                viewContent.setVisibility(View.GONE);
                popWindow.dismiss();
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
                viewContent.setVisibility(View.VISIBLE);
                viewScroll.scrollTo(0, 0);
                popWindow.showAtLocation(findViewById(R.id.viewMain), Gravity.TOP | Gravity.LEFT, 0, 0);
            }
            commonMedia.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_3_activity_media);
        Intent intent = this.getIntent();
        option = (CommonMediaOption) intent.getSerializableExtra("option");
        courseDownloadInfoDao = new CourseDownloadInfoDao(MediaActivity.this);
        courseDetailInfoDao = new CourseDetailInfoDao(MediaActivity.this);
        imgUrlInfoDao = new ImgUrlInfoDao(MediaActivity.this);
        hasCommonInit = false;
//        firstStart = true;
        startExercise = false;
        /*------初始化控件------------------------------------------------------*/
        tabHost = (TabHost) findViewById(R.id.tabHost);
        viewCourseList = findViewById(R.id.viewCourseList);
        viewTextOnly = findViewById(R.id.viewTextOnly);
        viewContent = findViewById(R.id.viewContent);
        viewBack = findViewById(R.id.viewBack);
        viewMain = findViewById(R.id.viewMain);
        viewSVContainer = (LinearLayout) findViewById(R.id.viewSVContainer);
        txtContentTextOnly = (WebView) findViewById(R.id.txtContentTextOnly);
        txtInfo = (WebView) findViewById(R.id.txtInfo);
        txtContent = (WebView) findViewById(R.id.txtContent);
        txtChat = (WebView) findViewById(R.id.txtChat);
        viewScroll = (ObservableScrollView) findViewById(R.id.viewScroll);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDownload = (TextView) findViewById(R.id.txtDownload);
        imgExercise = (ImageView) findViewById(R.id.imgExercise);
        imgDownload = (RoundProgressBar) findViewById(R.id.imgDownload);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (commonMedia != null)
                commonMedia.isOut = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!firstStart && isVideo()) {
//            this.onCreate(savedInstanceState);
            try {
                MediaServiceHelper.getService(MediaActivity.this, MediaService::stopMedia);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            viewSVContainer.removeAllViews();
            sv = new SurfaceView(this);
            viewSVContainer.addView(sv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            sv.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        Log.d(TAG, "surfaceCreated");
                        if (snapPlayStatus == MediaService.PLAYING) {
                            commonMedia.getMediaPlayer().pause();
                            commonMedia.setSV(sv);
                            commonMedia.getMediaPlayer().start();
                        } else {
                            commonMedia.setSV(sv);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });

        } else if (!firstStart && isMusic() && startExercise) {
            startExercise = false;
            MediaServiceHelper.getService(MediaActivity.this, mediaService -> {
                try {
                    if (option.getId().equals(mediaService.getCurrentOption().getId()) && option.getMediaID().equals(mediaService.getCurrentOption().getMediaID())) {

                    } else {
                        commonMedia.hardClose();
                        onCreate(savedInstanceState);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    commonMedia.hardClose();
                    onCreate(savedInstanceState);
                }
            });
        }
        if (!firstStart) {
            bindService(new Intent(this, DownloadService.class), mDownloadServiceConnection, Context.BIND_AUTO_CREATE);
        }
        firstStart = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if ((isVideo() || (!MediaServiceHelper.hasPermission(MediaActivity.this) && isMusic())) && commonMedia != null) {
                snapPlayStatus = commonMedia.playStatus;
                commonMedia.pause();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        try {
            unbindService(mDownloadServiceConnection);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (popWindow != null) {
                popWindow.dismiss();
            }
            if (commonMedia != null) {
                commonMedia.close();
                commonMedia = null;
            }
            if (disposable1 != null && !disposable1.isDisposed()) {
                disposable1.dispose();
            }
            if (disposable2 != null && !disposable2.isDisposed()) {
                disposable2.dispose();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "event.getAction:" + event.getAction());
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
        return super.dispatchTouchEvent(event);
    }


    private void init() {
        viewPop = LayoutInflater.from(this).inflate(R.layout.k1_3_popwindow, null, false);
        viewPop.setBackgroundColor(ContextCompat.getColor(MediaActivity.this, R.color.white));
        viewPop.getBackground().setAlpha(0);
        popWindow = new PopupWindow(viewPop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        imgBack2 = (ImageView) viewPop.findViewById(R.id.imgBack2);
        divider = viewPop.findViewById(R.id.divider);

        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(false);

        txtInfo.setFocusable(false);
        txtContent.setFocusable(false);
        txtChat.setFocusable(false);
        txtContentTextOnly.setFocusable(false);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    commonMedia = new CommonMedia(MediaActivity.this, option, false);
                    commonMedia.setOnInitCompleteListener(() -> {
                        hasCommonInit = true;
                        option = commonMedia.getOption();
                        if ((option.getType().equals(CommonMediaOption.MUSIC_MIX) || option.getType().equals(CommonMediaOption.MUSIC_ONLY)) && !MediaServiceHelper.hasPermission(this)) {
                            requestDrawOverLays();
                        }
                        if (!popWindow.isShowing())
                            popWindow.showAtLocation(findViewById(R.id.viewMain), Gravity.TOP | Gravity.LEFT, 0, 0);
                        if (option.getType() == CommonMediaOption.TEXT_ONLY) {
                            findViewById(R.id.commonMedia).setVisibility(View.GONE);
                            txtTitle.setVisibility(View.INVISIBLE);
                            updatePopWindowWidth(getOut().x);
                            imgBack2.setAlpha(1f);
                            divider.setAlpha(1f);
                        } else {
                            imgBack2.setAlpha(0f);
                            divider.setAlpha(0f);
                            viewTextOnly.getLayoutParams().height = 0;
                            commonMedia.prepareAsync();
                            updatePopWindowWidth(DensityUtil.dip2px(this, 100));
                        }
                        //初始化Tab
                        tabHost.setup();
                        Map<Integer, String> tabs = new LinkedHashMap<>();
                        if (option.getType() != CommonMediaOption.TEXT_ONLY) {
                            tabs.put(1, "介绍");
                            tabs.put(2, "文本");
                        } else {
//                            findViewById(R.id.tab1).setVisibility(View.GONE);
//                            findViewById(R.id.tab2).setVisibility(View.GONE);
                            tabHost.setVisibility(View.GONE);
                        }
//                        tabs.put(3, "互动精选");
                        findViewById(R.id.tab3).setVisibility(View.GONE);
                        initTabs(tabs);
                        initText();
                        //设置加载监听
                        tabHost.setOnTabChangedListener(tabId -> {
                            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                                View view = tabHost.getTabWidget().getChildAt(i);
                                View backLine = view.findViewById(R.id.backLine);
                                TextView text = (TextView) view.findViewById(R.id.title);
                                if (tabHost.getCurrentTab() == i) {
                                    backLine.setBackgroundColor(ContextCompat.getColor(MediaActivity.this, R.color.color_3c5fc5));
                                    text.setTextColor(ContextCompat.getColor(MediaActivity.this, R.color.color_3c5fc5));
                                } else {
                                    backLine.setBackgroundColor(0xFFFFFFFF);
                                    text.setTextColor(ContextCompat.getColor(MediaActivity.this, R.color.color_818181));
                                }
                            }
                        });
                        //练习题
                        imgExercise.setOnClickListener(v -> {
                            if (!commonMedia.isOk) {
                                return;
                            }
                            Intent intent1 = new Intent(this, ExerciseListActivity.class);
                            intent1.putExtra("lessonId", option.getId());
                            intent1.putExtra("clearFlag", "true");//固定
                            startActivity(intent1);
                            startExercise = true;
                            commonMedia.isOut = true;
                        });
                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong1 -> {
                                    if (tabHost.getTabWidget().getChildCount() <= 1) {
//                                        View view = tabHost.getTabWidget().getChildAt(0);
//                                        View backLine = view.findViewById(R.id.backLine);
//                                        TextView text = (TextView) view.findViewById(R.id.title);
//                                        backLine.setBackgroundColor(ContextCompat.getColor(MediaActivity.this, R.color.color_3c5fc5));
//                                        text.setTextColor(ContextCompat.getColor(MediaActivity.this, R.color.color_3c5fc5));
                                    } else {
                                        tabHost.setCurrentTab(1);
                                        tabHost.setCurrentTab(0);
                                    }
                                });
                        imgBack2.setOnClickListener(v -> {
                            super.onBackPressed();
                        });
                    });
                    commonMedia.init();
//                        if ((option.getType() == CommonMediaOption.VIDEO_MIX || option.getType() == CommonMediaOption.VIDEO_ONLY || !MediaServiceHelper.hasPermission(MediaActivity.this))) {
//                            commonMedia.prepareAsync();
//                        }
                });
        callBack = downloadInfo -> {
            try {
                if (downloadInfo.getLessonId() == option.getId()) {
                    imgDownload.setProgress((int) downloadInfo.getProgress());
                }
                List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(option.getId());
                int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                switch (downflg) {
                    case 1://下载中
                        txtDownload.setVisibility(View.GONE);
                        imgDownload.setVisibility(View.VISIBLE);
                        imgDownload.setBackgroundResource(R.mipmap.xiazaizhong);
                        break;
                    case 2://暂停中
                        txtDownload.setVisibility(View.GONE);
                        int progress1 = 0;
                        for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                            if (courseDownloadInfo.getState() == 4) {
                                progress1 = (int) courseDownloadInfo.getProgress();
                            }
                            if (courseDownloadInfo.getState() == 2) {
                                if ((int) courseDownloadInfo.getProgress() != 0) {
                                    progress1 = (int) courseDownloadInfo.getProgress();
                                }
                                break;
                            }
                        }
                        imgDownload.setVisibility(View.VISIBLE);
                        imgDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                        imgDownload.setProgress(progress1);
                        break;
                    case 3://等待中
                        imgDownload.setVisibility(View.GONE);
                        txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_waiting)));
                        txtDownload.setVisibility(View.VISIBLE);
                        break;
                    case 4://下载结束
                        txtDownload.setVisibility(View.GONE);
                        imgDownload.setProgress(1000);
                        imgDownload.setBackgroundResource(R.mipmap.xiazaiwanbi);
                        break;
                    case 5://下载失败
                        imgDownload.setVisibility(View.GONE);
                        txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_fail)));
                        txtDownload.setVisibility(View.VISIBLE);
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        mDownloadServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mDownloadService = ((DownloadService.MsgBinder) service).getService();
                mDownloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                    @Override
                    public void onNext(CourseDownloadInfo downloadInfo) {
                        try {
                            if (callBack != null) {
                                callBack.accept(downloadInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };
        bindService(new Intent(this, DownloadService.class), mDownloadServiceConnection, Context.BIND_AUTO_CREATE);


        if (option.isShowDownload()) {
            try {
                List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(option.getId());
                int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                imgDownload.setMax(1000);
                switch (downflg) {
                    case 0:
                        txtDownload.setVisibility(View.GONE);
                        imgDownload.setVisibility(View.VISIBLE);
                        imgDownload.setBackgroundResource(R.mipmap.xiazai);
                        break;
                    case 1://下载中
                        int progress2 = 0;
                        for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                            if (courseDownloadInfo.getState() == 4) {
                                progress2 = (int) courseDownloadInfo.getProgress();
                            }
                            if (courseDownloadInfo.getState() == 2) {
                                if ((int) courseDownloadInfo.getProgress() != 0) {
                                    progress2 = (int) courseDownloadInfo.getProgress();
                                }
                                break;
                            }
                        }
                        imgDownload.setProgress(progress2);
                        txtDownload.setVisibility(View.GONE);
                        imgDownload.setVisibility(View.VISIBLE);
                        imgDownload.setBackgroundResource(R.mipmap.xiazaizhong);
                        break;
                    case 2://暂停中
                        txtDownload.setVisibility(View.GONE);
                        imgDownload.setVisibility(View.VISIBLE);
                        int progress1 = 0;
                        for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                            if (courseDownloadInfo.getState() == 4) {
                                progress1 = (int) courseDownloadInfo.getProgress();
                            }
                            if (courseDownloadInfo.getState() == 2) {
                                if ((int) courseDownloadInfo.getProgress() != 0) {
                                    progress1 = (int) courseDownloadInfo.getProgress();
                                }
                                break;
                            }
                        }
                        imgDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                        imgDownload.setProgress(progress1);
                        break;
                    case 3://等待中
                        imgDownload.setVisibility(View.GONE);
                        txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_waiting)));
                        txtDownload.setVisibility(View.VISIBLE);
                        break;
                    case 4://下载结束
                        txtDownload.setVisibility(View.GONE);
                        imgDownload.setVisibility(View.VISIBLE);
                        imgDownload.setProgress(1000);
                        imgDownload.setBackgroundResource(R.mipmap.xiazaiwanbi);
                        break;
                    case 5://下载失败
                        imgDownload.setVisibility(View.GONE);
                        txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_fail)));
                        txtDownload.setVisibility(View.VISIBLE);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
        /*------初始化控件完成-------------------------------------------------*/


        /*------设置监听------------------------------------------------------*/

        txtDownload.setOnClickListener(v -> imgDownload.callOnClick());
        viewScroll.setScrollViewListener((scrollView, x, y, oldx, oldy) -> {
            if (out == null) {
                out = new Point();
                getWindowManager().getDefaultDisplay().getSize(getOut());
            }
            if(!popWindow.isShowing()){
                return;
            }
            if (y <= DensityUtil.dip2px(this, 216 - 40)) {
                if (option.getType() != CommonMediaOption.TEXT_ONLY) {
                    updatePopWindowWidth(DensityUtil.dip2px(this, 100));
                    viewPop.refreshDrawableState();
                    imgBack2.setAlpha(0f);
                    divider.setAlpha(0f);
                    viewPop.getBackground().setAlpha(0);
                } else {
                    updatePopWindowWidth(getOut().x);
                    viewPop.refreshDrawableState();
                    if (y == 0) {
                        viewPop.getBackground().setAlpha(0);
                    } else {
                        viewPop.getBackground().setAlpha(255);
                    }
                }
            } else {
                float alpha = 1f;
                int maxDp = 300;
                updatePopWindowWidth(getOut().x);
                if (y <= DensityUtil.dip2px(this, maxDp) && option.getType() != CommonMediaOption.TEXT_ONLY) {
                    alpha = (float) y / (float) DensityUtil.dip2px(this, maxDp);
                }
                imgBack2.setAlpha(alpha);
                divider.setAlpha(alpha);
                viewPop.getBackground().setAlpha((int) (255 * alpha));
            }
        });
        RxView.clicks(imgDownload)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Integer lessonId = option.getId();
                    permissions(() -> {
                        if (!hasCommonInit) {
                            return;
                        }
                        try {
                            List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(lessonId);
                            int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                            switch (downflg) {
                                case 0:
                                    disposable2 = RetrofitFactory.getInstance().getRequestServicesToken()
                                            .get("courseDetail/[\"" + option.getCourse().getLesson().getCourseId() + "\"]")
                                            .retry(3)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(jsonObject -> {
                                                if (jsonObject.getInteger("resultCode") != 1) {
                                                    throw new Exception(jsonObject.getString("exceptions"));
                                                }
                                                JSONArray lessonsJson = jsonObject.getJSONObject("resultObject").getJSONArray("lessons");
                                                JSONObject lessonObj = null;
                                                for (int i = 0; i < lessonsJson.size(); i++) {
                                                    JSONObject obj = lessonsJson.getJSONObject(i);
                                                    if (option.getId().equals(obj.getInteger("id"))) {
                                                        lessonObj = obj;
                                                        break;
                                                    }
                                                }
                                                Lesson lt = JSONObject.toJavaObject(lessonObj, Lesson.class);
                                                int fileCount = 0;
                                                List<Media> audioList = lt.getAudioList();
                                                List<Media> videoList = lt.getVideoList();
                                                if (!CollectionUtil.isEmpty(audioList)) {
                                                    fileCount += audioList.size();
                                                }
                                                if (!CollectionUtil.isEmpty(videoList)) {
                                                    fileCount += videoList.size();
                                                }
                                                List<CourseDownloadInfo> courseDownloadInfoList1 = new ArrayList<>();
                                                List<Media> listMedia = new ArrayList<>();
                                                if (!CollectionUtil.isEmpty(audioList)) {
                                                    for (Media media : audioList) {
                                                        listMedia.add(media);
                                                    }
                                                }
                                                if (!CollectionUtil.isEmpty(videoList)) {
                                                    listMedia.add(videoList.get(0));
                                                }
                                                for (int i = 0; i < listMedia.size(); i++) {
                                                    CourseDownloadInfo courseDownloadInfo = new CourseDownloadInfo();
                                                    Media media = listMedia.get(i);
                                                    courseDownloadInfo.seturl(media.getOrigUrl());
                                                    courseDownloadInfo.setfileName(media.getVid() + media.getVideoName());
                                                    courseDownloadInfo.setLessonId(option.getId());
                                                    courseDownloadInfo.setProgress(0);
                                                    courseDownloadInfo.setTotle(1000);
                                                    courseDownloadInfo.setProgress((1000 / fileCount) * i);
                                                    courseDownloadInfo.setVid(String.valueOf(media.getVid()));
                                                    courseDownloadInfo.setState(3);
                                                    courseDownloadInfo.setFilePath(String.valueOf(option.getId()));
                                                    courseDownloadInfo.setErrorMessage("");
                                                    courseDownloadInfo.setFileCount(fileCount);
                                                    courseDownloadInfo.setIndex(i);
                                                    courseDownloadInfo.setCourseId(option.getCourse().getLesson().getCourseId());
                                                    courseDownloadInfoList1.add(courseDownloadInfo);
                                                    try {
                                                        courseDownloadInfoDao.save(courseDownloadInfo);
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                mDownloadService.addWaitDownload(courseDownloadInfoList1);
                                                imgDownload.setVisibility(View.GONE);
                                                txtDownload.setVisibility(View.VISIBLE);
                                                txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_waiting)));
                                                try {
                                                    updateDetail(option.getCourse().getLesson().getCourseId(), jsonObject.toJSONString());
                                                    if (jsonObject.getJSONObject("resultObject").getJSONObject("course").getString("thumb") != null) {
                                                        Target target = saveImg(jsonObject.getJSONObject("resultObject").getJSONObject("course").getString("thumb"));
                                                        Picasso.with(MediaActivity.this).load(jsonObject.getJSONObject("resultObject").getJSONObject("course").getString("thumb")).into(target);
                                                    }
                                                    /*if (option.getCourse().getLesson().getThumb() != null) {
                                                        Target target = saveImg(option.getCourse().getLesson().getThumb());
                                                        Picasso.with(MediaActivity.this).load(option.getCourse().getLesson().getThumb()).into(target);
                                                    }*/
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }, throwable -> {
                                                throwable.printStackTrace();
                                                Log.e(TAG, throwable.getMessage());
                                                Toast.makeText(MediaActivity.this, "数据加载错误", Toast.LENGTH_SHORT).show();
                                            });
                                    break;
                                case 1://下载中
                                    txtDownload.setVisibility(View.GONE);
                                    imgDownload.setVisibility(View.VISIBLE);
                                    imgDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                                    mDownloadService.stopDownload(courseDownloadInfoList);
                                    break;
                                case 2://暂停中
                                    imgDownload.setVisibility(View.GONE);
                                    txtDownload.setVisibility(View.VISIBLE);
                                    txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_waiting)));
                                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                                        if (courseDownloadInfo.getState() != 4) {
                                            courseDownloadInfo.setState(3);
                                            try {
                                                courseDownloadInfoDao.update(courseDownloadInfo);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    mDownloadService.addWaitDownload(courseDownloadInfoList);
                                    break;
                                case 3://等待中
                                    imgDownload.setVisibility(View.VISIBLE);
                                    txtDownload.setVisibility(View.GONE);
                                    imgDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                                    mDownloadService.stopDownload(courseDownloadInfoList);
                                    break;
                                case 4://下载结束
                                    //do nothing
                                    break;
                                case 5://下载失败
                                    imgDownload.setVisibility(View.GONE);
                                    txtDownload.setVisibility(View.VISIBLE);
                                    txtDownload.setText(Html.fromHtml(getString(R.string.walkman_download_fail)));
                                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                                        if (courseDownloadInfo.getState() != 4) {
                                            courseDownloadInfo.setState(3);
                                            try {
                                                courseDownloadInfoDao.update(courseDownloadInfo);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    mDownloadService.addWaitDownload(courseDownloadInfoList);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(MediaActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
        /*------设置监听完成---------------------------------------------------*/
    }


    private void updateDetail(Integer courseId, String json) {
        try {
            CourseDetailInfo courseDetailInfo = courseDetailInfoDao.queryCourseId(courseId);
            if (courseDetailInfo == null) {
                courseDetailInfo = new CourseDetailInfo();
                courseDetailInfo.setCourseId(courseId);
                courseDetailInfo.setCourseJson(json);
                courseDetailInfoDao.save(courseDetailInfo);
            } else {
                String courseJson = courseDetailInfo.getCourseJson();
                CourseDetailGetBean courseDetailGetBeanOld = new Gson().fromJson(courseJson, CourseDetailGetBean.class);
                List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListOld = courseDetailGetBeanOld.getResultObject().getLessons();
                CourseDetailGetBean courseDetailGetBeanNew = new Gson().fromJson(json, CourseDetailGetBean.class);
                List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListNew = courseDetailGetBeanNew.getResultObject().getLessons();
                Map<Integer, CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListMapNew = new TreeMap<>();
                Map<Integer, CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListMapOld = new TreeMap<>();
                List<CourseDownloadInfo> courseDownloadInfoListOld = courseDownloadInfoDao.queryAllByCourseId(courseId);
                Set<Integer> lessonSetOld = new HashSet<>();

                if (courseDownloadInfoListOld != null && courseDownloadInfoListOld.size() > 0) {
                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoListOld) {
                        lessonSetOld.add(courseDownloadInfo.getLessonId());
                    }
                }

                for (CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean : lessonsBeanListNew) {
                    lessonsBeanListMapNew.put(lessonsBean.getId(), lessonsBean);
                }

                for (CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean : lessonsBeanListOld) {
                    lessonsBeanListMapOld.put(lessonsBean.getId(), lessonsBean);
                }

                Iterator<Map.Entry<Integer, CourseDetailGetBean.ResultObjectBean.LessonsBean>> it = lessonsBeanListMapNew.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, CourseDetailGetBean.ResultObjectBean.LessonsBean> entry = it.next();
                    if (lessonsBeanListMapOld.containsKey(entry.getKey())) {
                        CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBeanOld = lessonsBeanListMapOld.get(entry.getKey());
                        CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBeanNew = entry.getValue();
                        if (lessonSetOld.contains(entry.getKey())) {
                            lessonsBeanOld.setThumb(lessonsBeanNew.getThumb());
                            lessonsBeanOld.setTitle(lessonsBeanNew.getTitle());
                        } else {
                            lessonsBeanListMapOld.remove(entry.getKey());
                            lessonsBeanListMapOld.put(entry.getKey(), lessonsBeanNew);
                        }
                    } else {
                        lessonsBeanListMapOld.put(entry.getKey(), entry.getValue());
                    }
                }
                courseDetailGetBeanNew.getResultObject().setLessons(new ArrayList<>(lessonsBeanListMapOld.values()));
                String newjson = new Gson().toJson(courseDetailGetBeanNew);
                courseDetailInfo.setCourseJson(newjson);
                courseDetailInfoDao.update(courseDetailInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initTabs(Map<Integer, String> tabs) {
        for (Integer i : tabs.keySet()) {
            RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.k1_indicator_tab, tabHost.getTabWidget(), false);
            TextView txt1 = (TextView) tabIndicator1.findViewById(R.id.title);
            txt1.setText(tabs.get(i));
            tabHost.addTab(tabHost.newTabSpec("tab" + i).setIndicator(tabIndicator1).setContent(getResources().getIdentifier("tab" + i, "id", getPackageName())));
        }
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> v.findViewById(R.id.backLine).getLayoutParams().width = (int) (v.getWidth() * 0.6));
        }
    }

    //初始化文字(HTML)
    private void initText() {
        try {
            if (imgExercise != null) {
                if (option.getCourse().getLesson().getHasExercises() == 1) {
                    imgExercise.setVisibility(View.VISIBLE);
                } else {
                    imgExercise.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Lesson temp = option.getCourse().getLesson();
            if (TextUtils.isEmpty(temp.getAbouts()) || TextUtils.isEmpty(temp.getContent()) || (option.getType() != CommonMediaOption.TEXT_ONLY && TextUtils.isEmpty(option.getCurrentMedia().getOriginal()))) {
                disposable1 = RetrofitFactory.getInstance().getRequestServicesToken()
                        .get("lessonDetail/[\"" + option.getId() + "\"]")
                        .retry(3)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            String content = null;
                            String abouts = null;
                            String original = null;
                            if (!(s.getInteger("resultCode") == 1)) {
                                Log.e(TAG, s.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                            } else {
                                content = s.getJSONObject("resultObject").getJSONObject("lesson").getString("content");
                                abouts = s.getJSONObject("resultObject").getJSONObject("lesson").getString("abouts");
                                if (isVideo()) {
                                    original = s.getJSONObject("resultObject").getJSONObject("lesson").getJSONArray("videoList").getJSONObject(0).getString("original");
                                } else if (isMusic()) {
                                    for (Object ori : s.getJSONObject("resultObject").getJSONObject("lesson").getJSONArray("audioList")) {
                                        JSONObject obj = (JSONObject) ori;
                                        if (option.getCurrentMedia().getVid().equals(obj.getInteger("vid"))) {
                                            original = obj.getString("original");
                                            break;
                                        }
                                    }
                                }
                            }
                            if (TextUtils.isEmpty(abouts)) {
                                abouts = this.getString(R.string.media_nodata);
                            }
                            if (TextUtils.isEmpty(content)) {
                                content = this.getString(R.string.media_nodata);
                            }
                            if (TextUtils.isEmpty(original)) {
                                original = this.getString(R.string.media_nodata);
                            }
                            option.getCourse().getLesson().setAbouts(abouts);
                            option.getCourse().getLesson().setContent(content);
                            if (option.getType() != CommonMediaOption.TEXT_ONLY) {
                                option.getCurrentMedia().setOriginal(original);
                            }
                            option.getCourse().getLesson().setHasExercises(s.getJSONObject("resultObject").getJSONObject("lesson").getInteger("hasExercises"));
                            setTextIfNotNull(txtContentTextOnly, option.getCourse().getLesson().getContent());
                            setTextIfNotNull(txtInfo, option.getCourse().getLesson().getAbouts());
                            if (option.getType() != CommonMediaOption.TEXT_ONLY) {
                                setTextIfNotNull(txtContent, option.getCurrentMedia().getOriginal());
                            }
                            if (imgExercise != null) {
                                if (option.getCourse().getLesson().getHasExercises() == 1) {
                                    imgExercise.setVisibility(View.VISIBLE);
                                } else {
                                    imgExercise.setVisibility(View.GONE);
                                }
                            }
                            resetWebView();
                        }, throwable -> {
                            try {
                                throwable.printStackTrace();
                                option.getCourse().getLesson().setAbouts(this.getString(R.string.media_nodata));
                                option.getCourse().getLesson().setContent(this.getString(R.string.media_nodata));
                                option.getCurrentMedia().setOriginal(this.getString(R.string.media_nodata));
                                setTextIfNotNull(txtContentTextOnly, option.getCourse().getLesson().getContent());
                                setTextIfNotNull(txtInfo, option.getCourse().getLesson().getAbouts());
                                if (option.getType() != CommonMediaOption.TEXT_ONLY) {
                                    setTextIfNotNull(txtContent, option.getCurrentMedia().getOriginal());
                                }
                                resetWebView();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                            }
                        });
            } else {
                setTextIfNotNull(txtContentTextOnly, option.getCourse().getLesson().getContent());
                setTextIfNotNull(txtInfo, option.getCourse().getLesson().getAbouts());
                if (option.getType() != CommonMediaOption.TEXT_ONLY) {
                    setTextIfNotNull(txtContent, option.getCurrentMedia().getOriginal());
                }
                resetWebView();
            }
            //TODO:聊天精选
            setTextIfNotNull(txtChat, null);
            setTextIfNotNull(txtTitle, option.getCourse().getLesson().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }


    private void resetWebView() {
        if (option.getType() != CommonMediaOption.TEXT_ONLY) {
            return;
        }
        viewTextOnly.setVisibility(View.VISIBLE);
        /*Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> {
                    viewBack.setFocusable(true);
                    viewBack.setFocusableInTouchMode(true);
                    viewBack.requestFocus();
                    viewScroll.setY(0f);
                    viewTextOnly.setVisibility(View.VISIBLE);
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.e(TAG, throwable.getMessage());
                });*/
    }

    private void setTextIfNotNull(TextView view, String text) {
        if (view != null) {
            if (text != null && !"".equals(text)) {
                view.setText(text);
            } else {
                view.setText(this.getString(R.string.media_nodata));
            }
        }
    }

    private void setTextIfNotNull(WebView view, String text) {
        if (view != null) {
            if (text != null && !"".equals(text)) {
                view.getSettings().setDefaultTextEncodingName("UTF -8");
                view.loadData(text.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
            } else {
                view.loadData(this.getString(R.string.media_nodata), "text/html; charset=UTF-8", null);
            }
        }
    }

    //是否视频
    private boolean isVideo() {
        return option.getType() == CommonMediaOption.VIDEO_ONLY || option.getType() == CommonMediaOption.VIDEO_MIX;
    }

    //是否音频
    private boolean isMusic() {
        return option.getType() == CommonMediaOption.MUSIC_ONLY || option.getType() == CommonMediaOption.MUSIC_MIX;
    }

    private void updatePopWindowWidth(int width) {
        viewPop.getLayoutParams().width = width;
        popWindow.update(width, -1);
    }

    private Point getOut() {
        if (out == null) {
            out = new Point();
            getWindowManager().getDefaultDisplay().getSize(out);
        }
        return out;
    }

    private Target saveImg(String url) {
        return new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

                try {
                    ImgUrlInfo imgUrlInfo = imgUrlInfoDao.queryUrl(url);
                    if (imgUrlInfo == null) {
                        imgUrlInfo = new ImgUrlInfo();
                        imgUrlInfo.setUrl(url);
                        imgUrlInfo.setBlob(os.toByteArray());
                        imgUrlInfoDao.save(imgUrlInfo);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestDrawOverLays() {
        if (!MediaServiceHelper.hasPermission(this) && !MediaServiceHelper.hasRequestPermission) {
            Toast.makeText(this, "请授权悬浮窗权限", Toast.LENGTH_SHORT).show();
            MediaServiceHelper.hasRequestPermission = true;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(MediaActivity.this)) { // SYSTEM_ALERT_WINDOW permission not granted...
                Toast.makeText(this, "尚未授权悬浮窗权限，无法使用mini播放器", Toast.LENGTH_SHORT).show();
                commonMedia.hardClose();
                onCreate(savedInstanceState);
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show(); // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
                try {
                    commonMedia.hardClose();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                Intent intent = new Intent(getApplicationContext(), MediaService.class);
                startService(intent);
                onCreate(savedInstanceState);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    interface CallBack {
        void accept(CourseDownloadInfo downloadInfo);
    }

    protected void permissions(CallBack2 callBack) {
        requestPermissions(PermissionGroup.STORAGE, true, new CallbackListener() {
            @Override
            public void onPermissionsOK(int requestCode, List<String> perms) {
                callBack.accept();
            }

            @Override
            public void onPermissionsNG(int requestCode, List<String> perms) {
                Toast.makeText(MediaActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {
                Toast.makeText(MediaActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface CallBack2 {
        public void accept();
    }
}
