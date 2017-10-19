package com.pointhouse.chiguan.k1_12;

import com.pointhouse.chiguan.db.ExerciseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gyh on 2017/8/2.
 */

public class ExerciseUtil {

    public static final String COMMA = ",";

    /**
     * 取得对应的画面类型
     *
     * @param questionType 题目类型(1:选择题  2：问答题)
     * @return
     */
    public static int getLayoutType(int questionType) {
        return questionType == 1 ? ExerciseParameter.LAYOUT_CHOICE : ExerciseParameter.LAYOUT_QA;
    }

    /**
     * 判断是否是答案画面
     *
     * @param layoutType
     * @return
     */
    public static boolean isAnswerLayout(int layoutType) {
        return layoutType == ExerciseParameter.LAYOUT_QA_ANSWER || layoutType == ExerciseParameter.LAYOUT_CHOICE_ANSWER;
    }

    /**
     * 取得问答题答题信息
     *
     * @param lessonId
     * @param exerciseId
     * @param exercises
     * @return
     */
    public static List<ExerciseInfo> getQAExerciseInfos(int lessonId, int exerciseId, List<Exercise> exercises) {
        List<ExerciseInfo> list = null;
        if (exercises.size() > 0) {
            list = new ArrayList<>();
            for (Exercise e : exercises) {
                if (!e.isBtn()) {
                    ExerciseInfo ei = new ExerciseInfo();
                    ei.setLessonId(lessonId);
                    ei.setExerciseId(exerciseId);
                    ei.setQuestionNo(e.getQuestionNo());
                    ei.setUserAnswer(e.getQaUserAnswer());
                    list.add(ei);
                }
            }
        }

        return list;
    }

    /**
     * 取得选择题答题信息
     *
     * @param lessonId
     * @param exerciseId
     * @param exercises
     * @return
     */
    public static List<ExerciseInfo> getChoiceExerciseInfos(int lessonId, int exerciseId, List<Exercise> exercises) {
        List<ExerciseInfo> list = null;
        if (exercises.size() > 0) {
            list = new ArrayList<>();
            for (Exercise e : exercises) {
                if (!e.isBtn()) {
                    ExerciseInfo ei = new ExerciseInfo();
                    ei.setLessonId(lessonId);
                    ei.setExerciseId(exerciseId);
                    ei.setQuestionNo(e.getQuestionNo());
                    ei.setUserAnswer(getChoiceUserAnswerString(e.getChoiceUserAnswer()));
                    list.add(ei);
                }
            }
        }

        return list;
    }

    /**
     * 取得选择题用户答案字符串
     *
     * @param answers
     * @return "1,2,..."
     */
    public static String getChoiceUserAnswerString(List<String> answers) {
        if (answers.size() == 0) return null;

        StringBuilder sb = new StringBuilder();
        for (String s : answers) {
            sb.append(s + COMMA);
        }

        String result = sb.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 取得选择题用户答案集合
     *
     * @param answer
     * @return ["1", "2", ...]
     */
    public static List<String> getChoiceUserAnswerList(String answer) {
        if (answer == null || "".equals(answer)) return null;

        List<String> list = new ArrayList<>();
        for (String s : answer.split(COMMA)) {
            list.add(s);
        }

        return list;
    }

    /**
     * 取得答题状态Map
     *
     * @param infos
     * @return Key:题号,Value:答题状态信息
     */
    public static Map<String, ExerciseInfo> getExerciseInfoMap(List<ExerciseInfo> infos) {
        if (infos == null || infos.size() == 0) return null;

        Map<String, ExerciseInfo> exMap = new HashMap<>();
        for (ExerciseInfo info : infos) {
            exMap.put(info.getQuestionNo(), info);
        }

        return exMap;
    }

    /**
     * 取得表示选项号
     * asciiCode:65 => A
     *
     * @param offset
     * @return "A", "B", "C" ...
     */
    public static String getOptionNoLabel(int offset) {
        int asciiCode = 65 + offset;
        return String.valueOf((char) asciiCode);
    }
}
