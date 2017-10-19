package com.pointhouse.chiguan.k1_3;

import java.io.Serializable;
import java.util.List;

/**
 * 课程
 * Created by Maclaine on 2017/7/25.
 */

public class Course implements Serializable{
    //随时学使用
    private Integer id;
    //通用
    private Lesson lesson;
    //课程列表-随时学使用
    private List<Lesson> lessonList;
    //缩略图地址-随时学使用
    private String thumb;
    //标题-随时学使用
    private String title;
    //课程类型ID-随时学使用
    private Integer categoryId;
    //课程类型名称-随时学使用
    private String categoryTitle;
    //课程数量-随时学使用
    private Integer lessonNum;
    //0：文本 1：音频 2：视频 3：文本+音频 4：文本+视频 5：音频+视频 6：文本+视频+音频
    private Integer lessonType;

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Integer getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(Integer lessonNum) {
        this.lessonNum = lessonNum;
    }

    public Integer getLessonType() {
        return lessonType;
    }

    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }
}
