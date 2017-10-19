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
 * Created by gyh on 2017/8/14.
 */

public class MyStudyGetBean {

    private JSONObject json;
    private boolean hasError = true;
    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : [{"courseId":"所在专辑ID","courseTitle":"所在专辑名","courseThumb":"缩略图","lessonId":"所在课程ID","lessonTitle":"所在课程名","totalProcess":"50","totalSecond":"10000(当前课时已学习总秒数)","processDetail":[{"cvid":"001","cvname":"音频1","cprocess":"59","cSecond":"10000(当前音频已学习秒数)"},{"cvid":"002","cvname":"音频2","cprocess":"100","cSecond":"10000(当前音频已学习秒数)"}]},{"courseId":"所在专辑ID","courseTitle":"所在专辑名","courseThumb":"缩略图","lessonId":"所在课程ID","lessonTitle":"所在课程名","totalProcess":"50","totalSecond":"10000(当前课时已学习总秒数)","processDetail":[{"cvid":"001","cvname":"音频1","cprocess":"59","cSecond":"10000(当前音频已学习秒数)"},{"cvid":"002","cvname":"音频2","cprocess":"100","cSecond":"10000(当前音频已学习秒数)"}]}]
     */

    private int resultCode;
    private List<?> exceptions;
    private List<?> messages;
    private List<ResultObjectBean> resultObject;

    public Boolean hasError() {
        return hasError;
    }

    public MyStudyGetBean(String str) {
        JSONTokener jsonPaser = new JSONTokener(str);
        try {
            json = (JSONObject) jsonPaser.nextValue();
            resultCode = json.getInt("resultCode");
            if (resultCode == 1)
                hasError = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ResultObjectBean> getResultObjectBeanList() {
        if (hasError) return null;

        List<ResultObjectBean> list = new ArrayList<>();
        Type type = new TypeToken<List<ResultObjectBean>>() {
        }.getType();
        try {
            list = new Gson().fromJson(json.getString("resultObject"), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

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
         * courseId : 所在专辑ID
         * courseTitle : 所在专辑名
         * courseThumb : 缩略图
         * lessonId : 所在课程ID
         * lessonTitle : 所在课程名
         * totalProcess : 50
         * totalSecond : 10000(当前课时已学习总秒数)
         * processDetail : [{"cvid":"001","cvname":"音频1","cprocess":"59","cSecond":"10000(当前音频已学习秒数)"},{"cvid":"002","cvname":"音频2","cprocess":"100","cSecond":"10000(当前音频已学习秒数)"}]
         */

        private int courseId;
        private String courseTitle;
        private String courseThumb;
        private int lessonId;
        private String lessonTitle;
        private String totalProcess;
        private long totalSecond;
        private List<ProcessDetailBean> processDetail;

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getCourseTitle() {
            return courseTitle;
        }

        public void setCourseTitle(String courseTitle) {
            this.courseTitle = courseTitle;
        }

        public String getCourseThumb() {
            return courseThumb;
        }

        public void setCourseThumb(String courseThumb) {
            this.courseThumb = courseThumb;
        }

        public int getLessonId() {
            return lessonId;
        }

        public void setLessonId(int lessonId) {
            this.lessonId = lessonId;
        }

        public String getLessonTitle() {
            return lessonTitle;
        }

        public void setLessonTitle(String lessonTitle) {
            this.lessonTitle = lessonTitle;
        }

        public String getTotalProcess() {
            return totalProcess;
        }

        public void setTotalProcess(String totalProcess) {
            this.totalProcess = totalProcess;
        }

        public long getTotalSecond() {
            return totalSecond;
        }

        public void setTotalSecond(long totalSecond) {
            this.totalSecond = totalSecond;
        }

        public List<ProcessDetailBean> getProcessDetail() {
            return processDetail;
        }

        public void setProcessDetail(List<ProcessDetailBean> processDetail) {
            this.processDetail = processDetail;
        }

        public static class ProcessDetailBean {
            /**
             * cvid : 001
             * cvname : 音频1
             * cprocess : 59
             * cSecond : 10000(当前音频已学习秒数)
             */

            private String cvid;
            private String cvname;
            private String cprocess;
            private String cSecond;

            public String getCvid() {
                return cvid;
            }

            public void setCvid(String cvid) {
                this.cvid = cvid;
            }

            public String getCvname() {
                return cvname;
            }

            public void setCvname(String cvname) {
                this.cvname = cvname;
            }

            public String getCprocess() {
                return cprocess;
            }

            public void setCprocess(String cprocess) {
                this.cprocess = cprocess;
            }

            public String getCSecond() {
                return cSecond;
            }

            public void setCSecond(String cSecond) {
                this.cSecond = cSecond;
            }
        }
    }
}
