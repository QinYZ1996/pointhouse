package com.pointhouse.chiguan.w1_3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.StudyInfo;
import com.pointhouse.chiguan.db.StudyInfoDao;
import com.pointhouse.chiguan.k1_3.MediaActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/7/31.
 */

public class MyStudyActivity extends MediaBaseActivity {

    private static final String TAG = "W1-3";
    // 本地请求参数：一次取得记录数
    private static final int LIMIT = 20;

    private PullToRefreshListView refreshListView;
    private MyStudyAdapter adapter;
    private boolean canReloadData;
    // 请求参数：页码
    private int page = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_3_activity_mystudy);
        initView();
    }

    public void initView() {
        adapter = new MyStudyAdapter(this);
        refreshListView = (PullToRefreshListView) findViewById(R.id.mystudy_listview);
        refreshListView.getRefreshableView().setAdapter(adapter);
        refreshListView.setPullRefreshEnabled(false);
        refreshListView.setPullLoadEnabled(false);
        refreshListView.setScrollLoadEnabled(true);
        refreshListView.getRefreshableView().setOnItemClickListener(((parent, view, position, id) -> {
            MyStudy study = (MyStudy) adapter.getItem(position);
            final List<Integer> lessonIDList = new ArrayList<>();
            RetrofitFactory.getInstance().getRequestServicesToken().get(JsonUtil.getURLWithArrayParamIfExists("courseDetail", String.valueOf(study.getCourseId())))
                    .subscribeOn(Schedulers.newThread())
                    .map(result -> {
                        String msg;
                        switch (result.getIntValue("resultCode")) {
                            case 1:
                                JSONArray lessons = result.getJSONObject("resultObject").getJSONArray("lessons");
                                if (lessons != null) {
                                    for (int i = 0; i < lessons.size(); i++) {
                                        lessonIDList.add(lessons.getJSONObject(i).getIntValue("id"));
                                    }
                                }
                                msg = "";
                                break;
                            case 2: // 课程专辑不存在或者登录过期
                                msg = JsonUtil.getRequestErrMsg(TAG, view.getContext(), result);
                                break;
                            default:
                                Log.e(TAG, "课程列表获取失败");
                                msg = JsonUtil.getRequestErrMsg(TAG, view.getContext(), result);
                                lessonIDList.add(Integer.valueOf(study.getLessonId()));
                                break;
                        }

                        return msg;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if (!lessonIDList.isEmpty()) {
                            CommonMediaOption option = new CommonMediaOption();
                            option.setType(CommonMediaOption.AUTO);
                            option.setId(Integer.valueOf(study.getLessonId()));
                            option.setShowDownload(true);
                            option.setCourseName(study.getCourseTitle());
                            option.setLessonIDList(lessonIDList);
//                        if(model.courselistDraw == 0){ // 是否是文章?
//                            option.setShowRepeat(false);
//                        }
                            Intent intent;
                            Context context = view.getContext();
                            intent = new Intent(context, MediaActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("option", option);
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                            canReloadData = true;
                        }

                        if (!"".equals(result)) {
                            ToastUtil.getToast(this, result, "center", 0, 180).show();
                        }
                    }, throwable -> {
                        Log.e(TAG, throwable.getMessage());
                        ToastUtil.getToast(this, getString(R.string.req_fail_msg), "center", 0, 180).show();
                    });
        }));

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 判断是否最后一页
                if (adapter.getCount() < page * LIMIT) {
                    refreshListView.onRefreshComplete();
                } else {
                    requestGetMyStudyListFromLocal(false);
                }
            }
        });

        requestGetMyStudyListFromLocal(true);
    }

    @Override
    public void onResume() {
        if (canReloadData) {
            adapter.clearData();
            page = 0;
            requestGetMyStudyListFromLocal(true);
            canReloadData = false;
        }
        super.onResume();
    }

    private void requestGetMyStudyListFromLocal(boolean isInit) {
        page++;
        List<StudyInfo> studyInfoList = null;
        try {
            studyInfoList = new StudyInfoDao(this).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (studyInfoList == null || studyInfoList.size() == 0) {
            ListView listView = refreshListView.getRefreshableView();
            View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) listView.getParent());
            listView.setEmptyView(listEmptyView);
            return;
        }

        // 数据整合
        LinkedHashMap<Integer, LinkedHashMap<Integer, Date>> courseDateMap = new LinkedHashMap<>(); // 存放每个课程包含的音视频中的最新日期
        LinkedHashMap<Integer, LinkedHashMap<Integer, List<StudyInfo>>> courseStudyMap = new LinkedHashMap<>(); // 保存本地数据记录
        for (StudyInfo s : studyInfoList) {
            // 保存日期
            LinkedHashMap<Integer, Date> lessonDateMap;
            if (courseDateMap.containsKey(s.getCourseId())) {
                lessonDateMap = courseDateMap.get(s.getCourseId());
            } else {
                lessonDateMap = new LinkedHashMap<>();
                courseDateMap.put(s.getCourseId(), lessonDateMap);
            }

            if (lessonDateMap.containsKey(s.getLessonId())) {
                if (s.getUpdateDate().compareTo(lessonDateMap.get(s.getLessonId())) > 0) {
                    lessonDateMap.put(s.getLessonId(), s.getUpdateDate());
                }
            } else {
                lessonDateMap.put(s.getLessonId(), s.getUpdateDate());
            }

            // 保存记录
            LinkedHashMap<Integer, List<StudyInfo>> lessonStudyMap;
            if (courseStudyMap.containsKey(s.getCourseId())) {
                lessonStudyMap = courseStudyMap.get(s.getCourseId());
            } else {
                lessonStudyMap = new LinkedHashMap<>();
                courseStudyMap.put(s.getCourseId(), lessonStudyMap);
            }

            if (lessonStudyMap.containsKey(s.getLessonId())) {
                lessonStudyMap.get(s.getLessonId()).add(s);
            } else {
                List<StudyInfo> studyList = new ArrayList<>();
                studyList.add(s);
                lessonStudyMap.put(s.getLessonId(), studyList);
            }
        }

        int startIndex = (page - 1) * LIMIT + 1;
        int endIndex = page * LIMIT;
        int currentIndex = 1;
        for (Integer courseId : courseDateMap.keySet()) {
            if (currentIndex >= startIndex && currentIndex <= endIndex) {
                LinkedHashMap<Integer, Date> lessonDateMap = courseDateMap.get(courseId);
                LinkedHashMap<Integer, List<StudyInfo>> lessonStudyMap = courseStudyMap.get(courseId);
                // 取该专辑下最新日期的课程下面的学习记录
                List<StudyInfo> targetList = null;
                Date lastDate = null;
                for (Integer lessonId : lessonDateMap.keySet()) {
                    Date date = lessonDateMap.get(lessonId);
                    if (lastDate == null || date.compareTo(lastDate) > 0) {
                        lastDate = date;
                        targetList = lessonStudyMap.get(lessonId);
                    }
                }

                // 数据转换
                List<ProcessDetail> pdList = new ArrayList<>();
                MyStudy myStudy = new MyStudy();
                myStudy.setProcessDetails(pdList);
                boolean isFirst = true;
                long current = 0; // 当前总进度
                long length = 0; // 总时长
                for (StudyInfo s : targetList) {
                    if (isFirst) { // 设置课程信息
                        isFirst = false;
                        myStudy.setCourseId(s.getCourseId());
                        myStudy.setCourseTitle(s.getCourseName());
                        myStudy.setLessonId(s.getLessonId());
                        myStudy.setLessonTitle(s.getLessonName());
                    }

                    ProcessDetail pd = new ProcessDetail();
                    pd.setCvid(s.getVid());
                    pdList.add(pd);
                    current += s.getCurrent();
                    length += s.getvLength();
                }

                if (length == 0) {
                    myStudy.setTotalProcess(String.valueOf(0));
                } else {
                    double fProcess = (double) current / length * 100;
                    double upedProcess = Math.ceil(fProcess);
                    long lProcess = (long) upedProcess;
                    myStudy.setTotalProcess(String.valueOf(lProcess > 100 ? 100 : lProcess));
                }
                adapter.addMyStudy(myStudy);
            }

            currentIndex++;
        }

        adapter.notifyDataSetChanged();
        if (!isInit) {
            refreshListView.onRefreshComplete();
        }
    }

    public void Study_back(View view) {
        super.onBackPressed();
    }
}
