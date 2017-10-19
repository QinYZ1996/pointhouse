package com.pointhouse.chiguan.w1_7;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.pointhouse.chiguan.R;

/**
 * Created by P on 2017/7/13.
 */

public class TakePhotoPopWin extends PopupWindow {
    private View mMenuView;
    private Button takePhotoBtn;
    private Button pickPictureBtn;
    private Button cancelBtn;

    public TakePhotoPopWin(Context context,View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.common_popupwindow, null);
        takePhotoBtn = (Button) mMenuView.findViewById(R.id.btn_take_photo);
        pickPictureBtn = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
        cancelBtn = (Button) mMenuView.findViewById(R.id.btn_cancel);

        //取消按钮
        cancelBtn.setOnClickListener(v -> {
            //销毁弹出框
            dismiss();
        });
        //设置按钮监听
        takePhotoBtn.setOnClickListener(itemsOnClick);
        pickPictureBtn.setOnClickListener(itemsOnClick);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.DialogStyleBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener((v, event) -> {

            int height = mMenuView.findViewById(R.id.pop_layout).getTop();
            int y=(int) event.getY();
            if(event.getAction()==MotionEvent.ACTION_UP){
                if(y<height){
                    dismiss();
                }
            }
            return true;
        });
    }
}
