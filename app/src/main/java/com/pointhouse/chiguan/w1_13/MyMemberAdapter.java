package com.pointhouse.chiguan.w1_13;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pointhouse.chiguan.R;
import java.util.HashMap;
import java.util.List;

/**
 * Created by P on 2017/7/24.
 */

public class MyMemberAdapter extends BaseAdapter {

    private List<HashMap<String,String>> listmodel;
    private Context context;
    private List<HashMap<String,String>> textlist;
    // 构造函数
    public MyMemberAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        listmodel = list;
    }

    public void initModel(List<HashMap<String,String>> listmodel){
        this.textlist= listmodel;
    }
    @Override
    public int getCount() {
        return this.listmodel.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listmodel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  this.listmodel.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewCache viewCache = null;
        if(convertView==null){
            viewCache = new ViewCache();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.w1_13_item_mymember, null);
            viewCache.titlename = (TextView) convertView.findViewById(R.id.member_nametitle);
            viewCache.member_name = (TextView) convertView.findViewById(R.id.member_name);

            viewCache.titlename.setTag(position+"Title");
            viewCache.member_name.setTag(position+"Member");
            convertView.setTag(viewCache);
        } else {
            // 根据标记找到viewCache，达到缓存的目的（软引用）
            viewCache = (MyMemberAdapter.ViewCache) convertView.getTag();
        }
        viewCache.titlename.setText(this.listmodel.get(position).get("name")+"");
        viewCache.member_name.setText(this.textlist.get(position).get("name")+"");
        return convertView;
    }

    // 辅助缓存类
    class ViewCache {
        TextView titlename,member_name;
    }
}
