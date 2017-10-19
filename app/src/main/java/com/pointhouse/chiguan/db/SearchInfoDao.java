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
 * Created by ljj on 2017/7/22.
 */

public class SearchInfoDao extends SimpleDaoImpl<SearchInfo, Integer> implements Idao<SearchInfo, Integer> {

        public SearchInfoDao(Context context) {
                super(context, SearchInfo.class);
        }

        //取得老师检索记录
        public List<SearchInfo> queryTypeAll(Integer type) throws SQLException {
                Dao<SearchInfo,Integer> dao = getDao();
                DatabaseConnection databaseConnection = null;
                try {
                        databaseConnection = dao.startThreadConnection();
                        dao.setAutoCommit(databaseConnection, false);
                        Where<SearchInfo, Integer> where = dao.queryBuilder().where().eq("type", type);
                        List<SearchInfo> t = where.query();
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

        public int save(SearchInfo searchInfo) throws SQLException {
                return super.save(searchInfo);
        }

        public int updateProgress(SearchInfo searchInfo) throws SQLException {
                int result =  update(searchInfo);
                return result;
        }
}
