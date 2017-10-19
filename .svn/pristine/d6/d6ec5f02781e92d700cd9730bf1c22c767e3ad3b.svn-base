package com.pointhouse.chiguan.w1_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.jsonObject.MyOrderListGetBean;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyh on 2017/7/18.
 */

public class MyPurchaseItemAdapter extends BaseAdapter {

    private static final String CURRENCY = "¥";
    private static final String PURCHASE_CONST = "人已购买";
    private LayoutInflater mInflater;
    private List<MyPurchase> itemList;

    public List<MyPurchase> getItemList() {
        return itemList;
    }

    public MyPurchaseItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        itemList = new ArrayList<>();
    }

    /**
     * 追加课程
     *
     * @param cb
     */
    public void addCourse(MyOrderListGetBean.ResultObjectBean.CourseBean cb) {
        MyPurchase item = new MyPurchase();
        item.setDataType(0);
        item.setCategoryId(cb.getCategoryId());
        item.setCategoryTitle(cb.getCategoryTitle());
        item.setId(cb.getId());
        item.setLessonNum(cb.getLessonNum());
        item.setLessonType(cb.getLessonType());
        item.setPrice(cb.getPrice());
        item.setBuyersNum(cb.getBuyersNum());
        item.setThumb(cb.getThumb());
        item.setTitle(cb.getTitle());
        item.setType(cb.getType());
        itemList.add(item);
    }

    /**
     * 追加文章
     *
     * @param ab
     */
    public void addArticle(MyOrderListGetBean.ResultObjectBean.ArticleBean ab) {
        MyPurchase item = new MyPurchase();
        item.setDataType(1);
        item.setCategoryId(ab.getCategoryId());
        item.setCategoryTitle(ab.getCategoryTitle());
        item.setId(ab.getId());
        item.setFavoriteNum(ab.getFavoriteNum());
        item.setContentType(ab.getContentType());
        item.setPrice(ab.getPrice());
        item.setBuyersNum(ab.getBuyersNum());
        item.setReading(ab.getReading());
        item.setThumb(ab.getThumb());
        item.setTitle(ab.getTitle());
        item.setType(ab.getType());
        itemList.add(item);
    }

    /**
     * 追加日期行
     *
     * @param date
     */
    public void addDate(String date) {
        MyPurchase item = new MyPurchase();
        item.setDate(date == null ? "" : date);
        item.setDate(true);
        itemList.add(item);
    }

    /**
     * 已加载购买课程的最早时间(保留)
     *
     * @return
     */
    public String getEarliestDate() {
        for (int i = itemList.size() - 1; i >= 0; i--) {
            if (itemList.get(i).isDate()) {
                return itemList.get(i).getDate();
            }
        }

        return "";
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
        MyPurchase item = itemList.get(position);
        if (item.isDate()) { // 日期行
            if (convertView == null || convertView.getId() != R.id.mypurchase_date_layout) {
                convertView = mInflater.inflate(R.layout.w1_1_item_mypurchase_date, null);
            }

            TextView date = (TextView) convertView.findViewById(R.id.tv_date_time);
            date.setText(item.getDate());
        } else { // 课程行
            // 参照Adapter.getView()注释,除了null判断以外还应该判断是不是我们需要的view，尤其是应用多个模板的时候
            PurchaseItemHolder holder;
            if (convertView == null || convertView.getId() != R.id.mypurchase_course_layout) {
                convertView = mInflater.inflate(R.layout.w1_1_item_mypurchase_course, null);
                holder = new PurchaseItemHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (PurchaseItemHolder) convertView.getTag();
            }

            if (item.getThumb() != null && !item.getThumb().isEmpty()) {
                Picasso.with(convertView.getContext()).load(item.getThumb()).placeholder(R.drawable.icon1_default).into(holder.courseImage);
            } else {
                Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(holder.courseImage);
            }
            holder.title.setText(item.getTitle());
            holder.categoryTitle.setText(item.getCategoryTitle());
            holder.price.setText(CURRENCY + ToolUtil.doubleToString(item.getPrice()));
            if (item.getPrice() == 0) { // 免费课程或文章不显示
                holder.purchaseCount.setVisibility(View.GONE);
            } else {
                holder.purchaseCount.setVisibility(View.VISIBLE);
                holder.purchaseCount.setText(item.getBuyersNum() + PURCHASE_CONST);
            }
            // 如果当前行下面是日期行则不显示下划线
            if ((position == itemList.size() - 1)) {
                holder.line.setVisibility(View.VISIBLE);
            } else if (itemList.get(position + 1).isDate()) {
                holder.line.setVisibility(View.INVISIBLE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }

            setIcon(item, holder);
        }

        return convertView;
    }

    /**
     * 设置小图标(文章、视频、音频)
     *
     * @param item
     * @param holder
     */
    private void setIcon(MyPurchase item, PurchaseItemHolder holder) {
        if (item.getDataType() == 0) { // 课程
            switch (item.getLessonType()) {
                case 0: // 文本
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.GONE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                case 1: // 音频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.GONE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.music);
                    break;
                case 2: // 视频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.GONE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.video);
                    break;
                case 3: // 文本+音频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.VISIBLE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.music);
                    holder.iv2.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                case 4: // 文本+视频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.VISIBLE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.video);
                    holder.iv2.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                case 5: // 音频+视频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.VISIBLE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.music);
                    holder.iv2.setBackgroundResource(R.mipmap.video);
                    break;
                case 6: // 文本+视频+音频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.VISIBLE);
                    holder.iv3.setVisibility(View.VISIBLE);
                    holder.iv1.setBackgroundResource(R.mipmap.video);
                    holder.iv2.setBackgroundResource(R.mipmap.music);
                    holder.iv3.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                default:
                    break;
            }
        } else { // 文章
            switch (item.getContentType()) {
                case 0: // 文本
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.GONE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                case 2: // 视频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.GONE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.video);
                    break;
                case 3: // 文章+音频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.VISIBLE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.music);
                    holder.iv2.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                case 4: // 文章+视频
                    holder.iv1.setVisibility(View.VISIBLE);
                    holder.iv2.setVisibility(View.VISIBLE);
                    holder.iv3.setVisibility(View.GONE);
                    holder.iv1.setBackgroundResource(R.mipmap.video);
                    holder.iv2.setBackgroundResource(R.mipmap.wenzhangicon);
                    break;
                default:
                    break;
            }
        }
    }

    private class PurchaseItemHolder {
        ImageView courseImage;
        TextView title;
        TextView categoryTitle;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        TextView price;
        TextView purchaseCount;
        View line;

        public PurchaseItemHolder(View v) {
            courseImage = (ImageView) v.findViewById(R.id.iv_course);
            title = (TextView) v.findViewById(R.id.tv_course_name);
            categoryTitle = (TextView) v.findViewById(R.id.tv_course_classification);
            iv1 = (ImageView) v.findViewById(R.id.iv1);
            iv2 = (ImageView) v.findViewById(R.id.iv2);
            iv3 = (ImageView) v.findViewById(R.id.iv3);
            price = (TextView) v.findViewById(R.id.tv_course_price);
            purchaseCount = (TextView) v.findViewById(R.id.tv_course_purchase_count);
            line = v.findViewById(R.id.v_line);
        }
    }
}
