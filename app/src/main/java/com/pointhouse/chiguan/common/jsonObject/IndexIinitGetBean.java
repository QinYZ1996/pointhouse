package com.pointhouse.chiguan.common.jsonObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljj on 2017/7/25.
 */
public class IndexIinitGetBean {
    JSONObject json;
    private Boolean hasError = true;

    public Boolean gethasError() {
        return hasError;
    }

    public void sethasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public IndexIinitGetBean(String str){
        JSONTokener jsonParser = new JSONTokener(str);
        try {
            json = (JSONObject) jsonParser.nextValue();
            if(json.getInt("resultCode") == 1){
                hasError = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ResultObjectBean.CourseCategoryBean> getCourseCategoryList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.CourseCategoryBean>>() {
        }.getType();

        List<ResultObjectBean.CourseCategoryBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("courseCategory"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ResultObjectBean.KvBean> getKvList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.KvBean>>() {
        }.getType();

        List<ResultObjectBean.KvBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("kv"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ResultObjectBean.RecomenedCourseBean> getRecomenedCourseList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.RecomenedCourseBean>>() {
        }.getType();

        List<ResultObjectBean.RecomenedCourseBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("recomenedCourse"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ResultObjectBean.RecomenedArticleBean> getRecomenedArticleList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.RecomenedArticleBean>>() {
        }.getType();

        List<ResultObjectBean.RecomenedArticleBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("recomenedArticle"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"courseCategory":[{"abouts":"课程分类","createDatetime":"2017-07-19 09:15:14.0","id":6,"sort":1,"status":1,"thumb":"分类缩略图","title":"课程分类一"},{"abouts":"课程分类","createDatetime":"2017-07-19 09:15:14.0","id":1,"sort":2,"status":1,"thumb":"分类缩略图","title":"课程分类二"}],"kv":[{"categoryId":5,"categoryTitle":"文章分类三","id":1,"kvThumb":"KV缩略图","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"课程缩略图","title":"推广DEMO课程","type":0}],"recomenedCourse":[{"categoryId":5,"categoryTitle":"文章分类三","id":1,"kvThumb":"KV缩略图","lessonNum":10,"lessonType":0,"price":12,"studentNum":12,"thumb":"课程缩略图","title":"推荐DEMO课程","type":0}],"recomenedArticle":[{"categoryId":6,"categoryTitle":"文章分类二","id":2,"favoriteNum":10,"contentType":0,"price":12,"buyersNum":12,"reading":12,"thumb":"文章缩略图","title":"DEMO文章","type":0}]}
     */

    private int resultCode;
    private ResultObjectBean resultObject;
    private List<?> exceptions;
    private List<?> messages;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public ResultObjectBean getResultObject() {
        return resultObject;
    }

    public void setResultObject(ResultObjectBean resultObject) {
        this.resultObject = resultObject;
    }

    public List<?> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<?> exceptions) {
        this.exceptions = exceptions;
    }

    public List<?> getMessages() {
        return messages;
    }

    public void setMessages(List<?> messages) {
        this.messages = messages;
    }

    public static class ResultObjectBean {
        private List<CourseCategoryBean> courseCategory;
        private List<KvBean> kv;
        private List<RecomenedCourseBean> recomenedCourse;
        private List<RecomenedArticleBean> recomenedArticle;

        public List<CourseCategoryBean> getCourseCategory() {
            return courseCategory;
        }

        public void setCourseCategory(List<CourseCategoryBean> courseCategory) {
            this.courseCategory = courseCategory;
        }

        public List<KvBean> getKv() {
            return kv;
        }

        public void setKv(List<KvBean> kv) {
            this.kv = kv;
        }

        public List<RecomenedCourseBean> getRecomenedCourse() {
            return recomenedCourse;
        }

        public void setRecomenedCourse(List<RecomenedCourseBean> recomenedCourse) {
            this.recomenedCourse = recomenedCourse;
        }

        public List<RecomenedArticleBean> getRecomenedArticle() {
            return recomenedArticle;
        }

        public void setRecomenedArticle(List<RecomenedArticleBean> recomenedArticle) {
            this.recomenedArticle = recomenedArticle;
        }

        public static class CourseCategoryBean {
            /**
             * abouts : 课程分类
             * createDatetime : 2017-07-19 09:15:14.0
             * id : 6
             * sort : 1
             * status : 1
             * thumb : 分类缩略图
             * title : 课程分类一
             */

            private String abouts;
            private String createDatetime;
            private int id;
            private int sort;
            private int status;
            private String thumb;
            private String title;

            public String getAbouts() {
                return abouts;
            }

            public void setAbouts(String abouts) {
                this.abouts = abouts;
            }

            public String getCreateDatetime() {
                return createDatetime;
            }

            public void setCreateDatetime(String createDatetime) {
                this.createDatetime = createDatetime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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
        }

        public static class KvBean {
            /**
             * categoryId : 5
             * categoryTitle : 文章分类三
             * id : 1
             * kvThumb : KV缩略图
             * lessonNum : 10
             * lessonType : 0
             * price : 12
             * status : 1
             * studentNum : 12
             * thumb : 课程缩略图
             * title : 推广DEMO课程
             * type : 0
             */

            private int categoryId;
            private String categoryTitle;
            private int id;
            private String kvThumb;
            private int lessonNum;
            private int lessonType;
            private double price;
            private int status;
            private int studentNum;
            private String thumb;
            private String title;
            private int type;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryTitle() {
                return categoryTitle;
            }

            public void setCategoryTitle(String categoryTitle) {
                this.categoryTitle = categoryTitle;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKvThumb() {
                return kvThumb;
            }

            public void setKvThumb(String kvThumb) {
                this.kvThumb = kvThumb;
            }

            public int getLessonNum() {
                return lessonNum;
            }

            public void setLessonNum(int lessonNum) {
                this.lessonNum = lessonNum;
            }

            public int getLessonType() {
                return lessonType;
            }

            public void setLessonType(int lessonType) {
                this.lessonType = lessonType;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getStudentNum() {
                return studentNum;
            }

            public void setStudentNum(int studentNum) {
                this.studentNum = studentNum;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class RecomenedCourseBean {
            /**
             * categoryId : 5
             * categoryTitle : 文章分类三
             * id : 1
             * kvThumb : KV缩略图
             * lessonNum : 10
             * lessonType : 0
             * price : 12
             * studentNum : 12
             * thumb : 课程缩略图
             * title : 推荐DEMO课程
             * type : 0
             */

            private int categoryId;
            private String categoryTitle;
            private int id;
            private String kvThumb;
            private int lessonNum;
            private int lessonType;
            private double price;
            private int studentNum;
            private String thumb;
            private String title;
            private int type;
            private int buyersNum;

            public int getBuyersNum() {
                return buyersNum;
            }

            public void setBuyersNum(int buyersNum) {
                this.buyersNum = buyersNum;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryTitle() {
                return categoryTitle;
            }

            public void setCategoryTitle(String categoryTitle) {
                this.categoryTitle = categoryTitle;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKvThumb() {
                return kvThumb;
            }

            public void setKvThumb(String kvThumb) {
                this.kvThumb = kvThumb;
            }

            public int getLessonNum() {
                return lessonNum;
            }

            public void setLessonNum(int lessonNum) {
                this.lessonNum = lessonNum;
            }

            public int getLessonType() {
                return lessonType;
            }

            public void setLessonType(int lessonType) {
                this.lessonType = lessonType;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class RecomenedArticleBean {
            /**
             * categoryId : 6
             * categoryTitle : 文章分类二
             * id : 2
             * favoriteNum : 10
             * contentType : 0
             * price : 12
             * buyersNum : 12
             * reading : 12
             * thumb : 文章缩略图
             * title : DEMO文章
             * type : 0
             */

            private int categoryId;
            private String categoryTitle;
            private int id;
            private int favoriteNum;
            private int contentType;
            private double price;
            private int buyersNum;
            private int reading;
            private String thumb;
            private String title;
            private int type;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryTitle() {
                return categoryTitle;
            }

            public void setCategoryTitle(String categoryTitle) {
                this.categoryTitle = categoryTitle;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getFavoriteNum() {
                return favoriteNum;
            }

            public void setFavoriteNum(int favoriteNum) {
                this.favoriteNum = favoriteNum;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
