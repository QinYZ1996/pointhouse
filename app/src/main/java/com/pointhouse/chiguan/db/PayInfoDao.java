package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ljj on 2017/8/15.
 */

public class PayInfoDao extends SimpleDaoImpl<PayInfo, Integer> implements Idao<PayInfo, Integer> {
    public PayInfoDao(Context context) {
        super(context, PayInfo.class);
    }

    //取得课程信息
    public PayInfo queryCourseId(int isCourse,int id) throws SQLException {
        Dao<PayInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<PayInfo, Integer> where = dao.queryBuilder().where().eq("isCourse",isCourse).and().eq("courseId", id);
            PayInfo t = where.queryForFirst();
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

    //取得下载信息全表
    public List<PayInfo> queryAll() throws SQLException {
        Dao<PayInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            List<PayInfo> all = dao.queryForAll();
            dao.commit(databaseConnection);
            return all;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return null;
    }
}
