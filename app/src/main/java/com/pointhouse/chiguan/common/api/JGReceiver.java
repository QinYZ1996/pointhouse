package com.pointhouse.chiguan.common.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pointhouse.chiguan.Application.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by PC-sunjb on 2017/6/27.
 */

public class JGReceiver extends BroadcastReceiver{
        private static final String TAG = "JPush";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.e("推送测试","推送成功");
                String[] data = getId(bundle);
                if(data != null){
                    if(!data[0].isEmpty() && data[0].equals("1")){
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + data[1]));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                }
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }

        // 打印所有的 intent extra 数据
        private static String printBundle(Bundle bundle) {
            StringBuilder sb =new StringBuilder();
            for(String key : bundle.keySet()){
                if(key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)){
                    sb.append("\nkey:"+ key +", value:"+ bundle.getInt(key));
                }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                    sb.append("\nkey:"+ key +", value:"+ bundle.getBoolean(key));
                }else if(key.equals(JPushInterface.EXTRA_EXTRA)){
                    if(TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))){
                        Log.i(TAG,"This message has no Extra data");
                        continue;
                    }
                    try{
                        JSONObject json =new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator it =  json.keys();
                        while(it.hasNext()){
                            String myKey = it.next().toString();
                            sb.append("\nkey:"+ key +", value: ["+
                                    myKey +" - "+json.optString(myKey)+"]");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    sb.append("\nkey:"+ key +", value:"+ bundle.getString(key));
                }
            }
            return sb.toString();
        }

        private static String[] getId(Bundle bundle){
            for(String key : bundle.keySet()){
                if(key.equals(JPushInterface.EXTRA_EXTRA)){
                    if(TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))){
                        Log.i(TAG,"This message has no Extra data");
                        continue;
                    }
                    try{
                        JSONObject json =new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        String content = json.getString("content");
                        String type = json.getString("type");
                        return new String[]{type,content};
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        //send msg to MainActivity
        private void processCustomMessage(Context context, Bundle bundle) {
        }

}
