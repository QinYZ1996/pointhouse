package com.pointhouse.chiguan.Application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.RoundProgressBar.RoundProgressBar;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.CollectionUtil;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.db.StudyInfo;
import com.pointhouse.chiguan.db.StudyInfoDao;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Media;
import com.pointhouse.chiguan.k1_3.MediaActivity;
import com.pointhouse.chiguan.k1_9.ShareArticleActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 媒体悬浮窗服务
 * Created by Maclaine on 2017/7/7.
 */

public class MediaService extends Service {

    private static final String TAG = "MediaService";

    public static final int WAITING = 0x000;
    public static final int PLAYING = 0x001;
    public static final int PAUSE = 0x002;

    //定义浮动窗口布局
    private View mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    //播放器
    private IMediaPlayer mediaPlayer;
    //进度条
    private SeekBar seekBar;
    //图片
    private ImageView imgClose;
    private RoundProgressBar imgStatus;
    //文本
    TextView txtTimeMax, txtSubTitle, txtTitle;
    //计时器
    private Disposable mediaDisposable;
    //显示状态
    private int displayStatus = View.GONE;
    //参数
    private CommonMediaOption option;
    //时间转换
    private SimpleDateFormat formatter;
    //播放状态
    private int playStatus = PLAYING;
    private int breakStatus = WAITING;
    private boolean isBreaking = false;
    //ActivityManager
    ActivityManager mActivityManager;
    IMediaPlayer.OnCompletionListener listener;
    Context context;

    private Callback initCompleteListener;

    private StudyInfoDao studyInfoDao;

    private Snapshot snapshot;

    private Disposable disposable1, disposable2;

    public IMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onCreate() {
        try {
            super.onCreate();
            Log.d(TAG, "onCreate");
            if (!MediaServiceHelper.hasPermission(getApplication())) {
                Log.d(TAG, "NoPermission");
                return;
            }
            createFloatView();
            toggleView(View.GONE);
            studyInfoDao = new StudyInfoDao(getApplicationContext());
            mActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            //添加监听
            this.setOnCompletionListener(null, null);

            mediaDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            saveStudy();
                            imgStatus.setProgress((int) (seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()));
                            reFreshPlayStatus(PLAYING);
                            if (isBreaking) {
                                mediaPlayer.pause();
                                reFreshPlayStatus(PAUSE);
                            }
                        } else if (mediaPlayer != null) {
                            reFreshPlayStatus(PAUSE);
                            if (!isBreaking && breakStatus == PLAYING) {
                                mediaPlayer.start();
                                breakStatus = WAITING;
                                reFreshPlayStatus(PLAYING);
                            }
                        } else {
                            toggleView(View.GONE);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        Log.e(TAG, throwable.getMessage());
                    })
            ;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            return new MediaBinder();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    //创建界面
    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置
        wmParams.gravity = Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = DensityUtil.dip2px(getApplicationContext(), 50f);
        ;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = DensityUtil.dip2px(getApplicationContext(), 51f);
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = inflater.inflate(R.layout.common_mini_media, null, false);
        mFloatLayout.getBackground().setAlpha(220);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        seekBar = (SeekBar) mFloatLayout.findViewById(R.id.seekBar);
        imgStatus = (RoundProgressBar) mFloatLayout.findViewById(R.id.imgStatus);
        imgClose = (ImageView) mFloatLayout.findViewById(R.id.imgClose);
        txtTimeMax = (TextView) mFloatLayout.findViewById(R.id.txtTimeMax);
        txtTitle = (TextView) mFloatLayout.findViewById(R.id.txtTitle);
        txtSubTitle = (TextView) mFloatLayout.findViewById(R.id.txtSubTitle);

        formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        imgStatus.setMax(100);
        imgStatus.setCricleProgressColor(R.color.color_646464);

        RxView.clicks(imgStatus)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mediaPlayer == null) {
                        return;
                    }
                    if (playStatus == PLAYING) {
                        mediaPlayer.pause();
                        reFreshPlayStatus(PAUSE);
                    } else if (playStatus == PAUSE) {
                        mediaPlayer.start();
                        reFreshPlayStatus(PLAYING);
                    }
                }, Throwable::printStackTrace);

        RxView.clicks(imgClose)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    stopMedia();
//                    toggleView(View.GONE);
                }, Throwable::printStackTrace);
        RxView.clicks(mFloatLayout)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if(option.getCourse().getLesson().getCourseId()==null){
                        return;
                    }
                    if(isActivityAlive()){
                        return;
                    }
                    Intent intent ;
                    try {
                        if(option.getCourse().getLesson().getCourseId()>0){
                            intent= new Intent(getApplicationContext(), MediaActivity.class);
                            Bundle bundle = new Bundle();
                            option.setCurrentDuration(mediaPlayer.getCurrentPosition()*-1);
                            bundle.putSerializable("option", option);
                            intent.putExtras(bundle);
                        }else{
                            intent= new Intent(getApplicationContext(), ShareArticleActivity.class);
                            intent.putExtra("articleId",String.valueOf(option.getCourse().getLesson().getCourseId()*-1));
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
    }

    //刷新状态
    private void reFreshPlayStatus(int targetStatus) {
//        if (playStatus != targetStatus) {
        playStatus = targetStatus;
        switch (targetStatus) {
            case PLAYING:
                imgStatus.setBackgroundResource(R.drawable.media_play);
                imgClose.setVisibility(View.GONE);
                break;
            case PAUSE:
                imgStatus.setBackgroundResource(R.drawable.media_pause);
                if(isActivityAlive()){
                    imgClose.setVisibility(View.GONE);
                }else{
                    imgClose.setVisibility(View.VISIBLE);
                }
//                imgClose.setVisibility(View.VISIBLE);
                break;
        }
//        }
    }

    /**
     * 初始化mini播放器
     *
     * @param option URI地址
     */
    public void initMedia(CommonMediaOption option) {
        try {
            if (option == null || option.getId() == null) {
                return;
            }
            clearMedia();
            this.option = option;
            mediaPlayer = new IjkMediaPlayer();
            mediaPlayer.reset();
            playStatus = PLAYING;
            txtTimeMax.setText(R.string.media_zero);

            if (option.getMediaID() != null && option.getCourse() != null) {
                setMediaPlayer();
            } else {
                disposable1 = RetrofitFactory.getInstance().getRequestServicesToken()
                        .get("lessonDetail/[\"" + option.getId() + "\"]")
                        .retry(3)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            if (!(s.getInteger("resultCode") == 1)) {
                                RetrofitFactory.ErrorMessage(getApplicationContext(), s, getApplicationContext().getString(R.string.media_mini_init_error));
                                Log.e(TAG, s.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                                return;
                            }
                            Course course = JSON.toJavaObject(s.getJSONObject("resultObject"), Course.class);
                            option.setCourse(course);
                            //设置媒体ID
                            if (course.getLesson().getAudioList() != null) {
                                option.setMediaID(course.getLesson().getAudioList().get(0).getVid());
                                Observable.timer(100, TimeUnit.MILLISECONDS)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(a -> {
                                            setMediaPlayer();
                                        });
                            } else {
                                Observable.timer(100, TimeUnit.MILLISECONDS)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(a -> {
                                            doRepeat();
                                        });
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            mediaPlayer=null;
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.media_mini_init_error), Toast.LENGTH_SHORT).show();
                        })
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    //清除播放器
    private void clearMedia() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        try {
            if (disposable1 != null && !disposable1.isDisposed()) {
                disposable1.dispose();
            }
            if (disposable2 != null && !disposable2.isDisposed()) {
                disposable2.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    //设置播放器
    private void setMediaPlayer() {
        try {
            if (option.getCurrentMedia() == null) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.media_music_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!CollectionUtil.isEmpty(option.getCourse().getLesson().getVideoList())) {
                saveStudy(0L, option.getCourse().getLesson().getVideoList().get(0), false);
            }
            if (!CollectionUtil.isEmpty(option.getCourse().getLesson().getAudioList())) {
                for (Media media : option.getCourse().getLesson().getAudioList()) {
                    saveStudy(0L, media, false);
                }
            }
            txtTitle.setText(option.getCourse().getLesson().getTitle());
            txtSubTitle.setText(option.getCurrentMedia().getVideoName());

            if (option.isReadDownload()) {
                File file = new File(option.getCurrentMedia().getOrigUrl());
                if (!file.exists()) {
                    doComplete();
                    return;
                }
            }

            if (TextUtils.isEmpty(option.getCurrentMedia().getOrigUrl())) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.media_url_error), Toast.LENGTH_SHORT).show();
                return;
            }
            mediaPlayer.setDataSource(option.getCurrentMedia().getOrigUrl());
            //显示mini播放器
            toggleView(View.VISIBLE);

            mediaPlayer.setOnErrorListener((iMediaPlayer, i, i1) -> {
                Log.e(TAG, "setOnErrorListener:" + i);
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.media_play_error), Toast.LENGTH_SHORT).show();
                option.setShowRepeat(false);
                return false;
            });

            mediaPlayer.setOnInfoListener((iMediaPlayer, i, i1) -> {
                Log.d(TAG, "setOnInfoListener:" + i);
                switch (i) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        option.setCurrentDuration(0L);
                        txtTimeMax.setText(formatter.format(iMediaPlayer.getDuration()));
                        Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED");
                        break;
                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        option.setCurrentDuration(0L);
                        txtTimeMax.setText(formatter.format(iMediaPlayer.getDuration()));
                        Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START");
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Log.d(TAG, "MEDIA_INFO_BUFFERING_START");
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
                        break;
                }
                return true;
            });
            if(initCompleteListener !=null){
                initCompleteListener.accept(this);
                initCompleteListener =null;
            }
            prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void saveStudy(Long current, Media media, boolean cover) {
        if (option.getId() < 0) {
            return;
        }
        try {
            StudyInfo studyInfo = new StudyInfo();
            studyInfo.setVid(String.valueOf(media.getVid()));
            studyInfo.setvName(media.getVideoName());
//            studyInfo.setlLength(option.getLessonDuration());
            studyInfo.setlLength(1L);
            studyInfo.setvLength((long) media.getDuration());
            if (current == null) {
                studyInfo.setCurrent(mediaPlayer.getCurrentPosition() / 1000);
            } else {
                studyInfo.setCurrent(current);
            }
            studyInfo.setLessonId(option.getId());
            studyInfo.setCourseId(option.getCourse().getLesson().getCourseId());
            studyInfo.setCourseName(option.getCourse().getTitle());
            studyInfo.setLessonName(option.getCourse().getLesson().getTitle());
            studyInfo.setUpdateDate(new Date());
            if (cover) {
                studyInfoDao.saveOrUpdate(studyInfo);
            } else {
                studyInfoDao.saveIfNotExist(studyInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    //记录学习进度
    private void saveStudy(Long current) {
        try {
            saveStudy(current, option.getCurrentMedia(), true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void saveStudy() {
        saveStudy(null);
    }

    /**
     * 变更View状态
     *
     * @param status View.VISIBLE;View.INVISIBLE;View.GONE
     */
    public void toggleView(int status) {
        try {
            if (!MediaServiceHelper.hasPermission(getApplication())) {
                return;
            }
            displayStatus = status;
            this.mFloatLayout.setVisibility(status);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 刷新View状态
     */
    public void reFleshDisplay() {
        try {
            if (!MediaServiceHelper.hasPermission(getApplication())) {
                return;
            }
            this.mFloatLayout.setVisibility(displayStatus);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 后台隐藏(不改变View状态)
     */
    public void backGroundHide() {
        try {
            if (!MediaServiceHelper.hasPermission(getApplication())) {
                return;
            }
            this.mFloatLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 设置显示位置,null不修改
     *
     * @param x dp
     * @param y dp
     */
    public void setXY(Float x, Float y) {
        try {
            if (x != null) {
                wmParams.x = DensityUtil.dip2px(getApplicationContext(), x);
            }
            if (y != null) {
                wmParams.y = DensityUtil.dip2px(getApplicationContext(), y);
            }
            mWindowManager.updateViewLayout(this.mFloatLayout, wmParams);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 添加完成监听
     *
     * @param listener IMediaPlayer.OnCompletionListener
     * @param context  Context
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener, Context context) {
        try {
            if (!MediaServiceHelper.hasPermission(getApplication())) {
                return;
            }
            this.listener = listener;
            this.context = context;
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(iMediaPlayer -> {
                    try {
                        Log.d(TAG, "setOnCompletionListener");
                        imgStatus.setProgress(100);
                        if (option.getCurrentMedia() != null && option.getCurrentMedia().getDuration() != null) {
                            saveStudy((long) option.getCurrentMedia().getDuration());
                        }
                        doComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void doComplete() {
        boolean doActivity = false;
        if (listener != null && context != null) {
            Log.d(TAG, ((Activity) context).getLocalClassName());
            doActivity=isActivityAlive();
            Log.d(TAG, (String.valueOf(doActivity)));
        }
        option.setCurrentDuration(null);
        if (doActivity) {//Activity存活,因此由Activity控制连续播放
            listener.onCompletion(mediaPlayer);
        } else {//未注册Activity或Activity已被销毁,因此由Service控制连续播放
            if (option.getRepeatMode() == CommonMediaOption.REPEAT_ALL && option.isShowRepeat()) {
                Log.d(TAG, "repeat");
                doRepeat();
            }
        }
    }

    //Activity是否存活
    private boolean isActivityAlive(){
        if(context==null){
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//api17
                return !((Activity) context).isDestroyed();
            } else {//api17以下
                List<ActivityManager.RunningTaskInfo> appTask = mActivityManager.getRunningTasks(1);
                if (appTask != null && appTask.size() > 0) {
                    Log.d(TAG, appTask.get(0).topActivity.toString());
                    if (appTask.get(0).topActivity.toString().contains(((Activity) context).getLocalClassName())) {
                        return true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void doRepeat() {
        try {
            setNextMediaID();
            if (option.getMediaID() == null) {
                option.setId(option.getNextLessonID());
                if(CollectionUtil.isEmpty(option.getCourse().getLessonList())){
                    if(option.getId()!=null){
                        startNextLesson();
                    }
                }else{
                    if(option.getId()!=null){
                        option.setMediaID(option.getCourse().getLesson().getAudioList().get(0).getVid());
                        initMedia(option);
                    }
                }
            } else {//有下一个,直接跳转
                initMedia(option);
            }



/*            if (option.getMediaID() == null) {
                if (option.getNextLessonID() != null) {
                    option.setId(option.getNextLessonID());
                    if (CollectionUtil.isEmpty(option.getCourse().getLessonList())) {
//                        option.setCourse(null);
                        startNextLesson();
                    } else {
                        option.setCourse(null);
                        initMedia(option);
                    }
                }
                //没有下节课,停止
            } else {//有下一个,直接跳转
                initMedia(option);
            }*/
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    //预判下节课是否为有音频
    private void startNextLesson() {
        disposable2 = RetrofitFactory.getInstance().getRequestServicesToken()
                .get("lessonDetail/[\"" + option.getId() + "\"]")
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //异常处理
                    if (!(s.getInteger("resultCode") == 1)) {
                        Log.e(TAG, s.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                        return;
                    }
                    Course course = JSON.toJavaObject(s.getJSONObject("resultObject"), Course.class);
                    int optionType = CommonMediaOption.autoSetType(course);
                    if (optionType == CommonMediaOption.MUSIC_MIX || optionType == CommonMediaOption.MUSIC_ONLY) {//下一课有音频
                        //此处可增加option赋值减少下次网络访问
                        Observable.timer(100, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a -> {
                                    initMedia(option);
                                });
                    } else {//下一课没有音频
                        Integer nextLessonID = option.getNextLessonID();
                        if (nextLessonID == null) {
                            Log.d(TAG, "next lesson is not music and has no next");
                        } else {
                            Log.d(TAG, "next lesson is not music");
                            option.setId(nextLessonID);
                            Observable.timer(100, TimeUnit.MILLISECONDS)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(a -> {
                                        startNextLesson();
                                    });
                        }
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    mediaPlayer=null;
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.media_repeat_error), Toast.LENGTH_SHORT).show();
                })
        ;
    }

    //设置下一个(音频)
    private void setNextMediaID() throws Exception {
        if (option == null || option.getMediaID() == null || option.getCourse() == null || option.getCourse().getLesson() == null || CollectionUtil.isEmpty(option.getCourse().getLesson().getAudioList()) || option.getRepeatMode() == CommonMediaOption.REPEAT_ONE) {
            throw new Exception("can't do repeat");
        }
        for (int i = 0; i < option.getCourse().getLesson().getAudioList().size(); i++) {
            if (option.getMediaID().equals(option.getCourse().getLesson().getAudioList().get(i).getVid()) && i < option.getCourse().getLesson().getAudioList().size() - 1) {//没有最后课
                option.setMediaID(option.getCourse().getLesson().getAudioList().get(i + 1).getVid());
                break;
            } else if (i == option.getCourse().getLesson().getAudioList().size() - 1) {//最后一个,准备跳Course
                option.setMediaID(null);
//                option.setCourse(null);
                break;
            }
        }
    }

    /**
     * 停止音频并关闭mini播放器
     */
    public void stopMedia() {
        try {
            clearMedia();
            toggleView(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public void hardClose(){
        try {
            option=null;
            stopMedia();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 暂停音频
     */
    public void pauseMedia() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public void breakMedia() {
        try {
            if (!isBreaking) {
                breakStatus = playStatus;
            }
            backGroundHide();
            isBreaking = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public void resumeMedia() {
        try {
            reFleshDisplay();
            isBreaking = false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 预加载
     */
    public void prepareAsync() {
        try {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public CommonMediaOption getCurrentOption(){
        return option;
    }

    public void setOnInitCompleteListener(Callback callback){
        this.initCompleteListener =callback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mFloatLayout != null) {
                //移除悬浮窗口
                mWindowManager.removeView(mFloatLayout);
            }
            if (mediaDisposable != null && !mediaDisposable.isDisposed()) {
                mediaDisposable.dispose();
                mediaDisposable = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public class MediaBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    public interface Callback{
        public void accept(MediaService mediaService);
    }

    /**
     * 预留方法,保存快照
     */
    public void saveSnapshot() {
        Snapshot snapshot = new Snapshot();
        snapshot.option = this.option;
        snapshot.mediaPlayer = mediaPlayer;
        snapshot.playStatus = this.playStatus;
        this.snapshot = snapshot;
    }

    /**
     * 预留方法,还原快照
     */
    public void revertSnapshot() {
        stopMedia();
    }

    /**
     * 快照
     */
    private class Snapshot {
        CommonMediaOption option;
        IMediaPlayer mediaPlayer;
        int playStatus = PAUSE;

    }

}
