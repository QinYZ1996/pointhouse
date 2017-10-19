package com.pointhouse.chiguan.k1_3;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

import static android.R.attr.id;

/**
 * 音视频
 * Created by Maclaine on 2017/7/25.
 */

public class Media implements Serializable {
    private Integer vid;
    private String origUrl;
    private String videoName;
    private String original;
    //长度(秒).学习进度使用
    private Integer duration;

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
