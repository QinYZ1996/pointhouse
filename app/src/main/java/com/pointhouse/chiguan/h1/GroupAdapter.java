package com.pointhouse.chiguan.h1;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.netease.nim.demo.common.util.sys.GroupMsgUtil;
import com.netease.nim.demo.session.SessionHelper;
import com.netease.nim.uikit.common.util.dialog.DialogUtil;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.netease.nim.uikit.common.ui.imageview.HeadImageView.DEFAULT_AVATAR_THUMB_SIZE;

/**
 * 群组适配器
 * Created by Maclaine on 2017/7/5.
 */

class GroupAdapter extends BaseAdapter {
    private final static String TAG = "GroupAdapter";
    private Context mContext;
    private List<GroupItem> mList;

    GroupAdapter(Context context, List<GroupItem> list) {
        mContext = context;
        mList = list;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.h1_activity_group_item, parent, false);
            holder = new ViewHolder();
            holder.imgGroupImg = (ImageView) convertView.findViewById(R.id.groupImg);
            holder.txtGroupName = (TextView) convertView.findViewById(R.id.groupName);
            holder.txtGroupMember = (TextView) convertView.findViewById(R.id.groupMember);
            holder.txtGroupStatus = (TextView) convertView.findViewById(R.id.groupStatus);
            holder.txtMsgCount = (TextView) convertView.findViewById(R.id.txtMsgCount);
            convertView.setTag(holder);//将Holder存储到convertView中
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext.getApplicationContext())
                .load(mList.get(position).getThumb()).asBitmap().centerCrop()
                .placeholder(R.drawable.class_default)
                .error(R.drawable.class_default)
                .override(DEFAULT_AVATAR_THUMB_SIZE, DEFAULT_AVATAR_THUMB_SIZE)
                .into(holder.imgGroupImg);
        RxView.clicks(convertView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(v -> {
                    if( mList.get(position).getJoinStatus()!=null&&mList.get(position).getJoinStatus()==1){
                        SharedPreferencesUtil.saveSimple(mContext,"group",mList.get(position).getTid(),mList.get(position).getId());
                        SharedPreferencesUtil.saveSimple(mContext,"groupThumb",mList.get(position).getTid(),mList.get(position).getThumb());
                        SessionHelper.startTeamSession(mContext, mList.get(position).getTid());
                    }else{
                        holder.txtGroupStatus.callOnClick();
                    }
                })
        ;
        holder.txtGroupName.setText(mList.get(position).getTitle());
        holder.txtGroupMember.setText(mList.get(position).getMemberCount() + "/" + mList.get(position).getMaxCount());
        if (mList.get(position).getMemberCount() == mList.get(position).getMaxCount()) {
            holder.txtGroupMember.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff8c5f));
        } else {
            holder.txtGroupMember.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
        }
        Integer i = mList.get(position).getJoinStatus();
        if (i == null||i==-1) {//未申请
            holder.txtGroupStatus.setText(mContext.getString(R.string.group_apply_add));
            holder.txtGroupStatus.setOnClickListener(view -> applyDialog(position));
        } else if (i.equals(0)) {//待审核
            holder.txtGroupStatus.setText(mContext.getString(R.string.group_apply_verify));
            holder.txtGroupStatus.setOnClickListener(view -> {
                DialogUtil.Builder(mContext)
                        .setMessage(mContext.getString(R.string.group_apply_verify_tip))
                        .setPositiveButton(mContext.getString(R.string.group_apply_ok), (dialog, witch) -> {
                            dialog.dismiss();
                        });
            });
        } else if (i.equals(1)) {//审核通过
            holder.txtGroupStatus.setText("");
            holder.txtGroupStatus.setOnClickListener(null);
        } else if (i.equals(2)) {//审核失败
            holder.txtGroupStatus.setText(mContext.getString(R.string.group_apply_failed));
            holder.txtGroupStatus.setOnClickListener(view -> applyDialog(position));
        } else {
            holder.txtGroupStatus.setText("");
            holder.txtGroupStatus.setOnClickListener(null);
        }
        Integer count = GroupMsgUtil.read(mList.get(position).getTid());
        String strCount="";
        if(count>99){
            holder.txtMsgCount.setVisibility(View.VISIBLE);
            strCount="99+";
        }else if(count>0){
            holder.txtMsgCount.setVisibility(View.VISIBLE);
            strCount=String.valueOf(count);
        }else{
            holder.txtMsgCount.setVisibility(View.GONE);
        }
        holder.txtMsgCount.setText(strCount);
        return convertView;
    }

    static class ViewHolder {
        ImageView imgGroupImg;
        TextView txtGroupName;
        TextView txtGroupMember;
        TextView txtGroupStatus;
        TextView txtMsgCount;
    }

    private void applyDialog(Integer position) {
        DialogUtil.Builder(mContext)
                .setMessage(mContext.getString(R.string.group_apply_add_tip))
                .setPositiveButton(mContext.getString(R.string.group_apply_yes), (dialog, witch) -> {
                    Log.d(TAG, "申请");

                    RetrofitFactory.getInstance().getRequestServicesToken()
                            .get("groupApply/[\"" + mList.get(position).getTid() + "\"]")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(jsonObject -> {
                                if (jsonObject.getInteger("resultCode") != 1) {
                                    RetrofitFactory.ErrorMessage(mContext, jsonObject, mContext.getString(R.string.group_failed));
                                    throw new Exception(jsonObject.getString("exceptions"));
                                }
                                Toast.makeText(mContext, mContext.getString(R.string.group_apply_send), Toast.LENGTH_SHORT).show();
                                mList.get(position).setJoinStatus(0);
                                this.notifyDataSetChanged();
                            },Throwable::printStackTrace)
                    ;

                    /*NIMClient.getService(TeamService.class).applyJoinTeam(groupId, null).setCallback(new RequestCallback<Team>() {
                        @Override
                        public void onSuccess(Team team) {
                            // 申请成功, 等待验证入群
                            Log.d(TAG, "onSuccess");
                            Toast.makeText(mContext, mContext.getString(R.string.group_apply_send), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            // 申请失败
                            Log.d(TAG, "onFailed:" + code);
                            switch (code) {
                                case 808:
                                    Toast.makeText(mContext, mContext.getString(R.string.group_apply_verify_tip), Toast.LENGTH_SHORT).show();
                                    break;
                                case 809:
                                    Toast.makeText(mContext, "已经在群里", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onException(Throwable exception) {
                            // error
                            Log.d(TAG, "onException");
                            Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    dialog.dismiss();
                })
                .setNegativeButton(mContext.getString(R.string.group_apply_close), (dialog, witch) -> {
                    Log.d(TAG, "关闭");
                    dialog.dismiss();
                });
    }
}
