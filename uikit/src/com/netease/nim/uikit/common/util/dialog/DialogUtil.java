package com.netease.nim.uikit.common.util.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;

/**
 * 提示框
 * Created by Maclaine on 2017/9/5.
 */

public class DialogUtil {
    Context mContext;
    RelativeLayout layout;
    public Dialog dialog;

    private DialogUtil(Context context) {
        this.mContext = context;
        dialog = new AlertDialog.Builder(mContext).show();
        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_alert, null);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(layout);
    }

    public static DialogUtil Builder(Context context) {
        return new DialogUtil(context);
    }

    public DialogUtil setMessage(String message) {
        TextView text = (TextView) layout.findViewById(R.id.dialog_text);
        text.setText(message);
        return this;
    }

    public DialogUtil setPositiveButton(String message, final OnClickListener listener) {
        TextView view = (TextView) layout.findViewById(R.id.dialog_ok);
        setTextViewListener(view, message, listener);
        return this;
    }

    public DialogUtil setNegativeButton(String message, final OnClickListener listener) {
        TextView view = (TextView) layout.findViewById(R.id.dialog_cancel);
        setTextViewListener(view, message, listener);
        View d = layout.findViewById(R.id.dialog_text_divide);
        d.setVisibility(View.VISIBLE);
        return this;
    }

    private void setTextViewListener(TextView view, String message, final OnClickListener listener) {
        view.setVisibility(View.VISIBLE);
        view.setText(message);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, v);
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    public interface OnClickListener {
        public void onClick(Dialog dialog, View view);
    }
}
