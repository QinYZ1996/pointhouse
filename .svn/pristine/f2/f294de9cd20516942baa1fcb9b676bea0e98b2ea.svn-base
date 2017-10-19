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
import com.squareup.picasso.Picasso;

import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ljj on 2017/7/5.
 */


    public class TeacherItemAdapter extends BaseAdapter {

    public class TeacherItem {
            public String teacherlistImg;
            public int teacherlistIcon1;
            public String teacherlistName;
            public String teacherlistCountry;
            public int id;
        }

        private LayoutInflater mInflater;
        private Vector<TeacherItem> mModels = new Vector<TeacherItem>();
        //private ListView mListView;

        public TeacherItemAdapter(Context context, ListView listView) {
            mInflater = LayoutInflater.from(context);
            //mListView = listView;
        }

        public void addTeacher(String teacherlistImg, int teacherlistIcon1, String teacherlistName, String teacherlistCountry,int id) {
            TeacherItem model = new TeacherItem();
            model.id = id;
            model.teacherlistImg = teacherlistImg;
            model.teacherlistIcon1 = teacherlistIcon1;
            model.teacherlistName = teacherlistName;
            model.teacherlistCountry = teacherlistCountry;
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
            convertView = mInflater.inflate(R.layout.k1_adapter_teacherlist,
                        null);
            TeacherItem model = mModels.get(position);
            convertView.setTag(position);
            CircleImageView teacherlistImg = (CircleImageView) convertView.findViewById(R.id.teacherlistImg);
            ImageView teacherlistIcon1 = (ImageView) convertView.findViewById(R.id.teacherlistIcon1);
            TextView teacherlistName = (TextView) convertView.findViewById(R.id.teacherlistName);
            TextView teacherlistCountry = (TextView) convertView.findViewById(R.id.teacherlistCountry);

            View viewLine = (View) convertView.findViewById(R.id.viewLine);

            teacherlistName.setText(model.teacherlistName);
            teacherlistCountry.setText(model.teacherlistCountry);

            if(model.teacherlistImg != null && !model.teacherlistImg.isEmpty()){
                Picasso.with(convertView.getContext()).load(model.teacherlistImg).placeholder(R.drawable.icon2_default).into(teacherlistImg);
            } else {
                Picasso.with(convertView.getContext()).load(R.drawable.icon2_default).into(teacherlistImg);
            }

            if(model.teacherlistIcon1 == 1){
                Picasso.with(convertView.getContext()).load(R.mipmap.sexman).into(teacherlistIcon1);
            }else{
                Picasso.with(convertView.getContext()).load(R.mipmap.sexwoman).into(teacherlistIcon1);
            }


            //if(mModels.size() == position)
            //viewLine.setVisibility(View.GONE);

            return convertView;
        }
    }
