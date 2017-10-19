package com.pointhouse.chiguan.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pointhouse.chiguan.Application.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by P on 2017/8/10.
 */

/**
 * 自定义接收器，如果没有将接受不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver{

    private static final String TAG="JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG,"[MyReceiver] onReceive - "+ intent.getAction()+", extras: "+printBundle(bundle));

        if(JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())){
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            GlobalApplication.REGISTRATION_ID = regId;
            Log.d(TAG,"[MyReceiver]接收Registration Id : "+ regId);
        }else if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
            Log.d(TAG,"[MyReceiver]接收到推送下来的自定义消息: "+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context,bundle);
        }else if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            Log.d(TAG,"[MyReceiver]接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG,"[MyReceiver]接收到推送下来的通知的ID: "+ notifactionId);
            processNotification(context,bundle);
        }
    }


    //打印所有的intent extra数据
    private static String printBundle(Bundle bundle){
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


    private void processCustomMessage(Context context,Bundle bundle){
    }

    private void processNotification(Context context,Bundle bundle){

    }
}
