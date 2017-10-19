package com.pointhouse.chiguan.h1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshBase;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class GroupMainActivity extends Fragment {
    private final static String TAG = "GroupMainActivity";
    private View groupView = null;
    Disposable disposable = null;
    List<GroupItem> list;
    GroupAdapter mAdapter;
    Disposable timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (groupView != null) {
            return groupView;
        }
        groupView = inflater.inflate(R.layout.h1_activity_groupmain, container, false);

        View viewEmpty = groupView.findViewById(R.id.viewEmpty);
        TextView txtTip = (TextView) groupView.findViewById(R.id.txtTip);
        if (!RetrofitFactory.hasToken()) {
            txtTip.setText(this.getString(R.string.group_login));
        }
        PullToRefreshListView ptrListView = (PullToRefreshListView) groupView.findViewById(R.id.groupList);
        ptrListView.setPullLoadEnabled(false);
        ptrListView.setScrollLoadEnabled(false);
        ptrListView.setPullRefreshEnabled(true);//禁用下拉刷新
        ListView listView = ptrListView.getRefreshableView(); //(ListView) groupView.findViewById(R.id.groupList);
        list = new ArrayList<>();
        mAdapter = new GroupAdapter(groupView.getContext(), list);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(viewEmpty);
        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                disposable = RetrofitFactory.getInstance().getRequestServicesToken()
                        .get("groupList/")
                        .retry(3)
                        .filter(jsonObject -> {
                            Thread.sleep(1000);
                            return true;
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(jsonObject -> {
                            if (!(jsonObject.getInteger("resultCode") == 1)) {
                                RetrofitFactory.ErrorMessage(groupView.getContext(), jsonObject, groupView.getContext().getString(R.string.group_error));
                                throw new Exception(jsonObject.getString("exceptions"));
                            }
                            list.clear();
                            for (Object obj : jsonObject.getJSONObject("resultObject").getJSONArray("groups")) {
                                JSONObject jObj = (JSONObject) obj;
                                list.add(convertJson(jObj));
                            }
                            mAdapter.notifyDataSetChanged();
                            if (list.size() > 0) {
                                ptrListView.setVisibility(View.VISIBLE);
                                viewEmpty.setVisibility(View.GONE);
                            } else {
                                ptrListView.setVisibility(View.GONE);
                                viewEmpty.setVisibility(View.VISIBLE);
                            }
                            ptrListView.onRefreshComplete();
                        }, throwable -> {
                            throwable.printStackTrace();
                            ptrListView.onRefreshComplete();
                        });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                GroupItem item = new GroupItem("114881924", "groupName");
//                list.add(item);
//                ptrListView.onRefreshComplete();
            }
        });

        if (RetrofitFactory.hasToken()) {
            disposable = RetrofitFactory.getInstance().getRequestServicesToken()
                    .get("groupList/")
                    .retry(3)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        if (!(jsonObject.getInteger("resultCode") == 1)) {
                            RetrofitFactory.ErrorMessage(groupView.getContext(), jsonObject, this.getString(R.string.group_error));
                            throw new Exception(jsonObject.getString("exceptions"));
                        }
                        for (Object obj : jsonObject.getJSONObject("resultObject").getJSONArray("groups")) {
                            JSONObject jObj = (JSONObject) obj;
//                        GroupItem groupItem = JSONObject.toJavaObject(jObj, GroupItem.class);
                            list.add(convertJson(jObj));
                        }
                        mAdapter.notifyDataSetChanged();
                        if (list.size() > 0) {
                            ptrListView.setVisibility(View.VISIBLE);
                            viewEmpty.setVisibility(View.GONE);
                        } else {
                            ptrListView.setVisibility(View.GONE);
                            viewEmpty.setVisibility(View.VISIBLE);
                        }
                    }, throwable -> ptrListView.setVisibility(View.GONE));
        } else {
            ptrListView.setVisibility(View.GONE);
        }

        timer = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mAdapter.notifyDataSetChanged(), Throwable::printStackTrace);
        return groupView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (timer != null && !timer.isDisposed()) {
            timer.dispose();
        }
    }

    //针对小米不明原因无法转换进行适配
    private GroupItem convertJson(JSONObject jObj) {
        GroupItem groupItem = new GroupItem();
        groupItem.setId(jObj.get("id") == null ? null : jObj.getString("id"));
        groupItem.setTid(jObj.get("tid") == null ? null : jObj.getString("tid"));
        groupItem.setJoinStatus(jObj.get("joinStatus") == null ? null : jObj.getInteger("joinStatus"));
        groupItem.setMaxCount(jObj.get("maxCount") == null ? 1 : jObj.getInteger("maxCount"));
        groupItem.setMemberCount(jObj.get("memberCount") == null ? 200 : jObj.getInteger("memberCount"));
        groupItem.setThumb(jObj.get("thumb") == null ? null : jObj.getString("thumb"));
        groupItem.setTitle(jObj.get("title") == null ? null : jObj.getString("title"));
        return groupItem;
    }
}
