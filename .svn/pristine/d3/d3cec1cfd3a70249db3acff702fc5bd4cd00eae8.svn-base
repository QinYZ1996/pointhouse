package com.pointhouse.chiguan.l1;

import android.content.Context;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.db.UserInfoDao;

import java.sql.SQLException;


/**
 * Created by PC-sunjb on 2017/7/3.
 */

public class LoginDaoImpl {

    public void saveLoginUserinfo(Context context,UserInfo userInfo){
        UserInfoDao userInfoDao  = new UserInfoDao(context);
        try {
            UserInfo info;
            info = getUserinfo(context);
            if(info==null){
                userInfoDao.save(userInfo);
            }else{
                userInfoDao.update(userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserinfo(Context context){
        UserInfoDao userInfoDao = new UserInfoDao(context);
        try {
            UserInfo userInfo = userInfoDao.getUserinfo();
            if(userInfo!=null){
                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUserinfo(Context context){
        UserInfoDao userInfoDao = new UserInfoDao(context);
        userInfoDao.deleteUserinfo(GlobalApplication.user);
    }
}
