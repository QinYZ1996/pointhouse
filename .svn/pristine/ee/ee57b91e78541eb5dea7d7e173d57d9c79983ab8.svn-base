package com.pointhouse.chiguan.l1_2;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;

/**
 * Created by P on 2017/7/11.
 */

public class Protocol_Activity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l1_2_activity_protocol);
    }

    public void Pro_back(View view){
        super.onBackPressed();
    }

    public void onStart(){
        super.onStart();
        MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
    }
}
