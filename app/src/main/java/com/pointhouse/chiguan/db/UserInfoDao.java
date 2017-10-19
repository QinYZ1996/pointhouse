package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;
import com.pointhouse.chiguan.common.sqlite.util.DatabaseHelper;

import java.sql.SQLException;

/**
 * Created by P on 2017/7/13.
 */

public class UserInfoDao extends SimpleDaoImpl<UserInfo, Integer> implements Idao<UserInfo, Integer> {
    private Dao<UserInfo, Integer> userDao;
    private DatabaseHelper helper;

    public UserInfoDao(Context context) {
        super(context, UserInfo.class);
    }


    /**
     * 根据手机号获取用户
     * @param mobile 用户名
     * @return UserInfo
     * @throws SQLException
     */
    public UserInfo queryBymobile(String mobile) throws SQLException {
        Dao<UserInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<UserInfo, Integer> where = dao.queryBuilder().where().eq("mobile", mobile);
            UserInfo t = where.queryForFirst();
            dao.commit(databaseConnection);
            return t;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return null;
    }

    public int save(UserInfo userInfo) throws SQLException{
        int i = 0;
        Dao<UserInfo,Integer> dao = getDao();
        try
        {
            i = dao.create(userInfo);
            return i;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;
    }

    public int update(UserInfo userInfo) throws SQLException{
        int i = 0;
        Dao<UserInfo,Integer> dao = getDao();
        try
        {
            i = dao.update(userInfo);
            return i;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;
    }



    /**
     * 本地登陆验证
     * @param mobile 用户名
     * @return UserInfo
     * @throws SQLException
     */
    public UserInfo queryBymobileAndPsw(String mobile,String password) throws SQLException {
        Dao<UserInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<UserInfo, Integer> where = dao.queryBuilder().where().eq("mobile", mobile).and().eq("password",password);
            UserInfo t = where.queryForFirst();
            dao.commit(databaseConnection);
            return t;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return null;
    }

    public int updatePassword(UserInfo userInfo) throws SQLException {
        Dao<UserInfo,Integer> dao = getDao();
        int result =  dao.update(userInfo);
        return result;
    }

    public void deleteUserinfo(UserInfo userInfo) {
        Dao<UserInfo,Integer> dao;
        try {
            dao = getDao();
            dao.delete(userInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserinfo() throws SQLException{
        Dao<UserInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<UserInfo, Integer> where = dao.queryBuilder().where().isNotNull("id");
            UserInfo t = where.queryForFirst();
            dao.commit(databaseConnection);
            return t;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return null;
    }
}
