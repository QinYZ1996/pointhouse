package com.pointhouse.chiguan.k1_12;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyh on 2017/7/26.
 */

public class Exercise implements Parcelable {

    public Exercise() {
        options = new ArrayList<>();
        choiceAnswer = new ArrayList<>();
        choiceUserAnswer = new ArrayList<>();
    }

    // 是否按钮行
    private boolean isBtn;
    // 题号
    private String questionNo;
    // 问题
    private String question;
    // 选项
    private List<Option> options;
    // 选择题答案
    private List<String> choiceAnswer;
    // 选择题回答
    private List<String> choiceUserAnswer;
    // 问答题答案
    private String qaAnswer;
    // 问答题回答
    private String qaUserAnswer;

    public boolean isBtn() {
        return isBtn;
    }

    public void setBtn(boolean btn) {
        isBtn = btn;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public List<String> getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setChoiceAnswer(List<String> choiceAnswer) {
        this.choiceAnswer = choiceAnswer;
    }

    public String getQaAnswer() {
        return qaAnswer;
    }

    public void setQaAnswer(String qaAnswer) {
        this.qaAnswer = qaAnswer;
    }

    public List<String> getChoiceUserAnswer() {
        return choiceUserAnswer;
    }

    public void setChoiceUserAnswer(List<String> choiceUserAnswer) {
        this.choiceUserAnswer = choiceUserAnswer;
    }

    public String getQaUserAnswer() {
        return qaUserAnswer;
    }

    public void setQaUserAnswer(String qaUserAnswer) {
        this.qaUserAnswer = qaUserAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isBtn ? (byte) 1 : (byte) 0);
        dest.writeString(this.questionNo);
        dest.writeString(this.question);
        dest.writeList(this.options);
        dest.writeStringList(this.choiceAnswer);
        dest.writeStringList(this.choiceUserAnswer);
        dest.writeString(this.qaAnswer);
        dest.writeString(this.qaUserAnswer);
    }

    protected Exercise(Parcel in) {
        this.isBtn = in.readByte() != 0;
        this.questionNo = in.readString();
        this.question = in.readString();
        this.options = new ArrayList<>();
        in.readList(this.options, Option.class.getClassLoader());
        this.choiceAnswer = in.createStringArrayList();
        this.choiceUserAnswer = in.createStringArrayList();
        this.qaAnswer = in.readString();
        this.qaUserAnswer = in.readString();
    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
