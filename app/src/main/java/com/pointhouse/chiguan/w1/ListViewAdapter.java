package com.pointhouse.chiguan.w1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.DensityUtil;

import java.util.HashMap;
import java.util.List;


public class ListViewAdapter extends BaseAdapter {

	// 上下文
	private Context context;
	// List表数据
	private List<HashMap<String, String>> mList;

	// 构造函数
	public ListViewAdapter(Context context, List<HashMap<String, String>> list) {
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
		ViewCache viewCache;
		//类似cell

		viewCache = new ViewCache();
		// LayoutInflater布局填充器实例化。
		convertView = LayoutInflater.from(context).inflate(
				R.layout.w1_item_personcal, null);
		// 根据布局并通过控件各自的ID找控件。
		viewCache.name = (TextView) convertView.findViewById(R.id.resultname);
		viewCache.imageView =(ImageView) convertView.findViewById(R.id.TitleImage);
		viewCache.imageView.setBackground(null);
		viewCache.imageView.setImageDrawable(null);

		viewCache.divew = convertView.findViewById(R.id.dview);

		// 为每项的TextView加个Tag
		viewCache.name.setTag(position);
		// 为convertView添加标记
		convertView.setTag(viewCache);

		// 给TextView设置内容
		viewCache.name.setText(mList.get(position).get("name")+"");
		if(position==2){
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(viewCache.divew.getLayoutParams());
			lp.setMargins(0,0,0,0);
			viewCache.divew.setLayoutParams(lp);
			viewCache.divew.getLayoutParams().height = DensityUtil.dip2px(convertView.getContext(),8);
		}
		switch (position){
			case 0:
				viewCache.imageView.setImageResource(R.mipmap.learn);
				break;
			case 1:
				viewCache.imageView.setImageResource(R.mipmap.shopping);
				break;
            case 2:
                viewCache.imageView.setImageResource(R.mipmap.team);
                break;
			case 3:
				viewCache.imageView.setImageResource(R.mipmap.collection);
				break;
			case 4:
				viewCache.imageView.setImageResource(R.mipmap.message);
				break;
			default:
		}
		return convertView;
	}

	// 辅助缓存类
	class ViewCache {
		View divew;
		TextView name;
		ImageView imageView;
	}

}
