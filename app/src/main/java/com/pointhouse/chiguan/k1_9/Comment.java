package com.pointhouse.chiguan.k1_9;

import com.pointhouse.chiguan.common.jsonObject.ArticleDetailGetBean;

import java.util.List;

/**
 * Created by gyh on 2017/9/4.
 */

public class Comment {
    private String authorIconUrl;
    private String authorName;
    private String comment;
    private int id;
    private boolean deletable;
    private boolean isEmpty;

    public String getAuthorIconUrl() {
        return authorIconUrl;
    }

    public void setAuthorIconUrl(String authorIconUrl) {
        this.authorIconUrl = authorIconUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
