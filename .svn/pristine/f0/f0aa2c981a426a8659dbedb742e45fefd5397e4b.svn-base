package com.pointhouse.chiguan.w1_9;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.base.MediaBaseActivity;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.UserInfoDao;
import com.pointhouse.chiguan.w1.PersonalCenterActivity;
import com.pointhouse.chiguan.w1_7.MydataActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gyh on 2017/7/14.
 */

public class EditNicknameActivity extends MediaBaseActivity {

    private static final String TAG = "W1-9";

    private EditText editText;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.w1_9_activity_editnickname);

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        initView(nickname);
    }

    public void initView(String nickname) {
        editText = (EditText) findViewById(R.id.editnickname_text);
        editText.setText(nickname);
        Button saveBtn = (Button) findViewById(R.id.editnickname_savebtn);
        saveBtn.setOnClickListener(v -> {
            String editNickname = editText.getText().toString();
            Map<String, String> param = new HashMap<>();
            param.put("nickname", editNickname);
            JSONObject jsonObject = new JSONObject(param);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            String url = JsonUtil.getURLWithArrayParamIfExists("updateUserInfo");
            RetrofitFactory.getInstance().getRequestServicesToken().post(url, RetrofitFactory.createRequestBody(jsonArray.toString()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        int resultCode = result.getIntValue("resultCode");
                        if (resultCode == 1) {
                            GlobalApplication.user.setNickname(editNickname);
                            if (new UserInfoDao(this).update(GlobalApplication.user) != 1) {
                                Log.e(TAG, "本地保存失败");
                            }

                            Intent intent = new Intent(this, MydataActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            ToastUtil.getToast(this, JsonUtil.getRequestErrMsg(TAG, this, result), "center", 0, 180).show();
                        }
                    }, throwable -> {
                        Log.e(TAG, throwable.getMessage());
                        ToastUtil.getToast(this, getString(R.string.req_fail_msg), "center", 0, 180).show();
                    });
        });
    }

    public void EditNickname_back(View view){
        super.onBackPressed();
    }
}
