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
import com.pointhouse.chiguan.db.SystemMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by P on 2017/8/8.
 */

public class MyMessage_SystemAdapter extends BaseAdapter{

    private Context context;
    private List<SystemMessage> systemMessageList;
    public MyMessage_SystemAdapter(Context activity,List<SystemMessage> systemMessages){
        this.systemMessageList = systemMessages;
        context = activity;
    }
    public void delete(int p){//设置删除方法
        systemMessageList.remove(p);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.systemMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.systemMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SystemMessage systemMessage = systemMessageList.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.w1_10_item_mymessage_system, null);
        TextView firsttext = (TextView) convertView.findViewById(R.id.mymessage_firsttext);
        TextView secondtext = (TextView) convertView.findViewById(R.id.mymessage_secondtext);
        TextView datetext = (TextView) convertView.findViewById(R.id.sysdateText);
        ImageView titleimageview = (ImageView) convertView.findViewById(R.id.systemitem_imageview);

        if(systemMessage.getThumb() != null && !systemMessage.getThumb().isEmpty()){
            Picasso.with(convertView.getContext()).load(systemMessage.getThumb()).placeholder(R.drawable.icon1_default).into(titleimageview);
        } else {
            Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(titleimageview);
        }
        firsttext.setText(systemMessage.getTitle());
        secondtext.setText(systemMessage.getContent());
        datetext.setText(systemMessage.getMsgDate());


        convertView.setOnTouchListener((view, event) -> {
            //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
            if (view.getId() == R.id.listviewlayout) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });

        return convertView;
    }
}
