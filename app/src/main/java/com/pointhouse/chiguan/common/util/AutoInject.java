package com.pointhouse.chiguan.common.util;

import android.app.Activity;
import android.content.Context;

import com.pointhouse.chiguan.common.annotation.ViewId;

import java.lang.reflect.Field;

/**
 * 自动注入
 * Created by Maclaine on 2017/7/27.
 */
public class AutoInject {

    /**
     * 全部注入
     * @param context Activity
     */
    public static void injectAll(Context context) {
        try {
            Class<?> clazz= context.getClass();
            Field[] fields = clazz.getDeclaredFields();// 获得Activity中声明的字段
            for (Field field : fields) {
                // 查看这个字段是否有我们自定义的注解类标志的
                if (field.isAnnotationPresent(ViewId.class)) {
                    ViewId inject = field.getAnnotation(ViewId.class);
                    int id = inject.value();
                    if (id > 0) {
                        field.setAccessible(true);
                        field.set(context, ((Activity)context).findViewById(id));// 给我们要找的字段设置值
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}