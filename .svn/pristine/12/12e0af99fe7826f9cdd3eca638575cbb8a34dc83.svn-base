package com.pointhouse.chiguan.w1_2;

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
import com.pointhouse.chiguan.common.base.HttpActivityBase;
import com.pointhouse.chiguan.common.jsonObject.MyGroupListBean;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static com.pointhouse.chiguan.common.util.SharedPreferencesUtil.readToken;

/**
 * Created by ljj on 2017/9/4.
 */

public class MyGroupActivity extends HttpActivityBase implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>{
    PullToRefreshListView viewGroupList;
    private ListView mListView;
    MyGroupAdapter adapter;
    MyGroupListBean myGroupListBean;
    String token = "";
    Disposable timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_2_activity_mygroup);
        viewGroupList = (PullToRefreshListView) findViewById(R.id.viewgroupList);
        viewGroupList.setPullLoadEnabled(false);
        viewGroupList.setScrollLoadEnabled(false);
        viewGroupList.setOnRefreshListener(this);
        mListView = viewGroupList.getRefreshableView();
        adapter = new MyGroupAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        token = readToken(this);
        getAsynHttp(Constant.URL_BASE +"myGroupList","",token,1);

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

        timer = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> adapter.notifyDataSetChanged(), Throwable::printStackTrace);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onDestroy(){
       super.onDestroy();
       if (timer != null && !timer.isDisposed()) {
           timer.dispose();
       }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getAsynHttp(Constant.URL_BASE +"myGroupList","",token,1);
            }
        }, 1000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void getAsynHttpResponse(Call call, String json, int flg, int error) {
        if(error == -1) {
            viewGroupList.onRefreshComplete();
            return;
        }
        myGroupListBean = new MyGroupListBean(json);
        if (myGroupListBean.gethasError()) return;
        reload();
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
        viewGroupList.onRefreshComplete();
    }

    public void loadDate() {
        List<MyGroupListBean.ResultObjectBean.GroupsBean> groupsBeanList = myGroupListBean.getGroupsBeanList();
        for(MyGroupListBean.ResultObjectBean.GroupsBean groupsBean : groupsBeanList){
            adapter.addGroup(groupsBean.getTid(), groupsBean.getId() ,groupsBean.getThumb(),groupsBean.getTitle(),groupsBean.getMemberCount()+"/"+groupsBean.getMaxCount());
        }
    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {

    }
}
