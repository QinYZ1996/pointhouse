package com.pointhouse.chiguan.k1_13;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.CommonMedia;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.layout.MyScrollView;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.k1_3.Media;

import java.util.List;

/**
 * Created by gyh on 2017/7/27.
 */

public class AudioActivity extends MediaBaseActivity {

    private CommonMedia commonMedia;
    private RelativeLayout foldedLayout;
    private RelativeLayout expandedLayout;
    private View back, titleBar, whiteBackView;
    private MyScrollView scrollView;
    private boolean isExpanded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_13_activity_audio);

        CommonMediaOption option = (CommonMediaOption) getIntent().getSerializableExtra("mediaOption");
        int type = getIntent().getIntExtra("type", -1);
        initView(type, option);
    }

    private void initView(int type, CommonMediaOption option) {
        foldedLayout = (RelativeLayout) findViewById(R.id.rl_folded);
        expandedLayout = (RelativeLayout) findViewById(R.id.rl_expanded);
        back = findViewById(R.id.back);
        scrollView = (MyScrollView) findViewById(R.id.scroll_view);
        titleBar = findViewById(R.id.title_bar);
        whiteBackView = findViewById(R.id.image_back_white_layout);

        back.setOnClickListener(v -> finish());

        titleBar.setAlpha(0);
        scrollView.setOnScrollListener(scrollY -> {
            int mediaViewHeight = DensityUtil.dip2px(getApplicationContext(), 210 - 50);
            if (scrollY <= mediaViewHeight) {
                titleBar.setAlpha(0);
            } else {
                float noTransparentHeight = DensityUtil.dip2px(getApplicationContext(), 57);
                float alphaValue = (scrollY - mediaViewHeight) / noTransparentHeight;
                titleBar.setAlpha(alphaValue > 1 ? 1 : alphaValue);
            }
        });

        if (type != 1) {
            try {
                MediaServiceHelper.getService(this, MediaService::hardClose);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commonMedia = new CommonMedia(this, option);
        commonMedia.setOnAllCompleteListener(() -> {
            ImageView repeat = (ImageView) findViewById(R.id.imgRepeat);
            repeat.setVisibility(View.VISIBLE);
            repeat.setImageResource(R.mipmap.budanquxunhuan);
            repeat.setOnClickListener(new View.OnClickListener() {
                boolean isLoop;

                @Override
                public void onClick(View v) {
                    isLoop = !isLoop;
                    if (isLoop) {
                        repeat.setImageResource(R.mipmap.danquxunhuan);
                        commonMedia.getMediaPlayer().setLooping(true);
                    } else {
                        repeat.setImageResource(R.mipmap.budanquxunhuan);
                        commonMedia.getMediaPlayer().setLooping(false);
                    }
                }
            });
        });
        WebView wv = (WebView) findViewById(R.id.wv_original);
        List<Media> mediaList = type == 1 ? option.getCourse().getLesson().getAudioList() : option.getCourse().getLesson().getVideoList();
        if (mediaList != null && mediaList.size() > 0) {
            String original = mediaList.get(0).getOriginal();
            wv.getSettings().setDefaultTextEncodingName("UTF -8");
            wv.loadData(original == null ? "" : original.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
        }

        wv.setFocusable(false);

        TextView expandTV = (TextView) findViewById(R.id.tv_expand);
        expandTV.setOnClickListener(v -> {
            foldedLayout.setVisibility(View.GONE);
            expandedLayout.setVisibility(View.VISIBLE);
            isExpanded = true;
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout currentLayout = isExpanded ? expandedLayout : foldedLayout;
        if (commonMedia != null) {
            commonMedia.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
                currentLayout.setVisibility(View.GONE);
                titleBar.setVisibility(View.GONE);
                whiteBackView.setVisibility(View.GONE);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // 竖屏
                currentLayout.setVisibility(View.VISIBLE);
                titleBar.setVisibility(View.VISIBLE);
                whiteBackView.setVisibility(View.VISIBLE);
            }
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (commonMedia != null) {
                commonMedia.prepareAsync();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commonMedia != null) {
            commonMedia.close();
        }
    }
}
