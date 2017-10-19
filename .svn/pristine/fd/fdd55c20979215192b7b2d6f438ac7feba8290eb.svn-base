package com.pointhouse.chiguan.s1_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.annotation.ViewId;
import com.pointhouse.chiguan.common.base.PermissionActivityBase;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;
import com.pointhouse.chiguan.common.util.AutoInject;
import com.pointhouse.chiguan.common.util.CollectionUtil;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Lesson;
import com.pointhouse.chiguan.k1_3.MediaActivity;
import com.pointhouse.chiguan.s1.WalkmanItem;
import com.pointhouse.chiguan.s1.WalkmanItemAdapter;
import com.pointhouse.chiguan.s1.WalkmanMediaUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已下载/下载中课程列表
 * Created by Maclaine on 2017/7/27.
 */
public class WalkmanLessonActivity extends PermissionActivityBase {
    private static final String TAG = "WalkmanLessonActivity";

    @ViewId(R.id.imgBack)
    ImageView imgBack;
    @ViewId(R.id.tabHost)
    TabHost tabHost;
    @ViewId(R.id.lvDownload)
    ListView lvDownload;
    @ViewId(R.id.lvDownloading)
    ListView lvDownloading;
    @ViewId(R.id.txtTitle)
    TextView txtTitle;
    @ViewId(R.id.viewEmptyDownloading)
    View viewEmptyDownloading;
    @ViewId(R.id.viewEmptyDownload)
    View viewEmptyDownload;

    List<WalkmanItem> itemList = new ArrayList<>();
    List<WalkmanItem> itemDownloadingList = new ArrayList<>();

    private CourseDownloadInfoDao courseDownloadInfoDao;

    Bundle savedInstanceState;

    boolean firstStart=true;


    Course course;
    private WalkmanDownloadingAdapter downloadingAdapter;
    private WalkmanItemAdapter downloadAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState=savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s1_2_activity_walkmanlesson);
        Intent intent = this.getIntent();
        course = (Course) intent.getSerializableExtra("course");
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
        init();
        this.setResult(1111);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!firstStart){
            this.onCreate(savedInstanceState);
        }
        firstStart=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        downloadingAdapter.onDestory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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

    private void init() {
        AutoInject.injectAll(this);
        initStyle();
        initListener();
    }

    private void initStyle() {
        tabHost.setup();
        Map<Integer, String> tabs = new HashMap<>();
        tabs.put(1, "已下载" + course.getLessonList().size() + "节课");
        tabs.put(2, "下载中" + itemDownloadingList.size() + "节课");
        for (Integer i : tabs.keySet()) {
            RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.k1_indicator_tab, tabHost.getTabWidget(), false);
            TextView txt1 = (TextView) tabIndicator1.findViewById(R.id.title);
            txt1.setText(tabs.get(i));
            tabHost.addTab(tabHost.newTabSpec("tab" + i).setIndicator(tabIndicator1).setContent(getResources().getIdentifier("tab" + i, "id", getPackageName())));
        }
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(ContextCompat.getColor(WalkmanLessonActivity.this, R.color.color_f5f5f5));
            ((TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.title)).setTextColor(ContextCompat.getColor(WalkmanLessonActivity.this, R.color.black));
        }
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(ContextCompat.getColor(WalkmanLessonActivity.this, R.color.white));

        txtTitle.setText(course.getTitle());

        downloadAdapter = new WalkmanItemAdapter(this, itemList, R.layout.s1_2_item_walkmanlesson_download);
        refreshCourse();

        downloadAdapter.setClickListener((list, position) -> {
            try {
                Intent intent = new Intent(WalkmanLessonActivity.this, MediaActivity.class);
                Bundle bundle = new Bundle();
                CommonMediaOption option = new CommonMediaOption();
                option.setType(CommonMediaOption.AUTO);
                option.setReadDownload(true);
                option.setId((Integer) list.get(position).getId());
                course.setLesson(course.getLessonList().get(position));
                option.setCourse(course);
                if (!CollectionUtil.isEmpty(course.getLesson().getAudioList())) {
                    option.setMediaID(course.getLesson().getAudioList().get(0).getVid());
                } else if (!CollectionUtil.isEmpty(course.getLesson().getVideoList())) {
                    option.setMediaID(course.getLesson().getVideoList().get(0).getVid());
                } else {
                    throw new Exception("音视频不存在");
                }
                bundle.putSerializable("option", option);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (Exception e){
                Log.e(TAG,e.getMessage());
                Toast.makeText(WalkmanLessonActivity.this,"播放器打开失败",Toast.LENGTH_SHORT).show();
            }

        });
        downloadAdapter.setDeleteListener((list, position) -> permissions(() -> {
            try {
                WalkmanItem item = list.get(position);
                List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId((Integer) item.getId());
                if (courseDownloadInfoList != null) {
                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                        SDCardHelper.removeFileFromRom(courseDownloadInfo.getFilePath() + "/" + courseDownloadInfo.getfileName());
                    }
                }
                courseDownloadInfoDao.deleteByLessonId((Integer) item.getId());
                refreshCourse();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        lvDownload.setAdapter(downloadAdapter);
        //下载中列表
        Map<Integer, Course> downloadingMap = WalkmanMediaUtil.initCourse(WalkmanLessonActivity.this, WalkmanMediaUtil.Downloading);
        for (Map.Entry<Integer, Course> entry : downloadingMap.entrySet()) {
            if (entry.getKey().equals(course.getId())) {
                List<Lesson> lessonList = entry.getValue().getLessonList();
                Collections.sort(lessonList, (o1, o2) -> CollectionUtil.ascSort(o1.getId(),o2.getId()));
                itemDownloadingList.clear();
                for (Lesson lesson : lessonList) {
                    itemDownloadingList.add(new WalkmanItem(lesson));
                }
                break;
            }
        }
        downloadingAdapter = new WalkmanDownloadingAdapter(this, itemDownloadingList, R.layout.s1_2_item_walkmanlesson_downloading);
        if(itemDownloadingList.size()==0){
            lvDownloading.setEmptyView(viewEmptyDownloading);
        }else{
            lvDownloading.setAdapter(downloadingAdapter);
        }
        ((TextView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.title)).setText("下载中" + itemDownloadingList.size() + "节课");
    }

    void removeDownloading(int position) {
        itemDownloadingList.remove(position);
        ((TextView) tabHost.getTabWidget().getChildAt(1).findViewById(R.id.title)).setText("下载中" + itemDownloadingList.size() + "节课");
        if(itemDownloadingList.size()==0){
            lvDownloading.setEmptyView(viewEmptyDownloading);
        }
        downloadingAdapter.notifyDataSetChanged();
        refreshCourse();
    }

    private void initListener() {
        //返回键
        imgBack.setOnClickListener(view -> super.onBackPressed());
        tabHost.setOnTabChangedListener(tabId -> {
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                View view = tabHost.getTabWidget().getChildAt(i);
                if (tabHost.getCurrentTab() == i) {
                    view.setBackgroundColor(ContextCompat.getColor(WalkmanLessonActivity.this, R.color.white));
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(WalkmanLessonActivity.this, R.color.color_f5f5f5));
                }
            }
            refreshCourse();
        });
    }

    private void refreshCourse() {
        Map<Integer, Course> courseMap = WalkmanMediaUtil.initCourse(WalkmanLessonActivity.this, WalkmanMediaUtil.Download);
        if(courseMap.size()==0){
            itemList.clear();
        }else{
            boolean flag=false;
            for (Map.Entry<Integer, Course> entry : courseMap.entrySet()) {
                if (entry.getKey().equals(course.getId())) {
                    //已下载列表
                    List<Lesson> lessonList = entry.getValue().getLessonList();
                    Collections.sort(lessonList, (o1, o2) -> CollectionUtil.ascSort(o1.getId(),o2.getId()));
                    course = entry.getValue();
                    itemList.clear();
                    for (Lesson lesson : lessonList) {
                        itemList.add(new WalkmanItem(lesson));
                    }
                    flag=true;
                    break;
                }
            }
            if(!flag){
                itemList.clear();
            }
            ((TextView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.title)).setText("已下载" + course.getLessonList().size() + "节课");
        }
        if(itemList.size()==0){
            lvDownload.setEmptyView(viewEmptyDownload);
            ((TextView) tabHost.getTabWidget().getChildAt(0).findViewById(R.id.title)).setText("已下载0节课");
        }
        downloadAdapter.notifyDataSetChanged();
    }

    protected void permissions(CallBack callBack){
        requestPermissions(PermissionGroup.STORAGE, true, new CallbackListener() {
            @Override
            public void onPermissionsOK(int requestCode, List<String> perms) {
                callBack.accept();
            }

            @Override
            public void onPermissionsNG(int requestCode, List<String> perms) {
                Toast.makeText(WalkmanLessonActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {
                Toast.makeText(WalkmanLessonActivity.this,"授权取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface CallBack{
        public void accept();
    }
}
