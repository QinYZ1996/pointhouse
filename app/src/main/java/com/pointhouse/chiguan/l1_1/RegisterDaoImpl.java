package com.pointhouse.chiguan.l1_1;

import android.content.Context;
import com.pointhouse.chiguan.db.UserInfo;
import com.pointhouse.chiguan.db.UserInfoDao;
import java.sql.SQLException;


/**
 * Created by PC-sunjb on 2017/7/3.
 */

public class RegisterDaoImpl {

    public void updateUserInfo(Context context, UserInfo userInfo) throws SQLException {
        UserInfoDao userInfoDao  = new UserInfoDao(context);
        userInfoDao.update(userInfo);
    }

}


