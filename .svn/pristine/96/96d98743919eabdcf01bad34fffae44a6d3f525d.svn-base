package com.pointhouse.chiguan.common.sqlite.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.util.DatabaseHelper;

import java.sql.SQLException;

/**
 * 通用dao模板
 * Created by Maclaine on 2017/6/22.
 *
 * @param <T> 实体类
 * @param <Z> 主键类型
 */

public class SimpleDaoImpl<T, Z> implements Idao<T, Z> {
    protected Context mContext;
    private Class<T> clazz;

    public SimpleDaoImpl(Context context, Class<T> clazz) {
        this.clazz = clazz;
        this.mContext = context;
    }

    public Dao<T, Z> getDao() throws SQLException {
        return DatabaseHelper.getHelper(mContext).getDao(clazz);
    }

    /**
     * 增，带事务操作
     *
     * @param t
     *            泛型实体类
     * @return 影响的行数
     * @throws SQLException
     *             SQLException异常
     */
    @Override
    public int save(T t) throws SQLException {
        Dao<T, Z> dao = getDao();

        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            int save = dao.create(t);
            dao.commit(databaseConnection);
            return save;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return 0;
    }

    /**
     * /**
     * 删除，带事务操作
     *
     * @param t
     *            泛型实体类
     * @return 影响的行数
     * @throws SQLException
     *             SQLException异常
     */
    @Override
    public int delete(T t) throws SQLException {
        Dao<T, Z> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            int delete = dao.delete(t);
            dao.commit(databaseConnection);
            return delete;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return 0;
    }

    /**
     * 改，带事务操作
     *
     * @param t
     *            泛型实体类
     * @return 影响的行数
     * @throws SQLException
     *             SQLException异常
     */
    @Override
    public int update(T t) throws SQLException {
        Dao<T, Z> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            int update = dao.update(t);
            dao.commit(databaseConnection);
            return update;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return 0;
    }

    /**
     * 查，带事务操作
     *
     * @param id
     *            泛型实体类ID
     * @return 实体类，不存在则返回Null
     * @throws SQLException
     *             SQLException异常
     */
    @Override
    public T queryById(Z id) throws SQLException {
        Dao<T, Z> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            T t = dao.queryForId(id);
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
