package com.pointhouse.chiguan.k1_13;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.ImageView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;

/**
 * Created by gyh on 2017/7/27.
 */

public class ArticleActivity extends MediaBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_13_activity_article);

        Intent intent = getIntent();
        String article = intent.getStringExtra("article");
        initView(article);
    }

    private void initView(String article) {
        WebView wv = (WebView) findViewById(R.id.wv_article);
        wv.getSettings().setDefaultTextEncodingName("UTF -8");
        wv.loadData(article == null ? "" : article.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);

        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(v -> finish());
    }
}
