package com.pointhouse.chiguan.Application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;

import java.util.List;

/**
 * 音频课程列表适配器
 * Created by Maclaine on 2017/7/19.
 */

public class CommonMediaAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommonMediaItem> mList;

    public CommonMediaAdapter(Context mContext, List<CommonMediaItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.common_media_item,parent,false);
            holder = new ViewHolder();
            holder.imgPlayStatus=(ImageView) convertView.findViewById(R.id.imgPlayStatus);
            holder.txtMusic= (TextView) convertView.findViewById(R.id.txtMusic);
            holder.txtMusicID= (TextView) convertView.findViewById(R.id.txtMusicID);
            convertView.setTag(holder);//将Holder存储到convertView中
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        CommonMediaItem item=mList.get(position);
        if(item.isPlaying()){
            holder.imgPlayStatus.setVisibility(View.VISIBLE);
        }else{
            holder.imgPlayStatus.setVisibility(View.GONE);
        }
        holder.txtMusicID.setText(String.valueOf(item.getId()));
        holder.txtMusic.setText(item.getName());

        return convertView;
    }

    static class ViewHolder{
        ImageView imgPlayStatus;
        TextView txtMusic,txtMusicID;
    }
}
