package com.pointhouse.chiguan.k1_8;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.HttpActivityBase;
import com.pointhouse.chiguan.common.jsonObject.TeacherDetailGetBean;
import com.pointhouse.chiguan.common.layout.BackBtnViewLayout;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.k1.CourseItemAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by ljj on 2017/7/21.
 */

public class TeacherDetailActivity extends HttpActivityBase implements View.OnClickListener,AdapterView.OnItemClickListener{

    TextView personalData;
    View personalDataLine;
    TextView allCourses;
    View allCoursesLine;
    CourseItemAdapter adapter;
    ImageView gender_img;
    ListView viewCourseList;
    TeacherDetailGetBean teacherDetailGetBean;

    @Override
    public void getAsynHttpResponse(Call call, String json, int flg, int error) {
        if(error == -1) return;
        teacherDetailGetBean = new TeacherDetailGetBean(json);
        if (teacherDetailGetBean.gethasError()) return;
        updateAllDate(flg);
    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_8_activity_teacherdetail);

        BackBtnViewLayout backbtnll = (BackBtnViewLayout)findViewById(R.id.BackBtnViewLayout);

        backbtnll.setTitleClickListener(new View.OnClickListener() {
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

        viewCourseList = (ListView) findViewById(R.id.viewCourseList);
        viewCourseList.setFocusable(false);
        adapter = new CourseItemAdapter(this, viewCourseList);
        viewCourseList.setAdapter(adapter);
        viewCourseList.setOnItemClickListener(this);

        JSONArray jsonArray = new JSONArray();
        Intent it = getIntent();
        jsonArray.put(String.valueOf(it.getIntExtra("id",-1)));
        getAsynHttp(Constant.URL_BASE +"teacherDetail",jsonArray.toString(),null,1);
        bindViews();
    }

    private void bindViews() {
        personalData = (TextView) findViewById(R.id.PersonalData);
        personalDataLine = (View) findViewById(R.id.PersonalDataLine);
        allCourses = (TextView) findViewById(R.id.AllCourses);
        allCoursesLine = (View) findViewById(R.id.AllCoursesLine);
        gender_img = (ImageView) findViewById(R.id.Gender_img);
        personalData.setOnClickListener(this);
        allCourses.setOnClickListener(this);
        setSelected(true);
    }

    private void reload() {
        TextView titleText = (TextView)findViewById(R.id.Gender_txt);
        titleText.setText(teacherDetailGetBean.getTeacherBean().getNickname());
        TextView country_txt = (TextView)findViewById(R.id.Country_txt);
        country_txt.setText(teacherDetailGetBean.getTeacherBean().getCountry());
        CircleImageView teacher_icon = (CircleImageView)findViewById(R.id.teacher_icon);

        if(teacherDetailGetBean.getTeacherBean().getAvatar() != null && !teacherDetailGetBean.getTeacherBean().getAvatar().isEmpty()){
            Picasso.with(this).load(teacherDetailGetBean.getTeacherBean().getAvatar()).placeholder(R.drawable.icon2_default).into(teacher_icon);
        } else {
            Picasso.with(this).load(R.drawable.icon2_default).into(teacher_icon);
        }

        WebView personalDataText = (WebView)findViewById(R.id.personalDataText);
        String resume = teacherDetailGetBean.getTeacherBean().getResume();
        if(resume != null && !resume.isEmpty()){
            personalDataText.getSettings().setDefaultTextEncodingName("UTF -8");
            personalDataText.loadData(resume.replace("<img", "<img style=\"width:100%;\""),"text/html; charset=UTF-8", null);
        }else {
            personalDataText.loadData(getString(R.string.media_nodata), "text/html; charset=UTF-8", null);
        }


        personalDataText.setFocusable(false);

        if(teacherDetailGetBean.getTeacherBean().getSex() == 1){
            Picasso.with(this).load(R.mipmap.sexman).into(gender_img);
        }else{
            Picasso.with(this).load(R.mipmap.sexwoman).into(gender_img);
        }

        adapter.clean();
        loadDate();
        if(adapter.getCount() == 0){
            View listEmptyView = View.inflate(this, R.layout.common_empty,
                    (ViewGroup) viewCourseList.getParent());
            viewCourseList.setEmptyView(listEmptyView);
        }
        adapter.notifyDataSetChanged();
    }

    private void updateAllDate(int flg){
        if(flg == 1){
            reload();
        }
    }

    //重置所有文本的选中状态
    private void setSelected(boolean first) {
        personalData.setSelected(first);
        personalDataLine.setSelected(first);
        allCourses.setSelected(!first);
        allCoursesLine.setSelected(!first);
    }

    public void loadDate() {
        List<TeacherDetailGetBean.ResultObjectBean.CoursesBean> coursesBeanList = teacherDetailGetBean.getCoursesBeanList();
        for(TeacherDetailGetBean.ResultObjectBean.CoursesBean recomenedCourseBean : coursesBeanList){
            adapter.addCourse(recomenedCourseBean.getId(),recomenedCourseBean.getThumb(),recomenedCourseBean.getTitle(),recomenedCourseBean.getLessonType(),
                    recomenedCourseBean.getCategoryTitle(),recomenedCourseBean.getPrice(),recomenedCourseBean.getBuyersNum(),recomenedCourseBean.getType());
        }

        ViewGroup.LayoutParams layoutParams = viewCourseList.getLayoutParams();
        int count = coursesBeanList.size();
        float height = 101.5f * count;
        layoutParams.height = DensityUtil.dip2px(getApplicationContext(), height);
        viewCourseList.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PersonalData:
                setSelected(true);
                findViewById(R.id.RelativeLayout06).setVisibility(View.VISIBLE);
                findViewById(R.id.RelativeLayout07).setVisibility(View.GONE);
                break;
            case R.id.AllCourses:
                setSelected(false);
                findViewById(R.id.RelativeLayout06).setVisibility(View.GONE);
                findViewById(R.id.RelativeLayout07).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CourseItemAdapter.CourseItem courseItem = (CourseItemAdapter.CourseItem)parent.getAdapter().getItem(position);

        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(courseItem.id)));
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(it);
    }
}
