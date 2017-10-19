package com.pointhouse.chiguan.k1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.k1_5.SearchViewActivity;

/**
 * Created by ljj on 2017/6/28.
 */

public class CourseClassifiCationMainActivity extends Fragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener{

    private CourseFragmentPagerAdapter mAdapter;

    private TextView phCourseTxt;
    private TextView phTeacherTxt;

    private View phCourseLine;
    private View phTeacherLine;
    private ImageView searchBtnImg;

    private ViewPager vpager;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.k1_activity_courseclassificationmain, container,false);
        mAdapter = new CourseFragmentPagerAdapter(getChildFragmentManager());
        bindViews(view);
        phCourseTxt.performClick();

        searchBtnImg = (ImageView)view.findViewById(R.id.SearchBtnImg);
        searchBtnImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchViewActivity.class);
                intent.putExtra("key",phCourseTxt.isSelected());
                getContext().startActivity(intent);
            }
        });
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ChildPhCourseTxt:
                setSelected(true);
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.PhTeacherTxt:
                setSelected(false);
                vpager.setCurrentItem(PAGE_TWO);
                break;
        }
    }

    private void bindViews(View view) {

        phCourseTxt = (TextView) view.findViewById(R.id.ChildPhCourseTxt);
        phCourseLine = (View) view.findViewById(R.id.ChildPhCourseLine);
        phTeacherTxt = (TextView) view.findViewById(R.id.PhTeacherTxt);
        phTeacherLine = (View) view.findViewById(R.id.PhTeacherLine);

        phCourseTxt.setOnClickListener(this);
        phTeacherTxt.setOnClickListener(this);

        vpager = (ViewPager) view.findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
        setSelected(true);
    }

    //重置所有文本的选中状态
    private void setSelected(boolean first) {
        phCourseTxt.setSelected(first);
        phCourseLine.setSelected(first);
        phTeacherTxt.setSelected(!first);
        phTeacherLine.setSelected(!first);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.v("selectssdsf:","进入selectsdfdsf");

    }

    @Override
    public void onPageSelected(int position) {
        Log.v("select:","进入select");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    setSelected(true);
                    phCourseTxt.performClick();
                    break;
                case PAGE_TWO:
                    setSelected(false);
                    phTeacherTxt.performClick();
                    break;
            }
        }
    }
}
