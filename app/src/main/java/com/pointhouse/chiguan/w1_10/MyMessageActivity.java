package com.pointhouse.chiguan.w1_10;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by P on 2017/8/7.
 */

public class MyMessageActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener{
    private TextView SystemTxt;
    private TextView OtherTxt;
    private MyMessageAdapter adapter;
    private View SystemLine;
    private View OtherLine;
    private ViewPager vpager;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_10_activity_mymessage);
        bindViews();
        initViewPager();
        setSelected(true);
    }

    public void initViewPager(){
        //构造适配器
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new MyMessage_SystemMessageFragment());
        fragments.add(new MyMessage_OtherMessageFragment());
        adapter = new MyMessageAdapter(getSupportFragmentManager(), fragments);
        //设定适配器
        vpager = (ViewPager)findViewById(R.id.mymessage_vpager);
        vpager.setAdapter(adapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
        //vpager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.viewpagerleft));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_message:
                setSelected(true);
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.other_message:
                setSelected(false);
                vpager.setCurrentItem(PAGE_TWO);
                break;
        }
    }

    private void bindViews() {
        SystemTxt = (TextView) findViewById(R.id.system_message);
        SystemLine = findViewById(R.id.system_message_line);
        OtherTxt = (TextView) findViewById(R.id.other_message);
        OtherLine = findViewById(R.id.other_message_line);

        SystemTxt.setOnClickListener(this);
        OtherTxt.setOnClickListener(this);
    }

    public void Messsage_back(View view){
        super.onBackPressed();
    }

    //重置所有文本的选中状态
    private void setSelected(boolean first) {
        SystemTxt.setSelected(first);
        SystemLine.setSelected(first);
        OtherTxt.setSelected(!first);
        OtherLine.setSelected(!first);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    setSelected(true);
                    SystemTxt.performClick();
                    break;
                case PAGE_TWO:
                    setSelected(false);
                    OtherTxt.performClick();
                    break;
            }
        }
    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            //当手指移动的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) {
                MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
            }else if(y2 - y1 > 50) {
                MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
