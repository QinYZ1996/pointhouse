package com.pointhouse.chiguan.common.util;

import android.content.Context;
import android.content.SharedPreferences;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by P on 2017/7/25.
 */
public class SharedPreferencesUtil {
    public static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Context context,String spkey){
        sharedPreferences = context.getSharedPreferences(spkey,context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static Object readSharedPreferences(Context context,String spKey,Object object) throws IOException, JSONException {
        Object openobj = null;
        HashMap map = new HashMap();
        if(object==null){
            return null;
        }
        sharedPreferences = getSharedPreferences(context,spKey);
        if(sharedPreferences==null){
            return null;
        }
        HashMap hashMap = toHashMap(object);
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            HashMap.Entry entry = (HashMap.Entry)iterator.next();
            String key = entry.getKey()+"";
            String value = sharedPreferences.getString(key,"");
            map.put(key,value);
        }
        JSONObject jsonObject = new JSONObject(map);
        String str = jsonObject.toString();
        openobj = JsonUtil.JSONToObj(str,object.getClass());
        return openobj;
    }


    public static void saveSharedPreferences(Context context,String spKey,Object object) throws IOException, JSONException {
        if(object==null){
            return;
        }
        sharedPreferences = getSharedPreferences(context,spKey);
        if(sharedPreferences==null){
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        HashMap hashMap = toHashMap(object);
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            HashMap.Entry entry = (HashMap.Entry)iterator.next();
            String key = entry.getKey()+"";
            String value = entry.getValue()+"";
            editor.putString(key,value);
        }
        editor.commit();
    }


    public static String readToken(Context context){
        sharedPreferences = getSharedPreferences(context,"userinfo");
        if(sharedPreferences==null){
            return null;
        }
        String tokenValue = sharedPreferences.getString("token","");
        return tokenValue;
    }

    public static void saveToken(Context context,String token){
        sharedPreferences = getSharedPreferences(context,"userinfo");
        if(sharedPreferences==null){
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    public static String readSimple(Context context, String spname, String key) {
        sharedPreferences = getSharedPreferences(context, spname);
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(key, "");
    }

    public static void saveSimple(Context context, String spname, String key, String value){
        sharedPreferences = getSharedPreferences(context, spname);
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static HashMap<String, String> toHashMap(Object object) throws JSONException, IOException {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = null;
        jsonObject = JsonUtil.objectToJson(object);

        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext())
        {
            String key = String.valueOf(it.next());
            String value =jsonObject.get(key)+"";
            data.put(key, value);
        }
        return data;
    }

}
