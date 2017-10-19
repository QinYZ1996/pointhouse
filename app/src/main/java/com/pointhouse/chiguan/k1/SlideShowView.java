package com.pointhouse.chiguan.k1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.jsonObject.IndexIinitGetBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ljj on 2017/6/30.
 */

public class SlideShowView extends FrameLayout{

    //轮播图图片数量
    private final static int IMAGE_COUNT = 3;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 3;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;
    private int mChildCount = 0;
    //自定义轮播图的资源ID
    private int[] imagesResIds;
    private int[] imagesResIds2;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList = new ArrayList<>();
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //Handler
    private Handler handler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initData();
        initUI(context);
        if(isAutoPlay){
            startPlay();
        }

    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 3, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化相关Data
     */
    private void initData(){
        imagesResIds = new int[]{
                R.drawable.kv_default,
                R.drawable.kv_default,
                R.drawable.kv_default
        };
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context){
        LayoutInflater.from(context).inflate(R.layout.k1_item_slideshow, this, true);
        for(int imageID : imagesResIds){
            ImageView view =  new ImageView(context);
            view.setImageResource(imageID);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);
        }
        dotViewsList.add(findViewById(R.id.v_dot1));
        dotViewsList.add(findViewById(R.id.v_dot2));
        dotViewsList.add(findViewById(R.id.v_dot3));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
    }

    public void reload(Context context, IndexIinitGetBean indexIinitGetBean)
    {
        imageViewsList.clear();
        List<IndexIinitGetBean.ResultObjectBean.KvBean> kvBeanList = indexIinitGetBean.getKvList();
        if(kvBeanList.size() == 1){
            for(IndexIinitGetBean.ResultObjectBean.KvBean kvBean : kvBeanList){
                ImageView view =  new ImageView(context);
                if(kvBean.getKvThumb() != null && !kvBean.getKvThumb().isEmpty()){
                    Picasso.with(context).load(kvBean.getKvThumb()).placeholder(R.drawable.kv_default).into(view);
                }else {
                    Picasso.with(context).load(R.drawable.kv_default).into(view);
                }

                view.setScaleType(ScaleType.FIT_XY);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(kvBean.getId())));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                });
                imageViewsList.add(view);
                ImageView view2 =  new ImageView(context);
                if(kvBean.getKvThumb() != null && !kvBean.getKvThumb().isEmpty()){
                    Picasso.with(context).load(kvBean.getKvThumb()).placeholder(R.drawable.kv_default).into(view2);
                }else {
                    Picasso.with(context).load(R.drawable.kv_default).into(view2);
                }

                view2.setScaleType(ScaleType.FIT_XY);
                view2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(kvBean.getId())));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                });
                imageViewsList.add(view2);
                ImageView view3 =  new ImageView(context);
                if(kvBean.getKvThumb() != null && !kvBean.getKvThumb().isEmpty()){
                    Picasso.with(context).load(kvBean.getKvThumb()).placeholder(R.drawable.kv_default).into(view3);
                }else {
                    Picasso.with(context).load(R.drawable.kv_default).into(view3);
                }

                view3.setScaleType(ScaleType.FIT_XY);
                view3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(kvBean.getId())));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                });
                imageViewsList.add(view3);
            }
        }
        if(kvBeanList.size() == 2){
            int i = 1;
            for(IndexIinitGetBean.ResultObjectBean.KvBean kvBean : kvBeanList){
                ImageView view =  new ImageView(context);
                if(kvBean.getKvThumb() != null && !kvBean.getKvThumb().isEmpty()){
                    Picasso.with(context).load(kvBean.getKvThumb()).placeholder(R.drawable.kv_default).into(view);
                }else {
                    Picasso.with(context).load(R.drawable.kv_default).into(view);
                }

                view.setScaleType(ScaleType.FIT_XY);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(kvBean.getId())));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                });
                imageViewsList.add(view);
                if(i == 2){
                    ImageView view2 =  new ImageView(context);
                    if(kvBean.getKvThumb() != null && !kvBean.getKvThumb().isEmpty()){
                        Picasso.with(context).load(kvBean.getKvThumb()).placeholder(R.drawable.kv_default).into(view2);
                    }else {
                        Picasso.with(context).load(R.drawable.kv_default).into(view2);
                    }

                    view2.setScaleType(ScaleType.FIT_XY);
                    view2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(kvBean.getId())));
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(it);
                        }
                    });
                    imageViewsList.add(view2);
                }
                i++;
            }
        }

        if(kvBeanList.size() == 3){
            for(IndexIinitGetBean.ResultObjectBean.KvBean kvBean : kvBeanList){
                ImageView view =  new ImageView(context);
                if(kvBean.getKvThumb() != null && !kvBean.getKvThumb().isEmpty()){
                    Picasso.with(context).load(kvBean.getKvThumb()).placeholder(R.drawable.kv_default).into(view);
                }else {
                    Picasso.with(context).load(R.drawable.kv_default).into(view);
                }

                view.setScaleType(ScaleType.FIT_XY);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(kvBean.getId())));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                });
                imageViewsList.add(view);
            }
        }
        viewPager.getAdapter().notifyDataSetChanged();

    }

    /**
     * 填充ViewPager的页面适配器
     * @author caizhiming
     */
    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            Log.i("k1position",String.valueOf(position));
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(imageViewsList.get(position));

            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(ViewGroup arg0) {
            // TODO Auto-generated method stub

        }

        // 重点是加上下面的代码就可以了。
        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if ( mChildCount > 0) {
                mChildCount --;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     * @author caizhiming
     */
    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            for(int i=0;i < dotViewsList.size();i++){
                if(i == pos){
                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_focus);
                }else {
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_nomal);
                }
            }
        }

    }

    /**
     *执行轮播图切换任务
     *@author caizhiming
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                if(imageViewsList.size() == 3) {
                    currentItem = (currentItem + 1) % imageViewsList.size();
                    handler.obtainMessage().sendToTarget();
                }
            }
        }

    }
    /**
     * 销毁ImageView资源，回收内存
     * @author caizhiming
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }
}
