package com.pointhouse.chiguan;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.pointhouse.chiguan.common.sqlite.dao.Idao;
import com.pointhouse.chiguan.common.sqlite.dao.SimpleDaoImpl;
import com.pointhouse.chiguan.db.UserDao;
import com.pointhouse.chiguan.db.User;
import com.pointhouse.chiguan.common.sqlite.util.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
/**
 * Ormlite测试用例
 * Created by Maclaine on 2017/6/23.
 */

@RunWith(AndroidJUnit4.class)
public class SqliteTest {
    Context appContext;
    String tag="SqliteTest";
    @Before
    public void setUp(){
        appContext = InstrumentationRegistry.getTargetContext();
        //assertEquals("com.pointhouse.chiguan", appContext.getPackageName());
    }

    @Test
    public void useAppContext() throws Exception {
        DatabaseHelper helper = DatabaseHelper.getHelper(appContext);
        Dao<User, Integer> userDao = helper.getDao(User.class);
        Log.d(tag,userDao.toString());
        assertEquals(userDao.toString(),helper.getDao(User.class).toString());
    }
    @Test
    public void daoOp() throws Exception {
        Idao userDao=new SimpleDaoImpl(appContext,User.class);
        User user=new User();
        user.setName("b");
        int result=userDao.save(user);
        Log.d(tag,String.valueOf(result));
        User u = (User) userDao.queryById(2);
        if(u!=null){
            Log.d(tag,u.getName());
        }
    }
    @Test
    public void daoQuery() throws Exception {
        UserDao userDao=new UserDao(appContext);
        User user = userDao.queryByName("c");
        if(user!=null){
            Log.d(tag,String.valueOf(user.getId()));
        }else{
            Log.d(tag,"none");
        }
    }
}
