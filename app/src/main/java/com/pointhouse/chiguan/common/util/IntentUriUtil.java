package com.pointhouse.chiguan.common.util;

import android.content.Intent;
import android.net.Uri;

import com.pointhouse.chiguan.Application.GlobalApplication;

/**
 * Created by P on 2017/7/24.
 */

public class IntentUriUtil {

    public static Intent getIntent(String gotoId,String fromId){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+gotoId+"?id="+fromId));
        return intent;
    }
}
