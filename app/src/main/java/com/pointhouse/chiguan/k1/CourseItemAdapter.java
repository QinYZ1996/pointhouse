package com.pointhouse.chiguan.k1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.squareup.picasso.Picasso;

import java.util.Vector;

/**
 * Created by ljj on 2017/7/5.
 */


    public class CourseItemAdapter extends BaseAdapter {

        public class CourseItem {
            public int id;
            public String courselistImg;
            public String courselistName;
            public int courselistDraw;
            public String courselistClassification;
            public double courselistCost;
            public int courselistBuyNumber;
            public int courselistVipType;
        }

        private LayoutInflater mInflater;
        private Vector<CourseItem> mModels = new Vector<CourseItem>();
        private ListView mListView;

        public CourseItemAdapter(Context context, ListView listView) {
            mInflater = LayoutInflater.from(context);
            mListView = listView;
        }

        public void addCourse(int id,String courselistImg, String courselistName, int courselistDraw, String courselistClassification, double courselistCost,
                              int courselistBuyNumber,int courselistVipType) {
            CourseItem model = new CourseItem();
            model.id = id;
            model.courselistImg = courselistImg;
            model.courselistName = courselistName;
            model.courselistDraw = courselistDraw;
            model.courselistClassification = courselistClassification;
            model.courselistCost = courselistCost;
            model.courselistBuyNumber = courselistBuyNumber;
            model.courselistVipType = courselistVipType;
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
            convertView = mInflater.inflate(R.layout.k1_adapter_courselist,
                        null);

            CourseItem model = mModels.get(position);
            convertView.setTag(position);

            ImageView courselistImg = (ImageView) convertView.findViewById(R.id.courselistImg);
            ImageView courselistDraw1 = (ImageView) convertView.findViewById(R.id.courselistDraw1);
            ImageView courselistDraw2 = (ImageView) convertView.findViewById(R.id.courselistDraw2);
            ImageView courselistDraw3 = (ImageView) convertView.findViewById(R.id.courselistDraw3);
            TextView courselistName = (TextView) convertView.findViewById(R.id.courselistName);
            TextView courselistClassification = (TextView) convertView.findViewById(R.id.courselistClassification);
            TextView courselistCost = (TextView) convertView.findViewById(R.id.courselistCost);
            TextView courselistBuyNumber = (TextView) convertView.findViewById(R.id.courselistBuyNumber);
            View viewLine = (View) convertView.findViewById(R.id.viewLine);

            courselistName.setText(model.courselistName);
            courselistClassification.setText(model.courselistClassification);
            //价格
            if(model.courselistVipType == 0){
                courselistCost.setText("免费");
            } else if(model.courselistVipType == 1)
            {
                courselistCost.setText("¥" + ToolUtil.doubleToString(model.courselistCost));
                courselistBuyNumber.setText(model.courselistBuyNumber + "人已购买");
            }
            else if(model.courselistVipType == 2){
                courselistCost.setText("¥" + ToolUtil.doubleToString(model.courselistCost) + " (会员免费)");
                courselistBuyNumber.setText(model.courselistBuyNumber + "人已购买");
            }


            if(model.courselistImg != null && !model.courselistImg.isEmpty()){
                Picasso.with(convertView.getContext()).load(model.courselistImg).placeholder(R.drawable.icon1_default).into(courselistImg);
            } else {
                Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(courselistImg);
            }


            if(model.courselistDraw == 0){
                courselistDraw1.setBackgroundResource(R.mipmap.wenzhangicon);
                courselistDraw2.setVisibility(View.GONE);
                courselistDraw3.setVisibility(View.GONE);
            } else if (model.courselistDraw == 1)
            {
                courselistDraw1.setBackgroundResource(R.mipmap.music);
                courselistDraw2.setVisibility(View.GONE);
                courselistDraw3.setVisibility(View.GONE);
            }else if(model.courselistDraw == 2){
                courselistDraw1.setBackgroundResource(R.mipmap.video);
                courselistDraw2.setVisibility(View.GONE);
                courselistDraw3.setVisibility(View.GONE);
            }else if (model.courselistDraw == 3){
                courselistDraw1.setBackgroundResource(R.mipmap.music);
                courselistDraw2.setVisibility(View.VISIBLE);
                courselistDraw2.setBackgroundResource(R.mipmap.wenzhangicon);
                courselistDraw3.setVisibility(View.GONE);
            }else if (model.courselistDraw == 4){
                courselistDraw1.setBackgroundResource(R.mipmap.video);
                courselistDraw2.setVisibility(View.VISIBLE);
                courselistDraw2.setBackgroundResource(R.mipmap.wenzhangicon);
                courselistDraw3.setVisibility(View.GONE);
            }else if (model.courselistDraw == 5){
                courselistDraw1.setBackgroundResource(R.mipmap.video);
                courselistDraw2.setVisibility(View.VISIBLE);
                courselistDraw2.setBackgroundResource(R.mipmap.music);
                courselistDraw3.setVisibility(View.GONE);
            }else if (model.courselistDraw == 6){
                courselistDraw1.setBackgroundResource(R.mipmap.video);
                courselistDraw2.setVisibility(View.VISIBLE);
                courselistDraw2.setBackgroundResource(R.mipmap.music);
                courselistDraw3.setVisibility(View.VISIBLE);
                courselistDraw3.setBackgroundResource(R.mipmap.wenzhangicon);
            }

            return convertView;
        }
    }
