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
 * Created by ljj on 2017/7/25.
 */

public class TeacherListGetBean {

    JSONObject json;

    private Boolean hasError = true;

    public Boolean gethasError() {
        return hasError;
    }

    public void sethasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public TeacherListGetBean(String str){
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

    public List<ResultObjectBean.TeachersBean> getTeacherList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.TeachersBean>>() {
        }.getType();

        List<ResultObjectBean.TeachersBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("teachers"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"teachers":[{"avatar":"头像地址","country":"中国","id":1,"sex":1,"nickname":"DEMO昵称"}]}
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
        private List<TeachersBean> teachers;

        public List<TeachersBean> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<TeachersBean> teachers) {
            this.teachers = teachers;
        }

        public static class TeachersBean {
            /**
             * avatar : 头像地址
             * country : 中国
             * id : 1
             * sex : 1
             * nickname : DEMO昵称
             */

            private String avatar;
            private String country;
            private int id;
            private int sex;
            private String nickname;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
