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

public class CourseDetailGetBean {
    JSONObject json;

    private Boolean hasError = true;

    public Boolean gethasError() {
        return hasError;
    }

    public void sethasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public CourseDetailGetBean(String str){
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

    public ResultObjectBean.CourseBean.TeacherBean getTeacherBean() {
        if(hasError) return null;

        try {
            return new Gson().fromJson(json.getJSONObject("resultObject").getJSONObject("course").getString("teacher"), ResultObjectBean.CourseBean.TeacherBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultObjectBean.CourseBean getCourseBean() {
        if(hasError) return null;

        try {
            return new Gson().fromJson(json.getJSONObject("resultObject").getString("course"), ResultObjectBean.CourseBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ResultObjectBean.LessonsBean> getLessonsBeanList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.LessonsBean>>() {
        }.getType();

        List<ResultObjectBean.LessonsBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("lessons"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getIsPaid(){
        if(hasError) return -1;
        try {
            return json.getJSONObject("resultObject").getInt("isPaid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getIsFavorite(){
        if(hasError) return -1;

        try {
            return json.getJSONObject("resultObject").getInt("isFavorite");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public GroupBean getGroup(){
        try {
            if(json.getJSONObject("resultObject").getString("groups") == null || json.getJSONObject("resultObject").getString("groups").isEmpty()){
                return null;
            }
            JSONObject group = json.getJSONObject("resultObject").getJSONObject("groups");
            if(group != null){
                GroupBean groupBean = new GroupBean();
                groupBean.tid = group.getString("tid");
                groupBean.title = group.getString("title");
                return groupBean;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ExceptionsBean> getExceptionsBeanList() {

        Type listType = new TypeToken<ArrayList<ExceptionsBean>>() {
        }.getType();

        List<ExceptionsBean> list = null;
        try {
            list = new Gson().fromJson(json.getString("exceptions"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * exceptions : [{"id":" ","message":"课程专辑不存在！"}]
     * messages : []
     * resultCode : 1
     * resultObject : {"course":{"abouts":"DEMO课程专辑简介","categoryId":6,"categoryTitle":"课程专辑分类一","createDatetime":"1999-12-12 00:00:00.0","favoriteNum":11,"id":1,"isRecommend":1,"lessonNum":10,"lessonType":0,"price":12,"startDate":"2017-07-18","studentNum":12,"teacher":{"avatar":"头像地址","nickname":"DEMO昵称"},"thumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","tid":1,"title":"DEMO课程","type":0},"lessons":[{"courseId":1,"createDatetime":"2017-07-20 14:22:55.0","audioList":[{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}],"id":1,"learnTime":8,"thumb":"http://i4.piimg.com/601814/12317a59355b8d7c.png","title":"DEMO课程","type":0,"videoList":[{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}],"lesson_type":1},{"courseId":1,"createDatetime":"2017-07-20 14:22:55.0","audioList":[{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}],"id":1,"learnTime":800,"thumb":"http://i4.piimg.com/601814/12317a59355b8d7c.png","title":"DEMO课程2","type":1,"videoList":[{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}],"lesson_type":2},{"courseId":1,"createDatetime":"2017-07-20 14:22:55.0","audioList":[{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}],"id":1,"learnTime":8000,"thumb":"http://i4.piimg.com/601814/12317a59355b8d7c.png","title":"DEMO课程3","type":0,"videoList":[{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}],"lesson_type":4}],"isPaid":1,"isFavorite":0}
     */

    private int resultCode;
    private ResultObjectBean resultObject;
    private List<ExceptionsBean> exceptions;
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

    public List<ExceptionsBean> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ExceptionsBean> exceptions) {
        this.exceptions = exceptions;
    }

    public List<?> getMessages() {
        return messages;
    }

    public void setMessages(List<?> messages) {
        this.messages = messages;
    }

    public static class ResultObjectBean {
        /**
         * course : {"abouts":"DEMO课程专辑简介","categoryId":6,"categoryTitle":"课程专辑分类一","createDatetime":"1999-12-12 00:00:00.0","favoriteNum":11,"id":1,"isRecommend":1,"lessonNum":10,"lessonType":0,"price":12,"startDate":"2017-07-18","studentNum":12,"teacher":{"avatar":"头像地址","nickname":"DEMO昵称"},"thumb":"http://i4.piimg.com/601814/f258764ee806eaa0.png","tid":1,"title":"DEMO课程","type":0}
         * lessons : [{"courseId":1,"createDatetime":"2017-07-20 14:22:55.0","audioList":[{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}],"id":1,"learnTime":8,"thumb":"http://i4.piimg.com/601814/12317a59355b8d7c.png","title":"DEMO课程","type":0,"videoList":[{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}],"lesson_type":1},{"courseId":1,"createDatetime":"2017-07-20 14:22:55.0","audioList":[{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}],"id":1,"learnTime":800,"thumb":"http://i4.piimg.com/601814/12317a59355b8d7c.png","title":"DEMO课程2","type":1,"videoList":[{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}],"lesson_type":2},{"courseId":1,"createDatetime":"2017-07-20 14:22:55.0","audioList":[{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}],"id":1,"learnTime":8000,"thumb":"http://i4.piimg.com/601814/12317a59355b8d7c.png","title":"DEMO课程3","type":0,"videoList":[{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}],"lesson_type":4}]
         * isPaid : 1
         * isFavorite : 0
         */

        private CourseBean course;
        private int isPaid;
        private int isFavorite;
        private List<LessonsBean> lessons;

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public int getIsPaid() {
            return isPaid;
        }

        public void setIsPaid(int isPaid) {
            this.isPaid = isPaid;
        }

        public int getIsFavorite() {
            return isFavorite;
        }

        public void setIsFavorite(int isFavorite) {
            this.isFavorite = isFavorite;
        }

        public List<LessonsBean> getLessons() {
            return lessons;
        }

        public void setLessons(List<LessonsBean> lessons) {
            this.lessons = lessons;
        }

        public static class CourseBean {
            /**
             * abouts : DEMO课程专辑简介
             * categoryId : 6
             * categoryTitle : 课程专辑分类一
             * createDatetime : 1999-12-12 00:00:00.0
             * favoriteNum : 11
             * id : 1
             * isRecommend : 1
             * lessonNum : 10
             * lessonType : 0
             * price : 12
             * startDate : 2017-07-18
             * studentNum : 12
             * teacher : {"avatar":"头像地址","nickname":"DEMO昵称"}
             * thumb : http://i4.piimg.com/601814/f258764ee806eaa0.png
             * tid : 1
             * title : DEMO课程
             * type : 0
             */

            private String abouts;
            private int categoryId;
            private String categoryTitle;
            private String createDatetime;
            private int favoriteNum;
            private int id;
            private int isRecommend;
            private int lessonNum;
            private int lessonType;
            private double price;
            private String startDate;
            private int studentNum;
            private TeacherBean teacher;
            private String thumb;
            private int tid;
            private String title;
            private int type;

            public String getAbouts() {
                return abouts;
            }

            public void setAbouts(String abouts) {
                this.abouts = abouts;
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

            public String getCreateDatetime() {
                return createDatetime;
            }

            public void setCreateDatetime(String createDatetime) {
                this.createDatetime = createDatetime;
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

            public int getIsRecommend() {
                return isRecommend;
            }

            public void setIsRecommend(int isRecommend) {
                this.isRecommend = isRecommend;
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

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public int getStudentNum() {
                return studentNum;
            }

            public void setStudentNum(int studentNum) {
                this.studentNum = studentNum;
            }

            public TeacherBean getTeacher() {
                return teacher;
            }

            public void setTeacher(TeacherBean teacher) {
                this.teacher = teacher;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
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

            public static class TeacherBean {
                /**
                 * avatar : 头像地址
                 * nickname : DEMO昵称
                 */

                private String avatar;
                private String nickname;

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }
            }
        }

        public static class LessonsBean {
            /**
             * courseId : 1
             * createDatetime : 2017-07-20 14:22:55.0
             * audioList : [{"vid":"d5faf7a5-740e-406e-9021-887ad7af7bc9","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4","videoName":"音频频名","original":"原文"},{"vid":"CbUPxVWo_5196642_hd","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/CbUPxVWo_5196642_hd.flv","videoName":"音频频名","original":"原文"}]
             * id : 1
             * learnTime : 8
             * thumb : http://i4.piimg.com/601814/12317a59355b8d7c.png
             * title : DEMO课程
             * type : 0
             * videoList : [{"vid":"0a1f6997-36d5-4022-b8b7-1914854630db","origUrl":"http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4","videoName":"视频名","original":"原文"}]
             * lesson_type : 1
             */

            private int courseId;
            private String createDatetime;
            private int id;
            private int durationMsec;
            private String thumb;
            private String title;
            private int type;
            private int lessonType;
            private List<AudioListBean> audioList;
            private List<VideoListBean> videoList;

            public int getDurationMsec() {
                return durationMsec;
            }

            public void setDurationMsec(int durationMsec) {
                this.durationMsec = durationMsec;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
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

            public int getLessonType() {
                return lessonType;
            }

            public void setLessonType(int lessonType) {
                this.lessonType = lessonType;
            }

            public List<AudioListBean> getAudioList() {
                return audioList;
            }

            public void setAudioList(List<AudioListBean> audioList) {
                this.audioList = audioList;
            }

            public List<VideoListBean> getVideoList() {
                return videoList;
            }

            public void setVideoList(List<VideoListBean> videoList) {
                this.videoList = videoList;
            }

            public static class AudioListBean {
                /**
                 * vid : d5faf7a5-740e-406e-9021-887ad7af7bc9
                 * origUrl : http://vodfp3j4om5.vod.126.net/vodfp3j4om5/d5faf7a5-740e-406e-9021-887ad7af7bc9.mp4
                 * videoName : 音频频名
                 * original : 原文
                 */

                private String vid;
                private String origUrl;
                private String videoName;
                private String original;

                private int duration;
                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                private int durationMsec;

                private int initialSize;
                public int getInitialSize() {
                    return initialSize;
                }

                public void setInitialSize(int initialSize) {
                    this.initialSize = initialSize;
                }

                public int getDurationMsec() {
                    return durationMsec;
                }

                public void setDurationMsec(int durationMsec) {
                    this.durationMsec = durationMsec;
                }

                public String getVid() {
                    return vid;
                }

                public void setVid(String vid) {
                    this.vid = vid;
                }

                public String getOrigUrl() {
                    return origUrl;
                }

                public void setOrigUrl(String origUrl) {
                    this.origUrl = origUrl;
                }

                public String getVideoName() {
                    return videoName;
                }

                public void setVideoName(String videoName) {
                    this.videoName = videoName;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }
            }

            public static class VideoListBean {
                /**
                 * vid : 0a1f6997-36d5-4022-b8b7-1914854630db
                 * origUrl : http://vodfp3j4om5.vod.126.net/vodfp3j4om5/0a1f6997-36d5-4022-b8b7-1914854630db.mp4
                 * videoName : 视频名
                 * original : 原文
                 */

                private String vid;
                private String origUrl;
                private String videoName;
                private String original;
                private int durationMsec;

                private int initialSize;

                private int duration;
                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public int getInitialSize() {
                    return initialSize;
                }

                public void setInitialSize(int initialSize) {
                    this.initialSize = initialSize;
                }

                public int getDurationMsec() {
                    return durationMsec;
                }

                public void setDurationMsec(int durationMsec) {
                    this.durationMsec = durationMsec;
                }

                public String getVid() {
                    return vid;
                }

                public void setVid(String vid) {
                    this.vid = vid;
                }

                public String getOrigUrl() {
                    return origUrl;
                }

                public void setOrigUrl(String origUrl) {
                    this.origUrl = origUrl;
                }

                public String getVideoName() {
                    return videoName;
                }

                public void setVideoName(String videoName) {
                    this.videoName = videoName;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }
            }
        }
    }

    public static class ExceptionsBean {
        /**
         * id :
         * message : 课程专辑不存在！
         */

        private String id;
        private String message;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class GroupBean {
        /**
         * group":{"tid":"21231215412","title":"小组名称"}(小组信息，如果未创建小组则为空）
         */

        private String tid;
        private String title;

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
