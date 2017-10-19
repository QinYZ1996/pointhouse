package com.pointhouse.chiguan.h1;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 群组实体
 * Created by Maclaine on 2017/7/5.
 */

class GroupItem {
    private String id;
    private String tid;
    private String title;
    private Integer joinStatus =0;
    private String thumb;
    private int memberCount =1;
    private int maxCount =200;

    GroupItem() {}

    GroupItem(String tid, String title) {
        this.tid = tid;
        this.title = title;
    }

    GroupItem(String tid, String title, int joinStatus, int memberCount) {
        this.tid = tid;
        this.title = title;
        this.joinStatus = joinStatus;
        this.memberCount = memberCount;
    }

    GroupItem(String tid, String title, int joinStatus, int memberCount, int maxCount) {
        this.tid = tid;
        this.title = title;
        this.joinStatus = joinStatus;
        this.memberCount = memberCount;
        this.maxCount = maxCount;
    }

    /**
     * @return 群组ID
     */
    String getTid() {
        return tid;
    }

    void setTid(String tid) {
        this.tid = tid;
    }

    /**
     * @return 群组名
     */
    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return 群组状态,默认0
     */
    Integer getJoinStatus() {
        return joinStatus;
    }

    void setJoinStatus(Integer joinStatus) {
        this.joinStatus = joinStatus;
    }

    /**
     * @return 群组人数,默认1
     */
    int getMemberCount() {
        return memberCount;
    }

    void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * @return 群组最大人数,默认200
     */
    int getMaxCount() {
        return maxCount;
    }

    void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
