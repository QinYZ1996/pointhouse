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
 * Created by ljj on 2017/7/26.
 */

public class CourseListGetBean {

    JSONObject json;

    private Boolean hasError = true;

    public Boolean gethasError() {
        return hasError;
    }

    public void sethasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public CourseListGetBean(String str){
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

    public List<ResultObjectBean.CoursesBean> getCoursesBeanList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.CoursesBean>>() {
        }.getType();

        List<ResultObjectBean.CoursesBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("courses"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"courses":[{"categoryId":6,"categoryTitle":"课程分类一","id":1,"kvThumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","title":"DEMO课程1","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":2,"kvThumb":"http://i4.piimg.com/601814/bdc5d3a22b55bea1.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/bdc5d3a22b55bea1.png","title":"DEMO课程2","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":3,"kvThumb":"http://i4.piimg.com/601814/18362f6e1216e8ea.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/18362f6e1216e8ea.png","title":"DEMO课程3","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":4,"kvThumb":"http://i4.piimg.com/601814/aebfa10a3371519a.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/aebfa10a3371519a.png","title":"DEMO课程4","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":5,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程5","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":6,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程6","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":7,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程7","type":0},{"categoryId":6,"categoryTitle":"课程分类一","id":1,"kvThumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","title":"DEMO课程8","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":2,"kvThumb":"http://i4.piimg.com/601814/bdc5d3a22b55bea1.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/bdc5d3a22b55bea1.png","title":"DEMO课程9","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":3,"kvThumb":"http://i4.piimg.com/601814/18362f6e1216e8ea.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/18362f6e1216e8ea.png","title":"DEMO课程10","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":4,"kvThumb":"http://i4.piimg.com/601814/aebfa10a3371519a.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/aebfa10a3371519a.png","title":"DEMO课程11","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":5,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":6,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":7,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类一","id":1,"kvThumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":2,"kvThumb":"http://i4.piimg.com/601814/bdc5d3a22b55bea1.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/bdc5d3a22b55bea1.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":3,"kvThumb":"http://i4.piimg.com/601814/18362f6e1216e8ea.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/18362f6e1216e8ea.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":4,"kvThumb":"http://i4.piimg.com/601814/aebfa10a3371519a.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/aebfa10a3371519a.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":5,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程","type":0},{"categoryId":6,"categoryTitle":"课程分类二","id":6,"kvThumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","lessonNum":10,"lessonType":0,"price":12,"status":1,"studentNum":12,"thumb":"http://i4.piimg.com/601814/1ae6a053d6f3ca1b.png","title":"DEMO课程","type":0}]}
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
        private List<CoursesBean> courses;

        public List<CoursesBean> getCourses() {
            return courses;
        }

        public void setCourses(List<CoursesBean> courses) {
            this.courses = courses;
        }

        public static class CoursesBean {
            /**
             * categoryId : 6
             * categoryTitle : 课程分类一
             * id : 1
             * kvThumb : http://i4.piimg.com/601814/f258764ee806eaa0.png
             * lessonNum : 10
             * lessonType : 0
             * price : 12
             * status : 1
             * studentNum : 12
             * thumb : http://i4.piimg.com/601814/f258764ee806eaa0.png
             * title : DEMO课程1
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
    }
}
