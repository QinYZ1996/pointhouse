package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by gyh on 2017/8/2.
 */

public class ExerciseInfoDao extends SimpleDaoImpl<ExerciseInfo, Integer> {

    public ExerciseInfoDao(Context context, Class<ExerciseInfo> clazz) {
        super(context, clazz);
    }

    public List<ExerciseInfo> queryExerciseInfosByExerciseId(int exerciseId) throws SQLException {
        Dao<ExerciseInfo, Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<ExerciseInfo, Integer> where = dao.queryBuilder().where().eq("exerciseId", exerciseId);
            List<ExerciseInfo> t = where.query();
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

    public int deleteExerciseInfosByExerciseId(int exerciseId) throws SQLException {
        Dao<ExerciseInfo, Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            DeleteBuilder<ExerciseInfo, Integer> builder = dao.deleteBuilder();
            builder.where().eq("exerciseId", exerciseId);
            int delete = builder.delete();
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

    public int deleteExerciseInfosByLessonId(int lessonId) throws SQLException {
        Dao<ExerciseInfo, Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            DeleteBuilder<ExerciseInfo, Integer> builder = dao.deleteBuilder();
            builder.where().eq("lessonId", lessonId);
            int delete = builder.delete();
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

    public int saveExerciseInfos(List<ExerciseInfo> infos) throws SQLException {
        Dao<ExerciseInfo, Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            for (ExerciseInfo info : infos) {
                dao.create(info);
            }
            dao.commit(databaseConnection);
            return 1;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.endThreadConnection(databaseConnection);
        }
        return 0;
    }
}
