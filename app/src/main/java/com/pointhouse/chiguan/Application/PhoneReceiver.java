package com.pointhouse.chiguan.Application;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 来电监听
 * Created by Maclaine on 2017/8/23.
 */

public class PhoneReceiver extends BroadcastReceiver {
    private final static String TAG = "PhoneReceiver";
    static PhoneStateListener phoneListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        if (phoneListener == null) {
            phoneListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    try {
                        IBinder binder = peekService(context,new Intent(context, MediaService.class));
                        if(binder==null){
                            return;
                        }
                        MediaService service = ((MediaService.MediaBinder) binder).getService();
                        switch (state) {
                            case TelephonyManager.CALL_STATE_IDLE:
//                            Log.d(TAG, "来电-挂断");
                                service.resumeMedia();
                                break;
                            case TelephonyManager.CALL_STATE_OFFHOOK:
//                            Log.d(TAG, "来电-接听");
                                service.breakMedia();
                                break;
                            case TelephonyManager.CALL_STATE_RINGING:
//                            Log.d(TAG, "来电-响铃:来电号码" + incomingNumber);
                                service.breakMedia();
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,e.getMessage());
                    }
                }
            };
            tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
       /* if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "去电-挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "去电-接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "去电-响铃:来电号码" + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
                    // 输出来电号码
                    break;
            }
        } */
    }
}
