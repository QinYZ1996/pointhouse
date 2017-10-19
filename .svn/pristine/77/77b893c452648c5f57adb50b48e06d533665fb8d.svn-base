package com.pointhouse.chiguan.k1_9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RequestBuilder;
import com.pointhouse.chiguan.common.jsonObject.ArticleDetailGetBean;
import com.pointhouse.chiguan.common.jsonObject.CommentGetBean;
import com.pointhouse.chiguan.common.mydialog.MyDialogBuilder;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by UPC on 2017/9/3.
 */

public class CommentItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Comment> datalist;
    private List<Comment> tmp;

    public List<Comment> getDatalist() {
        return datalist;
    }

    public CommentItemAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        datalist = new ArrayList<>();
        tmp = new ArrayList<>();
    }

    public void clearComment() {
        datalist.clear();
    }

    public void hideCommentList() {
        int maxIndex = datalist.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            tmp.add(datalist.remove(i));
        }
        notifyDataSetChanged();
    }

    public void showCommentList() {
        int maxIndex = tmp.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            datalist.add(tmp.remove(i));
        }
        notifyDataSetChanged();
    }

    public void addComment(ArticleDetailGetBean.ResultObjectBean.CommentsBean cb) {
        Comment comment = new Comment();
        comment.setId(cb.getId());
        comment.setAuthorIconUrl(cb.getUser().getAvatar());
        comment.setAuthorName(cb.getUser().getNickname());
        comment.setComment(cb.getContent());
        comment.setDeletable(cb.getUserId() == GlobalApplication.user.getUserid());
//        comment.setDeletable(true); // for test
        datalist.add(comment);
    }

    public void addComment(CommentGetBean.ResultObjectBean.CommentsBean cb) {
        Comment comment = new Comment();
        comment.setId(cb.getId());
        comment.setAuthorIconUrl(cb.getUser().getAvatar());
        comment.setAuthorName(cb.getUser().getNickname());
        comment.setComment(cb.getContent());
        comment.setDeletable(cb.getUserId() == GlobalApplication.user.getUserid());
//        comment.setDeletable(true); // for test
        datalist.add(comment);
    }

    public void addNewComment(Comment value) {
        if (datalist.size() > 0 && datalist.get(0).isEmpty()) {
            datalist.remove(0);
        }
        datalist.add(0, value);
    }

    public void addEmptyView() {
        Comment comment = new Comment();
        comment.setEmpty(true);
        datalist.add(comment);
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment data = datalist.get(position);
        if (data.isEmpty()) {
            if (convertView == null || convertView.getId() != R.id.no_comment) {
                convertView = inflater.inflate(R.layout.k1_9_item_empty, null, false);
            }
        } else {
            CommentItemHolder holder;
            if (convertView == null || convertView.getId() != R.id.comment_item) {
                convertView = inflater.inflate(R.layout.k1_9_item_comment, null, false);
                holder = new CommentItemHolder();
                holder.authorIcon = (CircleImageView) convertView.findViewById(R.id.author_icon);
                holder.authorName = (TextView) convertView.findViewById(R.id.author_name);
                holder.comment = (TextView) convertView.findViewById(R.id.comment);
                holder.delComment = convertView.findViewById(R.id.delete_comment);
                convertView.setTag(holder);
            } else {
                holder = (CommentItemHolder) convertView.getTag();
            }

            String authorIcon = data.getAuthorIconUrl();
            if (authorIcon == null || authorIcon.isEmpty()) {
                Picasso.with(convertView.getContext()).load(R.drawable.icon1_default).into(holder.authorIcon);
            } else {
                Picasso.with(convertView.getContext()).load(authorIcon).placeholder(R.drawable.icon1_default).into(holder.authorIcon);
            }
            holder.authorName.setText(data.getAuthorName());
            holder.comment.setText(data.getComment());
            if (data.isDeletable()) {
                holder.delComment.setVisibility(View.VISIBLE);
                holder.delComment.setOnClickListener(v -> {
                    if (!ShareArticleActivity.isPaid) { // 会员过期
                        ToastUtil.getToast(v.getContext(), "请您购买后删除", "center", 0, 180).show();
                        return;
                    }
                    new MyDialogBuilder(v.getContext(), "确认删除评论？", () -> {
                        new RequestBuilder<>(JSONObject.class).getRequest("commentDel", new String[]{String.valueOf(data.getId())}, v.getContext(), jsonObject -> {
                            datalist.remove(position);
                            if (datalist.size() == 0) {
                                addEmptyView();
                                PullToRefreshListView refreshListView = (PullToRefreshListView) parent.getParent().getParent();
                                refreshListView.switchScrollLoadEnabled(false);
                            }
                            notifyDataSetChanged();
                            ToastUtil.getToast(v.getContext(), "评论已删除", "center", 0, 180).show();
                        }, throwable -> {
                            ToastUtil.getToast(v.getContext(), "删除失败，请检查网络", "center", 0, 180).show();
                        });
                    }).show();
                });
            } else {
                holder.delComment.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private class CommentItemHolder {
        View delComment;
        TextView authorName, comment;
        CircleImageView authorIcon;
    }
}
