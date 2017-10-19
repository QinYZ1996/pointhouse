package com.pointhouse.chiguan.w1_10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.mydialog.MyDialog;
import com.pointhouse.chiguan.common.updatecommon.PullToRefreshMenuView;
import com.pointhouse.chiguan.common.updatecommon.SwipeMenuCreator;
import com.pointhouse.chiguan.common.updatecommon.SwipeMenuItem;
import com.pointhouse.chiguan.common.updatecommon.SwipeMenuListView;
import com.pointhouse.chiguan.common.updatecommon.Utils;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.OtherMsg;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by P on 2017/8/7.
 */

public class MyMessage_OtherMessageFragment extends Fragment {
    Disposable disposable;
    private View view;
    private MyMessage_OtherAdapter myMessage_systemAdapter;
    private PullToRefreshMenuView listView;
    private SwipeMenuListView swipeMenuListView;
    private List<OtherMsg> otherMessageList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.w1_10_fragment_mymessage_othermessage, container, false);

        initListView();
        SwipeMenuListViewinit();
        uploadMySysMessage();
        //SwipeMenuListViewinit();
        return view;
    }

    public void uploadMySysMessage() {
        String parm = "[0]";
        String url = Constant.URL_BASE + "messageList/" + parm;
        disposable = RetrofitFactory.getInstance().getRequestServicesToken()
                .getStringRx(url)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String resultcode = response.getString("resultCode");
                    if (resultcode.equals("0")) {
                        ListView listView1 = listView.getRefreshableView();
                        View listEmptyView = View.inflate(getActivity(), R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                        listView1.setEmptyView(listEmptyView);
                        ToastUtil.getToast(GlobalApplication.sContext, "系统异常，请稍后再试！", "center", 0, 180).show();
                        return;
                    } else if (resultcode.equals("2")) {
                        JSONArray exceptions = response.getJSONArray("exceptions");
                        if (exceptions != null && exceptions.size() > 0) {
                            JSONObject jsonObject = exceptions.getJSONObject(0);
                            String message = jsonObject.getString("message");
                            View listEmptyView = View.inflate(getActivity(), R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                            swipeMenuListView.setEmptyView(listEmptyView);
                            ToastUtil.getToast(GlobalApplication.sContext, message, "center", 0, 180).show();
                            return;
                        }
                    } else if (resultcode.equals("1")) {
                        JSONObject jsonObject = response.getJSONObject("resultObject");
                        JSONArray OthermesArray = jsonObject.getJSONArray("otherMsg");
                        if (OthermesArray != null && OthermesArray.size() > 0) {
                            for (int i = 0; i < OthermesArray.size(); i++) {
                                JSONObject otherobj = OthermesArray.getJSONObject(i);
                                OtherMsg otherMessage = new OtherMsg();
                                otherMessage.setTargetId(otherobj.getString("targetId"));
                                otherMessage.setType(otherobj.getString("type"));
                                otherMessage.setTitle(otherobj.getString("title"));
                                otherMessage.setThumb(otherobj.getString("thumb"));
                                otherMessage.setContent(otherobj.getString("content"));
                                otherMessage.setId(otherobj.getString("id"));
                                otherMessage.setMsgDate(otherobj.getString("msgDate"));
                                otherMessageList.add(otherMessage);
                                myMessage_systemAdapter = new MyMessage_OtherAdapter(getActivity(), otherMessageList);
                                listView.getRefreshableView().setAdapter(myMessage_systemAdapter);
                            }
                        } else {
                            View listEmptyView = View.inflate(getActivity(), R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                            swipeMenuListView.setEmptyView(listEmptyView);
                            return;
                        }
                    }
                }, throwable -> {
                    View listEmptyView = View.inflate(getActivity(), R.layout.common_empty, (ViewGroup) swipeMenuListView.getParent());
                    swipeMenuListView.setEmptyView(listEmptyView);
                    ToastUtil.getToast(GlobalApplication.sContext, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }

    public void initListView() {
        listView = (PullToRefreshMenuView) view.findViewById(R.id.other_Listview);
        swipeMenuListView = listView.getRefreshableView();

        listView.setPullRefreshEnabled(false);
        listView.setPullLoadEnabled(false);
        listView.setScrollLoadEnabled(false);

        this.listView.getRefreshableView().setOnItemClickListener((parent, view, position, id) -> {
            Context context = view.getContext();
            OtherMsg otherMsg = (OtherMsg) this.myMessage_systemAdapter.getItem(position);
            if(otherMsg.getType().equals("1")){
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri+"k1_2" + "?id=" + String.valueOf(otherMsg.getTargetId())));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
            }
        });
    }


    public void removeOtherMessage(int position) {
        OtherMsg msg = (OtherMsg) this.myMessage_systemAdapter.getItem(position);
        String parm = JsonUtil.initGetRequestParm(msg.getId());
        String url = Constant.URL_BASE + "messageDel/" + parm;
        RetrofitFactory.getInstance().getRequestServicesToken()
                .getStringRx(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String resultCode = response.getString("resultCode");
                    if (resultCode.equals("0")) {
                        ToastUtil.getToast(GlobalApplication.sContext, "系统异常，请稍后再试！", "center", 0, 180).show();
                    } else if (resultCode.equals("1")) {
                        myMessage_systemAdapter.delete(position);
                        myMessage_systemAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                        ToastUtil.getToast(GlobalApplication.sContext, "删除成功", "center", 0, 180).show();
                    } else if (resultCode.equals("2")) {
                        JSONArray jsonArray = response.getJSONArray("exceptions");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String message = jsonObject.getString("message");
                        ToastUtil.getToast(GlobalApplication.sContext, message, "center", 0, 180).show();
                    }

                }, throwable -> {
                    ToastUtil.getToast(GlobalApplication.sContext, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }

    public void SwipeMenuListViewinit() {
        SwipeMenuCreator creator = menu -> {
            // 创建Item
            SwipeMenuItem openItem = new SwipeMenuItem(getContext());
            // 设置item的背景颜色
            openItem.setBackground(new ColorDrawable(Color.RED));
            // 设置item的宽度
            openItem.setWidth(Utils.dip2px(getActivity(), 69));
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
            showLoginDialog(position);
            return false;
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void showLoginDialog(int position) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.common_dialog, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText("确认删除？");
        final TextView btnYes = (TextView) view.findViewById(R.id.f_quchecbutton_btn_queding);
        final TextView btlNo = (TextView) view.findViewById(R.id.f_quchecbutton_btn_quxiao);
        final MyDialog builder = new MyDialog(getContext(),0,0,view,R.style.DialogTheme);

        builder.setCancelable(false);
        builder.show();
        //设置对话框显示的View
        //点击确定是的监听
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
                removeOtherMessage(position);
            }
        });


        btlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });
    }
}
