package com.pointhouse.chiguan.w1_7;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MydataListViewAdapter extends BaseAdapter {

	// 上下文
	private Context context;

	private List<HashMap<String, String>> tagnamelist = new ArrayList<>();

	// List表数据
	private List<Map<String, Object>> mList;

	// 构造函数
	public MydataListViewAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		mList = list;
	}

	// List表单的总数
	public int getCount() {
		return mList.size();
	}

	// 位于position处的List表单的一项
	public Object getItem(int position) {
		return mList.get(position);
	}

	// List表单的各项的索引
	public long getItemId(int position) {
		return position;
	}

	// 最重要的获得视图内容方法
	public View getView(int position, View convertView, ViewGroup parent) {
		Mydata_ViewCache viewCache = null;
		//类似cell
		if (convertView == null) {
			viewCache = new Mydata_ViewCache();
			// LayoutInflater布局填充器实例化。
			convertView = LayoutInflater.from(context).inflate(
					R.layout.w1_item_mydata, null);
			// 根据布局并通过控件各自的ID找控件。
			viewCache.mydate_text = (TextView) convertView.findViewById(R.id.mydate_text);
			viewCache.cellname = (TextView) convertView.findViewById(R.id.mydatecellname);
			viewCache.imageView =(ImageView) convertView.findViewById(R.id.mydate_image);
			viewCache.divew = convertView.findViewById(R.id.mydate_div);
			if(position==1){
				viewCache.divew.getLayoutParams().height =24;
			}
			// 为convertView添加标记
			convertView.setTag(viewCache);
		} else {
			// 根据标记找到viewCache，达到缓存的目的（软引用）
			viewCache = (Mydata_ViewCache) convertView.getTag();
		}
		viewCache.cellname.setText((String) mList.get(position).get("name"));
		if(SharedPreferencesUtil.readToken(context)!=null&&!SharedPreferencesUtil.readToken(context).equals("")){
			if(viewCache.cellname.getText().toString().equals("昵称")){
				if(GlobalApplication.user.getNickname()==null||GlobalApplication.user.getNickname().equals("")){
					viewCache.mydate_text.setText(GlobalApplication.user.getMobile());
				}else{
					viewCache.mydate_text.setText(GlobalApplication.user.getNickname());
				}
			}else if(viewCache.cellname.getText().toString().equals("绑定微信")){
				if(GlobalApplication.user.getOpenid()!=null&&!GlobalApplication.user.getOpenid().equals("")){
					viewCache.mydate_text.setText("已绑定");
				}else{
					viewCache.mydate_text.setText("未绑定");
				}
			}else if(viewCache.cellname.getText().toString().equals("绑定手机号")){
				if(GlobalApplication.user.getMobile()!=null&&!GlobalApplication.user.getMobile().equals("")){
					viewCache.mydate_text.setText(GlobalApplication.user.getMobile());
				}else{
					viewCache.mydate_text.setText("未绑定");
				}
			}else{
				viewCache.mydate_text.setText("");
			}
		}else{
			if(viewCache.cellname.getText().toString().equals("昵称")){
				viewCache.mydate_text.setText("");
			}else if(viewCache.cellname.getText().toString().equals("绑定微信")){
				viewCache.mydate_text.setText("未绑定");
			}else if(viewCache.cellname.getText().toString().equals("绑定手机号")){
				viewCache.mydate_text.setText("未绑定");
			}else{
				viewCache.mydate_text.setText("");
			}
		}

		return convertView;
	}

	// 辅助缓存类
	class Mydata_ViewCache {
		View divew;
		TextView cellname,mydate_text;
		ImageView imageView;

	}

}
