package com.pointhouse.chiguan.common.mydialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pointhouse.chiguan.R;

/**
 * Created by gyh on 2017/9/15.
 */

public class MyDialogBuilder {

    public MyDialog myDialog;

    public MyDialogBuilder(Context context, String msg, ConfirmCallBack confirmCallBack) {
        View view = View.inflate(context, R.layout.common_dialog, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(msg);
        final TextView btnYes = (TextView) view.findViewById(R.id.f_quchecbutton_btn_queding);
        final TextView btnNo = (TextView) view.findViewById(R.id.f_quchecbutton_btn_quxiao);
        myDialog = new MyDialog(view.getContext(), 0, 0, view, R.style.DialogTheme);
        myDialog.setCancelable(false);
        btnYes.setOnClickListener(v1 -> {
            if (confirmCallBack != null) {
                confirmCallBack.onConfirm();
            }
            myDialog.cancel();
        });

        btnNo.setOnClickListener(v1 -> myDialog.cancel());
    }

    public void show() {
        if (myDialog == null) return;
        myDialog.show();
    }

    public interface ConfirmCallBack {
        void onConfirm();
    }
}
