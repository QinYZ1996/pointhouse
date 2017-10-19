package com.pointhouse.chiguan.w1_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.netease.nim.demo.common.util.sys.GroupMsgUtil;
import com.netease.nim.demo.session.SessionHelper;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by ljj on 2017/9/4.
 */

public class MyGroupAdapter extends BaseAdapter {

    public class GroupItem {
        public String id;
        public int grouplistid;
        public String grouplistImg;
        public String grouplistName;
        public String grouplistMember;
    }

    private LayoutInflater mInflater;
    private Vector<MyGroupAdapter.GroupItem> mModels = new Vector<MyGroupAdapter.GroupItem>();

    public MyGroupAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void addGroup(String id, int grouplistid, String grouplistImg, String grouplistName, String grouplistMember) {
        MyGroupAdapter.GroupItem model = new MyGroupAdapter.GroupItem();
        model.id = id;
        model.grouplistid = grouplistid;
        model.grouplistImg = grouplistImg;
        model.grouplistName = grouplistName;
        model.grouplistMember = grouplistMember;
        mModels.add(model);
    }

    public void clean() {
        mModels.clear();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= getCount()) {
            return null;
        }
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.w1_2_adapter_grouplist,
                null);
        MyGroupAdapter.GroupItem model = mModels.get(position);
        convertView.setTag(position);
        ImageView groupImg = (ImageView) convertView.findViewById(R.id.groupImg);
        TextView groupName = (TextView) convertView.findViewById(R.id.groupName);
        TextView groupMember = (TextView) convertView.findViewById(R.id.groupMember);
        TextView txtMsgCount=(TextView) convertView.findViewById(R.id.txtMsgCount);
        groupName.setText(model.grouplistName);
        groupMember.setText(model.grouplistMember);

        if(model.grouplistImg != null && !model.grouplistImg.isEmpty()){
            Picasso.with(convertView.getContext()).load(model.grouplistImg).placeholder(R.drawable.icon1_default).into(groupImg);
        } else {
            Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(groupImg);
        }

        Context context = convertView.getContext();
        RxView.clicks(convertView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(v -> {
                    SharedPreferencesUtil.saveSimple(context,"group",model.id, String.valueOf(model.grouplistid));
                    SessionHelper.startTeamSession(context,model.id);
                });

        Integer count = GroupMsgUtil.read(model.id);
        String strCount="";
        if(count>99){
            txtMsgCount.setVisibility(View.VISIBLE);
            strCount="99+";
        }else if(count>0){
            txtMsgCount.setVisibility(View.VISIBLE);
            strCount=String.valueOf(count);
        }else{
            txtMsgCount.setVisibility(View.GONE);
        }
        txtMsgCount.setText(strCount);

        return convertView;
    }
}
