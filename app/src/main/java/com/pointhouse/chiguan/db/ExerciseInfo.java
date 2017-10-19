package com.pointhouse.chiguan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gyh on 2017/8/2.
 * 逻辑上应该题号+练习题ID可以确定一条数据
 */
@DatabaseTable
public class ExerciseInfo {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int lessonId;
    @DatabaseField
    private int exerciseId;
    @DatabaseField
    private String questionNo;
    @DatabaseField
    private String userAnswer;

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}
