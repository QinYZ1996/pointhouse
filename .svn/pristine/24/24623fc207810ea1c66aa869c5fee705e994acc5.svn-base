package com.pointhouse.chiguan.s1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.swipelistview.SwipeListLayout;
import com.pointhouse.chiguan.s1_2.WalkmanLessonActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 随身听数据填充
 * Created by Maclaine on 2017/7/26.
 */

public class WalkmanItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<WalkmanItem> mList;
    private Set<SwipeListLayout> sets = new HashSet<>();
    private Integer layoutID;
    private CallBack deleteListener;
    private CallBack clickListener;

    WalkmanItemAdapter(Context context, List<WalkmanItem> list) {
        this.mContext = context;
        this.mList = list;
    }

    public WalkmanItemAdapter(Context context, List<WalkmanItem> list, int layoutID) {
        this.mContext = context;
        this.mList = list;
        this.layoutID = layoutID;
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
        ViewHolder holder = new ViewHolder();
        /*if (convertView == null) {
            if (layoutID == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.s1_item_walkman, parent, false);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
            }
            holder = new ViewHolder();
            holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
            holder.imgMusic = (ImageView) convertView.findViewById(R.id.imgMusic);
            holder.imgVideo = (ImageView) convertView.findViewById(R.id.imgVideo);
            holder.imgContent = (ImageView) convertView.findViewById(R.id.imgContent);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtCount = (TextView) convertView.findViewById(R.id.txtCount);
            holder.txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/
        //图片显示问题无法复用
        if (layoutID == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.s1_item_walkman, parent, false);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        }
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
        holder.imgMusic = (ImageView) convertView.findViewById(R.id.imgMusic);
        holder.imgVideo = (ImageView) convertView.findViewById(R.id.imgVideo);
        holder.imgContent = (ImageView) convertView.findViewById(R.id.imgContent);
        holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
        holder.txtCount = (TextView) convertView.findViewById(R.id.txtCount);
        holder.txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
        holder.txtName.setText(mList.get(position).getName());
        if (holder.txtCount != null) {
            holder.txtCount.setText(mList.get(position).getDescribe());
        }
        if (mList.get(position).getThumb() != null) {
            holder.imgThumb.setImageBitmap(mList.get(position).getThumb());
        }
        setVisible(holder.imgMusic, mList.get(position).isHasAudio());
        setVisible(holder.imgVideo, mList.get(position).isHasVideo());
        setVisible(holder.imgContent, mList.get(position).isHasContent());

        final SwipeListLayout slView = (SwipeListLayout) convertView;
        slView.setOnSwipeStatusListener(new MyOnSlipStatusListener(slView));
        RxView.clicks(convertView.findViewById(R.id.viewCourse))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (clickListener != null) {
                        clickListener.accept(mList, position);
                    }
                });
        RxView.clicks(holder.txtDelete)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (deleteListener != null) {
                        deleteListener.accept(mList, position);
                    }
                })
        ;
        return convertView;
    }

    static class ViewHolder {
        ImageView imgThumb, imgMusic, imgVideo, imgContent;
        TextView txtName, txtCount, txtDelete;
    }

    public void setDeleteListener(CallBack callBack) {
        this.deleteListener = callBack;
    }

    public void setClickListener(CallBack callBack) {
        this.clickListener = callBack;
    }

    public interface CallBack {
        public void accept(List<WalkmanItem> list, int position);
    }

    private void setVisible(View view, boolean flag) {
        if (flag) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
}
