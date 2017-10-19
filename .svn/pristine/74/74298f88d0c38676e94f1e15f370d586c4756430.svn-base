package com.pointhouse.chiguan.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pointhouse.chiguan.k1_3.Course;
import com.pointhouse.chiguan.k1_3.Media;

import java.io.Serializable;
import java.util.List;

/**
 * 播放器参数
 * Created by Maclaine on 2017/7/19.
 */

public class CommonMediaOption implements Serializable, Cloneable {
    public static final int AUTO = 0x000;
    public static final int VIDEO_ONLY = 0x001;
    public static final int MUSIC_ONLY = 0x002;
    public static final int VIDEO_MIX = 0x003;
    public static final int MUSIC_MIX = 0x004;
    public static final int TEXT_ONLY = 0x005;

    public static final int REPEAT_ONE = 0x101;
    public static final int REPEAT_ALL = 0x102;

    public static final int SORT_ASC = 0x201;
    public static final int SORT_DESC = 0x202;

    //建议填写,默认为自动.AUTO状态下不可能出现VIDEO_MIX
    private Integer type = AUTO;
    //ID必填(LessonID,用于请求Lesson)
    private Integer id;
    //音频ID,用于连续播放.如果注入则必须同时注入course,否则无效
    private Integer MediaID;
    //是否显示课程列表
    private boolean showCourseList = true;
    //连续播放模式.如果需要REPEAT_NO,将showRepeat设置为false即可
    private int repeatMode = REPEAT_ALL;
    //是否显示下载按钮
    private boolean showDownload = false;
    //连续播放是否只播放已下载
    private boolean readDownload = false;
    //是否显示连续播放按钮
    private boolean showRepeat = true;
    //课程
    private Course course;
    //所有课程ID,如果需要跨课程连续播放必填
    private List<Integer> lessonIDList = null;
    //练习题专用关闭按钮
    private boolean showExerciseClose = false;
    //专辑名,仅需在线播放时传入.用于学习进度记录
    private String courseName;
    @Deprecated
    private Long lessonDuration;


    private Long currentDuration;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public boolean isShowCourseList() {
        return showCourseList;
    }

    public void setShowCourseList(boolean showCourseList) {
        this.showCourseList = showCourseList;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public boolean isShowDownload() {
        return showDownload;
    }

    public void setShowDownload(boolean showDownload) {
        this.showDownload = showDownload;
    }

    public boolean isReadDownload() {
        return readDownload;
    }

    public void setReadDownload(boolean readDownload) {
        this.readDownload = readDownload;
    }

    public boolean isShowRepeat() {
        return showRepeat;
    }

    public void setShowRepeat(boolean showRepeat) {
        this.showRepeat = showRepeat;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getMediaID() {
        return MediaID;
    }

    public void setMediaID(Integer mediaID) {
        MediaID = mediaID;
    }

    public List<Integer> getLessonIDList() {
        return lessonIDList;
    }

    public void setLessonIDList(List<Integer> lessonIDList) {
        this.lessonIDList = lessonIDList;
    }

    public boolean isShowExerciseClose() {
        return showExerciseClose;
    }

    public void setShowExerciseClose(boolean showExerciseClose) {
        this.showExerciseClose = showExerciseClose;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(Long currentDuration) {
        this.currentDuration = currentDuration;
    }

    @Override
    public CommonMediaOption clone() {
        CommonMediaOption option = null;
        try {
            option = (CommonMediaOption) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return option;
    }

    @JsonIgnore
    public Integer getNextLessonID() {
        if (!CollectionUtil.isEmpty(course.getLessonList())) {//随时学
            for (int i = 0; i < course.getLessonList().size(); i++) {
                if (id.equals(course.getLessonList().get(i).getId()) && i < course.getLessonList().size() - 1) {
                    course.setLesson(course.getLessonList().get(i + 1));
                    if (!CollectionUtil.isEmpty(course.getLesson().getAudioList())) {
                        MediaID = course.getLesson().getAudioList().get(0).getVid();
                    } else if (!CollectionUtil.isEmpty(course.getLesson().getVideoList())) {
                        MediaID = course.getLesson().getVideoList().get(0).getVid();
                    } else {
                        return null;
                    }
                    return course.getLessonList().get(i + 1).getId();
                }
            }
        }
        if (!CollectionUtil.isEmpty(lessonIDList)) {//普通
            for (int i = 0; i < lessonIDList.size(); i++) {
                if (id.equals(lessonIDList.get(i)) && i < lessonIDList.size() - 1) {
                    return lessonIDList.get(i + 1);
                }
            }
        }
        return null;
    }

    public static int autoSetType(Course course) {
        if (!CollectionUtil.isEmpty(course.getLesson().getAudioList()) && !CollectionUtil.isEmpty(course.getLesson().getVideoList())) {
            return CommonMediaOption.MUSIC_MIX;
        } else if (!CollectionUtil.isEmpty(course.getLesson().getAudioList())) {
            return CommonMediaOption.MUSIC_ONLY;
        } else if (!CollectionUtil.isEmpty(course.getLesson().getVideoList())) {
            return CommonMediaOption.VIDEO_ONLY;
        } else {
            return CommonMediaOption.TEXT_ONLY;
        }
    }

    public void autoSetType() {
        if (getType() == CommonMediaOption.AUTO) {
            setType(autoSetType(getCourse()));
        }
    }

    @Deprecated
    public Long getLessonDuration() throws Exception {
        if(getCourse()==null||getCourse().getLesson()==null){
            throw new Exception("course or lesson is null");
        }
        if (lessonDuration == null) {
            lessonDuration = 0L;
            if (!CollectionUtil.isEmpty(getCourse().getLesson().getVideoList())) {
                lessonDuration += getCourse().getLesson().getVideoList().get(0).getDuration();
            }
            if (!CollectionUtil.isEmpty(getCourse().getLesson().getAudioList())) {
                for (Media media : getCourse().getLesson().getAudioList()) {
                    lessonDuration += media.getDuration();
                }
            }
        }
        return lessonDuration;
    }

    public Media getCurrentMedia() throws Exception {
        if(getCourse()==null||getCourse().getLesson()==null){
            throw new Exception("course or lesson is null");
        }
        if (!CollectionUtil.isEmpty(getCourse().getLesson().getAudioList())) {
            for (int i = 0; i < getCourse().getLesson().getAudioList().size(); i++) {
                if (getMediaID().equals(getCourse().getLesson().getAudioList().get(i).getVid())) {
                    return getCourse().getLesson().getAudioList().get(i);
                }
            }
        }
        if (!CollectionUtil.isEmpty(getCourse().getLesson().getVideoList())) {
            for (int i = 0; i < getCourse().getLesson().getVideoList().size(); i++) {
                if (getMediaID().equals(getCourse().getLesson().getVideoList().get(i).getVid())) {
                    return getCourse().getLesson().getVideoList().get(i);
                }
            }
        }
        return null;
    }
}
