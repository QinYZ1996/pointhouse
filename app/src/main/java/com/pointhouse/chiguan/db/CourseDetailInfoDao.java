package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;

import java.sql.SQLException;

/**
 * Created by ljj on 2017/7/31.
 */

public class CourseDetailInfoDao extends SimpleDaoImpl<CourseDetailInfo, Integer> implements Idao<CourseDetailInfo, Integer> {

    public CourseDetailInfoDao(Context context) {
        super(context, CourseDetailInfo.class);
    }

    //取得课程信息
    public CourseDetailInfo queryCourseId(int id) throws SQLException {
        Dao<CourseDetailInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<CourseDetailInfo, Integer> where = dao.queryBuilder().where().eq("courseId", id);
            CourseDetailInfo t = where.queryForFirst();
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
