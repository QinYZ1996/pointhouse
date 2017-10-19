package com.pointhouse.chiguan.k1_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.BroadcastActivityBase;
import com.pointhouse.chiguan.common.jsonObject.CourseListGetBean;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.k1.CourseItemAdapter;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Call;

/**
 * Created by ljj on 2017/7/13.
 */

public class CourseListActivity extends BroadcastActivityBase implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>{
    PullToRefreshListView viewCourseList;
    private ListView mListView;
    CourseListGetBean courseListGetBean;
    CourseItemAdapter adapter;
    private int page = 1;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_1_activity_courselist);
        viewCourseList = (PullToRefreshListView) findViewById(R.id.viewCourseList);
        viewCourseList.setPullLoadEnabled(false);
        viewCourseList.setScrollLoadEnabled(true);
        viewCourseList.setOnRefreshListener(this);
        mListView = viewCourseList.getRefreshableView();
        Intent it = getIntent();
        TextView titleText = (TextView)findViewById(R.id.title_course) ;
        titleText.setText(it.getStringExtra("title"));

        adapter = new CourseItemAdapter(this, mListView);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        JSONArray jsonArray = new JSONArray();
        id = it.getStringExtra("id");
        if(id == null){
            jsonArray.put("").put("1").put(String.valueOf(page));
        }else {
            jsonArray.put(id).put("").put(String.valueOf(page));
        }

        getAsynHttp(Constant.URL_BASE +"courseList",jsonArray.toString(),null,1);

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
    public void getAsynHttpResponse(Call call, String json, int flg,int error) {
        if(error == -1) {
            viewCourseList.onRefreshComplete();
            return;
        }
        courseListGetBean = new CourseListGetBean(json);
        if (courseListGetBean.gethasError()) return;
        updateAllDate(flg);
    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {
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
        viewCourseList.onRefreshComplete();
    }

    private void reloadUp() {
        loadDate();
        adapter.notifyDataSetChanged();
        viewCourseList.onRefreshComplete();
    }

    public void loadDate() {
        List<CourseListGetBean.ResultObjectBean.CoursesBean> coursesBeanList = courseListGetBean.getCoursesBeanList();
        for(CourseListGetBean.ResultObjectBean.CoursesBean recomenedCourseBean : coursesBeanList){
            adapter.addCourse(recomenedCourseBean.getId(),recomenedCourseBean.getThumb(),recomenedCourseBean.getTitle(),recomenedCourseBean.getLessonType(),
                    recomenedCourseBean.getCategoryTitle(),recomenedCourseBean.getPrice(),recomenedCourseBean.getBuyersNum(),recomenedCourseBean.getType());
        }
    }

    @Override
    public void onNetChange(int netMobile) {
//        if(netMobile == 0){
//            Toast.makeText(getApplicationContext(), "无网络连接" , Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CourseItemAdapter.CourseItem courseItem = (CourseItemAdapter.CourseItem)parent.getAdapter().getItem(position);

        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(courseItem.id)));
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(it);
    }

    @Override
    public void onPullDownToRefresh(com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                page = 1;

                JSONArray jsonArray = new JSONArray();
                if(id == null){
                    jsonArray.put("").put("1").put(String.valueOf(page));
                }else {
                    jsonArray.put(id).put("").put(String.valueOf(page));
                }

                getAsynHttp(Constant.URL_BASE +"courseList",jsonArray.toString(),null,1);
            }
        }, 1000);
    }

    @Override
    public void onPullUpToRefresh(com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase<ListView> refreshView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                page ++;

                JSONArray jsonArray = new JSONArray();
                if(id == null){
                    jsonArray.put("").put("1").put(String.valueOf(page));
                }else {
                    jsonArray.put(id).put("").put(String.valueOf(page));
                }

                getAsynHttp(Constant.URL_BASE +"courseList",jsonArray.toString(),null,2);
            }
        }, 1000);
    }
}
