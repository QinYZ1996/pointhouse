package com.pointhouse.chiguan.w1_1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.http.RequestBuilder;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.jsonObject.MyOrderListGetBean;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.k1_9.ShareArticleActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-sunjb on 2017/6/26.
 */

public class MyPurchaseActivity extends MediaBaseActivity {

    private static final String TAG = "W1-1";

    private PullToRefreshListView refreshListView;
    private MyPurchaseItemAdapter adapter;
    private int page = 0;
    private Set<Integer> courseIds = new HashSet<>();
    private Set<Integer> articleIds = new HashSet<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_1_activity_mypurchase);

        initListView();
    }

    public void Pur_back(View view) {
        super.onBackPressed();
    }

    public void initListView() {
        adapter = new MyPurchaseItemAdapter(this);
        refreshListView = (PullToRefreshListView) findViewById(R.id.mypurchaseList);
        refreshListView.getRefreshableView().setAdapter(adapter);
        refreshListView.setPullRefreshEnabled(false);
        // setPullLoadEnabled(true) => 数据非满屏时，最后一行下面总会有分割线，但只有上拉到画面最后才会加载，所以不会加载数据，但上拉加载只有一行数据不滑动不会显示
        refreshListView.setPullLoadEnabled(false);
        // setScrollLoadEnabled(true) => 数据非满屏时，最后一行下面不会有分割线，但会加载数据，上下拉都会加载，直到满屏时上拉才加载
        refreshListView.setScrollLoadEnabled(true);
        refreshListView.getRefreshableView().setOnItemClickListener(((parent, view, position, id) -> {
            MyPurchase item = adapter.getItemList().get(position);
            if (item.isDate()) return;

            Intent intent;
            Context context = view.getContext();
            if (item.getDataType() == 0) { // 课程
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + String.valueOf(item.getId())));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else { // 文章
                intent = new Intent(this, ShareArticleActivity.class);
                intent.putExtra("articleId", String.valueOf(item.getId()));
                context.startActivity(intent);
            }
        }));

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestGetMyPurchaseList(false);
            }
        });

        requestGetMyPurchaseList(true);
    }

    private void requestGetMyPurchaseList(boolean isInit) {
        new RequestBuilder<>(MyOrderListGetBean.class).getRequest("myOrderList", new String[]{String.valueOf(++page)}, TAG, this, bean -> {
            ListView listView = refreshListView.getRefreshableView();
            List<MyOrderListGetBean.ResultObjectBean> resultObjectBeans = bean.getResultObject();
            boolean dateChecked = false;
            for (MyOrderListGetBean.ResultObjectBean rob : resultObjectBeans) {
                List<MyOrderListGetBean.ResultObjectBean.CourseBean> coursesBeans = rob.getCourse();
                List<MyOrderListGetBean.ResultObjectBean.ArticleBean> articleBeans = rob.getArticle();
                boolean hasCourses = coursesBeans != null && coursesBeans.size() > 0;
                boolean hasArticles = articleBeans != null && articleBeans.size() > 0;
                // check此次请求是否包含上次请求的数据，如果包含，则不追加
                boolean canLoad = true;
                if(hasCourses) {
                    for (MyOrderListGetBean.ResultObjectBean.CourseBean cb : coursesBeans) {
                        if (courseIds.contains(cb.getId())) {
                            canLoad = false;
                            break;
                        }
                    }
                }
                if (!canLoad) {
                    break;
                }
                if(hasArticles) {
                    for (MyOrderListGetBean.ResultObjectBean.ArticleBean ab : articleBeans) {
                        if (articleIds.contains(ab.getId())) {
                            canLoad = false;
                            break;
                        }
                    }
                }
                if (!canLoad) {
                    break;
                }

                // 该日期不等于已加载数据最早的日期，则追加日期行
                if (isInit || dateChecked || !adapter.getEarliestDate().equals(rob.getDate())) {
                    if (hasCourses || hasArticles) {
                        adapter.addDate(rob.getDate());
                    }
                }

                if (!dateChecked) dateChecked = true;

                // 课程
                if (hasCourses) {
                    for (MyOrderListGetBean.ResultObjectBean.CourseBean cb : coursesBeans) {
                        courseIds.add(cb.getId());
                        adapter.addCourse(cb);
                    }
                }

                // 文章
                if (hasArticles) {
                    for (MyOrderListGetBean.ResultObjectBean.ArticleBean ab : articleBeans) {
                        articleIds.add(ab.getId());
                        adapter.addArticle(ab);
                    }
                }
            }

            if (adapter.getCount() == 0) {
                View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) listView.getParent());
                listView.setEmptyView(listEmptyView);
            }

            adapter.notifyDataSetChanged();

            if (!isInit) {
                refreshListView.onRefreshComplete();
            }
        });
    }
}
