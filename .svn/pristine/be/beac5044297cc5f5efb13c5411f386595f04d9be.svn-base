package com.pointhouse.chiguan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 我的学习
 * Created by Maclaine on 2017/8/10.
 */
@DatabaseTable
public class StudyInfo {
    @DatabaseField(generatedId = true)
    private Integer id;

    //课程专辑id
    @DatabaseField
    private Integer courseId;

    //课程专辑名称
    @DatabaseField
    private String courseName;

    //课程id
    @DatabaseField
    private Integer lessonId;

    //课程名称
    @DatabaseField
    private String lessonName;

    //媒体编号
    @DatabaseField
    private String vid;

    //媒体名称
    @DatabaseField
    private String vName;

    //当前进度
    @DatabaseField
    private Long current;

    //媒体长度
    @DatabaseField
    private Long vLength;

    //课程长度
    @DatabaseField
    private Long lLength;

    //更新时间
    @DatabaseField
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public Long getvLength() {
        return vLength;
    }

    public void setvLength(Long vLength) {
        this.vLength = vLength;
    }

    public Long getlLength() {
        return lLength;
    }

    public void setlLength(Long lLength) {
        this.lLength = lLength;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
}
