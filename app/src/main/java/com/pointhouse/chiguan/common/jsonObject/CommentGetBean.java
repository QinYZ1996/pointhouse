package com.pointhouse.chiguan.common.jsonObject;

import java.util.List;

/**
 * Created by gyh on 2017/9/14.
 */

public class CommentGetBean {

    /**
     * exceptions : []
     * messages : []
     * resultCode : 1
     * resultObject : {"comments":[{"content":"4","createDate":"2017年09月14日","id":26,"user":{"avatar":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/avatar/2017/08/1503131346628.png","nickname":"Android测试-干"},"userId":9},{"content":"3","createDate":"2017年09月14日","id":25,"user":{"avatar":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/avatar/2017/08/1503131346628.png","nickname":"Android测试-干"},"userId":9},{"content":"2","createDate":"2017年09月14日","id":24,"user":{"avatar":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/avatar/2017/08/1503131346628.png","nickname":"Android测试-干"},"userId":9},{"content":"1","createDate":"2017年09月14日","id":23,"user":{"avatar":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/avatar/2017/08/1503131346628.png","nickname":"Android测试-干"},"userId":9}]}
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
        private List<CommentsBean> comments;

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * content : 4
             * createDate : 2017年09月14日
             * id : 26
             * user : {"avatar":"http://pointhouse.oss-cn-shanghai.aliyuncs.com/avatar/2017/08/1503131346628.png","nickname":"Android测试-干"}
             * userId : 9
             */

            private String content;
            private String createDate;
            private int id;
            private UserBean user;
            private int userId;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public static class UserBean {
                /**
                 * avatar : http://pointhouse.oss-cn-shanghai.aliyuncs.com/avatar/2017/08/1503131346628.png
                 * nickname : Android测试-干
                 */

                private String avatar;
                private String nickname;

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
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
}
