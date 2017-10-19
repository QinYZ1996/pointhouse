package com.pointhouse.chiguan.k1_12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.jsonObject.ExerciseDetailGetBean;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.ExerciseInfo;
import com.pointhouse.chiguan.db.ExerciseInfoDao;
import com.pointhouse.chiguan.k1_13.ArticleActivity;
import com.pointhouse.chiguan.k1_13.AudioActivity;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Lesson;
import com.pointhouse.chiguan.k1_3.Media;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gyh on 2017/7/25.
 */

public class ExerciseActivity extends MediaBaseActivity {

    private static final String TAG = "K1-12(%s)";
    private int layoutType;
    private int lessonId;
    private int exerciseId;
    private ListView exListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_12_activity_exercise);

        Intent intent = getIntent();
        ExerciseParameter param = intent.getParcelableExtra("param");
        lessonId = intent.getIntExtra("lessonId", -1);
        initView(param);
    }

    private void initView(ExerciseParameter param) {
        exerciseId = param.getExerciseId();
        layoutType = param.getLayoutType();
        exListView = (ListView) findViewById(R.id.lv_exerciselist);
        ExerciseAdapter adapter = new ExerciseAdapter(this, lessonId, param);
        exListView.setAdapter(adapter);

        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getExerciseTitle(param));

        RelativeLayout rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        rlBack.setOnClickListener(v -> doBack());

        RelativeLayout rlAudio = (RelativeLayout) findViewById(R.id.rl_audio);
        RelativeLayout rlVideo = (RelativeLayout) findViewById(R.id.rl_video);
        RelativeLayout rlArticle = (RelativeLayout) findViewById(R.id.rl_article);
        final int type = param.getType();
        switch (type) {
            case 1: // 听力题
                rlArticle.setVisibility(View.GONE);
                rlVideo.setVisibility(View.GONE);
                break;
            case 2: // 材料题
                rlAudio.setVisibility(View.GONE);
                rlVideo.setVisibility(View.GONE);
                break;
            case 3: // 视频题
                rlAudio.setVisibility(View.GONE);
                rlArticle.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        if (ExerciseUtil.isAnswerLayout(param.getLayoutType())) {
            switch (type) {
                case 1: // 听力题
                    rlAudio.setOnClickListener(v -> {
                        // k1_13 音频&文本
                        Intent intent = new Intent(this, AudioActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mediaOption", param.getMediaOption());
                        intent.putExtra("type", type);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });
                    break;
                case 2: // 材料题
                    rlArticle.setOnClickListener(v -> {
                        // k1_13 文本
                        Intent intent = new Intent(this, ArticleActivity.class);
                        intent.putExtra("article", param.getArticle());
                        startActivity(intent);
                    });
                    break;
                case 3: // 视频题
                    rlVideo.setOnClickListener(v -> {
                        // k1_13 音频&文本
                        Intent intent = new Intent(this, AudioActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mediaOption", param.getMediaOption());
                        intent.putExtra("type", type);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });
                    break;
                default:
                    break;
            }

            adapter.addAnswer(param.getExerciseList());
        } else {
            String url = JsonUtil.getURLWithArrayParamIfExists("exercisesDetail", String.valueOf(exerciseId));
            RetrofitFactory.getInstance().getRequestServicesToken().get(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        ExerciseDetailGetBean exerciseDetail = new ExerciseDetailGetBean(result.toJSONString());
//                        //for test
//                        final ExerciseDetailGetBean exerciseDetail = param.getLayoutType() == ExerciseParameter.LAYOUT_QA ?
//                                new ExerciseDetailGetBean(TestExerciseDetailGetBean.normalQAJson) : new ExerciseDetailGetBean(TestExerciseDetailGetBean.normalChoiceJson);

                        if (exerciseDetail.hasError()) {
                            ToastUtil.getToast(this, JsonUtil.getRequestErrMsg(String.format(TAG, param.getLayoutType()), this, result), "center", 0, 180).show();
                        } else {
                            CommonMediaOption mediaOption;
                            switch (type) {
                                case 1: // 听力题
                                    mediaOption = getMediaOption(type, exerciseDetail);
                                    param.setMediaOption(mediaOption);
                                    rlAudio.setOnClickListener(v -> {
                                        // k1_13 音频&文本
                                        Intent intent = new Intent();
                                        intent.setClass(this, AudioActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("mediaOption", mediaOption);
                                        intent.putExtra("type", type);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    });
                                    break;
                                case 2: // 材料题
                                    String article = exerciseDetail.getResultObjectBean().getArtice();
                                    param.setArticle(article);
                                    rlArticle.setOnClickListener(v -> {
                                        // k1_13 文本
                                        Intent intent = new Intent(this, ArticleActivity.class);
                                        intent.putExtra("article", article);
                                        startActivity(intent);
                                    });
                                    break;
                                case 3: // 视频题
                                    mediaOption = getMediaOption(type, exerciseDetail);
                                    param.setMediaOption(mediaOption);
                                    rlVideo.setOnClickListener(v -> {
                                        // k1_13 音频&文本
                                        Intent intent = new Intent();
                                        intent.setClass(this, AudioActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("mediaOption", mediaOption);
                                        intent.putExtra("type", type);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    });
                                default:
                                    break;
                            }

                            switch (param.getLayoutType()) {
                                case ExerciseParameter.LAYOUT_QA:
                                    for (int i = 0; i < exerciseDetail.getStemBeanList().size(); i++) {
                                        adapter.addQA(String.valueOf(i + 1), exerciseDetail.getStemBeanList().get(i));
                                    }
                                    applyQAExerciseStatus();
                                    adapter.addButton();
                                    break;
                                case ExerciseParameter.LAYOUT_CHOICE:
                                    for (int i = 0; i < exerciseDetail.getStemBeanList().size(); i++) {
                                        adapter.addChoice(String.valueOf(i + 1), exerciseDetail.getStemBeanList().get(i));
                                    }
                                    applyChoiceExerciseStatus();
                                    adapter.addButton();
                                    break;
                                default:
                                    break;
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }, throwable -> {
                        Log.e(String.format(TAG, param.getLayoutType()), throwable.getMessage());
                        ToastUtil.getToast(this, getString(R.string.req_fail_msg), "center", 0, 180).show();
                    });
        }
    }

    /**
     * 初始化视频控件所需参数
     *
     * @param type           类型( 1：听力题 2：材料题 3：视频题)
     * @param exerciseDetail
     * @return
     */
    private CommonMediaOption getMediaOption(int type, ExerciseDetailGetBean exerciseDetail) {
        CommonMediaOption option = new CommonMediaOption();
        option.setId(1);
//        option.setShowRepeat(false);
        option.setShowExerciseClose(true);
        option.setShowCourseList(false);
        Course course = new Course();
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setTitle("");
        course.setLesson(lesson);
        option.setId(-1);
        option.setCourse(course);

        List<Media> medias = new ArrayList<>();
        switch (type) {
            case 1:
                option.setType(CommonMediaOption.MUSIC_ONLY);
                lesson.setAudioList(medias);
                List<ExerciseDetailGetBean.ResultObjectBean.AudiosBean> audiosBeanList = exerciseDetail.getResultObjectBean().getAudios();
                if (audiosBeanList != null && audiosBeanList.size() > 0) {
                    for (ExerciseDetailGetBean.ResultObjectBean.AudiosBean eb : audiosBeanList) {
                        Media audio = new Media();
                        audio.setVid(Integer.parseInt(eb.getVid()));
                        audio.setVideoName(eb.getVideoName());
                        audio.setOrigUrl(eb.getOrigUrl());
                        audio.setOriginal(eb.getOriginal());
                        medias.add(audio);
                    }

                    option.setMediaID(medias.get(0).getVid());
                }
                break;
            case 3:
                option.setType(CommonMediaOption.VIDEO_ONLY);
                lesson.setVideoList(medias);
                List<ExerciseDetailGetBean.ResultObjectBean.VideosBean> videosBeanList = exerciseDetail.getResultObjectBean().getVideos();
                if (videosBeanList != null && videosBeanList.size() > 0) {
                    for (ExerciseDetailGetBean.ResultObjectBean.VideosBean vb : videosBeanList) {
                        Media audio = new Media();
                        audio.setVid(Integer.parseInt(vb.getVid()));
                        audio.setVideoName(vb.getVideoName());
                        audio.setOrigUrl(vb.getOrigUrl());
                        audio.setOriginal(vb.getOriginal());
                        medias.add(audio);
                    }

                    option.setMediaID(medias.get(0).getVid());
                }
                break;
            default:
                break;
        }

        return option;
    }

    /**
     * 画面标题
     *
     * @param param
     * @return
     */
    private String getExerciseTitle(ExerciseParameter param) {
        switch (param.getLayoutType()) {
            case ExerciseParameter.LAYOUT_QA:
            case ExerciseParameter.LAYOUT_QA_ANSWER:
                return "问答题";
            case ExerciseParameter.LAYOUT_CHOICE:
            case ExerciseParameter.LAYOUT_CHOICE_ANSWER:
                return "选择题";
            default:
                return null;
        }
    }

    /**
     * 返回处理
     */
    private void doBack() {
        switch (layoutType) {
            case ExerciseParameter.LAYOUT_QA_ANSWER:
            case ExerciseParameter.LAYOUT_CHOICE_ANSWER:
                Intent intent = new Intent(this, ExerciseListActivity.class);
                // 设置启动模式：FLAG_ACTIVITY_CLEAR_TOP
                // Activity栈中如果存在该Activity实例，就把该Activity以及在它上面的Activity都销毁掉然后重新创建该Activity的新实例
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("lessonId", lessonId);
                startActivity(intent);
                break;
            case ExerciseParameter.LAYOUT_QA:
                saveQAExerciseStatus();
                finish();
                break;
            case ExerciseParameter.LAYOUT_CHOICE:
                saveChoiceExerciseStatus();
                finish();
                break;
            default:
                finish();
        }
    }

    /**
     * 保存问答题答题状态
     */
    private void saveQAExerciseStatus() {
        ExerciseAdapter adapter = (ExerciseAdapter) exListView.getAdapter();
        List<Exercise> exercises = adapter.getExerciseList();
        if (exercises.size() == 0) return;

        try {
            new ExerciseInfoDao(this, ExerciseInfo.class).saveExerciseInfos(ExerciseUtil.getQAExerciseInfos(lessonId, exerciseId, exercises));
        } catch (SQLException e) {
            Log.e(String.format(TAG, layoutType), "状态保存失败");
            e.printStackTrace();
        }
    }

    /**
     * 保存选择题答题状态
     */
    private void saveChoiceExerciseStatus() {
        ExerciseAdapter adapter = (ExerciseAdapter) exListView.getAdapter();
        List<Exercise> exercises = adapter.getExerciseList();
        if (exercises.size() == 0) return;

        try {
            new ExerciseInfoDao(this, ExerciseInfo.class).saveExerciseInfos(ExerciseUtil.getChoiceExerciseInfos(lessonId, exerciseId, exercises));
        } catch (SQLException e) {
            Log.e(String.format(TAG, layoutType), "状态保存失败");
            e.printStackTrace();
        }
    }

    /**
     * 设定问答题答题状态
     */
    private void applyQAExerciseStatus() {
        List<ExerciseInfo> exerciseInfos = null;
        try {
            exerciseInfos = new ExerciseInfoDao(this, ExerciseInfo.class).queryExerciseInfosByExerciseId(exerciseId);
        } catch (SQLException e) {
            Log.e(String.format(TAG, layoutType), "状态取得失败");
            e.printStackTrace();
        }

        Map<String, ExerciseInfo> infoMap = ExerciseUtil.getExerciseInfoMap(exerciseInfos);
        if (infoMap != null) {
            ExerciseAdapter adapter = (ExerciseAdapter) exListView.getAdapter();
            List<Exercise> exercises = adapter.getExerciseList();
            for (Exercise e : exercises) {
                if (infoMap.containsKey(e.getQuestionNo())) {
                    ExerciseInfo ei = infoMap.get(e.getQuestionNo());
                    e.setQaUserAnswer(ei.getUserAnswer());
                }
            }
        }

        deleteExerciseStatus();
    }

    /**
     * 设定选择题答题状态
     */
    private void applyChoiceExerciseStatus() {
        List<ExerciseInfo> exerciseInfos = null;
        try {
            exerciseInfos = new ExerciseInfoDao(this, ExerciseInfo.class).queryExerciseInfosByExerciseId(exerciseId);
        } catch (SQLException e) {
            Log.e(String.format(TAG, layoutType), "状态取得失败");
            e.printStackTrace();
        }

        Map<String, ExerciseInfo> infoMap = ExerciseUtil.getExerciseInfoMap(exerciseInfos);
        if (infoMap != null) {
            ExerciseAdapter adapter = (ExerciseAdapter) exListView.getAdapter();
            List<Exercise> exercises = adapter.getExerciseList();
            for (Exercise e : exercises) {
                if (infoMap.containsKey(e.getQuestionNo())) {
                    ExerciseInfo ei = infoMap.get(e.getQuestionNo());
                    List<String> userAnswer = ExerciseUtil.getChoiceUserAnswerList(ei.getUserAnswer());
                    if (userAnswer != null && userAnswer.size() > 0) {
                        e.setChoiceUserAnswer(userAnswer);
                        for (Option option : e.getOptions()) {
                            option.setSelected(userAnswer.contains(option.getOptionNo()));
                        }
                    }
                }
            }
        }

        deleteExerciseStatus();
    }

    /**
     * 删除答题状态
     */
    private void deleteExerciseStatus() {
        try {
            new ExerciseInfoDao(this, ExerciseInfo.class).deleteExerciseInfosByExerciseId(exerciseId);
        } catch (SQLException e) {
            Log.e(String.format(TAG, layoutType), "状态删除失败");
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        doBack();
        super.onBackPressed();
    }
}
