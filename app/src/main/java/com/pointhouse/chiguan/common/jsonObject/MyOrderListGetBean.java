package com.pointhouse.chiguan.common.jsonObject;

import java.util.List;

/**
 * Created by gyh on 2017/8/4.
 */

public class MyOrderListGetBean {

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : [{"article":[{"categoryId":29,"categoryTitle":"语言艺术","id":2,"favoriteNum":10,"contentType":0,"price":12,"buyersNum":12,"reading":12,"thumb":"文章缩略图","title":"DEMO文章","type":0}],"course":[{"buyersNum":13,"categoryId":3,"categoryTitle":"语言触感","favoriteNum":0,"id":3,"lessonNum":4,"lessonType":2,"price":0.01,"studentNum":13,"thumb":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/course/2017/08/291503972248689980.png","title":"演说家之夜","type":1}],"date":"2017年08月21日"},{"article":[],"course":[{"buyersNum":26,"categoryId":1,"categoryTitle":"语言艺术","favoriteNum":0,"id":2,"lessonNum":5,"lessonType":6,"price":0.01,"studentNum":26,"thumb":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/course/2017/09/011504249110747753.png","title":"雪梨读诗I","type":1},{"buyersNum":14,"categoryId":2,"categoryTitle":"语言技术","favoriteNum":0,"id":4,"lessonNum":8,"lessonType":6,"price":0.01,"studentNum":24,"thumb":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/course/2017/08/291503972227397789.png","title":"21世纪应用型英语视听说课","type":2}],"date":"2017年08月20日"},{"article":[],"course":[{"buyersNum":29,"categoryId":1,"categoryTitle":"语言艺术","favoriteNum":0,"id":1,"lessonNum":4,"lessonType":3,"price":0.01,"studentNum":30,"thumb":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/course/2017/09/011504248851815821.png","title":"金雯时间I","type":1}],"date":"2017年08月19日"}]
     */

    private int resultCode;
    private List<?> exceptions;
    private List<?> messages;
    private List<ResultObjectBean> resultObject;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
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

    public List<ResultObjectBean> getResultObject() {
        return resultObject;
    }

    public void setResultObject(List<ResultObjectBean> resultObject) {
        this.resultObject = resultObject;
    }

    public static class ResultObjectBean {
        /**
         * article : [{"categoryId":29,"categoryTitle":"语言艺术","id":2,"favoriteNum":10,"contentType":0,"price":12,"buyersNum":12,"reading":12,"thumb":"文章缩略图","title":"DEMO文章","type":0}]
         * course : [{"buyersNum":13,"categoryId":3,"categoryTitle":"语言触感","favoriteNum":0,"id":3,"lessonNum":4,"lessonType":2,"price":0.01,"studentNum":13,"thumb":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/course/2017/08/291503972248689980.png","title":"演说家之夜","type":1}]
         * date : 2017年08月21日
         */

        private String date;
        private List<ArticleBean> article;
        private List<CourseBean> course;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ArticleBean> getArticle() {
            return article;
        }

        public void setArticle(List<ArticleBean> article) {
            this.article = article;
        }

        public List<CourseBean> getCourse() {
            return course;
        }

        public void setCourse(List<CourseBean> course) {
            this.course = course;
        }

        public static class ArticleBean {
            /**
             * categoryId : 29
             * categoryTitle : 语言艺术
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

        public static class CourseBean {
            /**
             * buyersNum : 13
             * categoryId : 3
             * categoryTitle : 语言触感
             * favoriteNum : 0
             * id : 3
             * lessonNum : 4
             * lessonType : 2
             * price : 0.01
             * studentNum : 13
             * thumb : http://pointhouse.oss-cn-shanghai.aliyuncs.com/course/2017/08/291503972248689980.png
             * title : 演说家之夜
             * type : 1
             */

            private int buyersNum;
            private int categoryId;
            private String categoryTitle;
            private int favoriteNum;
            private int id;
            private int lessonNum;
            private int lessonType;
            private double price;
            private int studentNum;
            private String thumb;
            private String title;
            private int type;

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

            public int getFavoriteNum() {
                return favoriteNum;
            }

            public void setFavoriteNum(int favoriteNum) {
                this.favoriteNum = favoriteNum;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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
    }
}
