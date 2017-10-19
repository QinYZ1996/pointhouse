package com.pointhouse.chiguan.s1_1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.annotation.ViewId;
import com.pointhouse.chiguan.common.base.PermissionActivityBase;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;
import com.pointhouse.chiguan.common.util.AutoInject;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.k1_2.DownloadService;
import com.pointhouse.chiguan.k1_2.DownloadStateUtil;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.s1.WalkmanItem;
import com.pointhouse.chiguan.s1.WalkmanItemAdapter;
import com.pointhouse.chiguan.s1.WalkmanMediaUtil;
import com.pointhouse.chiguan.s1_2.WalkmanLessonActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pointhouse.chiguan.R.id.courseList;
import static com.pointhouse.chiguan.R.id.viewMain;

/**
 * 已下载课程专辑(查看全部)
 * Created by Maclaine on 2017/7/27.
 */
public class WalkmanCourseActivity extends PermissionActivityBase {
    private static final String TAG = "WalkmanCourseActivity";
    @ViewId(R.id.imgBack)
    private ImageView imgBack;
    @ViewId(R.id.txtTitle)
    private TextView txtTitle;
    @ViewId(R.id.viewList)
    private ListView viewList;
    Map<Integer, Course> courseMap;
    String typeName;
    private CourseDownloadInfoDao courseDownloadInfoDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s1_1_activity_walkmancourse);
        Intent intent = this.getIntent();
        typeName = (String) intent.getSerializableExtra("typeName");
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
        courseMap = WalkmanMediaUtil.initCourse(WalkmanCourseActivity.this, WalkmanMediaUtil.ALL);
        init();
        this.setResult(1111);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        txtTitle.setText(typeName);
        List<WalkmanItem> itemList = new ArrayList<>();
        for (Map.Entry<Integer, Course> entry : courseMap.entrySet()) {
            if (!entry.getValue().getCategoryTitle().equals(typeName)) {
                continue;
            }
            itemList.add(new WalkmanItem(entry.getValue()));
        }
        WalkmanItemAdapter adapter = new WalkmanItemAdapter(WalkmanCourseActivity.this, itemList, R.layout.s1_1_item_walkmancourse);
        adapter.setClickListener((list, position) -> {
            Course course = courseMap.get(list.get(position).getId());
            Intent intent = new Intent(WalkmanCourseActivity.this, WalkmanLessonActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("course", course);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        adapter.setDeleteListener((list, position) -> {
            permissions(() -> {
                try {
                    Integer courseId = (Integer) list.get(position).getId();
                    List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryAllByCourseId(courseId);
                    int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                    switch (downflg) {
                        case 1://下载中
                        case 3://等待中
                            WalkmanCourseActivity.this.bindService(new Intent(WalkmanCourseActivity.this, DownloadService.class),
                                    new ServiceConnection() {
                                        ServiceConnection that = this;

                                        @Override
                                        public void onServiceConnected(ComponentName name, IBinder service) {
                                            DownloadService mDownloadService = ((DownloadService.MsgBinder) service).getService();
                                            mDownloadService.stopDownload(courseDownloadInfoList);
                                            WalkmanCourseActivity.this.unbindService(that);
                                        }

                                        @Override
                                        public void onServiceDisconnected(ComponentName name) {

                                        }
                                    }
                                    , Context.BIND_AUTO_CREATE);
                            break;
                    }
                    Observable.timer(500, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                if (courseDownloadInfoList != null) {
                                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                                        SDCardHelper.removeFileFromRom(courseDownloadInfo.getFilePath() + "/" + courseDownloadInfo.getfileName());
                                        courseDownloadInfoDao.delete(courseDownloadInfo);
                                    }
                                    itemList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                                courseMap.remove(courseId);
                            }, throwable -> {
                                Log.e(TAG, throwable.getMessage());
                            });
                } catch (SQLException e) {
                    Toast.makeText(WalkmanCourseActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        });
        viewList.setAdapter(adapter);
    }

    private void initListener() {
        //返回键
        imgBack.setOnClickListener(view -> super.onBackPressed());
    }

    private void permissions(CallBack callBack){
        requestPermissions(PermissionGroup.STORAGE, true, new CallbackListener() {
            @Override
            public void onPermissionsOK(int requestCode, List<String> perms) {
                callBack.accept();
            }

            @Override
            public void onPermissionsNG(int requestCode, List<String> perms) {
                Toast.makeText(WalkmanCourseActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {
                Toast.makeText(WalkmanCourseActivity.this,"授权取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface CallBack{
        public void accept();
    }
}
