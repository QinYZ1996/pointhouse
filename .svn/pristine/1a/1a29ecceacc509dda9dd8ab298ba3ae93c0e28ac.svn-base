package com.pointhouse.chiguan.w1_10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.db.OtherMsg;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by P on 2017/8/18.
 */

public class MyMessage_OtherAdapter  extends BaseAdapter {

    private Context context;
    private List<OtherMsg> otherMsgList;
    public MyMessage_OtherAdapter(Context activity,List<OtherMsg> otherMessages){
        this.otherMsgList = otherMessages;
        context = activity;
    }

    @Override
    public int getCount() {
        return this.otherMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.otherMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OtherMsg otherMsg = otherMsgList.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.w1_10_item_mymessage_other, null);


        TextView firsttext = (TextView) convertView.findViewById(R.id.otheritem__firsttext);
        TextView secondtext = (TextView) convertView.findViewById(R.id.otheritem_secondtext);
        TextView datetext = (TextView) convertView.findViewById(R.id.otherdateText);
        ImageView titleimageview = (ImageView) convertView.findViewById(R.id.otheritem_imageview);

        if(otherMsg.getThumb() != null && !otherMsg.getThumb().isEmpty()){
            Picasso.with(convertView.getContext()).load(otherMsg.getThumb()).placeholder(R.drawable.icon1_default).into(titleimageview);
        } else {
            Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(titleimageview);
        }

        firsttext.setText(otherMsg.getTitle());
        secondtext.setText(otherMsg.getContent());
        datetext.setText(otherMsg.getMsgDate());
        convertView.setOnTouchListener((view, event) -> {
            //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
            if (view.getId() == R.id.other_listviewlayout) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });

        return convertView;
    }

    public void delete(int p){//设置删除方法
        otherMsgList.remove(p);
        notifyDataSetChanged();
    }
}
