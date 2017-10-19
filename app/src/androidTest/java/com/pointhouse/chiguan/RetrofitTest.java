package com.pointhouse.chiguan;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.pointhouse.chiguan.common.http.RetrofitFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Ormlite测试用例
 * Created by Maclaine on 2017/6/23.
 */

@RunWith(AndroidJUnit4.class)
public class RetrofitTest {
    Context appContext;
    String TAG = "RetrofitTest";

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.pointhouse.chiguan", appContext.getPackageName());
    }

    @Test
    public void test5() throws Exception {
        RetrofitFactory.getInstance().getRequestServices()
                .get("http://apiita.pointhouse.cn:8080/phServerSpi/PH/teacherSearchInit/[老]")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    Log.d(TAG,jsonObject.toJSONString());
                })
        ;

        Thread.sleep(500000);
    }
}
