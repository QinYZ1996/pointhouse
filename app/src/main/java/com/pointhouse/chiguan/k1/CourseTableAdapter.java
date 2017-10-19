package com.pointhouse.chiguan.k1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.jsonObject.IndexIinitGetBean;
import com.pointhouse.chiguan.k1_1.CourseListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ljj on 2017/7/5.
 */


    public class CourseTableAdapter  {

        public static void setView(Context context, List<IndexIinitGetBean.ResultObjectBean.CourseCategoryBean> courseTable, TableRow tableRow){
            for (IndexIinitGetBean.ResultObjectBean.CourseCategoryBean course:courseTable) {
                LayoutInflater mInflater = LayoutInflater.from(context);
                View view =  mInflater.inflate(R.layout.k1_adapter_horizontalcourselist,
                        null);
                ImageView imageView = (ImageView) view.findViewById(R.id.courselistImg);
                if(course.getThumb() != null && !course.getThumb().isEmpty()){
                    Picasso.with(context).load(course.getThumb()).placeholder(R.drawable.icon2_default).into(imageView);
                } else {
                    Picasso.with(context).load(R.drawable.icon2_default).into(imageView);
                }

                TextView textView = (TextView) view.findViewById(R.id.courselistName);
                textView.setText(course.getTitle());

                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CourseListActivity.class);
                        intent.putExtra("id", String.valueOf(course.getId()));
                        intent.putExtra("title", course.getTitle());
                        context.startActivity(intent);
                    }
                });

                tableRow.addView(view);
            }
        }
    }
