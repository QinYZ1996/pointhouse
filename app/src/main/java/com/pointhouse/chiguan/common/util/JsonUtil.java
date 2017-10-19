package com.pointhouse.chiguan.common.util;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointhouse.chiguan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by PC-sunjb on 2017/6/29.
 */

public class JsonUtil {

    public JsonUtil() {
    }


    /**
     * 将json转化为实体POJO
     *
     * @param jsonStr
     * @param obj
     * @return
     */
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


    /**
     * 将实体POJO转化为JSON
     *
     * @param obj
     * @return
     * @throws JSONException
     * @throws IOException
     */
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

    public static String initGetRequestParm(String requestparm) {
        String[] requestparms;
        String rparm = null;
        if (requestparm != null) {
            requestparms = requestparm.split(",");
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            for (String parm : requestparms) {
                jsonArray.put(parm);
            }
            rparm = jsonArray.toString();

        } else {
            return null;
        }
        return rparm;
    }

    /**
     * 生成带API请求参数的url，params不传则返回不带参数url
     * 请求参数为数组格式
     *
     * @param requestMethod API请求方法名
     * @param params        参数值
     * @return
     */
    public static String getURLWithArrayParamIfExists(String requestMethod, String... params) {
        String url = Constant.URL_BASE;
        if (requestMethod == null || "".equals(requestMethod)) return url;
        if (requestMethod.startsWith("/") && requestMethod.length() > 1)
            requestMethod = requestMethod.substring(1);
        if (requestMethod.endsWith("/") && requestMethod.length() > 1)
            requestMethod = requestMethod.substring(0, requestMethod.lastIndexOf("/") - 1);

        url += requestMethod + "/";
        JSONArray jsonArray = new JSONArray();
        if (params != null) {
            for (String param : params) {
                jsonArray.put(param);
            }
        }

        return jsonArray.length() > 0 ? url + jsonArray.toString() : url;
    }

    /**
     * 返回API请求异常信息
     *
     * @param tag
     * @param context
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static String getRequestErrMsg(String tag, Context context, com.alibaba.fastjson.JSONObject jsonObject) throws JSONException {
        String errMsg = jsonObject.getJSONArray("exceptions").getJSONObject(0).getString("message");
        if (tag != null && !"".equals(tag)) {
            Log.e(tag, errMsg);
        }

        return jsonObject.getIntValue("resultCode") == 2 ? errMsg : context.getString(R.string.req_sys_err_msg);
    }
}
