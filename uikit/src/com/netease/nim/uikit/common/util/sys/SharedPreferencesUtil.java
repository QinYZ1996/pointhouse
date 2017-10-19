package com.netease.nim.uikit.common.util.sys;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * SharedPreferences工具类
 * Created by Maclaine on 2017/9/13.
 */

public class SharedPreferencesUtil {
    public static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Context context, String spkey){
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
        openobj = JSONToObj(str,object.getClass());
        return openobj;
    }

    public static String readToken(Context context){
        sharedPreferences = getSharedPreferences(context,"userinfo");
        if(sharedPreferences==null){
            return null;
        }
        String tokenValue = sharedPreferences.getString("token","");
        return tokenValue;
    }

    private static HashMap<String, String> toHashMap(Object object) throws JSONException, IOException {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = null;
        jsonObject = objectToJson(object);

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

    public static <T> JSONObject objectToJson(T obj) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw e;
        }
        return new JSONObject(jsonStr);
    }

    public static <T> Object JSONToObj(String jsonStr, Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
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
}
