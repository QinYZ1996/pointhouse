package com.pointhouse.chiguan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ljj on 2017/7/18.
 * 备注删除课程时请删除数据
 *
 */
@DatabaseTable
public class CourseDownloadInfo {
    @DatabaseField(generatedId=true)
    private Integer id;
    @DatabaseField()
    private String url;
    @DatabaseField()
    private String fileName;
    @DatabaseField()
    private int lessonId;
    @DatabaseField()
    private long Progress;
    @DatabaseField()
    private long Totle;
    @DatabaseField()
    private String vid;
    /**
     * 1下载中，2暂停中，3等待中，4下载结束, 5下载失败
     */
    @DatabaseField()
    private int State;
    @DatabaseField()
    private String filePath;
    @DatabaseField()
    private String errorMessage;
    @DatabaseField()
    private int index;
    @DatabaseField()
    private long fileCount;
    @DatabaseField()
    private long fileSize;
    @DatabaseField()
    private long fileProgress;
    @DatabaseField()
    private int courseId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getFileProgress() {
        return fileProgress;
    }

    public void setFileProgress(long fileProgress) {
        this.fileProgress = fileProgress;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getFileCount() {
        return fileCount;
    }

    public void setFileCount(long fileCount) {
        this.fileCount = fileCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }


    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }


    public String getfileName() {
        return fileName;
    }

    public void setfileName(String fileName) {
        this.fileName = fileName;
    }


    public long getProgress() {
        return Progress;
    }

    public void setProgress(long Progress) {
        this.Progress = Progress;
    }


    public long getTotle() {
        return Totle;
    }

    public void setTotle(long Totle) {
        this.Totle = Totle;
    }


    /**
     * DFF
     */
    @DatabaseField()
    private Integer integerDFF1;

    public Integer getIntegerDFF1() {
        return integerDFF1;
    }

    public void setIntegerDFF1(Integer integerDFF1) {
        this.integerDFF1 = integerDFF1;
    }

    @DatabaseField()
    private Integer integerDFF2;

    public Integer getIntegerDFF2() {
        return integerDFF2;
    }

    public void setIntegerDFF2(Integer integerDFF2) {
        this.integerDFF2 = integerDFF2;
    }

    @DatabaseField()
    private Integer integerDFF3;

    public Integer getIntegerDFF3() {
        return integerDFF3;
    }

    public void setIntegerDFF3(Integer integerDFF3) {
        this.integerDFF3 = integerDFF3;
    }

    @DatabaseField()
    private String stringDFF1;

    public String getStringDFF1() {
        return stringDFF1;
    }

    public void setStringDFF1(String stringDFF1) {
        this.stringDFF1 = stringDFF1;
    }

    @DatabaseField()
    private String stringDFF2;

    public String getStringDFF2() {
        return stringDFF2;
    }

    public void setStringDFF2(String stringDFF2) {
        this.stringDFF2 = stringDFF2;
    }

    @DatabaseField()
    private String stringDFF3;

    public String getStringDFF3() {
        return stringDFF3;
    }

    public void setStringDFF3(String stringDFF3) {
        this.stringDFF3 = stringDFF3;
    }
}
