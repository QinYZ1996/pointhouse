package com.pointhouse.chiguan.k1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.BroadcastFragmentBase;
import com.pointhouse.chiguan.common.jsonObject.IndexIinitGetBean;
import com.pointhouse.chiguan.common.updatelayout.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatelayout.PullToRefreshScrollView;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.k1_1.CourseListActivity;
import com.pointhouse.chiguan.k1_14.ShareListActivity;
import com.pointhouse.chiguan.k1_9.ShareArticleActivity;

import java.util.List;

import okhttp3.Call;

/**
 * bbbb
 * Created by ljj on 2017/7/3.
 * aaaa
 */

public class CourseClassifiCationActivity extends BroadcastFragmentBase implements AdapterView.OnItemClickListener {

    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    ListView viewCourseList,viewArticleList;
    CourseItemAdapter adapter;
    ArticleItemAdapter articleAdapter;
    View view;
    SlideShowView slideShowView;
    IndexIinitGetBean indexIinitGetBean;
    Context context;

    public CourseClassifiCationActivity() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.k1_activity_courselist, container, false);
        slideShowView = (SlideShowView)view.findViewById(R.id.slideshowView);
        context = getContext();
        viewCourseList = (ListView) view.findViewById(R.id.viewCourseList);
        viewCourseList.setFocusable(false);
        adapter = new CourseItemAdapter(getContext(), viewCourseList);
        viewCourseList.setAdapter(adapter);
        viewCourseList.setOnItemClickListener(this);
        ViewGroup.LayoutParams layoutParams = viewCourseList.getLayoutParams();
        layoutParams.height = DensityUtil.dip2px(context, 304);
        viewCourseList.setLayoutParams(layoutParams);

        viewArticleList = (ListView) view.findViewById(R.id.viewArticleList);
        viewArticleList.setFocusable(false);
        articleAdapter = new ArticleItemAdapter(getContext());
        viewArticleList.setAdapter(articleAdapter);
        viewArticleList.setOnItemClickListener(this);
        ViewGroup.LayoutParams layoutParams1 = viewArticleList.getLayoutParams();
        layoutParams1.height = DensityUtil.dip2px(context, 304);
        viewArticleList.setLayoutParams(layoutParams1);

        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        getAsynHttp(Constant.URL_BASE +"indexIinit","",null,1);


        RelativeLayout imageView = (RelativeLayout) view.findViewById(R.id.RelativeLayout);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CourseListActivity.class);
                intent.putExtra("title", "精选课程");
                startActivity(intent);
            }
        });

        RelativeLayout imageView10 = (RelativeLayout) view.findViewById(R.id.RelativeLayout10);
        imageView10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShareListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void getAsynHttpResponse(Call call, String json,int flg,int error) {
        if(error == -1) {
            mPullRefreshScrollView.onRefreshComplete();
            return;
        }
        indexIinitGetBean = new IndexIinitGetBean(json);
        if (indexIinitGetBean.gethasError()) return;
        updateAllDate(flg);
    }

    @Override
    public void postAsynHttpResponse(Call call, String json,int flg) {

    }

    private void reloadTable(){
        TableRow tableRow  = (TableRow)view.findViewById(R.id.courseTableRow);
        tableRow.removeAllViews();
        List<IndexIinitGetBean.ResultObjectBean.CourseCategoryBean> courseCategoryBeanList = indexIinitGetBean.getCourseCategoryList();
        CourseTableAdapter.setView(context,courseCategoryBeanList,tableRow);
    }

    private void reload() {
        adapter.clean();
        loadDate();
        adapter.notifyDataSetChanged();
    }

    public void loadDate() {
        List<IndexIinitGetBean.ResultObjectBean.RecomenedCourseBean> recomenedCourseBeanList = indexIinitGetBean.getRecomenedCourseList();
        ViewGroup.LayoutParams layoutParams = viewCourseList.getLayoutParams();
        int count = recomenedCourseBeanList.size() <= 3 ? recomenedCourseBeanList.size() : 3;
        float height = 101.5f * count;
        layoutParams.height = DensityUtil.dip2px(context, height);
        viewCourseList.setLayoutParams(layoutParams);
        for(IndexIinitGetBean.ResultObjectBean.RecomenedCourseBean recomenedCourseBean : recomenedCourseBeanList) {
            adapter.addCourse(recomenedCourseBean.getId(),recomenedCourseBean.getThumb(),recomenedCourseBean.getTitle(),recomenedCourseBean.getLessonType(),
                    recomenedCourseBean.getCategoryTitle(),recomenedCourseBean.getPrice(),recomenedCourseBean.getBuyersNum(),recomenedCourseBean.getType());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() instanceof CourseItemAdapter) {
            CourseItemAdapter.CourseItem courseItem = (CourseItemAdapter.CourseItem) parent.getAdapter().getItem(position);

            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + String.valueOf(courseItem.id)));
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(it);
        } else if(parent.getAdapter() instanceof ArticleItemAdapter){
            ArticleItemAdapter.ArticleItem articleItem = (ArticleItemAdapter.ArticleItem) parent.getAdapter().getItem(position);

            Intent it = new Intent(view.getContext(), ShareArticleActivity.class);
            it.putExtra("articleId",String.valueOf(articleItem.id));
            view.getContext().startActivity(it);
        }
    }

    private void updateAllDate(int flg){
        if(flg == 1){
            reload();
            reloadArticle();
            reloadTable();
            slideShowView.reload(context,indexIinitGetBean);
            mPullRefreshScrollView.onRefreshComplete();
        }
    }

    private void reloadArticle() {
        articleAdapter.clean();
        loadArticleDate();
        articleAdapter.notifyDataSetChanged();
    }

    public void loadArticleDate() {
        List<IndexIinitGetBean.ResultObjectBean.RecomenedArticleBean> recomenedArticleList = indexIinitGetBean.getRecomenedArticleList();
        if(recomenedArticleList != null){
            ViewGroup.LayoutParams layoutParams = viewArticleList.getLayoutParams();
            int count = recomenedArticleList.size() <= 3 ? recomenedArticleList.size() : 3;
            float height = 101.5f * count;
            layoutParams.height = DensityUtil.dip2px(context, height);
            viewArticleList.setLayoutParams(layoutParams);
            for(IndexIinitGetBean.ResultObjectBean.RecomenedArticleBean recomenedArticle : recomenedArticleList) {
                articleAdapter.addArticle(recomenedArticle.getCategoryId(),
                        recomenedArticle.getCategoryTitle(),
                        recomenedArticle.getId(),
                        recomenedArticle.getContentType(),
                        recomenedArticle.getPrice(),
                        recomenedArticle.getBuyersNum(),
                        recomenedArticle.getReading(),
                        recomenedArticle.getThumb(),
                        recomenedArticle.getTitle(),
                        recomenedArticle.getType());
            }
        }
    }

    @Override
    public void onNetChange(int netMobile) {
//        if(netMobile == 0){
//            Toast.makeText(getActivity().getApplicationContext(), "无网络连接" , Toast.LENGTH_SHORT).show();
//        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            getAsynHttp(Constant.URL_BASE +"indexIinit","",null,1);
            super.onPostExecute(result);
        }
    }
}
