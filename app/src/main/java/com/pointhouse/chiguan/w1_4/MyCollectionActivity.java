package com.pointhouse.chiguan.w1_4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.mydialog.MyDialogBuilder;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshMenuView;
import com.pointhouse.chiguan.common.updatecommon.SwipeMenuCreator;
import com.pointhouse.chiguan.common.updatecommon.SwipeMenuItem;
import com.pointhouse.chiguan.common.updatecommon.SwipeMenuListView;
import com.pointhouse.chiguan.common.updatecommon.Utils;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.k1_9.ShareArticleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by P on 2017/8/2.
 */

public class MyCollectionActivity extends Activity {
    MyCollectionAdapter adapter;
    private PullToRefreshMenuView listView;
    private SwipeMenuListView swipeMenuListView;
    private List<HashMap<String, String>> listdata = new ArrayList<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w1_4_activity_mycollection);
        adapter = new MyCollectionAdapter(this);
        initData();
    }

    @Override
    protected void onResume() {
        listdata.clear();
        adapter.clean();
        updateCollectionInfo();
        super.onResume();
    }

    //请求收藏数据处理页面
    public void updateCollectionInfo() {
        swipeMenuListView = listView.getRefreshableView();
        String url = Constant.URL_BASE + "myFavoriteList/";
        RetrofitFactory.getInstance().getRequestServicesToken()
                .myFavoriteList(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getString("resultCode").equals("2")) {
                        JSONArray jsonArray = response.getJSONArray("exceptions");
                        JSONObject js = jsonArray.getJSONObject(0);
                        String message = js.getString("message");
                        ToastUtil.getToast(this, message, "center", 0, 180).show();
                        View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                        swipeMenuListView.setEmptyView(listEmptyView);
                        return;
                    } else if (response.getString("resultCode").equals("0")) {
                        View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                        swipeMenuListView.setEmptyView(listEmptyView);
                        ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                        return;
                    } else if (response.getString("resultCode").equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("resultObject");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                listdata = new ArrayList<>();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String date = jsonObject.getString("date");
                                this.adapter.addDate(date);
                                JSONArray coursesArray = jsonObject.getJSONArray("course");
                                if (coursesArray != null && coursesArray.size() > 0) {
                                    for (int j = 0; j < coursesArray.size(); j++) {
                                        JSONObject courseinfoJson = coursesArray.getJSONObject(j);
                                        HashMap<String, String> hashMap = JSONObject.parseObject(courseinfoJson.toJSONString(), new TypeReference<HashMap<String, String>>() {
                                        });
                                        hashMap.put("resulttype", "courses");
                                        hashMap.put("date", date);
                                        hashMap.put("stype", "1");
                                        listdata.add(hashMap);
                                    }
                                }
                                JSONArray acticleArray = jsonObject.getJSONArray("article");
                                if (acticleArray != null && acticleArray.size() > 0) {
                                    for (int j = 0; j < acticleArray.size(); j++) {
                                        JSONObject acticleJson = acticleArray.getJSONObject(j);
                                        HashMap<String, String> hashMap = JSONObject.parseObject(acticleJson.toJSONString(), new TypeReference<HashMap<String, String>>() {
                                        });
                                        hashMap.put("resulttype", "article");
                                        hashMap.put("date", date);
                                        hashMap.put("stype", "2");
                                        listdata.add(hashMap);
                                    }
                                }
                                this.adapter.addData(listdata);
                            }
                            listView.onRefreshComplete();

                            listView.getRefreshableView().setAdapter(adapter);
                            SwipeMenuListViewinit();
                        } else {
                            View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                            swipeMenuListView.setEmptyView(listEmptyView);
                        }
                    }
                }, throwable -> {
                    View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                    swipeMenuListView.setEmptyView(listEmptyView);
                    ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }


    //删除收藏
    public void removeCollection(String id, String type, int position) {
        String parm = JsonUtil.initGetRequestParm(type + "," + id);
        String url = Constant.URL_BASE + "favoriteDel/" + parm;
        RetrofitFactory.getInstance().getRequestServicesToken()
                .removemyFavorite(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getString("resultCode").equals("2")) {
                        JSONArray jsonArray = response.getJSONArray("exceptions");
                        JSONObject js = jsonArray.getJSONObject(0);
                        String message = js.getString("message");
                        ToastUtil.getToast(this, message, "center", 0, 180).show();
                    } else if (response.getString("resultCode").equals("0")) {
                        ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                    } else if (response.getString("resultCode").equals("1")) {
                        String result = response.getString("resultObject");
                        if (result.equals("success")) {
                            ToastUtil.getToast(this, "删除成功", "center", 0, 180).show();
                            MyCollectionAdapter.MyCollectionItem istem = adapter.itemList.get(position);
                            String date = istem.date;
                            adapter.delete(position);
                            adapter.notifyDataSetChanged();
                            Boolean ishave = false;
                            int count = 0;
                            if (adapter.itemList != null && adapter.itemList.size() > 0) {
                                for (int i = 0; i < adapter.itemList.size(); i++) {
                                    MyCollectionAdapter.MyCollectionItem item = adapter.itemList.get(i);
                                    if ((item.thumb.equals("date")) && item.date.equals(date)) {
                                        count = i;
                                    }
                                    if (item.thumb != null && !item.thumb.equals("") && !item.thumb.equals("date") && item.date.equals(date)) {
                                        ishave = true;
                                    }
                                }
                                if (ishave == false) {
                                    adapter.delete(count);
                                }
                                listView.onRefreshComplete();
                                if (adapter.itemList.size() == 0) {
                                    View listEmptyView = View.inflate(this, R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                                    swipeMenuListView.setEmptyView(listEmptyView);
                                }
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtil.getToast(this, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }


    public void Collection_back(View view) {
        super.onBackPressed();
    }

    public void initData() { // TODO 需要传参=>取得数据
        listView = (PullToRefreshMenuView) findViewById(R.id.mycollection_Listview);
        listView.setPullRefreshEnabled(false);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(false);


        this.listView.getRefreshableView().setOnItemClickListener((parent, view, position, id) -> {
            MyCollectionAdapter.MyCollectionItem item = this.adapter.itemList.get(position);
            if (item.resulttype == null || item.resulttype.isEmpty()) {
                return;
            }
            if (item.resulttype.equals("courses")) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + String.valueOf(item.id)));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(it);
            } else {
                Intent it = new Intent(this, ShareArticleActivity.class);
                it.putExtra("articleId", String.valueOf(item.id));
                view.getContext().startActivity(it);
            }
        });

    }

    public void SwipeMenuListViewinit() {
        SwipeMenuCreator creator = menu -> {
            // 创建Item
            SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
            // 设置item的背景颜色
            openItem.setBackground(new ColorDrawable(Color.RED));
            // 设置item的宽度
            openItem.setWidth(Utils.dip2px(MyCollectionActivity.this, 69));
            // 设置item标题
            openItem.setTitle("删除");
            // 设置item字号
            openItem.setTitleSize(18);
            // 设置item字体颜色
            openItem.setTitleColor(Color.WHITE);
            // 添加到ListView的Item布局当中
            menu.addMenuItem(openItem);
        };


        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener((position, menu, index) -> {
            new MyDialogBuilder(this, "确认删除？", () -> {
                MyCollectionAdapter.MyCollectionItem myCollectionItem = (MyCollectionAdapter.MyCollectionItem) this.adapter.getItem(position);
                String id = myCollectionItem.id + "";
                String type = myCollectionItem.stype + "";
                removeCollection(id, type, position);
            }).show();
//            new AlertDialog.Builder(this).setTitle("确认删除？")
//                    .setPositiveButton("确定", (dialog, which) -> {
//                        MyCollectionAdapter.MyCollectionItem myCollectionItem = (MyCollectionAdapter.MyCollectionItem) this.adapter.getItem(position);
//                        String id = myCollectionItem.id + "";
//                        String type = myCollectionItem.stype + "";
//                        removeCollection(id, type, position);
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
            return false;
        });
        // 操作ListView左滑时的手势操作，这里用于处理上下左右滑动冲突：开始滑动时则禁止下拉刷新和上拉加载手势操作，结束滑动后恢复上下拉操作
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                listView.setPullRefreshEnabled(false);
                listView.setPullLoadEnabled(false);
                listView.setScrollLoadEnabled(false);
            }

            @Override
            public void onSwipeEnd(int position) {
                listView.setPullRefreshEnabled(false);
                listView.setPullLoadEnabled(false);
                listView.setScrollLoadEnabled(false);
            }
        });
    }


    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指移动的时候
            x2 = event.getX();
            y2 = event.getY();
            if (y1 - y2 > 50) {
                MediaServiceHelper.getService(this, MediaService::backGroundHide);//隐藏
                //MediaServiceHelper.getService(this, MediaService::stopMedia);  //关闭
            } else if (y2 - y1 > 50) {
                MediaServiceHelper.getService(this, MediaService::reFleshDisplay);//显示
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
