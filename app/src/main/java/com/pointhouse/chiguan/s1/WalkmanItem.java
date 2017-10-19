package com.pointhouse.chiguan.s1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.db.ImgUrlInfo;
import com.pointhouse.chiguan.db.ImgUrlInfoDao;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Lesson;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * 随时学VO
 * Created by Maclaine on 2017/8/2.
 */

public class WalkmanItem {
    private final static String TAG="WalkmanItem";
    private Serializable id;
    private String name;
    private String describe;
    private boolean hasContent =false;
    private boolean hasAudio=false;
    private boolean hasVideo=false;
    private Bitmap thumb;

    public WalkmanItem(){}

    public WalkmanItem(Course course){
        this.id=course.getId();
        this.name=course.getTitle();
        this.describe=course.getLessonList().size()+"/"+course.getLessonNum()+"课时";
        ImgUrlInfoDao imgUrlInfoDao=new ImgUrlInfoDao(GlobalApplication.sContext);
        try {
            if(course.getThumb()!=null){
                ImgUrlInfo imgUrlInfo=imgUrlInfoDao.queryUrl(course.getThumb());
                if(imgUrlInfo!=null){
                    thumb=BitmapFactory.decodeByteArray(imgUrlInfo.getBlob(), 0, imgUrlInfo.getBlob().length);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(course.getLessonType()==null){
            course.setLessonType(-1);
        }
        if(course.getLessonType()==0||course.getLessonType()==3||course.getLessonType()==4||course.getLessonType()==6){
            this.hasContent =true;
        }
        if(course.getLessonType()==1||course.getLessonType()==3||course.getLessonType()==5||course.getLessonType()==6){
            this.hasAudio=true;
        }
        if(course.getLessonType()==2||course.getLessonType()==4||course.getLessonType()==5||course.getLessonType()==6){
            this.hasVideo=true;
        }
//        for (Lesson lesson:course.getLessonList()){
//            if(lesson.getAudioList()!=null){
//                this.hasAudio=true;
//            }
//            if(lesson.getVideoList()!=null){
//                this.hasVideo=true;
//            }
//            if (lesson.getContent()!=null&&!"".equals(lesson.getContent())){
//                this.hasContent =true;
//            }
//        }
    }

    public WalkmanItem(Lesson lesson){
        this.id=lesson.getId();
        this.name=lesson.getTitle();
        ImgUrlInfoDao imgUrlInfoDao=new ImgUrlInfoDao(GlobalApplication.sContext);
        try {
            if(lesson.getThumb()!=null){
                ImgUrlInfo imgUrlInfo=imgUrlInfoDao.queryUrl(lesson.getThumb());
                if(imgUrlInfo!=null){
                    thumb=BitmapFactory.decodeByteArray(imgUrlInfo.getBlob(), 0, imgUrlInfo.getBlob().length);
                }
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
        if(lesson.getLessonType()==null){
            lesson.setLessonType(-1);
        }

        if(lesson.getLessonType()==0){
            this.hasContent =true;
        }
        if(lesson.getLessonType()==1||lesson.getLessonType()==4){
            this.hasAudio =true;
        }
        if(lesson.getLessonType()==2||lesson.getLessonType()==4){
            this.hasVideo =true;
        }
//        if(lesson.getAudioList()!=null){
//            this.hasAudio=true;
//        }
//        if(lesson.getVideoList()!=null){
//            this.hasVideo=true;
//        }
//        if (lesson.getContent()!=null&&!"".equals(lesson.getContent())){
//            this.hasContent =true;
//        }
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public boolean isHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }
}
