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
 * UserDao示例，建议通过继承SimpleDaoImpl来获取dao实例
 * Created by Maclaine on 2017/6/22.
 */

public class UserDao extends SimpleDaoImpl<User, Integer> implements Idao<User, Integer> {
    private Dao<User, Integer> userDao;
    private DatabaseHelper helper;

    public UserDao(Context context) {
        super(context, User.class);
    }

    /**
     * 根据用户名获取用户
     * @param name 用户名
     * @return User
     * @throws SQLException
     */
    public User queryByName(String name) throws SQLException {
        Dao<User, Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<User, Integer> where = dao.queryBuilder().where().eq("name", name);
            User t = where.queryForFirst();
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
