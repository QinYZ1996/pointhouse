package com.pointhouse.chiguan.common.jsonObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * Created by ljj on 2017/7/26.
 */

public class LessonDetailGetBean {

    JSONObject json;

    private Boolean hasError = true;

    public Boolean hasError() {
        return hasError;
    }

    public LessonDetailGetBean(){}

    public LessonDetailGetBean(String str) {
        JSONTokener jsonParser = new JSONTokener(str);
        try {
            json = (JSONObject) jsonParser.nextValue();
            resultCode = json.getInt("resultCode");
            if (resultCode == 1) {
                hasError = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ResultObjectBean getResultObjectBean() {
        if (hasError) return null;

        ResultObjectBean bean = null;
        try {
            bean = new Gson().fromJson(json.getString("resultObject"), new TypeToken<ResultObjectBean>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * exceptions : [{"id":" ","message":"该课程不存在！/请先购买课程专辑/请先登录！"}]
     * messages : []
     * resultCode : 1
     * resultObject : {"lesson":{"abouts":"<p>测试 添加试听前修改状态<\/p>","audioList":[{"duration":1687,"durationMsec":1687371,"initialSize":77216052,"origUrl":"http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4","original":"","vid":12998038,"videoName":"6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4"}],"content":"<p>测试 添加试听前修改状态<\/p>","courseId":4,"createDatetime":"2017-08-22 12:57:23.0","hasExercises":1,"id":36,"learnTime":0,"lessonType":1,"status":1,"theDivision":"","theFetchSize":2048,"theFetchStart":0,"theSchema":"","title":"测试 添加试听前修改状态","type":1,"updateDatetime":"2017-08-22 14:22:30.0","videoList":[{"duration":1687,"durationMsec":1687371,"initialSize":77216052,"origUrl":"http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4","original":"","vid":12998038,"videoName":"6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4"}]}}
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
         * lesson : {"abouts":"<p>测试 添加试听前修改状态<\/p>","audioList":[{"duration":1687,"durationMsec":1687371,"initialSize":77216052,"origUrl":"http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4","original":"","vid":12998038,"videoName":"6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4"}],"content":"<p>测试 添加试听前修改状态<\/p>","courseId":4,"createDatetime":"2017-08-22 12:57:23.0","hasExercises":1,"id":36,"learnTime":0,"lessonType":1,"status":1,"theDivision":"","theFetchSize":2048,"theFetchStart":0,"theSchema":"","title":"测试 添加试听前修改状态","type":1,"updateDatetime":"2017-08-22 14:22:30.0","videoList":[{"duration":1687,"durationMsec":1687371,"initialSize":77216052,"origUrl":"http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4","original":"","vid":12998038,"videoName":"6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4"}]}
         */

        private LessonBean lesson;

        public LessonBean getLesson() {
            return lesson;
        }

        public void setLesson(LessonBean lesson) {
            this.lesson = lesson;
        }

        public static class LessonBean {
            /**
             * abouts : <p>测试 添加试听前修改状态</p>
             * audioList : [{"duration":1687,"durationMsec":1687371,"initialSize":77216052,"origUrl":"http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4","original":"","vid":12998038,"videoName":"6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4"}]
             * content : <p>测试 添加试听前修改状态</p>
             * courseId : 4
             * createDatetime : 2017-08-22 12:57:23.0
             * hasExercises : 1
             * id : 36
             * learnTime : 0
             * lessonType : 1
             * status : 1
             * theDivision :
             * theFetchSize : 2048
             * theFetchStart : 0
             * theSchema :
             * title : 测试 添加试听前修改状态
             * type : 1
             * updateDatetime : 2017-08-22 14:22:30.0
             * videoList : [{"duration":1687,"durationMsec":1687371,"initialSize":77216052,"origUrl":"http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4","original":"","vid":12998038,"videoName":"6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4"}]
             */

            private String abouts;
            private String content;
            private int courseId;
            private String createDatetime;
            private int hasExercises;
            private int id;
            private int learnTime;
            private int lessonType;
            private int status;
            private String theDivision;
            private int theFetchSize;
            private int theFetchStart;
            private String theSchema;
            private String title;
            private int type;
            private String updateDatetime;
            private List<AudioListBean> audioList;
            private List<VideoListBean> videoList;

            public String getAbouts() {
                return abouts;
            }

            public void setAbouts(String abouts) {
                this.abouts = abouts;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public int getHasExercises() {
                return hasExercises;
            }

            public void setHasExercises(int hasExercises) {
                this.hasExercises = hasExercises;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLearnTime() {
                return learnTime;
            }

            public void setLearnTime(int learnTime) {
                this.learnTime = learnTime;
            }

            public int getLessonType() {
                return lessonType;
            }

            public void setLessonType(int lessonType) {
                this.lessonType = lessonType;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTheDivision() {
                return theDivision;
            }

            public void setTheDivision(String theDivision) {
                this.theDivision = theDivision;
            }

            public int getTheFetchSize() {
                return theFetchSize;
            }

            public void setTheFetchSize(int theFetchSize) {
                this.theFetchSize = theFetchSize;
            }

            public int getTheFetchStart() {
                return theFetchStart;
            }

            public void setTheFetchStart(int theFetchStart) {
                this.theFetchStart = theFetchStart;
            }

            public String getTheSchema() {
                return theSchema;
            }

            public void setTheSchema(String theSchema) {
                this.theSchema = theSchema;
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

            public String getUpdateDatetime() {
                return updateDatetime;
            }

            public void setUpdateDatetime(String updateDatetime) {
                this.updateDatetime = updateDatetime;
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
                 * duration : 1687
                 * durationMsec : 1687371
                 * initialSize : 77216052
                 * origUrl : http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4
                 * original :
                 * vid : 12998038
                 * videoName : 6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4
                 */

                private int duration;
                private int durationMsec;
                private int initialSize;
                private String origUrl;
                private String original;
                private int vid;
                private String videoName;

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public int getDurationMsec() {
                    return durationMsec;
                }

                public void setDurationMsec(int durationMsec) {
                    this.durationMsec = durationMsec;
                }

                public int getInitialSize() {
                    return initialSize;
                }

                public void setInitialSize(int initialSize) {
                    this.initialSize = initialSize;
                }

                public String getOrigUrl() {
                    return origUrl;
                }

                public void setOrigUrl(String origUrl) {
                    this.origUrl = origUrl;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }

                public int getVid() {
                    return vid;
                }

                public void setVid(int vid) {
                    this.vid = vid;
                }

                public String getVideoName() {
                    return videoName;
                }

                public void setVideoName(String videoName) {
                    this.videoName = videoName;
                }
            }

            public static class VideoListBean {
                /**
                 * duration : 1687
                 * durationMsec : 1687371
                 * initialSize : 77216052
                 * origUrl : http://vodxfturg0k.vod.126.net/vodxfturg0k/2e66d9c5-7901-4aa4-b526-478e78d6d300.mp4
                 * original :
                 * vid : 12998038
                 * videoName : 6.窗口导航 -《24个Android应用开发常用知识点精讲高手速成视频教程-极客学院 》课程 - 极客学院.mp4
                 */

                private int duration;
                private int durationMsec;
                private int initialSize;
                private String origUrl;
                private String original;
                private int vid;
                private String videoName;

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public int getDurationMsec() {
                    return durationMsec;
                }

                public void setDurationMsec(int durationMsec) {
                    this.durationMsec = durationMsec;
                }

                public int getInitialSize() {
                    return initialSize;
                }

                public void setInitialSize(int initialSize) {
                    this.initialSize = initialSize;
                }

                public String getOrigUrl() {
                    return origUrl;
                }

                public void setOrigUrl(String origUrl) {
                    this.origUrl = origUrl;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }

                public int getVid() {
                    return vid;
                }

                public void setVid(int vid) {
                    this.vid = vid;
                }

                public String getVideoName() {
                    return videoName;
                }

                public void setVideoName(String videoName) {
                    this.videoName = videoName;
                }
            }
        }
    }

    public static class ExceptionsBean {
        /**
         * id :
         * message : 该课程不存在！/请先购买课程专辑/请先登录！
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
}
