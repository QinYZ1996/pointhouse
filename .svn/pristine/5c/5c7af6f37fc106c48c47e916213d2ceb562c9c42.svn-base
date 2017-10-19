package com.pointhouse.chiguan.Application;

import com.alibaba.fastjson.annotation.JSONField;
import com.pointhouse.chiguan.common.util.CollectionUtil;
import com.pointhouse.chiguan.db.StudyInfo;

import java.util.List;

/**
 * 学习进度上传实体类
 * Created by Maclaine on 2017/8/14.
 */

public class StudyUpload {
    //所在专辑ID
    private String courseId;
    //所在专辑名
    private String courseTitle;
    //缩略图
    private String courseThumb;
    //所在课程ID
    private String lessonId;
    //所在课程名
    private String lessonTitle;
    private String totalProcess="0";
    //当前课时已学习总秒数
    private String totalSecond="0";
    private List<ProcessDetail> processDetail;

    public StudyUpload(){}

    public StudyUpload(StudyInfo studyInfo){
        this.courseId=String.valueOf(studyInfo.getCourseId());
        this.courseTitle=studyInfo.getCourseName();
        this.lessonId=String.valueOf(studyInfo.getLessonId());
        this.lessonTitle=studyInfo.getLessonName();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getTotalProcess() {
        /*int total=0;
        if(!CollectionUtil.isEmpty(processDetail)){
            for (ProcessDetail p:processDetail){
                total+=Integer.parseInt(p.cprocess);
            }
            return String.valueOf(total/processDetail.size());
        }*/
        return totalProcess;
    }

    public void setTotalProcess(String totalProcess) {
        this.totalProcess = totalProcess;
    }

    public String getTotalSecond() {
        /*int total=0;
        if(!CollectionUtil.isEmpty(processDetail)){
            for (ProcessDetail p:processDetail){
                total+=Integer.parseInt(p.csecond);
            }
            return String.valueOf(total);
        }*/
        return totalSecond;
    }

    public void setTotalSecond(String totalSecond) {
        this.totalSecond = totalSecond;
    }

    public String getCourseThumb() {
        return courseThumb;
    }

    public void setCourseThumb(String courseThumb) {
        this.courseThumb = courseThumb;
    }

    public List<ProcessDetail> getProcessDetail() {
        return processDetail;
    }

    public void setProcessDetail(List<ProcessDetail> processDetail) {
        this.processDetail = processDetail;
    }

    public static class ProcessDetail{
        //cvid
        private String cvid;
        //cvname
        private String cvname;
        //cprocess
        private String cprocess;
        //当前音频已学习秒数
        @JSONField(name = "cSecond")
        private String csecond;

        public ProcessDetail(){}

        public ProcessDetail(StudyInfo studyInfo){
            this.cvid=studyInfo.getVid();
            this.cvname=studyInfo.getvName();
            if(studyInfo.getvLength()==null||studyInfo.getvLength()==0){
                this.cprocess="0";
            }else{
                this.cprocess=String.valueOf((int)Math.ceil((double)studyInfo.getCurrent()/(double)studyInfo.getvLength()*100));
            }
            this.csecond =String.valueOf(studyInfo.getCurrent());
        }

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

        public String getCsecond() {
            return csecond;
        }

        public void setCsecond(String csecond) {
            this.csecond = csecond;
        }
    }
}
