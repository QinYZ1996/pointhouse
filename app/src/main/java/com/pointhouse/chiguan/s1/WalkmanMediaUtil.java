package com.pointhouse.chiguan.s1;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;
import com.pointhouse.chiguan.db.CourseDetailInfo;
import com.pointhouse.chiguan.db.CourseDetailInfoDao;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Lesson;
import com.pointhouse.chiguan.k1_3.Media;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 随时学工具类
 * Created by Maclaine on 2017/8/1.
 */

public class WalkmanMediaUtil {
    private static final String TAG="WalkmanMediaUtil";
    public static final int ALL=0x000;
    public static final int Download=0x001;
    public static final int Downloading=0x002;

    public static Map<Integer,Course> initCourse(Context context,Integer type){
        Map<Integer,Course> courseMap=new LinkedHashMap<>();
        CourseDownloadInfoDao courseDownloadInfoDao=new CourseDownloadInfoDao(context);
        CourseDetailInfoDao courseDetailInfoDao=new CourseDetailInfoDao(context);
        try {
            List<CourseDownloadInfo> cdiList ;
            switch (type){
                case Download:
                    cdiList= courseDownloadInfoDao.queryAllDownload();
                    break;
                case Downloading:
                    cdiList= courseDownloadInfoDao.queryAllDownloading();
                    break;
                default:
                    cdiList= courseDownloadInfoDao.queryAllDownloadList();
                    break;
            }
            if(cdiList!=null){
                for (CourseDownloadInfo cd:cdiList){
                    if (!courseMap.containsKey(cd.getCourseId())){
                        Course course=new Course();
                        course.setId(cd.getCourseId());
                        courseMap.put(cd.getCourseId(),course);
                    }
                    List<Lesson> lessonList=courseMap.get(cd.getCourseId()).getLessonList();
                    if(lessonList==null){
                        lessonList=new ArrayList<>();
                    }
                    boolean isExist=false;
                    for (Lesson lesson:lessonList){//遍历已存在课程
                        if(lesson.getId()==cd.getLessonId()){//如果存在则直接添加音视频
                            isExist=true;
                            Media media=new Media();
                            media.setVid(Integer.parseInt(cd.getVid()));
                            media.setOrigUrl(SDCardHelper.getSDExternalFilesDir(cd.getFilePath(),cd.getfileName()).getPath());
                            //由于下载无法获取到音视频类型因此统一写入音频,后续再判断
                            if(lesson.getAudioList()==null){
                                lesson.setAudioList(new ArrayList<>());
                            }
                            lesson.getAudioList().add(media);
                            break;
                        }
                    }
                    if (!isExist){//如果不存在则创建课程对象再添加
                        Lesson lesson=new Lesson();
                        lesson.setId(cd.getLessonId());
                        lesson.setCourseId(cd.getCourseId());
                        lesson.setAudioList(new ArrayList<>());
                        Media media=new Media();
                        media.setVid(Integer.parseInt(cd.getVid()));
                        media.setOrigUrl(SDCardHelper.getSDExternalFilesDir(cd.getFilePath(),cd.getfileName()).getPath());
                        //由于下载无法获取到音视频类型因此统一写入音频,后续再判断
                        lesson.getAudioList().add(media);
                        lessonList.add(lesson);
                    }
                    courseMap.get(cd.getCourseId()).setLessonList(lessonList);
                }
            }
            if(type==Download){
                Iterator<Map.Entry<Integer, Course>> iterator = courseMap.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<Integer, Course> entry=iterator.next();
                    Iterator<Lesson> it = entry.getValue().getLessonList().iterator();
                    while (it.hasNext()){
                        Lesson lesson=it.next();
                        List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(lesson.getId());
                        if(lesson.getAudioList().size()!=courseDownloadInfoList.size()){
                            it.remove();
                        }
                    }
                    if(entry.getValue().getLessonList().size()==0){
                        iterator.remove();
                    }
                }
            }
            for (Map.Entry<Integer,Course> entry:courseMap.entrySet()){//遍历map key
                CourseDetailInfo courseDetailInfo=courseDetailInfoDao.queryCourseId(entry.getKey());
                if(courseDetailInfo==null){
                    Log.w(TAG,"应当获取到CourseDetailInfo:ID:"+entry.getKey()+",但没有获取到");
                    continue;
                }
                JSONObject jsonObject= JSON.parseObject(courseDetailInfo.getCourseJson());
                JSONObject jsonCourse = jsonObject.getJSONObject("resultObject").getJSONObject("course");
                if(jsonCourse==null){
                    Log.w(TAG,"应当获取到CourseDetailInfo:course:ID:"+entry.getKey()+",但没有获取到");
                    continue;
                }
                entry.getValue().setTitle(jsonCourse.getString("title"));
                entry.getValue().setThumb(jsonCourse.getString("thumb"));
                entry.getValue().setCategoryId(jsonCourse.getInteger("categoryId"));
                entry.getValue().setCategoryTitle(jsonCourse.getString("categoryTitle"));
                entry.getValue().setLessonNum(jsonCourse.getInteger("lessonNum"));
                entry.getValue().setLessonType(jsonCourse.getInteger("lessonType"));

                JSONArray jsonLessons=jsonObject.getJSONObject("resultObject").getJSONArray("lessons");
                if(jsonLessons==null){
                    Log.w(TAG,"应当获取到CourseDetailInfo:lessons:CourseID:"+entry.getKey()+",但没有获取到");
                    continue;
                }
                List<Lesson> lessonList=entry.getValue().getLessonList();
                for (Lesson lesson:lessonList){

                    for (int i=0;i<jsonLessons.size();i++){
                        JSONObject jsonLesson = jsonLessons.getJSONObject(i);

                        if(lesson.getId().equals(jsonLesson.getInteger("id"))){
                            lesson.setTitle(jsonLesson.getString("title"));
                            //修改为显示专辑图片
                            lesson.setThumb(jsonCourse.getString("thumb"));
                            lesson.setLessonType(jsonLesson.getInteger("lessonType"));
//                            lesson.setContent(jsonLesson.getString("content"));
                            if (lesson.getAudioList()!=null){

                                Iterator<Media> it = lesson.getAudioList().iterator();
                                while(it.hasNext()){
                                    boolean isVideo=false;
                                    Media audio=it.next();
                                    for (int j=0;j<jsonLesson.getJSONArray("videoList").size();j++){
                                        if (audio.getVid().equals(jsonLesson.getJSONArray("videoList").getJSONObject(j).getInteger("vid"))){
                                            if(lesson.getVideoList()==null){
                                                lesson.setVideoList(new ArrayList<>());
                                            }
                                            audio.setVideoName(jsonLesson.getJSONArray("videoList").getJSONObject(j).getString("videoName"));
                                            audio.setDuration(jsonLesson.getJSONArray("videoList").getJSONObject(j).getInteger("duration"));
                                            lesson.getVideoList().add(audio);
                                            it.remove();
                                            isVideo=true;
                                            break;
                                        }
                                    }
                                    if(isVideo){
                                        continue;
                                    }

                                    for (int j=0;j<jsonLesson.getJSONArray("audioList").size();j++){
                                        if (audio.getVid().equals(jsonLesson.getJSONArray("audioList").getJSONObject(j).getInteger("vid"))){
                                            audio.setVideoName(jsonLesson.getJSONArray("audioList").getJSONObject(j).getString("videoName"));
                                            audio.setDuration(jsonLesson.getJSONArray("audioList").getJSONObject(j).getInteger("duration"));
                                            break;
                                        }
                                    }
                                }
//                                for (Media audio:lesson.getAudioList()){
//
//                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseMap;
    }
}
