package com.pointhouse.chiguan.k1_14;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.BroadcastActivityBase;
import com.pointhouse.chiguan.common.jsonObject.ArticleListGetBean;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.k1.ArticleItemAdapter;
import com.pointhouse.chiguan.k1_9.ShareArticleActivity;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Call;

/**
 * Created by ljj on 2017/8/31.
 */

public class ShareListActivity extends BroadcastActivityBase implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>{
    PullToRefreshListView viewShareList;
    private ListView mListView;
    ArticleListGetBean shareListGetBean;
    ArticleItemAdapter adapter;
    private int page = 1;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_14_activity_sharelist);
        viewShareList = (PullToRefreshListView) findViewById(R.id.viewShareList);
        viewShareList.setPullLoadEnabled(false);
        viewShareList.setScrollLoadEnabled(true);
        viewShareList.setOnRefreshListener(this);
        mListView = viewShareList.getRefreshableView();
        adapter = new ArticleItemAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put("").put("").put(String.valueOf(page));
        getAsynHttp(Constant.URL_BASE +"articleList",jsonArray.toString(),null,1);

        LinearLayout backbtnll = (LinearLayout)findViewById(R.id.findBackBtnView);
        backbtnll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //当前页面销毁
                finish();
                //创建一个事件
                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK);
                //设置activity监听此事件，返回true只监听一次，如果返回false则继续监听
                onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
            }
        });
        Button backbtn = (Button)findViewById(R.id.findBackBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //当前页面销毁
                finish();
                //创建一个事件
                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK);
                //设置activity监听此事件，返回true只监听一次，如果返回false则继续监听
                onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
            }
        });
    }

    @Override
    public void getAsynHttpResponse(Call call, String json, int flg, int error) {
        if(error == -1) {
            viewShareList.onRefreshComplete();
            return;
        }
        shareListGetBean = new ArticleListGetBean(json);
        if (shareListGetBean.gethasError()) return;
        updateAllDate(flg);
    }

    private void updateAllDate(int flg){
        if(flg == 1){
            reload();
        } else if( flg== 2){
            reloadUp();
        }
    }

    private void reload() {
        adapter.clean();
        loadDate();
        if(adapter.getCount() == 0){
            View listEmptyView = View.inflate(this, R.layout.common_empty,
                    (ViewGroup) mListView.getParent());
            mListView.setEmptyView(listEmptyView);
        }
        adapter.notifyDataSetChanged();
        viewShareList.onRefreshComplete();
    }

    private void reloadUp() {
        loadDate();
        adapter.notifyDataSetChanged();
        viewShareList.onRefreshComplete();
    }

    public void loadDate() {
        List<ArticleListGetBean.ResultObjectBean.ArticlesBean> shareBeanList = shareListGetBean.getArticlesBeanList();
        for(ArticleListGetBean.ResultObjectBean.ArticlesBean shareBean : shareBeanList){
            adapter.addArticle(shareBean.getCategoryId(),
                    shareBean.getCategoryTitle(),
                    shareBean.getId(),
                    shareBean.getContentType(),
                    shareBean.getPrice(),
                    shareBean.getBuyersNum(),
                    shareBean.getReading(),
                    shareBean.getThumb(),
                    shareBean.getTitle(),
                    shareBean.getType());
        }
    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleItemAdapter.ArticleItem articleItem = (ArticleItemAdapter.ArticleItem)parent.getAdapter().getItem(position);

        Intent it = new Intent(view.getContext(), ShareArticleActivity.class);
        it.putExtra("articleId",String.valueOf(articleItem.id));
        view.getContext().startActivity(it);
    }

    @Override
    public void onNetChange(int netMobile) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                page = 1;

                JSONArray jsonArray = new JSONArray();
                jsonArray.put("").put("").put(String.valueOf(page));

                getAsynHttp(Constant.URL_BASE +"articleList",jsonArray.toString(),null,1);
            }
        }, 1000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                page ++;

                JSONArray jsonArray = new JSONArray();
                jsonArray.put("").put("").put(String.valueOf(page));

                getAsynHttp(Constant.URL_BASE +"articleList",jsonArray.toString(),null,2);
            }
        }, 1000);
    }


}
