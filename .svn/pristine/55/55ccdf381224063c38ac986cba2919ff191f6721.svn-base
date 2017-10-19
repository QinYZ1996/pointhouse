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
 * Created by ljj on 2017/9/18.
 */

public class MyGroupListBean {
    JSONObject json;

    private Boolean hasError = true;

    public Boolean gethasError() {
        return hasError;
    }

    public void sethasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public MyGroupListBean(String str){
        JSONTokener jsonParser = new JSONTokener(str);
        try {
            json = (JSONObject) jsonParser.nextValue();
            if(json.getInt("resultCode") == 1){
                hasError = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ResultObjectBean.GroupsBean> getGroupsBeanList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.GroupsBean>>() {
        }.getType();

        List<ResultObjectBean.GroupsBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("groups"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"groups":[{"courseId":"关联课程id","id":1,"joinStatus":"1","thumb":"群缩略图","tid":"79252648","title":"群名称"}]}
     */

    private int resultCode;
    private ResultObjectBean resultObject;
    private List<?> exceptions;
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

    public static class ResultObjectBean {
        private List<GroupsBean> groups;

        public List<GroupsBean> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsBean> groups) {
            this.groups = groups;
        }

        public static class GroupsBean {
            /**
             * courseId : 关联课程id
             * id : 1
             * joinStatus : 1
             * thumb : 群缩略图
             * tid : 79252648
             * title : 群名称
             */

            private int courseId;
            private int id;
            private String joinStatus;
            private String thumb;
            private String tid;
            private String title;

            private int maxCount;

            public int getMaxCount() {
                return maxCount;
            }

            public void setMaxCount(int maxCount) {
                this.maxCount = maxCount;
            }

            private int memberCount;

            public int getMemberCount() {
                return memberCount;
            }

            public void setMemberCount(int memberCount) {
                this.memberCount = memberCount;
            }


            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getJoinStatus() {
                return joinStatus;
            }

            public void setJoinStatus(String joinStatus) {
                this.joinStatus = joinStatus;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
