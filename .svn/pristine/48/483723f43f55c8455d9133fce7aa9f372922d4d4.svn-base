package com.pointhouse.chiguan.k1_5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.HttpActivityBase;
import com.pointhouse.chiguan.common.jsonObject.CourseListGetBean;
import com.pointhouse.chiguan.common.jsonObject.TeacherListGetBean;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.db.SearchInfo;
import com.pointhouse.chiguan.db.SearchInfoDao;
import com.pointhouse.chiguan.k1.CourseItemAdapter;
import com.pointhouse.chiguan.k1.TeacherItemAdapter;
import com.pointhouse.chiguan.k1_8.TeacherDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by ljj on 2017/7/22.
 */
public class SearchViewActivity extends HttpActivityBase implements AdapterView.OnItemClickListener {
    private SearchInfoDao searchInfoDao;
    private WarpLinearLayout warpLinearLayout;
    private RelativeLayout relativeLayout2;
    /*UI组件*/
    private TextView tv_clear;
    private EditText et_search;
    private TextView tv_cancel;
    private ImageView iv_search;
    ListView associationList;
    AssociationListAdapter associationAdapter;
    ListView viewCourseList;
    CourseItemAdapter viewCourseAdapter;
    ListView viewTeacherList;
    TeacherItemAdapter viewTeacherAdapter;
    Boolean isCourseTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_5_activity_searchview);
        viewCourseList = (ListView) findViewById(R.id.viewCourseList);

        viewCourseAdapter = new CourseItemAdapter(this, viewCourseList);
        viewCourseList.setAdapter(viewCourseAdapter);
        viewCourseList.setOnItemClickListener(this);

        viewTeacherList = (ListView) findViewById(R.id.viewTeacherList);

        viewTeacherAdapter = new TeacherItemAdapter(this, viewTeacherList);
        viewTeacherList.setAdapter(viewTeacherAdapter);
        viewTeacherList.setOnItemClickListener(this);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setFocusable(true);
        et_search.setFocusableInTouchMode(true);
        et_search.requestFocus();
        et_search.requestFocusFromTouch();
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        warpLinearLayout = (WarpLinearLayout) findViewById(R.id.warpLinearLayout);
        iv_search = (ImageView) findViewById(R.id.searchImg);

        associationList = (ListView) findViewById(R.id.viewAssociationList);
        associationAdapter = new AssociationListAdapter(this, associationList);
        associationList.setAdapter(associationAdapter);
        associationList.setOnItemClickListener(this);
        Intent it = getIntent();
        isCourseTag = it.getBooleanExtra("key",true);
        init();
        if (isCourseTag){
            viewCourseList.setVisibility(View.GONE);
            viewTeacherList.setVisibility(View.GONE);
            et_search.setHint("请输入课程名");
        } else {
            viewCourseList.setVisibility(View.GONE);
            viewTeacherList.setVisibility(View.GONE);
            et_search.setHint("请输入老师名");
        }
        relativeLayout2.setVisibility(View.VISIBLE);
        warpLinearLayout.setVisibility(View.VISIBLE);
        associationList.setVisibility(View.GONE);
    }


    /*初始化搜索框*/
    private void init(){
        searchInfoDao = new SearchInfoDao(GlobalApplication.sContext);

        // 第一次进入时查询所有的历史记录
        queryData();

        //"清空搜索历史"按钮
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空数据库
                deleteData();
            }
        });

        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                // 此处为得到焦点时的处理内容
                    if (et_search.getText().toString().trim().replace(".","").length() == 0) {
                        relativeLayout2.setVisibility(View.VISIBLE);
                        warpLinearLayout.setVisibility(View.VISIBLE);
                        associationList.setVisibility(View.GONE);
                    } else {
                        relativeLayout2.setVisibility(View.GONE);
                        warpLinearLayout.setVisibility(View.GONE);
                        associationList.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //输入后调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().replace(".","").length() == 0) {
                    queryData();
                    relativeLayout2.setVisibility(View.VISIBLE);
                    warpLinearLayout.setVisibility(View.VISIBLE);
                    associationList.setVisibility(View.GONE);
                } else {
                    relativeLayout2.setVisibility(View.GONE);
                    warpLinearLayout.setVisibility(View.GONE);
                    associationList.setVisibility(View.VISIBLE);
                    searchInit(s.toString().trim().replace(".",""));
                }
            }
        });

        // 搜索框的键盘搜索键
        // 点击回调
        et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            // 修改回车键功能
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String searchtxt = et_search.getText().toString().trim().replace(".","");
                    if(searchtxt.isEmpty()) return false;
                    saveHistoricalRecord(searchtxt);

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(searchtxt);
                    if (isCourseTag){
                        getAsynHttp(Constant.URL_BASE +"courseSearch",jsonArray.toString(),null,3);
                    } else {
                        getAsynHttp(Constant.URL_BASE +"teacherSearch",jsonArray.toString(),null,4);
                    }
                    return true;
                }
                return false;
            }
        });

        this.tv_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //当前页面销毁
                finish();
                //创建一个事件
                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK);
                //设置activity监听此事件，返回true只监听一次，如果返回false则继续监听
                SearchViewActivity.this.onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);

            }
        });


        //点击搜索按钮后的事件
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtxt = et_search.getText().toString().trim().replace(".","");
                if(searchtxt.isEmpty()) return;
                saveHistoricalRecord(searchtxt);

                Intent it = getIntent();
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(searchtxt);
                if (isCourseTag){
                    getAsynHttp(Constant.URL_BASE +"courseSearch",jsonArray.toString(),null,3);
                } else {
                    getAsynHttp(Constant.URL_BASE +"teacherSearch",jsonArray.toString(),null,4);
                }
            }
        });
    }

    /**
     * 关键字提示
     */
    private void searchInit(String key){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(key);

        if (isCourseTag){
            getAsynHttp(Constant.URL_BASE +"courseSearchInit",jsonArray.toString(),null,1);
        } else {
            getAsynHttp(Constant.URL_BASE +"teacherSearchInit",jsonArray.toString(),null,2);
        }
    }

    /**
     * 保存历史记录
     */
    private void saveHistoricalRecord(String historicalRecord){
        if(historicalRecord.isEmpty()) return;
        List<SearchInfo> searchInfos = new ArrayList<>();

        try {
            if(isCourseTag) {
                searchInfos = searchInfoDao.queryTypeAll(1);
            } else {
                searchInfos = searchInfoDao.queryTypeAll(2);
            }

            if(searchInfos != null && searchInfos.size() != 0){
                for(int i = 0;i < searchInfos.size() ; i++ ){
                    if(searchInfos.size() >= 8){
                        SearchInfo searchInfo = searchInfos.get(0);
                        searchInfoDao.delete(searchInfo);
                    }
                    if(searchInfos.size() == i + 1){
                        SearchInfo searchInfo = searchInfos.get(i);
                        SearchInfo searchInfo1 = new SearchInfo();
                        searchInfo1.setsort(searchInfo.getsort() + 1);
                        if(isCourseTag) {
                            searchInfo1.setType(1);
                        } else {
                            searchInfo1.setType(2);
                        }
                        searchInfo1.setsearchText(historicalRecord);
                        searchInfoDao.save(searchInfo1);
                    }

                }
            } else {
                SearchInfo searchInfo1 = new SearchInfo();
                searchInfo1.setsort(1);
                if(isCourseTag) {
                    searchInfo1.setType(1);
                } else {
                    searchInfo1.setType(2);
                }
                searchInfo1.setsearchText(historicalRecord);
                searchInfoDao.save(searchInfo1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*历史记录*/
    private void queryData() {
        List<SearchInfo> searchInfos = new ArrayList<>();

        //历史记录
        try {
            if(isCourseTag) {
                searchInfos = searchInfoDao.queryTypeAll(1);
            } else {
                searchInfos = searchInfoDao.queryTypeAll(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        warpLinearLayout.removeAllViews();
        if(searchInfos != null){
            for(SearchInfo searchInfo:searchInfos){
                TextView tv = new TextView(this);
                tv.setTextColor(ContextCompat.getColor(this, R.color.searchColor));
                tv.setText(searchInfo.getsearchText());
                tv.setBackgroundResource(R.drawable.shape_corner_srtoke);
                tv.setPadding(10,10,10,10);
                tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String searchtxt = tv.getText().toString();
                        saveHistoricalRecord(searchtxt);

                        Intent it = getIntent();
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(searchtxt);
                        et_search.setText(searchtxt);
                        if (isCourseTag){
                            getAsynHttp(Constant.URL_BASE +"courseSearch",jsonArray.toString(),null,3);
                        } else {
                            getAsynHttp(Constant.URL_BASE +"teacherSearch",jsonArray.toString(),null,4);
                        }
                    }
                });
                warpLinearLayout.addView(tv);
            }
        }
    }

    /*清空数据*/
    private void deleteData() {
        List<SearchInfo> searchInfos = new ArrayList<>();
        //历史记录
        try {
            if(isCourseTag) {
                searchInfos = searchInfoDao.queryTypeAll(1);
            } else {
                searchInfos = searchInfoDao.queryTypeAll(2);
            }

            for(SearchInfo searchInfo:searchInfos){
                searchInfoDao.delete(searchInfo);
            }

            warpLinearLayout.removeAllViews();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() instanceof AssociationListAdapter){
            AssociationListAdapter.AssociationItem associationItem =
                (AssociationListAdapter.AssociationItem)parent.getAdapter().getItem(position);
            String searchtxt = associationItem.associationName;
            saveHistoricalRecord(searchtxt);

            Intent it = getIntent();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(searchtxt);
            et_search.setText(searchtxt);
            if (isCourseTag){
                getAsynHttp(Constant.URL_BASE +"courseSearch",jsonArray.toString(),null,3);
            } else {
                getAsynHttp(Constant.URL_BASE +"teacherSearch",jsonArray.toString(),null,4);
            }
        } else if (parent.getAdapter() instanceof TeacherItemAdapter){
            if(findViewById(R.id.warpLinearLayout).getVisibility() == View.VISIBLE) return;
            TeacherItemAdapter.TeacherItem teacherItem = (TeacherItemAdapter.TeacherItem)parent.getAdapter().getItem(position);
            Intent intent = new Intent(view.getContext(), TeacherDetailActivity.class);
            intent.putExtra("id",teacherItem.id);
            view.getContext().startActivity(intent);
        } else if (parent.getAdapter() instanceof CourseItemAdapter){
            if(findViewById(R.id.warpLinearLayout).getVisibility() == View.VISIBLE) return;
            CourseItemAdapter.CourseItem courseItem = (CourseItemAdapter.CourseItem)parent.getAdapter().getItem(position);
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(courseItem.id)));
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(it);
        }
    }

    public void setVisibilitytoGONE(){
        findViewById(R.id.relativeLayout2).setVisibility(View.GONE);
        findViewById(R.id.warpLinearLayout).setVisibility(View.GONE);
        findViewById(R.id.viewAssociationList).setVisibility(View.GONE);
    }


    @Override
    public void getAsynHttpResponse(Call call, String json, int flg,int error) {
        if(error == -1) return;
        updateAllDate(flg,json);
    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {

    }

    private void updateAllDate(int flg,String json){
        if(flg == 1 || flg == 2){
            JSONTokener jsonParser = new JSONTokener(json);
            try {
                JSONObject jSONObject = (JSONObject)jsonParser.nextValue();
                JSONArray jSONArray = jSONObject.getJSONArray("resultObject");
                associationAdapter.clean();
                for (int j = 0; j < jSONArray.length(); j++) {
                    loadDateAssociation(jSONArray.getString(j));
                }
                associationAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(flg == 3){
            CourseListGetBean courseListGetBean = new CourseListGetBean(json);
            List<CourseListGetBean.ResultObjectBean.CoursesBean> coursesBean = courseListGetBean.getCoursesBeanList();
            reloadCourse(coursesBean);
        }
        else if(flg == 4){
            TeacherListGetBean teacherListGetBean = new TeacherListGetBean(json);
            List<TeacherListGetBean.ResultObjectBean.TeachersBean> teachersBean = teacherListGetBean.getTeacherList();
            reloadTeacher(teachersBean);
        }
    }

    private void reloadTeacher(List<TeacherListGetBean.ResultObjectBean.TeachersBean> teachersBeanList) {
        et_search.clearFocus();
        hideKeyboard(getWindow().getDecorView());
        viewTeacherAdapter.clean();
        loadDateTeacher(teachersBeanList);
        if(viewTeacherAdapter.getCount() == 0){
            viewTeacherList.setVisibility(View.GONE);
            LinearLayout viewCourseNodata = (LinearLayout)findViewById(R.id.nodataLL);
            viewCourseNodata.setVisibility(View.VISIBLE);
            viewCourseNodata.setFocusable(true);
            viewCourseNodata.setFocusableInTouchMode(true);
            viewCourseNodata.requestFocus();
            viewCourseNodata.requestFocusFromTouch();
        } else {
            LinearLayout viewCourseNodata = (LinearLayout)findViewById(R.id.nodataLL);
            viewCourseNodata.setVisibility(View.GONE);
            viewTeacherList.setVisibility(View.VISIBLE);
            viewTeacherList.setFocusable(true);
            viewTeacherList.setFocusableInTouchMode(true);
            viewTeacherList.requestFocus();
            viewTeacherList.requestFocusFromTouch();
        }
        viewTeacherAdapter.notifyDataSetChanged();
        setVisibilitytoGONE();
    }

    private void reloadCourse(List<CourseListGetBean.ResultObjectBean.CoursesBean> coursesBeanList) {
        et_search.clearFocus();
        hideKeyboard(getWindow().getDecorView());
        viewCourseAdapter.clean();
        loadDateCourse(coursesBeanList);
        if(viewCourseAdapter.getCount() == 0){
            viewCourseList.setVisibility(View.GONE);
            LinearLayout viewCourseNodata = (LinearLayout)findViewById(R.id.nodataLL);
            viewCourseNodata.setVisibility(View.VISIBLE);
            viewCourseNodata.setFocusable(true);
            viewCourseNodata.setFocusableInTouchMode(true);
            viewCourseNodata.requestFocus();
            viewCourseNodata.requestFocusFromTouch();
        } else {
            LinearLayout viewCourseNodata = (LinearLayout)findViewById(R.id.nodataLL);
            viewCourseNodata.setVisibility(View.GONE);
            viewCourseList.setVisibility(View.VISIBLE);
            viewCourseList.setFocusable(true);
            viewCourseList.setFocusableInTouchMode(true);
            viewCourseList.requestFocus();
            viewCourseList.requestFocusFromTouch();
        }
        viewCourseAdapter.notifyDataSetChanged();
        setVisibilitytoGONE();
    }

    public void loadDateAssociation(String association) {
        associationAdapter.addAssociation(association,et_search.getText().toString().trim().replace(".",""));
    }

    public void loadDateTeacher(List<TeacherListGetBean.ResultObjectBean.TeachersBean> teachersBeanList) {
        for(TeacherListGetBean.ResultObjectBean.TeachersBean teachersBean : teachersBeanList) {
            viewTeacherAdapter.addTeacher(teachersBean.getAvatar(), teachersBean.getSex(), teachersBean.getNickname(), teachersBean.getCountry(), teachersBean.getId());
        }
    }

    public void loadDateCourse(List<CourseListGetBean.ResultObjectBean.CoursesBean> coursesBeanList) {
        for(CourseListGetBean.ResultObjectBean.CoursesBean recomenedCourseBean : coursesBeanList){
            viewCourseAdapter.addCourse(recomenedCourseBean.getId(),recomenedCourseBean.getThumb(),recomenedCourseBean.getTitle(),recomenedCourseBean.getLessonType(),
                    recomenedCourseBean.getCategoryTitle(),recomenedCourseBean.getPrice(),recomenedCourseBean.getBuyersNum(),recomenedCourseBean.getType());
        }
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
        }
    }

    //显示虚拟键盘
    public static void showKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
    }
}
