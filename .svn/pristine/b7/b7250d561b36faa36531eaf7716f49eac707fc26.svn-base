package com.pointhouse.chiguan.common.jsonObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyh on 2017/8/1.
 */

public class ExerciseListGetBean {
    private JSONObject json;
    private Boolean hasError = true;

    public Boolean hasError() {
        return hasError;
    }

    public ExerciseListGetBean(String str) {
        JSONTokener jsonParser = new JSONTokener(str);
        try {
            json = (JSONObject) jsonParser.nextValue();
            resultCode = json.getInt("resultCode");
            if (resultCode == 1) {
                hasError = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ResultObjectBean.ExercisesBean> getExercisesBean() {
        if (hasError) return null;

        List<ResultObjectBean.ExercisesBean> list = new ArrayList<>();
        try {
            list.addAll(new Gson().fromJson(json.getJSONObject("resultObject").getString("exercises"), new TypeToken<ArrayList<ResultObjectBean.ExercisesBean>>() {
            }.getType()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public ExceptionsBean getExceptionsBean() {
        if (!hasError) return null;

        List<ExceptionsBean> list = new ArrayList<>();
        try {
            list = new Gson().fromJson(json.getString("exceptions"), new TypeToken<ArrayList<ExceptionsBean>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"exercises":[{"id":1,"lessonId":1,"questionType":1,"theDivision":"","theFetchSize":2048,"theFetchStart":0,"theSchema":"","title":"测试习题","type":1}]}
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
        private List<ExercisesBean> exercises;

        public List<ExercisesBean> getExercises() {
            return exercises;
        }

        public void setExercises(List<ExercisesBean> exercises) {
            this.exercises = exercises;
        }

        public static class ExercisesBean {
            /**
             * id : 1
             * lessonId : 1
             * questionType : 1
             * theDivision :
             * theFetchSize : 2048
             * theFetchStart : 0
             * theSchema :
             * title : 测试习题
             * type : 1
             */

            private int id;
            private int lessonId;
            private int questionType;
            private String theDivision;
            private int theFetchSize;
            private int theFetchStart;
            private String theSchema;
            private String title;
            private int type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLessonId() {
                return lessonId;
            }

            public void setLessonId(int lessonId) {
                this.lessonId = lessonId;
            }

            public int getQuestionType() {
                return questionType;
            }

            public void setQuestionType(int questionType) {
                this.questionType = questionType;
            }

            public String getTheDivision() {
                return theDivision;
            }

            public void setTheDivision(String theDivision) {
                this.theDivision = theDivision;
            }

            public int getTheFetchSize() {
                return theFetchSize;
            }

            public void setTheFetchSize(int theFetchSize) {
                this.theFetchSize = theFetchSize;
            }

            public int getTheFetchStart() {
                return theFetchStart;
            }

            public void setTheFetchStart(int theFetchStart) {
                this.theFetchStart = theFetchStart;
            }

            public String getTheSchema() {
                return theSchema;
            }

            public void setTheSchema(String theSchema) {
                this.theSchema = theSchema;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }

    public static class ExceptionsBean {

        private String id;
        private String message;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
