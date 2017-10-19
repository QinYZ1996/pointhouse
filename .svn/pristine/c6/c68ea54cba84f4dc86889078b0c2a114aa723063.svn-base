package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;

import java.sql.SQLException;
import java.util.List;

import static android.R.attr.id;

/**
 * 我的学习
 * Created by Maclaine on 2017/8/10.
 */

public class StudyInfoDao extends SimpleDaoImpl<StudyInfo, Integer> implements Idao<StudyInfo, Integer> {
    public StudyInfoDao(Context context) {
        super(context, StudyInfo.class);
    }

    /**
     * 保存或更新
     * @param t StudyInfo
     * @throws SQLException SQLException
     */
    public void saveOrUpdate(StudyInfo t) throws SQLException {
        Dao<StudyInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<StudyInfo, Integer> where = dao.queryBuilder().where().eq("vid", t.getVid()).and().eq("lessonId",t.getLessonId());
            StudyInfo temp = where.queryForFirst();
            if(temp!=null){
                t.setId(temp.getId());
            }
            dao.createOrUpdate(t);
            dao.commit(databaseConnection);
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
    }

    /**
     * 查找全部
     * @return List<StudyInfo>
     * @throws SQLException SQLException
     */
    public List<StudyInfo> getAll() throws SQLException{
        Dao<StudyInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            QueryBuilder<StudyInfo, Integer> where = dao.queryBuilder().orderBy("updateDate",false).orderBy("id", true);
            List<StudyInfo> t = where.query();
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
     * 只在StudyInfo不存在时保存
     * @param t StudyInfo
     * @throws SQLException SQLException
     */
    public void saveIfNotExist(StudyInfo t) throws SQLException {
        Dao<StudyInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<StudyInfo, Integer> where = dao.queryBuilder().where().eq("vid", t.getVid()).and().eq("lessonId",t.getLessonId());
            StudyInfo temp = where.queryForFirst();
            if(temp==null){
                dao.create(t);
            }
            dao.commit(databaseConnection);
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
    }

    /**
     * 查找全部
     * @return List<StudyInfo>
     * @throws SQLException SQLException
     */
    public List<StudyInfo> getAllForMyStudy() throws SQLException{
        Dao<StudyInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            List<StudyInfo> t = dao.queryForAll();
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
