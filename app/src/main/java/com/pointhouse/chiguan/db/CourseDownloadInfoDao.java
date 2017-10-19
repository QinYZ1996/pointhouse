package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ljj on 2017/7/18.
 */

public class CourseDownloadInfoDao extends SimpleDaoImpl<CourseDownloadInfo, Integer> implements Idao<CourseDownloadInfo, Integer> {

    public CourseDownloadInfoDao(Context context) {
        super(context, CourseDownloadInfo.class);
    }

    //取得单独文件下载信息
    public CourseDownloadInfo queryId(Integer id) throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<CourseDownloadInfo, Integer> where = dao.queryBuilder().where().eq("id", id);
            CourseDownloadInfo t = where.queryForFirst();
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

    //取得课程下载信息
    public List<CourseDownloadInfo> queryLessonId(int id) throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<CourseDownloadInfo, Integer> where = dao.queryBuilder().where().eq("lessonId", id);
            List<CourseDownloadInfo> t = where.query();
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
    public List<CourseDownloadInfo> queryAll() throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            List<CourseDownloadInfo> all = dao.queryForAll();
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

    public int save(CourseDownloadInfo courseDownloadInfo) throws SQLException {
        return super.save(courseDownloadInfo);

    }

    public int updateProgress(CourseDownloadInfo courseDownloadInfo) throws SQLException {
        int result =  update(courseDownloadInfo);
        return result;
    }

    /**
     * 所有已下载
     * @return CourseDownloadInfo
     * @throws SQLException SQLException
     */
    public List<CourseDownloadInfo> queryAllDownload() throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<CourseDownloadInfo, Integer> where = dao.queryBuilder().orderBy("index",true).orderBy("id",false).where().eq("State",4);
            List<CourseDownloadInfo> t = where.query();
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

    /**
     * 所有下载
     * @return CourseDownloadInfo
     * @throws SQLException SQLException
     */
    public List<CourseDownloadInfo> queryAllDownloadList() throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            QueryBuilder<CourseDownloadInfo, Integer> where = dao.queryBuilder().orderBy("index",true).orderBy("id", false);
            List<CourseDownloadInfo> t = where.query();
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

    /**
     * 所有已下载
     * @return CourseDownloadInfo
     * @throws SQLException SQLException
     */
    public List<CourseDownloadInfo> queryAllDownloading() throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<CourseDownloadInfo, Integer> where = dao.queryBuilder().orderBy("index",true).orderBy("id",false).where().notIn("State",4);
            List<CourseDownloadInfo> t = where.query();
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

    /**
     * 根据课程查找全部
     * @return CourseDownloadInfo
     * @throws SQLException SQLException
     */
    public List<CourseDownloadInfo> queryAllByCourseId(Integer courseId) throws SQLException {
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<CourseDownloadInfo, Integer> where = dao.queryBuilder().where().eq("courseId",courseId);
            List<CourseDownloadInfo> t = where.query();
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

    public int deleteByLessonId(Integer lessonId) throws SQLException{
        Dao<CourseDownloadInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            DeleteBuilder<CourseDownloadInfo, Integer> deleteBuilder  = dao.deleteBuilder();
            deleteBuilder.where().eq("lessonId",lessonId);
            int delete = deleteBuilder.delete();
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
}
