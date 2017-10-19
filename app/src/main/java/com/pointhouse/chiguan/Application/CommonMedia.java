package com.pointhouse.chiguan.Application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.RoundProgressBar.RoundProgressBar;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.CollectionUtil;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.db.StudyInfo;
import com.pointhouse.chiguan.db.StudyInfoDao;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Media;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.pointhouse.chiguan.Application.MediaService.PLAYING;

/**
 * 通用音视频播放,参数列表含义参考CommonMediaOption的注释
 * <br>XML:&lt;include layout="@layout/common_media" /&gt;
 * <br>CommonMediaOption option = new CommonMediaOption();
 * <br>option.setType(CommonMediaOption.AUTO);
 * <br>option.setId(1);
 * <br>CommonMedia commonMedia = new CommonMedia(this, option);
 * <br>如果要切换全屏(横屏),必须在Activity中重写onConfigurationChanged方法,切换到横屏时将除播放器以外的控件全部隐藏;
 * <br>重写onBackPressed(),在横屏时切换为竖屏
 * <br>
 * Created by Maclaine on 2017/7/17.
 */

public class CommonMedia {
    private static final String TAG = "CommonMedia";

    private Context mContext;

    //控件定义
    private IMediaPlayer mediaPlayer;
    private Disposable mediaDisposable;

    private SurfaceView sv;
    private GifImageView giv;
    private GifDrawable gid;
    private SeekBar seekBar;
    private TextView txtTimeCurrent, txtTimeMax, txtTimeCurrent2, txtTimeMax2, txtPoPRepeat, txtSpeed, txtBack, txtSpeedFull, txtPoPSort;
    private ImageView imgFull, imgBack, imgStatus, imgMenu, imgToVideo, imgToMusic, imgPoPRepeat, imgRepeat, imgStatusFull, imgPoPSort;
    private RoundProgressBar imgDownload;
    private View viewFastForward, viewFastBack, viewOperate, viewProgress, viewSpeed, viewPop, viewBottom, viewStatus, commonMedia, viewStatusFull, viewLoading;
    private FrameLayout frame;
    private LinearLayout viewSVContainer;
    private PopupWindow popWindow;
    private PullToRefreshListView ptrListView;
    private ListView mListView;
    private List<CommonMediaItem> mList;

    private CallBack completeCallBack, allCompleteCallBack;

    //时间转换
    private SimpleDateFormat formatter;
    //计时器
    private long viewShowCount = 0;
    //是否已经预加载
    private boolean hasPrepared = false;
    //是否加载完毕
    public boolean isOk = false;
    private boolean isFirst = true;
    public boolean isOut = false;
    //循环模式,默认单曲循环
    private int repeatMode = CommonMediaOption.REPEAT_ALL;
    //倍速
    private float speed = 1f;
    //排序
    private int sortType = CommonMediaOption.SORT_ASC;
    public int playStatus = PLAYING;
    private CommonMediaOption option;
    private Integer tempMediaID;
    private Integer tempOptionID;
    private Integer tempType;

    private StudyInfoDao studyInfoDao;
    private Disposable disposable1, disposable2;

    /**
     * @param context Context
     * @param option  CommonMediaOption
     */
    public CommonMedia(Context context, @NonNull CommonMediaOption option) {
        this.mContext = context;
        this.option = option;
        init();
    }

    public CommonMedia(Context context, @NonNull CommonMediaOption option, boolean init) {
        this.mContext = context;
        this.option = option;
        if (init) {
            init();
        }
    }

    /**
     * 初始化
     *
     * @return CommonMedia
     */
    public CommonMedia init() {
        if (option == null || option.getId() == null) {
            Log.e(TAG, "option或课程id为null");
            Toast.makeText(mContext, mContext.getString(R.string.media_init_error), Toast.LENGTH_SHORT).show();
            return this;
        }
        studyInfoDao = new StudyInfoDao(mContext);
        formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        //初始化控件
        initView();
        disposable1 = Observable.create((ObservableEmitter<CommonMediaOption> o) -> {
            if (MediaServiceHelper.hasPermission(mContext)) {
                MediaServiceHelper.getService(mContext, mediaService -> {
                    try {
                        CommonMediaOption currentOption = mediaService.getCurrentOption();
                        if (option != null && currentOption != null  && mediaService.getMediaPlayer() != null && currentOption.getCurrentDuration() != null
                                && (
                                ((option.getLessonIDList() != null || option.getCourse().getLessonList() != null) && currentOption.getId().equals(option.getId()))
                                        || (option.getMediaID() != null&& option.getMediaID().equals(currentOption.getMediaID())))
                                ) {
//                            option = currentOption;
                            option.setMediaID(currentOption.getMediaID());
                            option.setType(currentOption.getType());
                            if (!mediaService.getMediaPlayer().isPlaying()) {
                                option.setCurrentDuration(mediaService.getMediaPlayer().getCurrentPosition() * -1);
                            } else {
                                option.setCurrentDuration(mediaService.getMediaPlayer().getCurrentPosition());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        o.onNext(option);
                    }
                });
            } else {
                o.onNext(option);
            }
        })
                .filter(o -> {
                    if (option.getMediaID() != null && option.getCourse() != null) {//如果不为空说明必然是切换音视频或连续播放(或外部注入)
                        option.autoSetType();
                        if (isVideo() || isMusic()) {
                            commonMedia.setVisibility(View.VISIBLE);
                        } else if (option.getType() == CommonMediaOption.TEXT_ONLY) {
                            doRepeat();
                            return false;
                        }
                        initAfterView();
                        return false;
                    }
                    return true;
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .flatMap(o -> RetrofitFactory.getInstance().getRequestServicesToken().get("lessonDetail/[\"" + option.getId() + "\"]").retry(3))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(s -> {
                    if (!(s.getInteger("resultCode") == 1)) {
                        RetrofitFactory.ErrorMessage(mContext, s, mContext.getString(R.string.media_init_error));
                        Log.e(TAG, s.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                        return false;
                    }
                    return true;
                })
                .subscribe(s -> {
                    Course course = JSON.toJavaObject(s.getJSONObject("resultObject"), Course.class);
                    option.setCourse(course);
                    option.getCourse().setTitle(option.getCourseName());
                    //如果类型为AUTO,自动判断
                    option.autoSetType();
                    //设置媒体ID
                    if (isVideo() && !CollectionUtil.isEmpty(course.getLesson().getVideoList())) {
                        if (option.getMediaID() == null) {
                            option.setMediaID(course.getLesson().getVideoList().get(0).getVid());
                        }
                        commonMedia.setVisibility(View.VISIBLE);
                    } else if (isMusic() && !CollectionUtil.isEmpty(course.getLesson().getAudioList())) {
                        if (option.getMediaID() == null) {
                            option.setMediaID(course.getLesson().getAudioList().get(0).getVid());
                        }
                        commonMedia.setVisibility(View.VISIBLE);
                    } else if (option.getType() == CommonMediaOption.TEXT_ONLY) {
                        Log.d(TAG, "只有文章");
                        option.setShowRepeat(false);
                    } else {
                        Log.e(TAG, "类型异常");
                        Toast.makeText(mContext, mContext.getString(R.string.media_init_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    initAfterView();
                }, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(mContext, mContext.getString(R.string.media_init_error), Toast.LENGTH_SHORT).show();
                });
        return this;
    }

    private Activity getActivity() {
        return (Activity) mContext;
    }

    //初始化控件
    private void initView() {
        viewOperate = getActivity().findViewById(R.id.viewOperate);
        viewFastForward = getActivity().findViewById(R.id.viewFastForward);
        viewFastBack = getActivity().findViewById(R.id.viewFastBack);
        viewProgress = getActivity().findViewById(R.id.viewProgress);
        viewSpeed = getActivity().findViewById(R.id.viewSpeed);
        viewStatus = getActivity().findViewById(R.id.viewStatus);
        commonMedia = getActivity().findViewById(R.id.commonMedia);
        frame = (FrameLayout) getActivity().findViewById(R.id.frame);
        viewSVContainer = (LinearLayout) getActivity().findViewById(R.id.viewSVContainer);
        viewStatusFull = getActivity().findViewById(R.id.viewStatusFull);
        viewLoading = getActivity().findViewById(R.id.viewLoading);

        imgBack = (ImageView) getActivity().findViewById(R.id.imgBack);
        imgFull = (ImageView) getActivity().findViewById(R.id.imgFull);
        imgStatus = (ImageView) getActivity().findViewById(R.id.imgStatus);
        imgMenu = (ImageView) getActivity().findViewById(R.id.imgMenu);
        imgToVideo = (ImageView) getActivity().findViewById(R.id.imgToVideo);
        imgToMusic = (ImageView) getActivity().findViewById(R.id.imgToMusic);
        imgStatusFull = (ImageView) getActivity().findViewById(R.id.imgStatusFull);
        imgRepeat = (ImageView) getActivity().findViewById(R.id.imgRepeat);
        imgDownload = (RoundProgressBar) getActivity().findViewById(R.id.imgDownload);

        txtTimeCurrent = (TextView) getActivity().findViewById(R.id.txtTimeCurrent);
        txtTimeMax = (TextView) getActivity().findViewById(R.id.txtTimeMax);
        txtTimeCurrent2 = (TextView) getActivity().findViewById(R.id.txtTimeCurrent2);
        txtTimeMax2 = (TextView) getActivity().findViewById(R.id.txtTimeMax2);
        txtSpeed = (TextView) getActivity().findViewById(R.id.txtSpeed);
        txtBack = (TextView) getActivity().findViewById(R.id.txtBack);
        txtSpeedFull = (TextView) getActivity().findViewById(R.id.txtSpeedFull);

        seekBar = (SeekBar) getActivity().findViewById(R.id.seekBar);

        giv = (GifImageView) getActivity().findViewById(R.id.imgMusic);
        gid = (GifDrawable) giv.getDrawable();

        viewPop = LayoutInflater.from(mContext).inflate(R.layout.common_media_popwindow, null, false);
        popWindow = new PopupWindow(viewPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        viewBottom = viewPop.findViewById(R.id.viewBottom);
        imgPoPRepeat = (ImageView) viewPop.findViewById(R.id.imgPoPRepeat);
        imgPoPSort = (ImageView) viewPop.findViewById(R.id.imgPoPSort);
        txtPoPRepeat = (TextView) viewPop.findViewById(R.id.txtPoPRepeat);
        txtPoPSort = (TextView) viewPop.findViewById(R.id.txtPoPSort);

        ptrListView = (PullToRefreshListView) viewPop.findViewById(R.id.courseList);
        mListView = ptrListView.getRefreshableView();

        ptrListView.setPullLoadEnabled(false);
        ptrListView.setScrollLoadEnabled(true);
        ptrListView.setPullRefreshEnabled(false);//禁用上拉刷新

    }

    //初始化控件之后,此时option已经请求完毕
    private void initAfterView() {
        if (completeCallBack != null) {
            try {
                completeCallBack.accept();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //初始化样式
        initStyle();
        //初始化监听
        initListener();
        /*------初始化播放器------------------------------------------------------*/
        //初始化学习进度
        if (option.getType() == CommonMediaOption.TEXT_ONLY) {
            Media text = new Media();
            text.setDuration(1);
            text.setVid(option.getId());
            text.setVideoName("文章");
            saveStudy(1L, text, true);
        }
        if (!CollectionUtil.isEmpty(option.getCourse().getLesson().getVideoList())) {
            saveStudy(0L, option.getCourse().getLesson().getVideoList().get(0), false);
        }
        if (!CollectionUtil.isEmpty(option.getCourse().getLesson().getAudioList())) {
            for (Media media : option.getCourse().getLesson().getAudioList()) {
                saveStudy(0L, media, false);
            }
        }
        if (isVideo()) {
            if (MediaServiceHelper.hasPermission(mContext)) {
                MediaServiceHelper.getService(mContext, MediaService::stopMedia);
            }
            initPlayer();
        } else if (isMusic()) {
            initPlayer();
        }
        /*------初始化播放器完成------------------------------------------------------*/

        txtBack.setText(option.getCourse().getLesson().getTitle());

        if (allCompleteCallBack != null) {
            try {
                allCompleteCallBack.accept();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //初始化样式
    private void initStyle() {
        viewProgress.getBackground().setAlpha(120);
        viewFastBack.getBackground().setAlpha(120);
        viewSpeed.getBackground().setAlpha(120);

        //停止gif动画
//        gid.stop();

        switch (option.getType()) {
            case CommonMediaOption.VIDEO_MIX:
                imgToMusic.setVisibility(View.VISIBLE);
            case CommonMediaOption.VIDEO_ONLY:
                viewSVContainer.setVisibility(View.VISIBLE);
                if (MediaServiceHelper.hasPermission(mContext)) {
                    MediaServiceHelper.getService(mContext, MediaService::stopMedia);
                }
                break;
            case CommonMediaOption.MUSIC_MIX:
                imgMenu.setVisibility(View.VISIBLE);
                imgToVideo.setVisibility(View.VISIBLE);
            case CommonMediaOption.MUSIC_ONLY:
                viewSVContainer.setVisibility(View.INVISIBLE);
                giv.setVisibility(View.VISIBLE);
                viewSpeed.setVisibility(View.VISIBLE);
                viewFastForward.setBackgroundResource(R.mipmap.baiyuan);
                viewFastForward.getBackground().setAlpha(120);
                viewFastBack.setBackgroundResource(R.mipmap.baiyuan);
                viewFastBack.getBackground().setAlpha(120);
                viewStatusFull.setBackgroundResource(R.mipmap.baiyuan);
                viewStatusFull.getBackground().setAlpha(120);
                break;
            case CommonMediaOption.TEXT_ONLY:
                //do nothing
                break;
            default:
                viewSVContainer.setVisibility(View.VISIBLE);
                break;
        }

        initOptionBooleanINVISIBLE(option.isShowRepeat(), imgRepeat);
//        initOptionBoolean(option.isShowDownload(), imgDownload);
        initOptionBoolean(option.isShowCourseList() && isMusic(), imgMenu);
//        initOptionBoolean(!option.isShowExerciseClose(), imgBack);
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//要为popWindow设置一个背景才有效
    }

    //初始化监听
    private void initListener() {
        //设置返回键
        imgBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });

        //课程列表
        if (isMusic()) {
            imgMenu.setOnClickListener(this::showPopWindow);
        }
        //全屏
        imgFull.setOnClickListener(view -> {
            if (!isOk) {
                return;
            }
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏切换到竖屏
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏切换到横屏
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
        //设置控制面板触摸隐藏
        viewOperate.setOnClickListener(view -> {
            viewOperate.setVisibility(View.INVISIBLE);
            if (!isOk) {
                viewLoading.setVisibility(View.VISIBLE);
            }
        });
        //设置快退10秒
        viewFastBack.setOnClickListener(view -> {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10 * 1000);
                    seekBar.setProgress((int) (seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

        });
        //设置快进10秒
        viewFastForward.setOnClickListener(view -> {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10 * 1000);
                    seekBar.setProgress((int) (seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

        });
        //监听倍速
        viewSpeed.setOnClickListener(view -> {
            toggleSpeed();
        });
        txtSpeedFull.setOnClickListener(view -> {
            toggleSpeed();
        });
        //监听时间长度变化
        txtTimeCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtTimeCurrent2.setText(s);
            }
        });
        txtTimeMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtTimeMax2.setText(s);
            }
        });
        //监听进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer == null) {
                    return;
                }
                try {
                    this.progress = (int) (progress * mediaPlayer.getDuration() / seekBar.getMax());
                    txtTimeCurrent.setText(formatter.format(mediaPlayer.getCurrentPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(progress);
            }
        });
        //开始播放
        imgStatus.setOnClickListener(view -> statusChange());
        viewStatusFull.setOnClickListener(view -> statusChange());
        //设置点击视频显示控制面板
        viewSVContainer.setOnClickListener(view -> {
            viewOperate.setVisibility(View.VISIBLE);
            viewShowCount = 0;
            viewLoading.setVisibility(View.GONE);
        });
        giv.setOnClickListener(view -> {
            viewOperate.setVisibility(View.VISIBLE);
            viewShowCount = 0;
            viewLoading.setVisibility(View.GONE);
        });
        //设置切换按钮
        imgToVideo.setOnClickListener(view -> {
            try {
                close();
                option.setMediaID(option.getCourse().getLesson().getVideoList().get(0).getVid());//视频永远只有1个
                option.setType(CommonMediaOption.VIDEO_MIX);
                startIntent();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        });
        imgToMusic.setOnClickListener(view -> {
            try {
                close();
                option.setType(CommonMediaOption.MUSIC_MIX);
                option.setMediaID(option.getCourse().getLesson().getAudioList().get(0).getVid());//切回第一个音频
                startIntent();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        });
        //切换连续播放
        imgRepeat.setOnClickListener(view -> toggleRepeat());

        //关闭pop
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor((v1, event) -> false);
        if (MediaServiceHelper.hasPermission(mContext)) {
            popWindow.setOnDismissListener(() -> MediaServiceHelper.getService(mContext, MediaService::reFleshDisplay));
        }
        viewBottom.setOnClickListener(view -> popWindow.dismiss());
        //切换连续播放
        imgPoPRepeat.setOnClickListener(view -> toggleRepeat());
        txtPoPRepeat.setOnClickListener(view -> toggleRepeat());

        //下拉加载
        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //do nothing
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.d(TAG, "下拉加载");
                if (mList.size() < option.getCourse().getLesson().getAudioList().size()) {
                    int size = mList.size();
                    for (int i = size; i < size + 20 && i < option.getCourse().getLesson().getAudioList().size(); i++) {
                        mList.add(new CommonMediaItem(option.getCourse().getLesson().getAudioList().get(i)));
                    }
                    if (size == mList.size()) {
                        ptrListView.setPullLoadEnabled(false);
                    }
                }
                ptrListView.onRefreshComplete();
            }
        });

        //ListView监听
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            CommonMediaItem item = mList.get(position);
            option.setMediaID(item.getId());
            startIntent();
        });

        imgPoPSort.setOnClickListener(v -> sort());
        txtPoPSort.setOnClickListener(v -> sort());
    }

    //点击播放事件
    private void statusChange() {
        try {
            if (mediaPlayer == null) {
                imgStatus.setImageResource(R.mipmap.kaishi2);
                imgStatusFull.setImageResource(R.mipmap.bofang);
            } else if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                if (isMusic()) {
                    gid.start();
                }
                imgStatus.setImageResource(R.mipmap.zanting2);
                imgStatusFull.setImageResource(R.mipmap.zantingzhong);
            } else if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                if (isMusic()) {
                    gid.stop();
                }
                imgStatus.setImageResource(R.mipmap.kaishi2);
                imgStatusFull.setImageResource(R.mipmap.bofang);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    //初始化播放器
    private void initPlayer() {//音频播放器
        if (isMusic() && MediaServiceHelper.hasPermission(mContext.getApplicationContext())) {
            MediaServiceHelper.getService(mContext, mediaService -> {
                mediaService.setOnInitCompleteListener(m->{
                    setPlayerListener(m.getMediaPlayer());
                });
                mediaService.initMedia(option);
                mediaPlayer = mediaService.getMediaPlayer();
                mediaService.setOnCompletionListener(iMediaPlayer -> {
                    gid.stop();
                    seekBar.setProgress(seekBar.getMax());
                    txtTimeCurrent.setText(formatter.format(iMediaPlayer.getDuration()));
                    imgStatus.setImageResource(R.mipmap.kaishi2);
                    imgStatusFull.setImageResource(R.mipmap.bofang);
                    viewOperate.setVisibility(View.VISIBLE);
                    option.setCurrentDuration(null);
                    doRepeat();
                }, mContext);

            });
        } else if (isVideo() || (isMusic() && !MediaServiceHelper.hasPermission(mContext.getApplicationContext()))) {//视频播放器
            mediaPlayer = new IjkMediaPlayer();
            mediaPlayer.reset();
            try {
                String path = null;
                if (isMusic()) {
                    for (Media media : option.getCourse().getLesson().getAudioList()) {
                        if (media.getVid().equals(option.getMediaID())) {
                            path = media.getOrigUrl();
                            break;
                        }
                    }
                } else {
                    path = option.getCourse().getLesson().getVideoList().get(0).getOrigUrl();
                }
                if (path == null) {
                    doRepeat();
                    return;
                }
                if (option.isReadDownload()) {
                    File file = new File(path);
                    if (!file.exists()) {
                        doRepeat();
                        return;
                    }
                }
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.media_url_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaPlayer.setDataSource(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setPlayerListener(mediaPlayer);
        }

    }

    //设置播放器
    private void setPlayerListener(IMediaPlayer mediaPlayer) {
        if (option == null || option.getCourse() == null) {
            return;
        }

        //只有视频或视频音频混合才绑定显示
        /*if (isVideo()) {
            mediaPlayer.setDisplay(sv.getHolder());
        }*/
        if (isVideo()) {
            viewSVContainer.removeAllViews();
            sv = new SurfaceView(mContext);
            viewSVContainer.addView(sv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            sv.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    Log.d(TAG, "surfaceCreated");
                    try {
                        setSV(sv);
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
        }

        //监听缓冲
        mediaPlayer.setOnBufferingUpdateListener((iMediaPlayer, percent) -> {
//            Log.d(TAG, "setOnBufferingUpdateListener");
            seekBar.setSecondaryProgress(percent);
        });
        //监听准备完毕
        mediaPlayer.setOnPreparedListener(iMediaPlayer -> {
            Log.d(TAG, "setOnPreparedListener");
            isOk = true;
            viewLoading.setVisibility(View.GONE);
            txtTimeMax.setText(formatter.format(iMediaPlayer.getDuration()));
            if (option.getCurrentDuration() != null) {
                if (option.getCurrentDuration() < 0) {
                    option.setCurrentDuration(option.getCurrentDuration() * -1);
                    mediaPlayer.pause();
                }
                mediaPlayer.seekTo(option.getCurrentDuration());
                seekBar.setProgress((int) (seekBar.getMax() * option.getCurrentDuration() / mediaPlayer.getDuration()));
                txtTimeCurrent.setText(formatter.format(mediaPlayer.getCurrentPosition()));
//                option.setCurrentDuration(null);
            }
            option.setCurrentDuration(0L);
        });
        mediaPlayer.setOnSeekCompleteListener(iMediaPlayer -> {
            Log.d(TAG, "setOnSeekCompleteListener");
        });
        try {
            IjkMediaPlayer ijkMediaPlayer = ((IjkMediaPlayer) mediaPlayer);
//            ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L);
//            ijkMediaPlayer.setOption(1, "probesize", 10240L);
//            ijkMediaPlayer.setOption(1, "flush_packets", 1L);
            ijkMediaPlayer.setOption(4, "packet-buffering", 0L);
//            ijkMediaPlayer.setOption(4, "framedrop", 1L);
            //        ijkMediaPlayer.setOption(OPT_CATEGORY_PLAYER,"packet-buffering",0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        if (!isMusic() || !MediaServiceHelper.hasPermission(mContext.getApplicationContext())) {
            mediaPlayer.setOnCompletionListener(iMediaPlayer -> {
                try {
                    Log.d(TAG, "setOnCompletionListener");
                    //播放结束进度无法走完，强行走完
                    seekBar.setProgress(seekBar.getMax());
                    txtTimeCurrent.setText(formatter.format(iMediaPlayer.getDuration()));
                    imgStatus.setImageResource(R.mipmap.kaishi2);
                    imgStatusFull.setImageResource(R.mipmap.bofang);
                    viewOperate.setVisibility(View.VISIBLE);
                    option.setCurrentDuration(null);
                    try {
                        saveStudy((long) option.getCurrentMedia().getDuration());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                    doRepeat();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

            });
            mediaPlayer.setOnErrorListener((iMediaPlayer, i, i1) -> {
                Log.e(TAG, "setOnErrorListener:" + i);
                Toast.makeText(mContext, mContext.getString(R.string.media_play_error), Toast.LENGTH_SHORT).show();
                option.setShowRepeat(false);
                return false;
            });
        }

        mediaDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    /*try {
                        if(sv!=null&&mediaPlayer.getVideoWidth()!=0){
                            sv.getLayoutParams().width=300;
                            sv.getLayoutParams().height=100;
                            Log.d(TAG,"sv width:"+sv.getMeasuredWidth());
                            Log.d(TAG,"video width:"+mediaPlayer.getVideoWidth());
//                            Log.d(TAG,"video width:"+mediaPlayer.getVideoWidth());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,e.getMessage());
                    }*/
                    if (isOut) {
                        return;
                    }
                    if (mediaPlayer.isPlaying()) {
                        saveStudy();
                        if (mediaPlayer.getCurrentPosition() > mediaPlayer.getDuration()) {
                            seekBar.setProgress(seekBar.getMax());
                            mediaPlayer.pause();
                            imgStatus.setImageResource(R.mipmap.kaishi2);
                            imgStatusFull.setImageResource(R.mipmap.bofang);
                        } else if (mediaPlayer.getDuration() > 0) {
                            seekBar.setProgress((int) (seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()));
                            txtTimeCurrent.setText(formatter.format(mediaPlayer.getCurrentPosition()));
                        }
                        //设置播放中操作面板5秒隐藏
                        if (viewOperate.getVisibility() == View.VISIBLE && viewShowCount <= 5) {
                            viewShowCount++;
                        } else if (viewOperate.getVisibility() == View.VISIBLE && viewShowCount > 5) {
                            viewShowCount = 0;
                            viewOperate.setVisibility(View.INVISIBLE);
                        }
                        reFreshPlayStatus(MediaService.PLAYING);
                    } else {
                        reFreshPlayStatus(MediaService.PAUSE);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.e(TAG, throwable.getMessage());
                });
    }

    //刷新播放状态
    private void reFreshPlayStatus(int targetStatus) {
        playStatus = targetStatus;
        switch (targetStatus) {
            case MediaService.PLAYING:
                imgStatus.setImageResource(R.mipmap.zanting2);
                imgStatusFull.setImageResource(R.mipmap.zantingzhong);
                if (!gid.isPlaying())
                    gid.start();
                break;
            case MediaService.PAUSE:
                imgStatus.setImageResource(R.mipmap.kaishi2);
                imgStatusFull.setImageResource(R.mipmap.bofang);
                if (gid.isPlaying())
                    gid.stop();
                break;
        }
    }

    //初始化配置参数
    private void initOptionBoolean(boolean isShow, View view) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void initOptionBooleanINVISIBLE(boolean isShow, View view) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
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

    //显示弹出窗(课程列表)
    private void showPopWindow(View v) {
        if (getActivity().findViewById(R.id.viewMain) == null) {
            return;
        }
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAtLocation(getActivity().findViewById(R.id.viewMain), Gravity.BOTTOM, 0, 0);
        if (mList == null) {
            initItemList();
        }
        mListView.setSelection(getCurrentMediaPosition());
        if (MediaServiceHelper.hasPermission(mContext)) {
            MediaServiceHelper.getService(mContext, MediaService::backGroundHide);
        }
        Log.d(TAG, getCurrentMediaPosition() + "");
    }

    //切换循环
    private void toggleRepeat() {
        switch (repeatMode) {
            case CommonMediaOption.REPEAT_ALL://当前循环列表,切换到单曲循环
                mediaPlayer.setLooping(true);
                repeatMode = CommonMediaOption.REPEAT_ONE;
                option.setRepeatMode(CommonMediaOption.REPEAT_ONE);
                if (imgRepeat != null) {
                    imgRepeat.setImageResource(R.mipmap.danquxunhuan);
                }
                imgPoPRepeat.setImageResource(R.mipmap.danquxunhuan);
                txtPoPRepeat.setText(R.string.media_repeat_one);
                break;
            case CommonMediaOption.REPEAT_ONE://当前单曲循环,切换到循环列表
                mediaPlayer.setLooping(false);
                repeatMode = CommonMediaOption.REPEAT_ALL;
                option.setRepeatMode(CommonMediaOption.REPEAT_ALL);
                if (imgRepeat != null) {
                    imgRepeat.setImageResource(R.mipmap.bofangliebiaoxunhuan);
                }
                imgPoPRepeat.setImageResource(R.mipmap.bofangliebiaoxunhuan);
                txtPoPRepeat.setText(R.string.media_repeat_seq);
                break;
        }
    }

    //切换倍速
    private void toggleSpeed() {
        try {
            if (speed == 1f) {
                speed = 1.2f;
                txtSpeed.setText(R.string.media_speed_1_2);
                txtSpeedFull.setText(R.string.media_speed_1_2);
            } else if (speed == 1.2f) {
                speed = 1.5f;
                txtSpeed.setText(R.string.media_speed_1_5);
                txtSpeedFull.setText(R.string.media_speed_1_5);
            } else if (speed == 1.5f) {
                speed = 2f;
                txtSpeed.setText(R.string.media_speed_2);
                txtSpeedFull.setText(R.string.media_speed_2);
            } else if (speed == 2f) {
                speed = 0.5f;
                txtSpeed.setText(R.string.media_speed_0_5);
                txtSpeedFull.setText(R.string.media_speed_0_5);
            } else if (speed == 0.5f) {
                speed = 0.75f;
                txtSpeed.setText(R.string.media_speed_0_75);
                txtSpeedFull.setText(R.string.media_speed_0_75);
            } else if (speed == 0.75f) {
                speed = 1f;
                txtSpeed.setText(R.string.media_speed_1);
                txtSpeedFull.setText(R.string.media_speed_1);
            }
            ((IjkMediaPlayer) mediaPlayer).setSpeed(speed);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    //初始化课程列表
    private void initItemList() {
        try {
            mList = new ArrayList<>();
            boolean flag = true;
            int nowPosition = 0;
            for (int i = 0; i < option.getCourse().getLesson().getAudioList().size(); i++) {
                if (i > 20 && !flag && i > nowPosition + 10) {
                    break;
                }
                CommonMediaItem item = new CommonMediaItem(option.getCourse().getLesson().getAudioList().get(i));
                if (option.getMediaID().equals(item.getId())) {
                    item.setPlaying(true);
                    flag = false;
                    nowPosition = i;
                }
                mList.add(item);

            }
            CommonMediaAdapter mAdapter = new CommonMediaAdapter(mContext, mList);
            mListView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置下一个(音频)
    private void setNextMediaID() {
        if (isMusic()) {
            for (int i = 0; i < option.getCourse().getLesson().getAudioList().size(); i++) {
                if (option.getMediaID().equals(option.getCourse().getLesson().getAudioList().get(i).getVid()) && i < option.getCourse().getLesson().getAudioList().size() - 1) {//没有最后课
                    option.setMediaID(option.getCourse().getLesson().getAudioList().get(i + 1).getVid());
                    break;
                } else if (i == option.getCourse().getLesson().getAudioList().size() - 1) {//最后一个,准备播放视频
                    tempMediaID = option.getMediaID();
                    option.setMediaID(null);
//                    option.setCourse(null);
                    break;
                }
            }
        } else if (isVideo()) {//视频连续播放需要跳课程,准备播放下节课音频
            tempMediaID = option.getMediaID();
            option.setMediaID(null);
//            option.setCourse(null);
        } else if (option.getType() == CommonMediaOption.TEXT_ONLY) {
            option.setMediaID(null);
        }
    }

    //获取当前播放的
    private int getCurrentMediaPosition() {
        for (int i = 0; i < option.getCourse().getLesson().getAudioList().size(); i++) {
            if (option.getMediaID().equals(option.getCourse().getLesson().getAudioList().get(i).getVid()) && i < option.getCourse().getLesson().getAudioList().size()) {
                return i;
            }
        }
        return 0;
    }

    //循环播放
    private void doRepeat() {
        if (option.getRepeatMode() == CommonMediaOption.REPEAT_ALL && option.isShowRepeat()) {
            setNextMediaID();
            Log.d(TAG, "REPEAT");
            if (option.getMediaID() == null) {
                Log.d(TAG, "getMediaID() IS NULL");
                tempType = option.getType();
                switch (option.getType()) {
                    case CommonMediaOption.MUSIC_ONLY:
                    case CommonMediaOption.MUSIC_MIX:
                        //当前音频,转为播放视频
                        if (!CollectionUtil.isEmpty(option.getCourse().getLesson().getVideoList())) {
                            option.setType(CommonMediaOption.VIDEO_MIX);
                            option.setMediaID(option.getCourse().getLesson().getVideoList().get(0).getVid());
                            break;
                        }//播放下节课
                    case CommonMediaOption.VIDEO_ONLY:
                    case CommonMediaOption.VIDEO_MIX:
                        //当前视频,转为播放下节课音频
                        tempOptionID = option.getId();
                        option.setType(CommonMediaOption.AUTO);
                        option.setId(option.getNextLessonID());
                        break;
                    default:
                        Log.w(TAG, "未正确识别类型,终止连续播放");
                        option.setId(null);
                        tempMediaID = option.getMediaID();
                        tempOptionID = option.getId();
                        break;
                }
                if (option.getId() != null) {
                    if (option.getMediaID() == null && CollectionUtil.isEmpty(option.getCourse().getLessonList())) {
                        Log.d(TAG, "NEXT LESSON");
                        startNextLesson();
                    } else {
                        Log.d(TAG, "NEXT MEDIA");
                        startNext();
                    }
                } else {
                    Log.d(TAG, "STOP");
                    option.setMediaID(tempMediaID);
                    option.setId(tempOptionID);
                    option.setType(tempType);
                }
            } else {//有下一个,直接跳转
                Log.d(TAG, "getMediaID() NOT NULL");
                startNext();
            }
        }

    }

    private void startIntent() {
        option.setCurrentDuration(null);
        Intent intent = new Intent(mContext, mContext.getClass());
        Bundle bundle = new Bundle();
        bundle.putSerializable("option", option);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private void startNext() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    startIntent();
                });
    }

    //预判下节课是否为图文课程
    private void startNextLesson() {
        disposable2 = RetrofitFactory.getInstance().getRequestServicesToken()
                .get("lessonDetail/[\"" + option.getId() + "\"]")
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //异常处理
                    if (!(s.getInteger("resultCode") == 1)) {
                        RetrofitFactory.ErrorMessage(mContext, s, mContext.getString(R.string.media_init_error));
                        Log.e(TAG, s.getJSONArray("exceptions").getJSONObject(0).getString("message"));
                        return;
                    }
                    Course course = JSON.toJavaObject(s.getJSONObject("resultObject"), Course.class);
                    int optionType = CommonMediaOption.autoSetType(course);
                    if (optionType != CommonMediaOption.TEXT_ONLY) {//下一课有音视频
                        //此处可增加option赋值减少下次网络访问
                        Observable.timer(100, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a -> {
                                    startIntent();
                                });
                    } else {//下一课是文章
                        Integer nextLessonID = option.getNextLessonID();
                        if (nextLessonID == null) {
                            Log.d(TAG, "next lesson is text and has no next");
                            option.setMediaID(tempMediaID);
                            option.setId(tempOptionID);
                            option.setType(tempType);
                        } else {
                            Log.d(TAG, "next lesson is text");
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
                    Toast.makeText(mContext, mContext.getString(R.string.media_repeat_error), Toast.LENGTH_SHORT).show();
                })
        ;
    }

    //排序
    private void sort() {
        switch (sortType) {
            case CommonMediaOption.SORT_ASC://原本顺序,切换到倒序
                sortType = CommonMediaOption.SORT_DESC;
                imgPoPSort.setImageResource(R.mipmap.nipaixu);
                sortList();
                initItemList();
                mListView.setSelection(getCurrentMediaPosition());
                break;
            case CommonMediaOption.SORT_DESC://原本倒序,切换到顺序
                sortType = CommonMediaOption.SORT_ASC;
                imgPoPSort.setImageResource(R.mipmap.shunpaixun);
                sortList();
                initItemList();
                mListView.setSelection(getCurrentMediaPosition());
                break;
        }
    }

    private void sortList() {
        List<Media> sortList = new ArrayList<>();
        List<Media> audios = option.getCourse().getLesson().getAudioList();
        for (int i = audios.size() - 1; i >= 0; i--) {
            sortList.add(audios.get(i));
        }
        option.getCourse().getLesson().setAudioList(sortList);
    }

    /**
     * 保存学习进度
     *
     * @param current 当前秒数
     * @param media   媒体对象
     * @param cover   是否强行覆盖
     */
    private void saveStudy(Long current, Media media, boolean cover) {
        if (option.getId() < 0) {
            return;
        }
        try {
            StudyInfo studyInfo = new StudyInfo();
            studyInfo.setVid(String.valueOf(media.getVid()));
            studyInfo.setvName(media.getVideoName());
            /*if (option.getLessonDuration() == null || option.getLessonDuration() == 0) {
                studyInfo.setlLength(1L);
            } else {
                studyInfo.setlLength(option.getLessonDuration());
            }*/
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
//            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    private void saveStudy() {
        saveStudy(null);
    }


    /**
     * 播放器预加载
     */
    public void prepareAsync() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (isMusic() && MediaServiceHelper.hasPermission(mContext)) {
                        return;
                    }
                    if (mediaPlayer != null && !hasPrepared && !mediaPlayer.isPlaying()) {
                        mediaPlayer.prepareAsync();
                        hasPrepared = true;
                        isFirst = false;
                    } else if (isFirst) {
                        prepareAsync();
                    }
                }, Throwable::printStackTrace);

    }

    /**
     * 清除播放器
     */
    public void close() {
        try {
            if (mediaPlayer != null) {
                if (!isMusic() || !MediaServiceHelper.hasPermission(mContext)) {
                    mediaPlayer.release();
                }
                mediaPlayer = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        try {
            if (popWindow != null) {
                popWindow.dismiss();
                popWindow = null;
            }
            if (mediaDisposable != null && !mediaDisposable.isDisposed()) {
                mediaDisposable.dispose();
            }
            completeCallBack = null;
        } catch (Exception e) {
            e.printStackTrace();
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

    public void hardClose() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        close();
    }


    /**
     * 切换横竖屏
     *
     * @param newConfig Configuration
     */
    public void onConfigurationChanged(Configuration newConfig) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                commonMedia.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
            commonMedia.getLayoutParams().height = wm.getDefaultDisplay().getHeight();
            frame.getLayoutParams().height = wm.getDefaultDisplay().getHeight();
            if (sv != null) {
                sv.getLayoutParams().height = wm.getDefaultDisplay().getHeight();
            }
            viewStatus.setVisibility(View.GONE);

            //修改全屏为缩小
            imgFull.setImageResource(R.mipmap.tuichuquanping);
            //显示后退
            imgBack.setVisibility(View.VISIBLE);
            //显示后退文字
            txtBack.setVisibility(View.VISIBLE);
            //显示全屏开始/暂停按钮
            viewStatusFull.setVisibility(View.VISIBLE);
            //显示全屏倍速
            if (isMusic()) {
                txtSpeedFull.setVisibility(View.VISIBLE);
                if (!option.isShowCourseList()) {
                    imgMenu.setVisibility(View.GONE);
                }
            }
            //隐藏播放/循环条
            viewStatus.setVisibility(View.GONE);
            //隐藏倍速
            viewSpeed.setVisibility(View.GONE);
            if (isMusic()) {
                imgMenu.setOnClickListener(v -> getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));//全屏下菜单返回竖屏
            }
            if (MediaServiceHelper.hasPermission(mContext)) {
                MediaServiceHelper.getService(mContext, MediaService::backGroundHide);
            }
        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                commonMedia.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            commonMedia.getLayoutParams().height = DensityUtil.dip2px(mContext, 534 / 2);
            frame.getLayoutParams().height = DensityUtil.dip2px(mContext, 420 / 2);
            if (sv != null) {
                sv.getLayoutParams().height = DensityUtil.dip2px(mContext, 420 / 2);
            }
            viewStatus.setVisibility(View.VISIBLE);

            //隐藏后退文字
            txtBack.setVisibility(View.GONE);
            //隐藏全屏开始/暂停按钮
            viewStatusFull.setVisibility(View.GONE);
            //隐藏全屏倍速
            txtSpeedFull.setVisibility(View.GONE);

            //修改缩小为全屏
            imgFull.setImageResource(R.mipmap.quanping);
            //显示播放/循环条
            viewStatus.setVisibility(View.VISIBLE);
            imgBack.setVisibility(View.INVISIBLE);


            //显示倍速
            if (isMusic()) {
                viewSpeed.setVisibility(View.VISIBLE);
                imgMenu.setOnClickListener(this::showPopWindow);
            }
            if (MediaServiceHelper.hasPermission(mContext)) {
                MediaServiceHelper.getService(mContext, MediaService::reFleshDisplay);
            }
        }


    }

    public CommonMediaOption getOption() {
        return option;
    }

    public void setOnInitCompleteListener(CallBack callBack) {
        this.completeCallBack = callBack;
    }

    public void setOnAllCompleteListener(CallBack callBack) {
        this.allCompleteCallBack = callBack;
    }

    public void setSV(SurfaceView newSV) {
        mediaPlayer.setDisplay(newSV.getHolder());

    }

    public IMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void pause() {
        try {
            reFreshPlayStatus(MediaService.PAUSE);
            mediaPlayer.pause();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }

    public interface CallBack {
        public void accept();
    }
}
