package com.pointhouse.chiguan.common.sqlite.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pointhouse.chiguan.db.CourseDetailInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.ExerciseInfo;
import com.pointhouse.chiguan.db.ImgUrlInfo;
import com.pointhouse.chiguan.db.PayInfo;
import com.pointhouse.chiguan.db.SearchInfo;
import com.pointhouse.chiguan.db.StudyInfo;
import com.pointhouse.chiguan.db.User;
import com.pointhouse.chiguan.db.UserInfo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maclaine on 2017/6/22.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    /**
     * 数据库名字
     */
    private static final String DB_NAME = "pointhouse.db";
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;
    /**
     * 单例实例
     */
    private static DatabaseHelper instance;
    /**
     * 用来存放Dao的缓存
     */
    private Map<String, Dao> daos = null;

    /**
     * 构造方法<br>
     * 当DB_VERSION变更时，会调用onUpgrade
     *
     * @param context
     */
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * 创建表
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.d("========>","更新表");

        try {
            //新建的实体类需要在这里创建表
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, CourseDownloadInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, SearchInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, CourseDetailInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, ImgUrlInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, UserInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, ExerciseInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, PayInfo.class);
            TableUtils.createTableIfNotExists(connectionSource, StudyInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新表
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.d("========>","更新表");
//        try {
//            if(newVersion == 0) {
//                //更新数据库时删除表重建，需要保留数据时自行设定逻辑
//                TableUtils.dropTable(connectionSource, User.class, true);
//                TableUtils.dropTable(connectionSource, CourseDownloadInfo.class, true);
//                TableUtils.dropTable(connectionSource, SearchInfo.class, true);
//                TableUtils.dropTable(connectionSource, CourseDetailInfo.class, true);
//                TableUtils.dropTable(connectionSource, ImgUrlInfo.class, true);
//                TableUtils.dropTable(connectionSource, UserInfo.class, true);
//                TableUtils.dropTable(connectionSource, ExerciseInfo.class, true);
//                TableUtils.dropTable(connectionSource, PayInfo.class, true);
//                TableUtils.dropTable(connectionSource, StudyInfo.class, true);
//            }
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    Log.d("========>","创建DataBaseHelper");
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 通过类来获得指定的Dao
     */
    public synchronized <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
            throws SQLException {
        if(daos==null){
            daos=new HashMap<String, Dao>();
        }else if(daos.containsKey(clazz.getSimpleName())){
            return (D) daos.get(clazz.getSimpleName());
        }
        Dao<T, ?> dao = DaoManager.createDao(getConnectionSource(), clazz);
        dao.setObjectCache(true);
        daos.put(clazz.getSimpleName(),dao);
        return (D) dao;
    }

    /**
     * 通过类来获得指定的ExceptionDao
     */
    public synchronized <T> RuntimeExceptionDao<T, ?> getExceptionDao(
            Class<T> clazz) {
        RuntimeExceptionDao<T, ?> exceptionDao = null;
        try {
            exceptionDao = RuntimeExceptionDao.createDao(getConnectionSource(),clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exceptionDao;
    }

    /**
     * 通过类来获得指定的Dao
     */
//    public synchronized Dao getDao(Class clazz) throws SQLException {
//        Dao dao = null;
//        String className = clazz.getSimpleName();
//        if (daos.containsKey(className)) {
//            dao = super.getDao(clazz);
//            daos.put(className, dao);
//        }
//        return dao;
//    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        daos = null;
        DaoManager.clearDaoCache();
        connectionSource.close();
        OpenHelperManager.releaseHelper();
    }
}
