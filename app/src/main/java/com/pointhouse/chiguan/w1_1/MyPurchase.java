package com.pointhouse.chiguan.w1_1;

/**
 * Created by gyh on 2017/8/4.
 */

public class MyPurchase {
    private boolean isDate;
    // 0：课程 1：文章
    private int dataType;

    private String date;
    private String title;
    private String categoryTitle;
    private String thumb;
    private int id;
    private int categoryId;
    // 包含课程数
    private int lessonNum;
    // 收藏人数
    private int favoriteNum;
    // 0：文本 1：音频 2：视频 3：文本+音频 4：文本+视频 5：音频+视频 6：文本+视频+音频
    private int lessonType;
    // 0：文本 1：音频 2：视频 频 4：音频+视频
    private int contentType;
    private double price;
    private int studentNum;
    private int buyersNum;
    // 阅读数
    private int reading;
    private int type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(int lessonNum) {
        this.lessonNum = lessonNum;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public int getLessonType() {
        return lessonType;
    }

    public void setLessonType(int lessonType) {
        this.lessonType = lessonType;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getBuyersNum() {
        return buyersNum;
    }

    public void setBuyersNum(int buyersNum) {
        this.buyersNum = buyersNum;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean date) {
        isDate = date;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}
