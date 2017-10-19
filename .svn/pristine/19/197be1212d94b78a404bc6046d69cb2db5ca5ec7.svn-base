package com.pointhouse.chiguan.k1_12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.jsonObject.ExerciseListGetBean;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.ExerciseInfo;
import com.pointhouse.chiguan.db.ExerciseInfoDao;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gyh on 2017/7/24.
 */

public class ExerciseListActivity extends MediaBaseActivity {

    private static final String TAG = "K1-12(List)";
    private int lessonId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_12_activity_exerciselist);

        Intent intent = getIntent();
        int lessonId = intent.getIntExtra("lessonId", -1);
        this.lessonId = lessonId;
        // 试听画面进来时需要传值
        if ("true".equals(intent.getStringExtra("clearFlag")))
            clearExercises();

        initView(lessonId);
    }

    private void initView(int lessonId) {
        RelativeLayout rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        rlBack.setOnClickListener(v -> {
            try {
                new ExerciseInfoDao(this, ExerciseInfo.class).deleteExerciseInfosByLessonId(lessonId);
            } catch (SQLException e) {
                Log.e(TAG, "删除失败");
                e.printStackTrace();
            }
            finish();
        });

        ExerciseListAdapter adapter = new ExerciseListAdapter(this, lessonId);
        ListView ceList = (ListView) findViewById(R.id.lv_exerciselist);
        ceList.setAdapter(adapter);

        // 数据绑定
        String url = JsonUtil.getURLWithArrayParamIfExists("exercisesList", String.valueOf(lessonId));
        RetrofitFactory.getInstance().getRequestServicesToken().get(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            ExerciseListGetBean exerciseList = new ExerciseListGetBean(result.toJSONString());
//                            ExerciseListGetBean exerciseList = new ExerciseListGetBean(TestExerCiseListGetBean.normalJson); //for test
                            if (exerciseList.hasError()) {
                                ToastUtil.getToast(this, JsonUtil.getRequestErrMsg(TAG, this, result), "center", 0, 180).show();
                            } else {
                                List<ExerciseListGetBean.ResultObjectBean.ExercisesBean> dataList = exerciseList.getExercisesBean();
                                if (dataList == null || dataList.size() == 0) {
                                    ceList.setEmptyView(View.inflate(this, R.layout.common_empty, (ViewGroup) ceList.getParent()));
                                } else {
                                    adapter.addAllExercise(dataList);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Log.e(TAG, throwable.getMessage());
                            ToastUtil.getToast(this, getString(R.string.req_fail_msg), "center", 0, 180).show();
                        });
    }

    /**
     * 删除选项
     */
    private void clearExercises() {
        try {
            new ExerciseInfoDao(this, ExerciseInfo.class).deleteExerciseInfosByLessonId(lessonId);
        } catch (SQLException e) {
            Log.e(TAG, "删除失败");
            e.printStackTrace();
        }
    }
}
