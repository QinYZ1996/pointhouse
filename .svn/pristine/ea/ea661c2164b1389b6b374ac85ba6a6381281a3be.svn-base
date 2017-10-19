package com.pointhouse.chiguan.k1_12;

import android.os.Parcel;
import android.os.Parcelable;

import com.pointhouse.chiguan.common.util.CommonMediaOption;

import java.util.List;

/**
 * Created by gyh on 2017/7/26.
 */

public class ExerciseParameter implements Parcelable {

    // 画面类型：问答题
    public static final int LAYOUT_QA = 0x00000001;
    // 画面类型：问答题答案
    public static final int LAYOUT_QA_ANSWER = 0x00000002;
    // 画面类型：选择题
    public static final int LAYOUT_CHOICE = 0x00000003;
    // 画面类型：选择题答案
    public static final int LAYOUT_CHOICE_ANSWER = 0x00000004;

    // 画面类型
    private int layoutType;
    // 题目ID
    private int exerciseId;
    // 类型( 1：听力题 2：材料题 3：视频题)
    private int type;
    // 练习题集合
    private List<Exercise> exerciseList;
    // 文本
    private String article;
    // 媒体参数
    private CommonMediaOption mediaOption;

    public static ExerciseParameter create(ExerciseParameter src) {
        if (src == null) return null;
        ExerciseParameter param = new ExerciseParameter();
        param.layoutType = src.layoutType;
        param.exerciseId = src.exerciseId;
        param.type = src.type;
        param.exerciseList = src.exerciseList;
        param.article = src.article;
        param.mediaOption = src.mediaOption;
        return param;
    }

    public CommonMediaOption getMediaOption() {
        return mediaOption;
    }

    public void setMediaOption(CommonMediaOption mediaOption) {
        this.mediaOption = mediaOption;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.layoutType);
        dest.writeInt(this.exerciseId);
        dest.writeInt(this.type);
        dest.writeTypedList(this.exerciseList);
        dest.writeString(this.article);
        dest.writeSerializable(this.mediaOption);
    }

    public ExerciseParameter() {
    }

    protected ExerciseParameter(Parcel in) {
        this.layoutType = in.readInt();
        this.exerciseId = in.readInt();
        this.type = in.readInt();
        this.exerciseList = in.createTypedArrayList(Exercise.CREATOR);
        this.article = in.readString();
        this.mediaOption = (CommonMediaOption) in.readSerializable();
    }

    public static final Parcelable.Creator<ExerciseParameter> CREATOR = new Parcelable.Creator<ExerciseParameter>() {
        @Override
        public ExerciseParameter createFromParcel(Parcel source) {
            return new ExerciseParameter(source);
        }

        @Override
        public ExerciseParameter[] newArray(int size) {
            return new ExerciseParameter[size];
        }
    };
}
