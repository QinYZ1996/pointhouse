package com.pointhouse.chiguan.Application;

import com.pointhouse.chiguan.k1_3.Media;

/**
 * 音频课程列表
 * Created by Maclaine on 2017/7/19.
 */

public class CommonMediaItem {
    private Integer id;
    private String name;
    private boolean isPlaying=false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public CommonMediaItem() {
    }

    public CommonMediaItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CommonMediaItem(Integer id, String name, boolean isPlaying) {
        this.id = id;
        this.name = name;
        this.isPlaying = isPlaying;
    }

    public CommonMediaItem(Media media){
        this.id=media.getVid();
        this.name=media.getVideoName();
    }
}
