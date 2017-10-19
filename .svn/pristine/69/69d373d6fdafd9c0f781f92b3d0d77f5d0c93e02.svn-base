package com.pointhouse.chiguan.k1_9;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.CommonMedia;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.http.RequestBuilder;
import com.pointhouse.chiguan.common.jsonObject.ArticleDetailGetBean;
import com.pointhouse.chiguan.common.jsonObject.CommentGetBean;
import com.pointhouse.chiguan.common.updatecommon.ILoadingLayout;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.pointhouse.chiguan.db.PayInfo;
import com.pointhouse.chiguan.db.PayInfoDao;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Lesson;
import com.pointhouse.chiguan.k1_3.Media;
import com.pointhouse.chiguan.k1_4.PayActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by P on 2017/8/30.
 */

public class ShareArticleActivity extends MediaBaseActivity implements View.OnLayoutChangeListener {

    private static final String TAG = "k1-9";
    private static final String CURRENCY = "¥";
    private static final String COLLECTION_TYPE = "2";
    private static final int SOFT_INPUT_SHOW_DELAY = 100; // ms
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    private CommonMedia commonMedia;
    private View main, contentView, mediaView, purchaseHintView, collection, purchase, addComment, popView, titleBar, back, share, headerView,
            commentDivider, viewLoading, viewOperate, articleView, viewSVContainer, giv, partArticleView, commentView, titleSpace, whiteBackView, authorDivider;
    private ImageView imgShare, imgRepeat;
    private CircleImageView teacherIcon;
    private TextView teacherName, releaseDate, price, collectionLabel, category, viewLoadingText, inputSize;
    private EditText etComment;
    private WebView article, partArticle;
    private ListView listView;
    private CommentItemAdapter adapter;
    private PopupWindow popupWindow;
    private String tmpComment;
    private PullToRefreshListView refreshListView;

    public static boolean isPaid;
    private boolean isFree;
    private boolean isReload;
    private boolean isFavorite;
    private String articleId;
    private int page = 2; // 第二页开始分页
    private Bundle savedInstanceState;

    private void bindView() {
        // 主画面部分
        main = findViewById(R.id.main);
        purchase = findViewById(R.id.purchase);
        refreshListView = (PullToRefreshListView) findViewById(R.id.comment_list);
        refreshListView.setPullRefreshEnabled(false);
        refreshListView.setPullLoadEnabled(false);
        refreshListView.setScrollLoadEnabled(false);
        listView = refreshListView.getRefreshableView();
        titleBar = findViewById(R.id.title_bar);
        back = findViewById(R.id.sharearticle_back);
        share = findViewById(R.id.share);
        whiteBackView = findViewById(R.id.image_back_white_layout);
        // listView部分
        headerView = LayoutInflater.from(this).inflate(R.layout.k1_9_item_header, null, false);
        listView.addHeaderView(headerView);
        adapter = new CommentItemAdapter(this);
        listView.setAdapter(adapter);
        // headerView部分
        contentView = headerView.findViewById(R.id.content_layout);
        mediaView = headerView.findViewById(R.id.commonMedia);
        viewLoading = headerView.findViewById(R.id.viewLoading);
        viewOperate = headerView.findViewById(R.id.viewOperate);
        viewSVContainer = headerView.findViewById(R.id.viewSVContainer);
        giv = headerView.findViewById(R.id.imgMusic);
        viewLoadingText = (TextView) findViewById(R.id.viewLoadingText);
        imgShare = (ImageView) headerView.findViewById(R.id.imgShare);
        imgRepeat = (ImageView) headerView.findViewById(R.id.imgRepeat);
        teacherIcon = (CircleImageView) headerView.findViewById(R.id.teacher_icon);
        teacherName = (TextView) headerView.findViewById(R.id.teacher_name);
        releaseDate = (TextView) headerView.findViewById(R.id.release_date);
        price = (TextView) headerView.findViewById(R.id.price);
        collection = headerView.findViewById(R.id.collection);
        collectionLabel = (TextView) headerView.findViewById(R.id.collection_label);
        category = (TextView) headerView.findViewById(R.id.category);
        articleView = headerView.findViewById(R.id.article_layout);
        article = (WebView) headerView.findViewById(R.id.article);
        purchaseHintView = headerView.findViewById(R.id.purchase_hint);
        addComment = headerView.findViewById(R.id.add_comment_layout);
        titleSpace = headerView.findViewById(R.id.title_space);
        commentDivider = headerView.findViewById(R.id.comment_divider);
        partArticleView = headerView.findViewById(R.id.part_article_layout);
        partArticle = (WebView) headerView.findViewById((R.id.part_article));
        commentView = headerView.findViewById(R.id.rl_comment);
        authorDivider = headerView.findViewById(R.id.author_divider);
        // popupwindow
        popView = LayoutInflater.from(this).inflate(R.layout.k1_9_popupwindow, null, false);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        etComment = (EditText) popView.findViewById(R.id.et_comment);
        inputSize = (TextView) popView.findViewById(R.id.text_num_current);

        mediaView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_9_activity_sharearticle);
        articleId = getIntent().getStringExtra("articleId");
        bindView();
        initView();
    }

    private void initView() {
        new RequestBuilder<>(ArticleDetailGetBean.class).getRequest("articleDetail", new String[]{articleId}, TAG, this, bean -> {
//            bean = JSON.parseObject(TestArticleDetailGetBean.jsonNormal, ArticleDetailGetBean.class); // for test
            ArticleDetailGetBean.ResultObjectBean rob = bean.getResultObject();
            ArticleDetailGetBean.ResultObjectBean.ArticleBean ab = rob.getArticle();
            // 共通设定
            if (ab.getAuthorAvatar() != null && !ab.getAuthorAvatar().isEmpty()) {
                Picasso.with(this).load(ab.getAuthorAvatar()).placeholder(R.drawable.icon1_default).into(teacherIcon);
            } else {
                Picasso.with(this).load(R.drawable.icon1_default).into(teacherIcon);
            }
            teacherName.setText(ab.getAuthor());
            releaseDate.setText(ab.getCreateDate());
            category.setText(ab.getCategoryTitle());
            isFavorite = rob.getIsFavorite() == 1;

            setViewByCondition(rob);
            bindEvent(rob);
        });
    }

    private void setViewByCondition(ArticleDetailGetBean.ResultObjectBean rob) {
        ArticleDetailGetBean.ResultObjectBean.ArticleBean ab = rob.getArticle();
        int type = ab.getType();
        // 价格
        switch (type) {
            case 0:
                price.setText("免费");
                break;
            case 1:
                price.setText(CURRENCY + ToolUtil.doubleToString(ab.getPrice()));
                break;
            case 2:
                price.setText(CURRENCY + ToolUtil.doubleToString(ab.getPrice()) + " (会员免费)");
                break;
        }
        // 收藏已收藏表示
        if (isFavorite) {
            collectionLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            collectionLabel.setText("已收藏");
            collection.setBackgroundResource(R.mipmap.yishoucang);
        } else {
            collectionLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            collectionLabel.setText("收藏");
            collection.setBackgroundResource(R.mipmap.shoucang);
        }
        // 购买提示、购买表示
        if (type == 0) { // 免费
            isPaid = true;
            isFree = true;
            purchase.setVisibility(View.GONE);
            purchaseHintView.setVisibility(View.GONE);
            share.setVisibility(View.VISIBLE);
        } else if (isLogin()) { // 收费，已登录
            if (rob.getIsPaid() == 1) { // 已支付
                isPaid = true;
                purchase.setVisibility(View.GONE);
                purchaseHintView.setVisibility(View.GONE);
            } else if (GlobalApplication.user.getVip() == 1 && type == 2) { // 文章类型：会员免费
                isPaid = true;
                purchase.setVisibility(View.GONE);
                purchaseHintView.setVisibility(View.GONE);
            } else { // 未支付
                isPaid = false;
                purchase.setVisibility(View.VISIBLE);
                purchaseHintView.setVisibility(View.VISIBLE);
            }
        } else { // 收费，未登录
            isPaid = false;
            purchase.setVisibility(View.VISIBLE);
            purchaseHintView.setVisibility(View.VISIBLE);
        }
        // 评论表示
        if (ab.getCommentDisable() == 0) { // 可留言
            refreshListView.setScrollLoadEnabled(true);
            loadListData(rob);
            commentDivider.setVisibility(isPaid ? View.VISIBLE : View.GONE);
            commentView.setVisibility(View.VISIBLE);
        } else {
            refreshListView.setScrollLoadEnabled(false);
            adapter.clearComment();
            adapter.notifyDataSetChanged();
            commentDivider.setVisibility(View.GONE);
            commentView.setVisibility(View.GONE);
        }

        setViewByArticleType(rob);
    }

    private void loadListData(ArticleDetailGetBean.ResultObjectBean rob) {
        adapter.clearComment();
        List<ArticleDetailGetBean.ResultObjectBean.CommentsBean> comments = rob.getComments();
        if (comments != null && comments.size() > 0) { // 有数据
            for (ArticleDetailGetBean.ResultObjectBean.CommentsBean cb : comments) {
                adapter.addComment(cb);
            }
            refreshListView.switchScrollLoadEnabled(true);
        } else {
            adapter.addEmptyView();
            refreshListView.switchScrollLoadEnabled(false);
        }
        adapter.notifyDataSetChanged();
    }

    private void setViewByArticleType(ArticleDetailGetBean.ResultObjectBean rob) {
        ArticleDetailGetBean.ResultObjectBean.ArticleBean ab = rob.getArticle();
        String content;
        switch (rob.getArticle().getContentType()) {
            case 0: // 文章
            {
                authorDivider.setVisibility(View.GONE);
                titleSpace.setVisibility(View.VISIBLE);
                mediaView.setVisibility(View.GONE);
                article.getSettings().setDefaultTextEncodingName("UTF-8");
                content = rob.getArticle().getContent() == null ? "" : rob.getArticle().getContent();
                if (isPaid) {
                    partArticleView.setVisibility(View.GONE);
                    article.setVisibility(View.VISIBLE);
                    article.loadData(content.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
                } else {
                    article.setVisibility(View.GONE);
                    partArticleView.setVisibility(View.VISIBLE);
                    partArticle.loadData(content.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
                }
            }
            break;
            case 2: // 视频
            {
                articleView.setVisibility(View.GONE);
                purchaseHintView.setVisibility(View.GONE);
                commentDivider.setVisibility(View.VISIBLE);
                mediaView.setVisibility(View.VISIBLE);
                commonMedia = new CommonMedia(this, getOption(2, rob));
                commonMedia.setOnAllCompleteListener(() -> {
                    imgRepeat.setVisibility(View.GONE);
                    if (ab.getType() == 0) {
                        imgShare.setVisibility(View.VISIBLE);
                        imgShare.setOnClickListener(v -> {
                            MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
                            showShare(rob);
                        });
                    }
                    if (isPaid) {
                        commonMedia.prepareAsync();
                    } else {
                        giv.setOnClickListener(null);
                        viewSVContainer.setOnClickListener(null);
                        viewOperate.setOnClickListener(null);
                        viewOperate.setVisibility(View.INVISIBLE);
                        viewLoading.setVisibility(View.VISIBLE);
                        viewLoadingText.setText("请您购买后视听");
                        try {
                            MediaServiceHelper.getService(this, MediaService::hardClose);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            break;
            case 3: // 文章+音频
            {
                mediaView.setVisibility(View.VISIBLE);
                article.getSettings().setDefaultTextEncodingName("UTF-8");
                commonMedia = new CommonMedia(this, getOption(1, rob));
                commonMedia.setOnAllCompleteListener(() -> {
                    imgRepeat.setVisibility(View.GONE);
                    if (ab.getType() == 0) {
                        imgShare.setVisibility(View.VISIBLE);
                        imgShare.setOnClickListener(v -> {
                            MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
                            showShare(rob);
                        });
                    }
                    if (isPaid) {
                        requestDrawOverLays();
                    } else {
                        giv.setOnClickListener(null);
                        viewSVContainer.setOnClickListener(null);
                        viewOperate.setOnClickListener(null);
                        viewOperate.setVisibility(View.INVISIBLE);
                        viewLoading.setVisibility(View.VISIBLE);
                        viewLoadingText.setText("请您购买后视听");
                        try {
                            MediaServiceHelper.getService(this, MediaService::hardClose);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                content = rob.getArticle().getContent() == null ? "" : rob.getArticle().getContent();
                if (isPaid) {
                    partArticleView.setVisibility(View.GONE);
                    article.setVisibility(View.VISIBLE);
                    article.loadData(content.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
                } else {
                    article.setVisibility(View.GONE);
                    partArticleView.setVisibility(View.VISIBLE);
                    partArticle.loadData(content.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
                }
            }
            break;
            case 4: // 文章+视频
            {
                mediaView.setVisibility(View.VISIBLE);
                article.getSettings().setDefaultTextEncodingName("UTF-8");
                commonMedia = new CommonMedia(this, getOption(2, rob));
                commonMedia.setOnAllCompleteListener(() -> {
                    imgRepeat.setVisibility(View.GONE);
                    if (ab.getType() == 0) {
                        imgShare.setVisibility(View.VISIBLE);
                        imgShare.setOnClickListener(v -> {
                            MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
                            showShare(rob);
                        });
                    }
                    if (isPaid) {
                        commonMedia.prepareAsync();
                    } else {
                        giv.setOnClickListener(null);
                        viewSVContainer.setOnClickListener(null);
                        viewOperate.setOnClickListener(null);
                        viewOperate.setVisibility(View.INVISIBLE);
                        viewLoading.setVisibility(View.VISIBLE);
                        viewLoadingText.setText("请您购买后视听");
                        try {
                            MediaServiceHelper.getService(this, MediaService::hardClose);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                content = rob.getArticle().getContent() == null ? "" : rob.getArticle().getContent();
                if (isPaid) {
                    partArticleView.setVisibility(View.GONE);
                    article.setVisibility(View.VISIBLE);
                    article.loadData(content.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
                } else {
                    article.setVisibility(View.GONE);
                    partArticleView.setVisibility(View.VISIBLE);
                    partArticle.loadData(content.replace("<img", "<img style=\"width:100%;\""), "text/html; charset=UTF-8", null);
                }
            }
            break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReload) {
            isReload = false;
            if (commonMedia != null) {
                commonMedia.hardClose();
            }
            onCreate(savedInstanceState);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaServiceHelper.getService(getApplicationContext(), MediaService::backGroundHide);
    }

    private void bindEvent(ArticleDetailGetBean.ResultObjectBean rob) {
        if (rob.getArticle().getContentType() != 0) {
            refreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    float headerViewScrollHeight = -headerView.getTop();
                    int mediaViewHeight = DensityUtil.dip2px(getApplicationContext(), 210 - 56);
                    if (headerViewScrollHeight <= mediaViewHeight) {
                        titleBar.setAlpha(0);
                    } else {
                        int noTransparentHeight = DensityUtil.dip2px(getApplicationContext(), 57);
                        float alphaValue = (headerViewScrollHeight - mediaViewHeight) / noTransparentHeight;
                        titleBar.setAlpha(alphaValue > 1 ? 1 : alphaValue);
                    }
                }
            });
        }

        ArticleDetailGetBean.ResultObjectBean.ArticleBean ab = rob.getArticle();
        back.setOnClickListener(v -> finish());
        // 文章分享
        share.setOnClickListener(v -> {
            MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
            showShare(rob);
        });
//        category.setOnClickListener(v -> {
//            Intent intent = new Intent(this, ShareListActivity.class);
//            startActivity(intent);
//        });
        purchase.setOnClickListener(v -> {
            if (!validateLogin(rob.getArticle().getId())) {
                return;
            }

            PayInfoDao payInfoDao = new PayInfoDao(this);
            PayInfo payInfo = null;
            try {
                payInfo = payInfoDao.queryCourseId(2, ab.getId());
                if (payInfo != null) {
                    payInfoDao.delete(payInfo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(v.getContext(), PayActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("courseId", ab.getId());
            intent.putExtra("courseName", ab.getTitle());
            intent.putExtra("price", ab.getPrice());
            v.getContext().startActivity(intent);
            isReload = true;
        });

        collection.setOnClickListener(v -> {
            if (!validateLogin(rob.getArticle().getId())) {
                return;
            }

            if (!isFavorite) { // 收藏
                // 传入的bean类需要有无参构造方法
                new RequestBuilder<>(JSONObject.class).getRequest("favorite", new String[]{COLLECTION_TYPE, String.valueOf(ab.getId())}, TAG, this, jsonObject -> {
                    collectionLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                    collectionLabel.setText("已收藏");
                    collection.setBackgroundResource(R.mipmap.yishoucang);
                    ToastUtil.getToast(getApplicationContext(), "收藏成功", "center", 0, 180).show();
                    isFavorite = true;
                });
            } else { // 取消收藏
                new RequestBuilder<>(JSONObject.class).getRequest("favoriteDel", new String[]{COLLECTION_TYPE, String.valueOf(ab.getId())}, TAG, this, jsonObject -> {
                    collectionLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                    collectionLabel.setText("收藏");
                    collection.setBackgroundResource(R.mipmap.shoucang);
                    ToastUtil.getToast(getApplicationContext(), "取消收藏成功", "center", 0, 180).show();
                    isFavorite = false;
                });
            }
        });

        addComment.setOnClickListener(v -> {
            if (!validateLogin(rob.getArticle().getId())) return;
            if (!isPaid) {
                ToastUtil.getToast(getApplicationContext(), "请您购买后评论", "center", 0, 180).show();
                return;
            }

            MediaServiceHelper.getService(this, MediaService::backGroundHide);
            showPopupWindow(rob);
        });

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                List<Comment> datalist = adapter.getDatalist();
                // 全屏、画面初期化异常(没有数据)、一开始没有数据时不发送请求
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                        || datalist.size() == 0
                        || (datalist.size() == 1 && datalist.get(0).isEmpty())) { // 一开始没有数据
                    refreshListView.onRefreshComplete();
                    return;
                }

                loadCommentList(ab.getId(), datalist);
            }
        });
    }

    private void loadCommentList(int articleId, List<Comment> datalist) {
        RequestBuilder<CommentGetBean> builder = new RequestBuilder<>(CommentGetBean.class);
        builder.setOnSystemErrorListener(() -> {
            refreshListView.onRefreshComplete();
        });
        builder.setOnBusinessErrorListener(() -> {
            refreshListView.onRefreshComplete();
        });
        // 如果该次请求中包含之前没有加载过的数据(id判断)，把这次请求中未加载过的数据加载出来，停止请求
        // 如果该次请求中都是加载过的数据(id判断)，继续发送请求
        // 当本次请求中没有数据时，认为已经加载到底部，停止发送请求
        builder.getRequest("commentList", new String[]{String.valueOf(articleId), "1", String.valueOf(page)}, TAG, this, bean -> {
            List<CommentGetBean.ResultObjectBean.CommentsBean> comments = bean.getResultObject().getComments();
            if (comments != null && comments.size() > 0) {
                page++;
                int lastCommentId = datalist.get(datalist.size() - 1).getId();
                boolean hasData = false;
                for (CommentGetBean.ResultObjectBean.CommentsBean cb : comments) {
                    if (cb.getId() < lastCommentId) {
                        adapter.addComment(cb);
                        hasData = true;
                    }
                }
                if (hasData) {
                    adapter.notifyDataSetChanged();
                } else {
                    loadCommentList(articleId, datalist);
                    return;
                }
            } else {
                refreshListView.setHasMoreData(false);
                return;
            }

            refreshListView.onRefreshComplete();
        }, throwable -> {
            ToastUtil.getToast(this, getString(R.string.req_fail_msg), "center", 0, 180).show();
            refreshListView.onRefreshComplete();
        });
    }

    private boolean validateLogin(int articleId) {
        if (!isLogin()) {
            startActivity(IntentUriUtil.getIntent("l1", "k1_9," + articleId));
            finish();
            return false;
        }

        return true;
    }

    private boolean isLogin() {
        return SharedPreferencesUtil.readToken(this) != null && !SharedPreferencesUtil.readToken(this).equals("");
    }

    /**
     * 软键盘是否弹出
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isSoftInputShowing() {
        // 当前屏幕总高度
        int totalHeight = getWindow().getDecorView().getHeight();
        // 可见区域高度
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int visibleHeight = rect.bottom;
        // 部分手机下部的虚拟键盘bar高度
        int virtualBtnBarHeight = getSoftButtonBarHeight();

        return totalHeight - visibleHeight - virtualBtnBarHeight > 0;
    }

    /**
     * 底部虚拟按键栏高度
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        // 非屏幕真实高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        // 屏幕真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (Build.VERSION.SDK_INT >= 17) {
            if (!isSoftInputShowing()) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        }
    }

    private void showPopupWindow(ArticleDetailGetBean.ResultObjectBean rob) {
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputSize.setText(String.valueOf(s.length()));
                tmpComment = s.toString();
            }
        });
        // 追加评论
        View addCommentBtn = popView.findViewById(R.id.btn_add_comment);
        addCommentBtn.setOnClickListener(v -> {
            String commentText = etComment.getText().toString();
            if ("".equals(commentText.trim())) {
                ToastUtil.getToast(v.getContext(), "评论不能为空", "center", 0, 180).show();
                return;
            }

            addCommentBtn.setClickable(false);
            new Handler().postDelayed(() -> addCommentBtn.setClickable(true), 3000);
            new RequestBuilder<>(JSONObject.class).postRequest("comment", new String[]{String.valueOf(rob.getArticle().getId()), "1", commentText}, TAG, this, jsonObject -> {
                new RequestBuilder<>(ArticleDetailGetBean.class).getRequest("articleDetail", new String[]{String.valueOf(rob.getArticle().getId())}, TAG, this, bean -> {
                    loadListData(bean.getResultObject());
                    refreshListView.switchScrollLoadEnabled(true);
                    page = 2;
                });

                ToastUtil.getToast(v.getContext(), "评论已追加", "center", 0, 180).show();
                popupWindow.dismiss();
                tmpComment = null;
            }, throwable -> {
                ToastUtil.getToast(v.getContext(), "追加失败，请检查网络", "center", 0, 180).show();
                popupWindow.dismiss();
                tmpComment = null;
            });
        });

        popupWindow.setOnDismissListener(() -> main.removeOnLayoutChangeListener(this));

        if (tmpComment != null) {
            etComment.setText(tmpComment);
            etComment.setSelection(tmpComment.length());
        } else {
            etComment.setText("");
        }

        // popupwindow要等主画面加载完才能显示，异步可以确保主画面加载完成后调用显示
        new Handler().post(() -> popupWindow.showAtLocation(main, Gravity.BOTTOM, 0, 0));
        // 要等到popupwindow画面加载完成后软键盘showSoftInput才有效
        new Handler().postDelayed(() -> {
            etComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etComment, 0);
            }

            // 确保软键盘弹出以后才设置监听
            new Handler().postDelayed(() -> main.addOnLayoutChangeListener(this), 1000);
        }, SOFT_INPUT_SHOW_DELAY);
    }

    private boolean noMoreData;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (commonMedia != null) {
            commonMedia.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
                contentView.setVisibility(View.GONE);
                adapter.hideCommentList();
                if (isFree) {
                    imgShare.setVisibility(View.GONE);
                }
                titleBar.setVisibility(View.GONE);
                whiteBackView.setVisibility(View.GONE);
//                refreshListView.setScrollLoadEnabled(false); // 屏幕切换时设定会引起layout异常
                noMoreData = refreshListView.getFooterLoadingLayout().getState() == ILoadingLayout.State.NO_MORE_DATA;
                refreshListView.switchScrollLoadEnabled(false);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // 竖屏
                contentView.setVisibility(View.VISIBLE);
                adapter.showCommentList();
                if (isFree) {
                    imgShare.setVisibility(View.VISIBLE);
                }
                titleBar.setVisibility(View.VISIBLE);
                whiteBackView.setVisibility(View.VISIBLE);
//                refreshListView.setScrollLoadEnabled(true);
                List<Comment> comments = adapter.getDatalist();
                if (comments.size() > 1 || !comments.get(0).isEmpty()) {
                    refreshListView.switchScrollLoadEnabled(true);
                    if (noMoreData) {
                        refreshListView.setHasMoreData(false);
                    }
                }
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
    protected void onDestroy() {
        super.onDestroy();
        if (commonMedia != null) {
            commonMedia.close();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    /**
     * 取得媒体数据所需参数
     *
     * @return
     */
    private CommonMediaOption getOption(int type, ArticleDetailGetBean.ResultObjectBean rob) {
        CommonMediaOption option = new CommonMediaOption();
        option.setId(-1);
        option.setShowCourseList(false);
        Course course = new Course();
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setTitle("");
        course.setLesson(lesson);
        option.setCourse(course);

        List<Media> audios;
        List<Media> videos;
        List<ArticleDetailGetBean.ResultObjectBean.ArticleBean.AudioListBean> audiosList;
        List<ArticleDetailGetBean.ResultObjectBean.ArticleBean.VideoListBean> videosList;
        switch (type) {
            case 1: // 音频
                option.setType(CommonMediaOption.MUSIC_ONLY);
                audios = new ArrayList<>();
                lesson.setAudioList(audios);
                audiosList = rob.getArticle().getAudioList();
                if (audiosList != null && audiosList.size() > 0) {
                    for (ArticleDetailGetBean.ResultObjectBean.ArticleBean.AudioListBean ab : audiosList) {
                        Media audio = new Media();
                        audio.setVid(Integer.valueOf(ab.getVid()));
                        audio.setVideoName(ab.getVideoName());
                        audio.setOrigUrl(ab.getOrigUrl());
                        audios.add(audio);
                    }
                    option.setMediaID(audios.get(0).getVid());
                }
                lesson.setCourseId(Integer.valueOf(articleId) * -1);
                break;
            case 2: // 视频
                option.setType(CommonMediaOption.VIDEO_ONLY);
                videos = new ArrayList<>();
                lesson.setVideoList(videos);
                videosList = rob.getArticle().getVideoList();
                if (videosList != null && videosList.size() > 0) {
                    for (ArticleDetailGetBean.ResultObjectBean.ArticleBean.VideoListBean vb : videosList) {
                        Media video = new Media();
                        video.setVid(Integer.valueOf(vb.getVid()));
                        video.setVideoName(vb.getVideoName());
                        video.setOrigUrl(vb.getOrigUrl());
                        videos.add(video);
                    }
                    option.setMediaID(videos.get(0).getVid());
                }
                break;
        }

        return option;
    }

    /**
     * 打开分享页面
     *
     * @param rob
     */
    private void showShare(ArticleDetailGetBean.ResultObjectBean rob) {
        ArticleDetailGetBean.ResultObjectBean.ArticleBean ab = rob.getArticle();
        String author = ab.getAuthor();
        String title = ab.getTitle() == null ? "" : ab.getTitle();
        String thumb = ab.getThumb();
        String shareContent = Constant.URL_SHARE_BASE + ab.getId();

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 不显示腾讯微博
        oks.addHiddenPlatform("TencentWeibo");
        oks.setShareContentCustomizeCallback((platform, paramsToShare) -> {
            switch (platform.getName()) {
                case "SinaWeibo":
                    paramsToShare.setText(String.format(getString(R.string.sina_weibo_share_content), title, author, shareContent));
                    if (thumb != null && !"".equals(thumb)) {
                        paramsToShare.setImageUrl(thumb);
                    } else {
                        paramsToShare.setImagePath(getImagePath());
                    }
                    break;
                case "Wechat":
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setTitle(title);
                    paramsToShare.setText(author);
                    paramsToShare.setUrl(shareContent);
                    if (thumb != null && !"".equals(thumb)) {
                        paramsToShare.setImageUrl(thumb);
                    } else {
                        paramsToShare.setImagePath(getImagePath());
                    }
                    break;
                case "WechatMoments":
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setTitle(title);
                    paramsToShare.setUrl(shareContent);
                    if (thumb != null && !"".equals(thumb)) {
                        paramsToShare.setImageUrl(thumb);
                    } else {
                        paramsToShare.setImagePath(getImagePath());
                    }
                    break;
                case "QQ":
                    paramsToShare.setTitle(title);
                    paramsToShare.setTitleUrl(shareContent);
                    paramsToShare.setText(author);
                    if (thumb != null && !"".equals(thumb)) {
                        paramsToShare.setImageUrl(thumb);
                    } else {
                        // 貌似只能放在sd卡上
                        paramsToShare.setImagePath(getImagePath());
                    }
                    break;
            }
        });
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtil.getToast(ShareArticleActivity.this, getString(R.string.ssdk_oks_share_completed), "center", 0, 180).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtil.getToast(ShareArticleActivity.this, getString(R.string.ssdk_oks_share_failed), "center", 0, 180).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtil.getToast(ShareArticleActivity.this, getString(R.string.ssdk_oks_share_canceled), "center", 0, 180).show();
            }
        });

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 获取默认分享图标
     *
     * @return
     */
    private String getImagePath() {
        String IMAGE_NAME = "default_share_icon.png";
        InputStream is = null;
        OutputStream os = null;
        AssetManager am = getResources().getAssets();
        File dir = getExternalCacheDir();
        if (dir == null) {
            dir = getCacheDir();
        }
        String imagePath = dir.getAbsolutePath() + File.separator + IMAGE_NAME;
        File file = new File(imagePath);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        // 把assets下面的图标拷贝到应用本地路径下面
        try {
            is = am.open(IMAGE_NAME);
            os = new FileOutputStream(file);
            byte[] read = new byte[1024];
            int len;
            while ((len = is.read(read)) > 0) {
                os.write(read, 0, len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imagePath;
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
            if (!Settings.canDrawOverlays(this)) { // SYSTEM_ALERT_WINDOW permission not granted...
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
}
