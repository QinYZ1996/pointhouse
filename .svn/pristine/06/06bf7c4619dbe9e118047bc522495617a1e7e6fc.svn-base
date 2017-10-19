package com.pointhouse.chiguan.common.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.R;

/**
 * Created by ljj on 2017/9/26.
 */

public class BackBtnViewLayout extends RelativeLayout {
    private LinearLayout findBackBtnView;
    private Button findBackBtn;
    private TextView titleBarTitle;
    private RelativeLayout relativeLayout;

    public BackBtnViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_backbtnview, this, true);
        findBackBtnView = (LinearLayout) findViewById(R.id.findBackBtnView);
        findBackBtn = (Button) findViewById(R.id.findBackBtn);
        titleBarTitle = (TextView) findViewById(R.id.titleBarTitle);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);


        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BackBtnViewLayout);

        if (attributes != null) {
            String title_text = attributes.getString(R.styleable.BackBtnViewLayout_title_text);
            titleBarTitle.setText(title_text);
            //设置左边按钮文字颜色
            int title_text_color = attributes.getColor(R.styleable.BackBtnViewLayout_title_text_color, Color.WHITE);
            titleBarTitle.setTextColor(title_text_color);
            float title_text_size = attributes.getDimension(R.styleable.BackBtnViewLayout_title_text_size,0);
            titleBarTitle.setTextSize(title_text_size);
            boolean title_text_horizontal = attributes.getBoolean(R.styleable.BackBtnViewLayout_title_text_horizontal,false);
            if(title_text_horizontal) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                titleBarTitle.setLayoutParams(lp);
            }
        }
    }

    public void setTitleClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            findBackBtnView.setOnClickListener(onClickListener);
            findBackBtn.setOnClickListener(onClickListener);
        }
    }

   public void setAlpha(float alpha){
       relativeLayout.setAlpha(alpha);
   }
}
