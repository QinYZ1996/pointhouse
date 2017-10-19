package com.pointhouse.chiguan.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;

import java.sql.SQLException;

/**
 * Created by ljj on 2017/8/1.
 */

public class ImgUrlInfoDao extends SimpleDaoImpl<ImgUrlInfo, Integer> implements Idao<ImgUrlInfo, Integer> {
    public ImgUrlInfoDao(Context context) {
        super(context, ImgUrlInfo.class);
    }

    //取得下载信息
    public ImgUrlInfo queryUrl(String url) throws SQLException {
        Dao<ImgUrlInfo,Integer> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            Where<ImgUrlInfo, Integer> where = dao.queryBuilder().where().eq("url", url);
            ImgUrlInfo t = where.queryForFirst();
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
