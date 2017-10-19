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

public class ExerciseDetailGetBean {
    private JSONObject json;
    private Boolean hasError = true;

    public Boolean hasError() {
        return hasError;
    }

    public ExerciseDetailGetBean(String str) {
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

    public ResultObjectBean getResultObjectBean() {
        if (hasError) return null;

        ResultObjectBean result;
        try {
            result = new Gson().fromJson(json.getString("resultObject"), new TypeToken<ResultObjectBean>() {
            }.getType());
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ResultObjectBean.AudiosBean> getAudiosBean() {
        if (hasError) return null;

        List<ResultObjectBean.AudiosBean> list = new ArrayList<>();
        try {
            list.addAll(new Gson().fromJson(json.getJSONObject("resultObject").getString("audios"), new TypeToken<ArrayList<ResultObjectBean.AudiosBean>>() {
            }.getType()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ResultObjectBean.StemBean> getStemBeanList() {
        if (hasError) return null;

        List<ResultObjectBean.StemBean> list = new ArrayList<>();
        try {
            list.addAll(new Gson().fromJson(json.getJSONObject("resultObject").getString("stem"), new TypeToken<ArrayList<ResultObjectBean.StemBean>>() {
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

        return list.size() > 0 ? list.get(0) : new ExceptionsBean();
    }

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"audios":[{"vid":"音频IDSDK下载可用","original":"原文","videoName":"音频频名","origUrl":"音频URL，流下载可用"}],"artice":"&lt;p&gt;文本内容1&lt;/p&gt;","id":1,"stem":[{"no":"2(题号)","question":"xxxxxxxx","answer":["A","B"],"option":["AAA","BBB","CCC","DDD"]}]}
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

    public static class ResultObjectBean {
        /**
         * audios : [{"vid":"音频IDSDK下载可用","original":"原文","videoName":"音频频名","origUrl":"音频URL，流下载可用"}]
         * artice : &lt;p&gt;文本内容1&lt;/p&gt;
         * id : 1
         * stem : [{"no":"2(题号)","question":"xxxxxxxx","answer":["A","B"],"option":["AAA","BBB","CCC","DDD"]}]
         */

        private String artice;
        private int id;
        private List<AudiosBean> audios;
        private List<StemBean> stem;
        private List<VideosBean> videos;

        public List<VideosBean> getVideos() {
            return videos;
        }

        public void setVideos(List<VideosBean> videos) {
            this.videos = videos;
        }

        public String getArtice() {
            return artice;
        }

        public void setArtice(String artice) {
            this.artice = artice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<AudiosBean> getAudios() {
            return audios;
        }

        public void setAudios(List<AudiosBean> audios) {
            this.audios = audios;
        }

        public List<StemBean> getStem() {
            return stem;
        }

        public void setStem(List<StemBean> stem) {
            this.stem = stem;
        }

        public static class AudiosBean {
            /**
             * vid : 音频IDSDK下载可用
             * original : 原文
             * videoName : 音频频名
             * origUrl : 音频URL，流下载可用
             */

            private String vid;
            private String original;
            private String videoName;
            private String origUrl;

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getVideoName() {
                return videoName;
            }

            public void setVideoName(String videoName) {
                this.videoName = videoName;
            }

            public String getOrigUrl() {
                return origUrl;
            }

            public void setOrigUrl(String origUrl) {
                this.origUrl = origUrl;
            }
        }

        public static class VideosBean {

            private String vid;
            private String original;
            private String videoName;
            private String origUrl;

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getVideoName() {
                return videoName;
            }

            public void setVideoName(String videoName) {
                this.videoName = videoName;
            }

            public String getOrigUrl() {
                return origUrl;
            }

            public void setOrigUrl(String origUrl) {
                this.origUrl = origUrl;
            }
        }

        public static class StemBean {
            /**
             * no : 2(题号)
             * question : xxxxxxxx
             * answer : ["A","B"]
             * option : ["AAA","BBB","CCC","DDD"]
             */

            private String no;
            private String question;
            private List<String> answer;
            private List<String> option;

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public List<String> getAnswer() {
                return answer;
            }

            public void setAnswer(List<String> answer) {
                this.answer = answer;
            }

            public List<String> getOption() {
                return option;
            }

            public void setOption(List<String> option) {
                this.option = option;
            }
        }
    }
}
