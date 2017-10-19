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
 * Created by ljj on 2017/9/8.
 */

public class ArticleListGetBean {
    JSONObject json;

    private Boolean hasError = true;

    public Boolean gethasError() {
        return hasError;
    }

    public void sethasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public ArticleListGetBean(String str){
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

    public List<ResultObjectBean.ArticlesBean> getArticlesBeanList() {
        if(hasError) return null;

        Type listType = new TypeToken<ArrayList<ResultObjectBean.ArticlesBean>>() {
        }.getType();

        List<ResultObjectBean.ArticlesBean> list = null;
        try {
            list = new Gson().fromJson(json.getJSONObject("resultObject").getString("articles"), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"articles":[{"categoryId":6,"categoryTitle":"文章分类一","id":1,"price":12,"status":1,"reading":12,"buyersNum":10,"type":0,"contentType":0,"thumb":"文章缩略图","title":"DEMO文章"}]}
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
        private List<ArticlesBean> articles;

        public List<ArticlesBean> getArticles() {
            return articles;
        }

        public void setArticles(List<ArticlesBean> articles) {
            this.articles = articles;
        }

        public static class ArticlesBean {
            /**
             * categoryId : 6
             * categoryTitle : 文章分类一
             * id : 1
             * price : 12
             * status : 1
             * reading : 12
             * buyersNum : 10
             * type : 0
             * contentType : 0
             * thumb : 文章缩略图
             * title : DEMO文章
             */

            private int categoryId;
            private String categoryTitle;
            private int id;
            private double price;
            private int status;
            private int reading;
            private int buyersNum;
            private int type;
            private int contentType;
            private String thumb;
            private String title;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryTitle() {
                return categoryTitle;
            }

            public void setCategoryTitle(String categoryTitle) {
                this.categoryTitle = categoryTitle;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getReading() {
                return reading;
            }

            public void setReading(int reading) {
                this.reading = reading;
            }

            public int getBuyersNum() {
                return buyersNum;
            }

            public void setBuyersNum(int buyersNum) {
                this.buyersNum = buyersNum;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getContentType() {
                return contentType;
            }

            public void setContentType(int contentType) {
                this.contentType = contentType;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
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
