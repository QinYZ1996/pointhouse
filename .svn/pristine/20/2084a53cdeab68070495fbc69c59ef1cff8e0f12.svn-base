package com.pointhouse.chiguan.w1_4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.pointhouse.chiguan.k1_2.CourseDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by P on 2017/8/2.
 */

public class MyCollectionAdapter extends BaseAdapter {

     class MyCollectionItem {
         public String date;
         public int categoryId;
         public String thumb;
         public int id;
         public String categoryTitle;
         public String title;
         public int lessonNum;
         public int studentNum;
         public String price; // 类型暂定
         public int type; // 类型暂定
         public int lessonType;
         public String resulttype;
         public String stype;
         public int favoriteNum;
         public int contentType;
         public int buyersNum;
         public int reading;
     }

    private LayoutInflater mInflater;
    List<MyCollectionAdapter.MyCollectionItem> itemList;

    public MyCollectionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        itemList = new ArrayList<>();
    }

    public void addData(List<HashMap<String,String>> list){
        for(HashMap hashMap : list){
            String date="";
            String stype="";
            String resulttype="";
            String id="";
            String categoryId="";
            String thumb="";
            String title="";
            String categoryTitle="";
            String price="";
            String studentNum="";
            String type="";
            String lessonType="";
            String favoriteNum="";
            String contentType="";
            String reading="";
            String buyersNum="";
            if( hashMap.get("resulttype")!=null){
                resulttype = hashMap.get("resulttype").toString();
            }
            if( hashMap.get("id")!=null){
                id = hashMap.get("id").toString();
            }
            if( hashMap.get("categoryId")!=null){
                categoryId = hashMap.get("categoryId").toString();
            }
            if( hashMap.get("thumb")!=null){
                thumb = hashMap.get("thumb").toString();
            }
            if( hashMap.get("title")!=null){
                title = hashMap.get("title").toString();
            }
            if( hashMap.get("categoryTitle")!=null){
                categoryTitle = hashMap.get("categoryTitle").toString();
            }
            if( hashMap.get("price")!=null){
                price = hashMap.get("price").toString();
            }
            if( hashMap.get("type")!=null){
                type = hashMap.get("type").toString();
            }
            if( hashMap.get("stype")!=null){
                stype = hashMap.get("stype").toString();
            }
            if(hashMap.get("date")!=null){
                date = hashMap.get("date").toString();
            }
            if(hashMap.get("buyersNum")!=null){
                buyersNum = hashMap.get("buyersNum").toString();
            }

            MyCollectionAdapter.MyCollectionItem item = new MyCollectionAdapter.MyCollectionItem();
            if(resulttype.equals("courses")){
                if( hashMap.get("buyersNum").toString()!=null){
                    buyersNum = hashMap.get("buyersNum").toString();
                }

                if( hashMap.get("lessonType").toString()!=null){
                    lessonType = hashMap.get("lessonType").toString();
                }
                item.id = Integer.parseInt(id);
                item.resulttype = resulttype;
                item.categoryId = Integer.parseInt(categoryId);
                item.thumb = thumb;
                item.title = title;
                item.categoryTitle = categoryTitle;
                item.price = price;
                //item.studentNum = Integer.parseInt(studentNum);
                item.buyersNum = Integer.parseInt(buyersNum);
                item.type = Integer.parseInt(type);
                item.lessonType=Integer.parseInt(lessonType);
                item.date=date;
                item.stype = stype;
                itemList.add(item);
            }else if(resulttype.equals("article")){
                if( hashMap.get("favoriteNum").toString()!=null){
                    favoriteNum = hashMap.get("favoriteNum").toString();
                }
                if( hashMap.get("contentType").toString()!=null){
                    contentType = hashMap.get("contentType").toString();
                }
                if( hashMap.get("reading").toString()!=null){
                    reading = hashMap.get("reading").toString();
                }
                if( hashMap.get("buyersNum").toString()!=null){
                    buyersNum = hashMap.get("buyersNum").toString();
                }
                item.buyersNum = Integer.parseInt(buyersNum);
                item.resulttype = resulttype;
                item.id = Integer.parseInt(id);
                item.categoryId = Integer.parseInt(categoryId);
                item.thumb = thumb;
                item.title = title;
                item.date=date;
                item.stype = stype;
                item.categoryTitle = categoryTitle;
                item.price = price;
                item.buyersNum = Integer.parseInt(buyersNum);
                item.type = Integer.parseInt(type);
                item.contentType=Integer.parseInt(contentType);
                item.favoriteNum = Integer.parseInt(favoriteNum);
                item.reading = Integer.parseInt(reading);
                itemList.add(item);
            }

        }
    }

    public void clean() {
        itemList.clear();
    }

    // 追加日期行
    public void addDate(String date) {
        MyCollectionAdapter.MyCollectionItem item = new MyCollectionAdapter.MyCollectionItem();
        item.date = date;
        item.thumb = "date";
        itemList.add(item);
    }

    // 已加载购买课程的最早时间
    public String getEarliestDate() {
        for (int i = itemList.size() - 1; i >= 0; i--) {
            if (itemList.get(i).date != null) {
                return itemList.get(i).date;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyCollectionAdapter.MyCollectionItem item = itemList.get(position);

        if (item.date!= null && item.thumb!=null &&!item.thumb.equals("date")) { // 课程行
            // 参照Adapter.getView()注释,除了null判断以外还应该判断是不是我们需要的view，尤其是应用多个模板的时候
            MyCollectionAdapter.CollectionItemHolder holder;
            if (convertView == null || convertView.getId() != R.id.mycollection_course_layout) {
                convertView = mInflater.inflate(R.layout.w1_4_item_mycollection_course, null);
                holder = new MyCollectionAdapter.CollectionItemHolder(convertView);
                holder.courseImage = (ImageView) convertView.findViewById(R.id.mycollection_iv_course);
                holder.videoImage = (ImageView) convertView.findViewById(R.id.mycollection_iv_video);
                holder.musicImage = (ImageView) convertView.findViewById(R.id.mycollection_iv_music);
                holder.courselistDraw3 = (ImageView) convertView.findViewById(R.id.mycollection_iv_3);
                holder.courseName = (TextView) convertView.findViewById(R.id.mycollection_tv_course_name);
                holder.courseClassification = (TextView) convertView.findViewById(R.id.mycollection_tv_course_classification);
                holder.coursePrice = (TextView) convertView.findViewById(R.id.mycollection_tv_course_price);
                holder.purchaseCount = (TextView) convertView.findViewById(R.id.mycollection_tv_course_purchase_count);
                convertView.setTag(holder);
            } else {
                holder = (MyCollectionAdapter.CollectionItemHolder) convertView.getTag();
            }


            holder.courseName.setText(item.title);
            holder.courseClassification.setText(item.categoryTitle);
            if(item.thumb != null && !item.thumb.isEmpty()){
                Picasso.with(convertView.getContext()).load(item.thumb).placeholder(R.drawable.icon1_default).into(holder.courseImage);
            } else {
                Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(holder.courseImage);
            }
            if(item.resulttype.equals("courses")){
                if(item.type == 0){
                    holder.coursePrice.setText("免费");
                    holder.purchaseCount.setVisibility(View.INVISIBLE);
                } else if(item.type == 1)
                {
                    holder.coursePrice.setText("¥" + ToolUtil.doubleToString(Double.valueOf(item.price)));
                }
                else if(item.type == 2){
                    holder.coursePrice.setText("¥" + ToolUtil.doubleToString(Double.valueOf(item.price)) + " (会员免费)");
                }

                if(item.lessonType == 0){
                    holder.videoImage.setBackgroundResource(R.mipmap.wenzhangicon);
                    holder.musicImage.setVisibility(View.GONE);
                    holder.courselistDraw3.setVisibility(View.GONE);
                } else if (item.lessonType == 1)
                {
                    holder.videoImage.setBackgroundResource(R.mipmap.music);
                    holder.musicImage.setVisibility(View.GONE);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if(item.lessonType == 2){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.GONE);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.lessonType == 3){
                    holder.videoImage.setBackgroundResource(R.mipmap.music);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.wenzhangicon);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.lessonType == 4){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.wenzhangicon);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.lessonType == 5){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.music);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.lessonType == 6){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.music);
                    holder.courselistDraw3.setVisibility(View.VISIBLE);
                    holder.courselistDraw3.setBackgroundResource(R.mipmap.wenzhangicon);
                }
                holder.purchaseCount.setText(item.buyersNum + "人已购买");
//                holder.courseName.setOnClickListener(v -> {
//                    Intent intent;
//                    Context context = v.getContext();
//                    intent = new Intent(context, CourseDetailsActivity.class);
//                    intent.putExtra("id", String.valueOf(item.id));
//                    context.startActivity(intent);
//                });
                if(position<itemList.size()-1){
                    MyCollectionAdapter.MyCollectionItem iitem = itemList.get(position+1);
                    if(iitem.thumb.equals("date")){
                        View view = convertView.findViewById(R.id.mycollection_viewLine);
                        view.setVisibility(View.INVISIBLE);
                    }
                }
            }else if(item.resulttype.equals("article")){
                if(item.type == 0){
                    holder.coursePrice.setText("免费");
                    holder.purchaseCount.setVisibility(View.INVISIBLE);
                } else if(item.type == 1)
                {
                    holder.coursePrice.setText("¥" + ToolUtil.doubleToString(Double.valueOf(item.price)));
                }
                else if(item.type == 2){
                    holder.coursePrice.setText("¥" + ToolUtil.doubleToString(Double.valueOf(item.price)) + " (会员免费)");
                }

                if(item.contentType == 0){
                    holder.videoImage.setBackgroundResource(R.mipmap.wenzhangicon);
                    holder.musicImage.setVisibility(View.GONE);
                    holder.courselistDraw3.setVisibility(View.GONE);
                } else if (item.contentType == 1)
                {
                    holder.videoImage.setBackgroundResource(R.mipmap.music);
                    holder.musicImage.setVisibility(View.GONE);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if(item.contentType == 2){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.GONE);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.contentType == 3){
                    holder.videoImage.setBackgroundResource(R.mipmap.music);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.wenzhangicon);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.contentType == 4){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.wenzhangicon);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.contentType == 5){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.music);
                    holder.courselistDraw3.setVisibility(View.GONE);
                }else if (item.contentType == 6){
                    holder.videoImage.setBackgroundResource(R.mipmap.video);
                    holder.musicImage.setVisibility(View.VISIBLE);
                    holder.musicImage.setBackgroundResource(R.mipmap.music);
                    holder.courselistDraw3.setVisibility(View.VISIBLE);
                    holder.courselistDraw3.setBackgroundResource(R.mipmap.wenzhangicon);
                }
                holder.purchaseCount.setText(item.buyersNum + "人已购买");
                //holder.courseName.setOnClickListener(v -> {
//                    Intent intent;
//                    Context context = v.getContext();
//                    intent = new Intent(context, EditNicknameActivity.class);
//                    intent.putExtra("nickname", "nickname");
//                    context.startActivity(intent);
               // });
                if(position<itemList.size()-1){
                    MyCollectionAdapter.MyCollectionItem iitem = itemList.get(position+1);
                    if(iitem.thumb.equals("date")){
                        View view = convertView.findViewById(R.id.mycollection_viewLine);
                        view.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } else { // 日期行
            if (convertView == null || convertView.getId() != R.id.mycollection_date_layout) {
                convertView = mInflater.inflate(R.layout.w1_4_item_mycollection_date, null);
                convertView.setEnabled(false);
            }
            TextView date = (TextView) convertView.findViewById(R.id.mycollection_date_time);
            date.setText(item.date);
        }
        return convertView;
    }

    public boolean getSwipEnableByPosition(int position){
        MyCollectionItem item = this.itemList.get(position);
        return item.categoryTitle != null;
    }

    public void delete(int p){//设置删除方法
        itemList.remove(p);
        notifyDataSetChanged();
    }

    private class CollectionItemHolder {
        ImageView courseImage;
        TextView courseName;
        TextView courseClassification;
        ImageView videoImage;
        ImageView musicImage;
        ImageView courselistDraw3;
        TextView coursePrice;
        TextView purchaseCount;

        public CollectionItemHolder(View v) {
            courseImage = (ImageView) v.findViewById(R.id.mycollection_iv_course);
            courseName = (TextView) v.findViewById(R.id.mycollection_tv_course_name);
            courseClassification = (TextView) v.findViewById(R.id.mycollection_tv_course_classification);
            videoImage = (ImageView) v.findViewById(R.id.mycollection_iv_video);
            musicImage = (ImageView) v.findViewById(R.id.mycollection_iv_music);
            courselistDraw3 = (ImageView) v.findViewById(R.id.mycollection_iv_3);
            coursePrice = (TextView) v.findViewById(R.id.mycollection_tv_course_price);
            purchaseCount = (TextView) v.findViewById(R.id.mycollection_tv_course_purchase_count);
        }
    }
}
