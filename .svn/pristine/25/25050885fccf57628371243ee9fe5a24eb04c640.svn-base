package com.pointhouse.chiguan.db;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by PC-sunjb on 2017/6/23.
 */
@DatabaseTable
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo{
    /**
     * user : {"accid":"123","authKey":"认证key","avatar":"头像地址","background":"背景地址","createDatetime":"1999-01-0211: 22: 33","email":"邮箱","lastLoginIp":"最后登录IP","mobile":"132331222111","nickname":"池馆小二","nimToken":"网易云信IM登录密码","openid":"微信OPENID","password":"密码","pushStatus":" 是否允许推送","type":0,"vip":12}
     */

        /**
         * accid : 123
         * authKey : 认证key
         * avatar : 头像地址
         * background : 背景地址
         * createDatetime : 1999-01-0211: 22: 33
         * email : 邮箱
         * lastLoginIp : 最后登录IP
         * mobile : 132331222111
         * nickname : 池馆小二
         * nimToken : 网易云信IM登录密码
         * openid : 微信OPENID
         * password : 密码
         * pushStatus :  是否允许推送
         * type : 0（0：老师 1：学生）,
         * vip : 1 是否VIP（0：否  1：是）
         */
        @DatabaseField(unique = true)
        private long userid;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getTheSchema() {
        return theSchema;
    }

    public void setTheSchema(String theSchema) {
        this.theSchema = theSchema;
    }

    public String getTheFetchStart() {
        return theFetchStart;
    }

    public void setTheFetchStart(String theFetchStart) {
        this.theFetchStart = theFetchStart;
    }

    public String getTheFetchSize() {
        return theFetchSize;
    }

    public void setTheFetchSize(String theFetchSize) {
        this.theFetchSize = theFetchSize;
    }

    public String getTheDivision() {
        return theDivision;
    }

    public void setTheDivision(String theDivision) {
        this.theDivision = theDivision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true)
    private long id;

    private String theSchema="";
    private String theFetchStart="";
    private String theFetchSize="";
    private String theDivision="";
    @DatabaseField
    private String status="";
    @DatabaseField
    private String level="";
    @DatabaseField
    private int jsselectedid=1;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @DatabaseField
    private String registrationId="";
    public int getJsselectedid() {
        return jsselectedid;
    }

    public void setJsselectedid(int jsselectedid) {
        this.jsselectedid = jsselectedid;
    }

    @DatabaseField()
        private String accid="";
    @DatabaseField()
        private String authKey="";
    @DatabaseField()
        private String avatar="";
    @DatabaseField()
        private String background="";
    @DatabaseField()
        private String createDatetime="";
    @DatabaseField()
        private String email="";
    @DatabaseField()
        private String lastLoginIp="";
    @DatabaseField()
        private String mobile="";
    @DatabaseField()
        private String nickname="";
    @DatabaseField()
        private String nimToken="";
    @DatabaseField()
        private String openid="";
    @DatabaseField()
        private String password="";
    @DatabaseField()
        private String pushStatus="";
    @DatabaseField()
        private int type=1;
    @DatabaseField()
    /**
     * 1vip 0不是vip
     */
        private int vip=0;
    @DatabaseField()
        private String vipEndDate="";

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    private String wechat="";
    public UserInfo(){}

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getAuthKey() {
            return authKey;
        }

        public void setAuthKey(String authKey) {
            this.authKey = authKey;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLastLoginIp() {
            return lastLoginIp;
        }

        public void setLastLoginIp(String lastLoginIp) {
            this.lastLoginIp = lastLoginIp;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNimToken() {
            return nimToken;
        }

        public void setNimToken(String nimToken) {
            this.nimToken = nimToken;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPushStatus() {
            return pushStatus;
        }

        public void setPushStatus(String pushStatus) {
            this.pushStatus = pushStatus;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public String getVipEndDate() {
            return vipEndDate;
        }

        public void setVipEndDate(String vipEndDate) {
            this.vipEndDate = vipEndDate;
        }
    }

