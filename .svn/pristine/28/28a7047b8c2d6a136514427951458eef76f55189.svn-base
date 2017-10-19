package com.pointhouse.chiguan.k1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.HttpFragmentBase;
import com.pointhouse.chiguan.common.jsonObject.TeacherListGetBean;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.k1_8.TeacherDetailActivity;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Call;

/**
 * Created by ljj on 2017/7/3.
 */

public class PhTeacherListActivity extends HttpFragmentBase implements PullToRefreshBase.OnRefreshListener<ListView>, AdapterView.OnItemClickListener {
    private PullToRefreshListView viewTeacherList;
    private TeacherItemAdapter adapter;
    private ListView mListView;
    private int pageint = 1;
    private TeacherListGetBean teacherListGetBean;
    public PhTeacherListActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.k1_activity_teacherintroduction, container, false);
        viewTeacherList = (PullToRefreshListView) view.findViewById(R.id.refreshlistview);
        viewTeacherList.setPullLoadEnabled(false);
        viewTeacherList.setScrollLoadEnabled(true);
        viewTeacherList.setOnRefreshListener(this);
        mListView = viewTeacherList.getRefreshableView();

        adapter = new TeacherItemAdapter(getContext(), mListView);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        JSONArray page = new JSONArray();
        page.put(String.valueOf(pageint));
        getAsynHttp(Constant.URL_BASE +"teacherList",page.toString(),null,1);
        return view;
    }

    @Override
    public void getAsynHttpResponse(Call call, String json,int flg,int error) {
        if(error == -1) {
            viewTeacherList.onRefreshComplete();
            return;
        }
        teacherListGetBean = new TeacherListGetBean(json);
        if(teacherListGetBean.gethasError()) return;
        if(flg == 1) {
            reload();
        } else if(flg == 2){
            reloadUp();
        }

    }

    @Override
    public void postAsynHttpResponse(Call call, String json,int flg) {

    }

    private void reload() {
        adapter.clean();
        loadDate();
        if(adapter.getCount() == 0){
            View listEmptyView = View.inflate(getContext(), R.layout.common_empty,
                    (ViewGroup) mListView.getParent());
            mListView.setEmptyView(listEmptyView);
        }
        adapter.notifyDataSetChanged();
        viewTeacherList.onRefreshComplete();
    }

    private void reloadUp() {
        loadDate();
        if(adapter.getCount() == 0){
            mListView.setBackgroundResource(R.drawable.nodata);
        }else{
            mListView.setBackgroundResource(0);
        }
        adapter.notifyDataSetChanged();
        viewTeacherList.onRefreshComplete();
    }

    public void loadDate() {
        List<TeacherListGetBean.ResultObjectBean.TeachersBean> teachersBeanList = teacherListGetBean.getTeacherList();
        for(TeacherListGetBean.ResultObjectBean.TeachersBean teachersBean : teachersBeanList) {
            adapter.addTeacher(teachersBean.getAvatar(), teachersBean.getSex(), teachersBean.getNickname(), teachersBean.getCountry(), teachersBean.getId());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TeacherItemAdapter.TeacherItem teacherItem = (TeacherItemAdapter.TeacherItem)parent.getAdapter().getItem(position);
        Intent intent = new Intent(view.getContext(), TeacherDetailActivity.class);
        intent.putExtra("id",teacherItem.id);
        view.getContext().startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pageint = 1;
                JSONArray page = new JSONArray();
                page.put(String.valueOf(pageint));
                getAsynHttp(Constant.URL_BASE +"teacherList",page.toString(),null,1);
            }
        }, 1000);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pageint++;

                JSONArray page = new JSONArray();
                page.put(String.valueOf(pageint));
                getAsynHttp(Constant.URL_BASE +"teacherList",page.toString(),null,2);
            }
        }, 1000);
    }
}
