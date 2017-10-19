package com.pointhouse.chiguan.l1;


import com.pointhouse.chiguan.db.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PC-sunjb on 2017/7/3.
 */

public class Vaildate {

    static final String phone_null = "手机号不能为空";
    static final String phone_error = "手机号长度不符";
    static final String psw_null = "密码不能为空";
    static final String psw_shorterror = "密码长度不能小于6位数";
    static final String psw_longterror = "密码长度不能大于16位数";
    static final String psw_differ = "前后密码不一致";
    static final String phone_failed = "请输入有效的手机号";
    static final String oldPswError = "原密码长度必须大于6位并小于16位";
    static final String newPswError = "新密码长度必须大于6位并小于16位";
    static final String password_error ="密码不能有特殊字符";
    public static String vaildateUserLogin(UserInfo userInfo){
        if(userInfo.getMobile().equals("")){
            return phone_null;
        }else if(userInfo.getMobile().length()>20){
            return phone_error;
        }else if(userInfo.getPassword().length()<6){
            return psw_shorterror;
        }else if(userInfo.getPassword().equals("")){
            return psw_null;
        }else if(userInfo.getPassword().length()>16){
            return psw_longterror;
        }
        return null;
    }

    public static String VaildateMobile(String mobile){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(mobile);
        if(!matcher.matches()){
            return phone_failed;
        }else {
            return null;
        }
    }

    public static String VailDateEditPassWord(String oldpsd,String fnewpsd,String snewpsd){
        if(oldpsd.length()>16){
            return oldPswError;
        }else if(oldpsd.length()<6){
            return oldPswError;
        }else if(fnewpsd.length()>16){
            return newPswError;
        }else if(fnewpsd.length()<6){
            return newPswError;
        }else if(snewpsd.length()>16){
            return newPswError;
        }else if(snewpsd.length()<6){
            return newPswError;
        }
        return null;
    }

//    public static boolean VaildateisLogin(Context context){
//        if(SharedPreferencesUtil.readToken(context)!=null&&!SharedPreferencesUtil.readToken(context).equals("")){
//            return false;
//        }else {
//            return true;
//        }
//    }

    public static String VaildateisPassword(String password){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+${6,16}$");
        Matcher matcher = pattern.matcher(password);
        if(matcher.matches()==false){
            return password_error;
        }else {
            return null;
        }
    }
}
