package com.pointhouse.chiguan.l1;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.session.SessionHelper;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.UserPreferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.Application.MediaService;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.http.RetrofitFactory;
import com.pointhouse.chiguan.common.jsonObject.MyStudyGetBean;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.JsonUtil;
import com.pointhouse.chiguan.common.util.MediaServiceHelper;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;
import com.pointhouse.chiguan.common.util.ToastUtil;
import com.pointhouse.chiguan.db.StudyInfo;
import com.pointhouse.chiguan.db.StudyInfoDao;
import com.pointhouse.chiguan.db.UserInfo;
import com.alibaba.fastjson.JSONObject;

import com.pointhouse.chiguan.k1_9.ShareArticleActivity;
import com.pointhouse.chiguan.w1_14.FindPsw_activity;
import com.pointhouse.chiguan.wxapi.WXEntryActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by P on 2017/7/28.
 */

public class LoginNetWork {
    String token;
    UserInfo userinfo;

    public void UserLogin(ContextWrapper context, String mobile, String pas, String fromid) {
        String parm = JsonUtil.initGetRequestParm(mobile + "," + pas);
        String url = Constant.URL_BASE + "login/" + parm;
        RetrofitFactory.getInstance().getRequestServices().UserLogin(url)
                .subscribeOn(Schedulers.newThread())
                .map(response -> {
                    token = null;
                    userinfo = null;
                    String result = "";
                    String resultCode = response.getString("resultCode");
                    if (resultCode.equals("2")) {
                        com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                        if (exceptions != null && exceptions.size() > 0) {
                            JSONObject exlist = exceptions.getJSONObject(0);
                            result = exlist.getString("message");
                        }
                    } else if (resultCode.equals("1")) {
                        JSONObject resultObject = response.getJSONObject("resultObject");
                        JSONObject userInfo = resultObject.getJSONObject("user");
                        token = resultObject.getString("token");
                        String userstr = userInfo.toString();

                        Gson gson = new Gson();
                        userinfo = gson.fromJson(userstr, UserInfo.class);
                        userinfo.setUserid(userinfo.getId());
                        userinfo.setPassword(pas);
                    } else if (resultCode.equals("0")) {
                        result = null;
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result == null) {
                        ToastUtil.getToast(context, "系统异常，请稍后再试！", "center", 0, 180).show();
                        if (SharedPreferencesUtil.readToken(context) != null || !SharedPreferencesUtil.readToken(context).equals("")) {
                            SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, "userinfo");
                            sharedPreferences.edit().clear().commit();
                        }
                        if (context.getClass().getName().equals("com.pointhouse.chiguan.Application.GlobalApplication")) {
                            return;
                        } else if (context.getClass().getName().equals("com.pointhouse.chiguan.l1.Login_activity")) {
                            Login_activity login_activity = (Login_activity) context;
                            login_activity.loginbtn.setBackgroundResource(R.mipmap.signin);
                            login_activity.loginbtn.setEnabled(true);
                            login_activity.dialog.dismiss();
                            return;
                        } else if (context.getClass().getName().equals("com.pointhouse.chiguan.w1_14.FindPsw_activity")) {
                            FindPsw_activity findpsw_activity = (FindPsw_activity) context;
                            findpsw_activity.loginbtn.setBackgroundResource(R.mipmap.signin);
                            findpsw_activity.loginbtn.setEnabled(true);
                            return;
                        }
                    }
                    Activity activity = null;
                    if (result != null && !result.equals("")) {
                        if (SharedPreferencesUtil.readToken(context) != null || !SharedPreferencesUtil.readToken(context).equals("")) {
                            SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, "userinfo");
                            sharedPreferences.edit().clear().commit();
                        }
                        if (context.getClass().getName().equals("com.pointhouse.chiguan.Application.SplashActivity")) {
                            return;
                        } else {
                            ToastUtil.getToast(context, result, "center", 0, 180).show();
                            if (context.getClass().getName().equals("com.pointhouse.chiguan.l1.Login_activity")) {
                                Login_activity login_activity = (Login_activity) context;
                                login_activity.loginbtn.setBackgroundResource(R.mipmap.signin);
                                login_activity.loginbtn.setEnabled(true);
                                login_activity.dialog.dismiss();
                                return;
                            } else if (context.getClass().getName().equals("com.pointhouse.chiguan.w1_14.FindPsw_activity")) {
                                FindPsw_activity findpsw_activity = (FindPsw_activity) context;
                                findpsw_activity.loginbtn.setBackgroundResource(R.mipmap.signin);
                                findpsw_activity.loginbtn.setEnabled(true);
                                findpsw_activity.dialog.dismiss();
                                return;
                            }
                        }
                    } else {
                        if (context.getClass().getName().equals("com.pointhouse.chiguan.welcome.SplashActivity")) {

                        } else {
                            activity = (Activity) context;
                            if (fromid != null) {
                                if (fromid.equals("w1") || fromid.equals("w1_11")) {
                                    Intent intent = IntentUriUtil.getIntent("t1", fromid);
                                    activity.startActivity(intent);
                                    activity.finish();
                                } else if (fromid.equals("k1_2")) {
                                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + Login_activity.k1_2id));
                                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(it);
                                    activity.finish();
                                } else if (fromid.equals("k1_9")) {
                                    Intent it = new Intent(activity, ShareArticleActivity.class);
                                    it.putExtra("articleId", Login_activity.paramId);
                                    activity.startActivity(it);
                                    activity.finish();
                                } else {
                                    Intent intent = IntentUriUtil.getIntent(fromid, "l1");
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            } else {
                                Intent intent = IntentUriUtil.getIntent("t1", "l1");
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            if (context.getClass().getName().equals("com.pointhouse.chiguan.l1.Login_activity")) {
                                ToastUtil.getToast(activity, "欢迎登录", "center", 0, 180).show();
                            } else if (context.getClass().getName().equals("com.pointhouse.chiguan.w1_14.FindPsw_activity")) {
                                ToastUtil.getToast(activity, "修改成功", "center", 0, 180).show();
                            }
                        }
                        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, "userinfo");
                        sharedPreferences.edit().clear().commit();
                        GlobalApplication.user = null;
                        GlobalApplication.user = userinfo;
                        GlobalApplication.LoginType = 1;
                        GlobalApplication.user.setStatus(GlobalApplication.LoginType + "");
//                        if(GlobalApplication.user.getAvatar()!=null&&!GlobalApplication.user.getAvatar().equals("")){
//                            JSONObject jsonObject = JSONObject.parseObject(GlobalApplication.user.getAvatar());
//                            String avatar = jsonObject.getString("url");
//                            GlobalApplication.user.setAvatar(avatar);
//                        }
                        GlobalApplication.personalCenterActivity = null;
//                        GlobalApplication.user.setVip(1);
//                        GlobalApplication.user.setVipEndDate("2089-00-00");
                        new LoginDaoImpl().saveLoginUserinfo(context, GlobalApplication.user);
                        SharedPreferencesUtil.saveToken(context, token);
                        SharedPreferencesUtil.saveSharedPreferences(context, "userinfo", userinfo);
                        RetrofitFactory.resetRetrofitFactory(token);
                        requestGetMyStudyListFromServer();
                        String rid = JPushInterface.getRegistrationID(context);
                        if (!GlobalApplication.user.getRegistrationId().equals(rid)) {
                            uploadRegId(rid);
                        }
                        MediaServiceHelper.getService(context, MediaService::reFleshDisplay);//显示

                        //网易登录
                        NimUIKit.doLogin(new LoginInfo(userinfo.getAccid(), userinfo.getNimToken()), new RequestCallback<LoginInfo>() {
                            @Override
                            public void onSuccess(LoginInfo loginInfo) {
//                                UserPreferences.setEarPhoneModeEnable(false);
                                DemoCache.setAccount(userinfo.getAccid());
                                SessionHelper.saveLoginInfo(userinfo.getAccid(), userinfo.getNimToken());
                                // 初始化消息提醒配置
                                SessionHelper.initNotificationConfig();
                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(context, "系统异常，请稍后再试！", "center", 0, 180).show();
                    if (context.getClass().getName().equals("com.pointhouse.chiguan.l1.Login_activity")) {
                        Login_activity login_activity = (Login_activity) context;
                        login_activity.loginbtn.setBackgroundResource(R.mipmap.signin);
                        login_activity.loginbtn.setEnabled(true);
                        login_activity.dialog.dismiss();
                    } else if (context.getClass().getName().equals("com.pointhouse.chiguan.w1_14.FindPsw_activity")) {
                        FindPsw_activity findpsw_activity = (FindPsw_activity) context;
                        findpsw_activity.loginbtn.setBackgroundResource(R.mipmap.signin);
                        findpsw_activity.loginbtn.setEnabled(true);
                        findpsw_activity.dialog.dismiss();
                    }
                });
    }

    //微信登录
    public void getWXImageUrl(Context context, String openid, String wxname, String imageUrl) {
        String parm = JsonUtil.initGetRequestParm(openid + "," + wxname + "," + imageUrl);
        String url = Constant.URL_BASE + "thirdPartLogin/";
        RetrofitFactory.getInstance().getRequestServices().wxlogin(url, RetrofitFactory.createRequestBody(parm))
                .subscribeOn(Schedulers.newThread())
                .map(response -> {
                    token = null;
                    userinfo = null;
                    String result = "";
                    String resultCode = response.getString("resultCode");
                    if (resultCode.equals("2")) {
                        com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                        if (exceptions != null && exceptions.size() > 0) {
                            JSONObject exlist = exceptions.getJSONObject(0);
                            result = exlist.getString("message");
                        }
                    } else if (resultCode.equals("1")) {
                        JSONObject resultObject = response.getJSONObject("resultObject");
                        JSONObject userInfo = resultObject.getJSONObject("user");
                        token = resultObject.getString("token");
                        String userstr = userInfo.toString();

                        Gson gson = new Gson();
                        userinfo = gson.fromJson(userstr, UserInfo.class);
                        userinfo.setUserid(userinfo.getId());
                    } else if (resultCode.equals("0")) {
                        result = null;
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Activity activity = null;
                    if (result == null) {
                        if (SharedPreferencesUtil.readToken(context) != null || !SharedPreferencesUtil.readToken(context).equals("")) {
                            SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, "userinfo");
                            sharedPreferences.edit().clear().commit();
                        }
                        ToastUtil.getToast(context, "系统异常，请稍后再试！", "center", 0, 180).show();
                        WXEntryActivity entryActivity = (WXEntryActivity) context;
                        entryActivity.finish();
                    }
                    if (result != null && !result.equals("")) {
                        if (SharedPreferencesUtil.readToken(context) != null || !SharedPreferencesUtil.readToken(context).equals("")) {
                            SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, "userinfo");
                            sharedPreferences.edit().clear().commit();
                        }
                        ToastUtil.getToast(context, result, "center", 0, 180).show();
                        WXEntryActivity entryActivity = (WXEntryActivity) context;
                        entryActivity.finish();
                        return;
                    } else {
                        activity = (Activity) context;
                        if (GlobalApplication.fromid != null) {
                            if (GlobalApplication.fromid.equals("w1") || GlobalApplication.fromid.equals("w1_11")) {
                                Intent intent = IntentUriUtil.getIntent("t1", GlobalApplication.fromid);
                                activity.startActivity(intent);
                                activity.finish();
                            } else if (GlobalApplication.fromid.equals("k1_2")) {
                                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalApplication.scheme_uri + "k1_2" + "?id=" + Login_activity.k1_2id));
                                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(it);
                                activity.finish();
                            } else if (GlobalApplication.fromid.equals("k1_9")) {
                                Intent it = new Intent(activity, ShareArticleActivity.class);
                                it.putExtra("articleId", Login_activity.paramId);
                                activity.startActivity(it);
                                activity.finish();
                            } else {
                                Intent intent = IntentUriUtil.getIntent(GlobalApplication.fromid, "l1");
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        } else {
                            Intent intent = IntentUriUtil.getIntent("t1", "l1");
                            activity.startActivity(intent);
                            activity.finish();
                        }
                        ToastUtil.getToast(activity, "欢迎登录", "center", 0, 180).show();
                        SharedPreferences sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, "userinfo");
                        sharedPreferences.edit().clear().commit();
                        GlobalApplication.BandweChat = 1;
                        GlobalApplication.personalCenterActivity = null;
                        SharedPreferencesUtil.saveToken(context, token);
                        SharedPreferencesUtil.saveSharedPreferences(context, "userinfo", userinfo);
                        RetrofitFactory.resetRetrofitFactory(token);
                        requestGetMyStudyListFromServer();
                        GlobalApplication.user = userinfo;
                        if (GlobalApplication.user.getNickname() == null || GlobalApplication.user.getNickname().equals("")) {
                            GlobalApplication.user.setNickname(GlobalApplication.wxnickname);
                        }

                        GlobalApplication.LoginType = 2;
                        GlobalApplication.user.setStatus(GlobalApplication.LoginType + "");
                        GlobalApplication.user.setOpenid(GlobalApplication.openid);
                        String rid = JPushInterface.getRegistrationID(context);
                        new LoginDaoImpl().saveLoginUserinfo(context, GlobalApplication.user);
                        if (!GlobalApplication.user.getRegistrationId().equals(rid)) {
                            uploadRegId(rid);
                        }
                        WXEntryActivity.login_activity.finish();
                        MediaServiceHelper.getService(context, MediaService::reFleshDisplay);//显示

                        //网易登录
                        NimUIKit.doLogin(new LoginInfo(userinfo.getAccid(), userinfo.getNimToken()), new RequestCallback<LoginInfo>() {
                            @Override
                            public void onSuccess(LoginInfo loginInfo) {
//                                UserPreferences.setEarPhoneModeEnable(false);
                                DemoCache.setAccount(userinfo.getAccid());
                                SessionHelper.saveLoginInfo(userinfo.getAccid(), userinfo.getNimToken());
                                // 初始化消息提醒配置
                                SessionHelper.initNotificationConfig();
                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(context, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }


    //上传极光id
    public void uploadRegId(String id) {
        JSONArray jsonArray = new JSONArray();
        HashMap hashMap = new HashMap();
        hashMap.put("registrationId", id);
        jsonArray.add(hashMap);
        String parm = jsonArray.toString();
        String url = Constant.URL_BASE + "updateUserInfo/" + parm;
        RetrofitFactory.getInstance().getRequestServicesToken().uploadRegID(url)
                .subscribeOn(Schedulers.newThread())
                .map(response -> {
                    String result = "";
                    String resultCode = response.getString("resultCode");
                    if (resultCode.equals("2")) {
                        com.alibaba.fastjson.JSONArray exceptions = response.getJSONArray("exceptions");
                        if (exceptions != null && exceptions.size() > 0) {
                            JSONObject exlist = exceptions.getJSONObject(0);
                            result = exlist.getString("message");
                        }
                    } else if (resultCode.equals("1")) {
                        result = response.getString("resultObject");
                    } else if (resultCode.equals("0")) {
                        result = null;
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(GlobalApplication.sContext, "系统异常，请稍后再试！", "center", 0, 180).show();
                });
    }

    private void requestGetMyStudyListFromServer() {

        String url = JsonUtil.getURLWithArrayParamIfExists("myLearn", String.valueOf(0));
        RetrofitFactory.getInstance().getRequestServicesToken().get(url)
                .subscribeOn(Schedulers.newThread())
                .map(response -> {
                    Log.i("MyStudyJson", response.toJSONString());
                    MyStudyGetBean myStudyGetBean = new MyStudyGetBean(response.toJSONString());
//                    MyStudyGetBean myStudyGetBean = new MyStudyGetBean(TestMyStudyGetBean.jsonEmpty);//for test
//                    MyStudyGetBean myStudyGetBean = new MyStudyGetBean(TestMyStudyGetBean.jsonException);//for test
//                    MyStudyGetBean myStudyGetBean = new MyStudyGetBean(TestMyStudyGetBean.jsonNormal);//for test
                    if (myStudyGetBean.hasError()) {
                        Log.e("登录", JsonUtil.getRequestErrMsg(null, GlobalApplication.sContext, response));
                    } else {
                        List<MyStudyGetBean.ResultObjectBean> beanList = myStudyGetBean.getResultObjectBeanList();
                        if (beanList != null) {
                            StudyInfoDao dao = new StudyInfoDao(GlobalApplication.sContext);
                            Date updateDate = new Date();
                            for (MyStudyGetBean.ResultObjectBean rob : beanList) {
                                // 保存到本地
                                if (rob.getProcessDetail() != null) {
                                    for (MyStudyGetBean.ResultObjectBean.ProcessDetailBean pdb : rob.getProcessDetail()) {
                                        StudyInfo studyInfo = new StudyInfo();
                                        studyInfo.setCourseId(rob.getCourseId());
                                        studyInfo.setCourseName(rob.getCourseTitle());
                                        studyInfo.setLessonId(rob.getLessonId());
                                        studyInfo.setLessonName(rob.getLessonTitle());
//                                        studyInfo.setlLength(getStudyLength(rob.getTotalSecond(), rob.getTotalProcess()));
                                        studyInfo.setVid(pdb.getCvid());
                                        studyInfo.setvName(pdb.getCvname());
                                        studyInfo.setvLength(getStudyLength(pdb.getCSecond(), pdb.getCprocess()));
                                        studyInfo.setCurrent(Long.valueOf(pdb.getCSecond()));
                                        studyInfo.setUpdateDate(updateDate);
                                        dao.saveIfNotExist(studyInfo);
                                    }
                                }
                            }
                        }
                    }

                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtil.getToast(GlobalApplication.sContext, GlobalApplication.sContext.getString(R.string.req_fail_msg), "center", 0, 180).show();
                });
    }

    /**
     * 计算学习时长
     *
     * @param current
     * @param process
     * @return
     */
    private long getStudyLength(String current, String process) {
        int intProcess = Integer.valueOf(process);
        return intProcess == 0 ? 0 : (long) Math.ceil(Long.valueOf(current) * 100 / intProcess);
    }
}
