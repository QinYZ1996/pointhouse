package com.pointhouse.chiguan.k1_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.RoundProgressBar.RoundProgressBar;
import com.pointhouse.chiguan.common.base.PermissionActivityBase;
import com.pointhouse.chiguan.common.jsonObject.CourseDetailGetBean;
import com.pointhouse.chiguan.common.util.CommonMediaOption;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.db.CourseDetailInfo;
import com.pointhouse.chiguan.db.CourseDetailInfoDao;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.db.ImgUrlInfo;
import com.pointhouse.chiguan.db.ImgUrlInfoDao;
import com.pointhouse.chiguan.k1_3.MediaActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import static com.pointhouse.chiguan.common.util.SharedPreferencesUtil.readToken;

/**
 * Created by ljj on 2017/7/5.
 */
    public class CourseDetailsAdapter extends BaseAdapter{

        private CourseDownloadInfoDao courseDownloadInfoDao;
        private CourseDetailInfoDao courseDetailInfoDao;
        private ImgUrlInfoDao imgUrlInfoDao;
        private CourseDetailsActivity mActivity;


    protected class CourseItem {
            public int id;
            public String courselistImg;
            public String courselistName;
            public int courselistDraw;
            public int courselistTime;
            public int courseId;
            public String json;
            public String courseThumb;
            public List<CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean> audiosBeanList;
            public List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanList;
            public String title;
        }

        private LayoutInflater mInflater;
        private Vector<CourseItem> mModels = new Vector<CourseItem>();
        private ListView mListView;

        public CourseDetailsAdapter(Context context, ListView listView) {
            mActivity = (CourseDetailsActivity)context;
            mInflater = LayoutInflater.from(context);
            mListView = listView;
            courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
            courseDetailInfoDao = new CourseDetailInfoDao(GlobalApplication.sContext);
            imgUrlInfoDao = new ImgUrlInfoDao(GlobalApplication.sContext);
        }

        public void addCourse(int id, String courselistImg, String courselistName, int courselistDraw, int courselistTime,int courseId,
           List<CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean> audiosBeanList,String json,String courseThumb,
           List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanList,String title) {
            CourseItem model = new CourseItem();
            model.id = id;
            model.courselistImg = courselistImg;
            model.courselistName = courselistName;
            model.courselistDraw = courselistDraw;
            model.courselistTime = courselistTime;
            model.courseId = courseId;
            model.audiosBeanList = audiosBeanList;
            model.json = json;
            model.courseThumb = courseThumb;
            model.lessonsBeanList = lessonsBeanList;
            model.title = title;
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
            convertView = mInflater.inflate(R.layout.k1_2_adapter_courselist, null);

            CourseItem model = mModels.get(position);
            convertView.setTag(position);
            ImageView courselistImg = (ImageView) convertView.findViewById(R.id.courselistImg);
            ImageView courselistDraw1 = (ImageView) convertView.findViewById(R.id.courselistDraw1);
            ImageView courselistDraw2 = (ImageView) convertView.findViewById(R.id.courselistDraw2);
            ImageView courselistDraw3 = (ImageView) convertView.findViewById(R.id.courselistDraw3);
            TextView courselistName = (TextView) convertView.findViewById(R.id.courselistName);
            TextView courselistTime = (TextView) convertView.findViewById(R.id.courselistTime);
            RoundProgressBar progress = (RoundProgressBar) convertView.findViewById(R.id.downloadprogress);
            progress.setVisibility(View.GONE);
            if(model.audiosBeanList != null && model.audiosBeanList.size() != 0) {
                TextView downloadText = (TextView) convertView.findViewById(R.id.downloadText);
                TextView downloadTextTip = (TextView) convertView.findViewById(R.id.downloadTextTip);
                RelativeLayout downloadRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.downloadRelativeLayout);
                progress.setMax(1000);
                try {
                    List<CourseDownloadInfo> courseDownloadInfoList = new ArrayList<>();
                    int downflg = -1;
                    courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(model.id);
                    downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);

                    if (downflg == 0) {
                        downloadText.setVisibility(View.GONE);
                        downloadTextTip.setVisibility(View.GONE);
                        progress.setBackgroundResource(R.mipmap.xiazai);
                        progress.setVisibility(View.VISIBLE);
                    } else if (downflg == 1) {
                        downloadText.setVisibility(View.GONE);
                        downloadTextTip.setVisibility(View.GONE);
                        progress.setBackgroundResource(R.mipmap.xiazaizhong);
                        progress.setVisibility(View.VISIBLE);
                    } else if (downflg == 2) {
                        downloadText.setVisibility(View.GONE);
                        downloadTextTip.setVisibility(View.GONE);
                        int progress1 = 0;
                        for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                            if (courseDownloadInfo.getState() == 4) {
                                progress1 = (int) courseDownloadInfo.getProgress();
                            }

                            if (courseDownloadInfo.getState() == 2) {
                                if ((int) courseDownloadInfo.getProgress() != 0) {
                                    progress1 = (int) courseDownloadInfo.getProgress();
                                }
                                break;
                            }
                        }
                        progress.setProgress(progress1);
                        progress.setBackgroundResource(R.mipmap.xiazaizanting);
                        progress.setVisibility(View.VISIBLE);
                    } else if (downflg == 4) {
                        downloadText.setVisibility(View.GONE);
                        downloadTextTip.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        progress.setProgress(1000);
                        progress.setBackgroundResource(R.mipmap.xiazaiwanbi);
                    } else if (downflg == 3) {
                        progress.setVisibility(View.GONE);
                        downloadText.setVisibility(View.VISIBLE);
                        downloadText.setText("正在等待...");
                        downloadTextTip.setVisibility(View.VISIBLE);
                        downloadTextTip.setText("点击暂停下载");
                    } else if (downflg == 5) {
                        progress.setVisibility(View.GONE);
                        downloadText.setVisibility(View.VISIBLE);
                        downloadText.setText("下载失败...");
                        downloadText.setTextColor(Color.RED);
                        downloadTextTip.setVisibility(View.VISIBLE);
                        downloadTextTip.setText("点击重新下载");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //下载按钮
                downloadRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (readToken(v.getContext()) == null || readToken(v.getContext()).equals("")) {
                            mActivity.finishActivity();
                            return;
                        }
                        if(mActivity.isPaid == 0){
                            Toast.makeText(v.getContext(), "请您购买后查看" , Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mActivity.requestPermissions(
                                PermissionActivityBase.PermissionGroup.STORAGE,
                                true,
                                new PermissionActivityBase.CallbackListener() {
                                    @Override
                                    public void onPermissionsOK(int requestCode, List<String> perms) {
                                        mActivity.checkLessonDetail2(model.id, v, model, progress, downloadText,downloadTextTip);
                                    }

                                    @Override
                                    public void onPermissionsNG(int requestCode, List<String> perms) {

                                    }

                                    @Override
                                    public void onAppSettingsBack(int requestCode, int resultCode, Intent data) {

                                    }
                                });
                    }
                });

                mActivity.progressBarList.put(model.id, progress);
                mActivity.progressTxtList.put(model.id, downloadText);
                mActivity.progressTxtTipList.put(model.id, downloadTextTip);
            }

            //k1_2跳转
            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(readToken(v.getContext()) == null || readToken(v.getContext()).equals("")){
                        mActivity.finishActivity();
                        return;
                    }


//                    List<CourseDownloadInfo> courseDownloadInfoList = new ArrayList<>();
//                    int downflg = -1;
//                    try {
//                        courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(model.id);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
//                    if(courseDownloadInfoList == null) return;
//                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
//
//                        SDCardHelper.removeFileFromRom(courseDownloadInfo.getFilePath() + "/" + courseDownloadInfo.getfileName());
//                        try {
//                            if (courseDownloadInfoDao.queryVid(courseDownloadInfo.getVid()) != null) {
//                                courseDownloadInfoDao.delete(courseDownloadInfoDao.queryVid(courseDownloadInfo.getVid()));
//                            }
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    if(mActivity.isPaid == 0){
                        Toast.makeText(v.getContext(), "请您购买后查看" , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mActivity.checkLessonDetail(model.id,v,model);
                }
            });
            courselistName.setText(model.courselistName);
            courselistTime.setText("时长" + formatSecond(model.courselistTime));

            if(model.courselistImg != null && !model.courselistImg.isEmpty()){
                Picasso.with(convertView.getContext()).load(model.courselistImg).placeholder(R.drawable.icon1_default)
                        .into(courselistImg);
            }else {
                Picasso.with(convertView.getContext()).load(R.drawable.icon1_default)
                        .into(courselistImg);
            }

            if(model.courselistDraw == 0){
                courselistDraw1.setBackgroundResource(R.mipmap.wenzhangicon);
                courselistDraw2.setVisibility(View.GONE);
                courselistDraw3.setVisibility(View.GONE);
                courselistTime.setText("时长--: --");
            } else if (model.courselistDraw == 1)
            {
                courselistDraw1.setBackgroundResource(R.mipmap.music);
                courselistDraw2.setVisibility(View.GONE);
                courselistDraw3.setVisibility(View.GONE);
            }else if(model.courselistDraw == 2){
                courselistDraw1.setBackgroundResource(R.mipmap.video);
                courselistDraw2.setVisibility(View.GONE);
                courselistDraw3.setVisibility(View.GONE);
            } else if (model.courselistDraw == 4){
                courselistDraw1.setBackgroundResource(R.mipmap.music);
                courselistDraw2.setVisibility(View.VISIBLE);
                courselistDraw2.setBackgroundResource(R.mipmap.video);
                courselistDraw3.setVisibility(View.GONE);
            }
            return convertView;
        }

    protected void downLoadFile(View v,CourseItem model,RoundProgressBar progress,TextView downloadText, TextView downloadTextTip){
        List<CourseDownloadInfo> courseDownloadInfoList = new ArrayList<>();
        int downflg = -1;
        try {
            courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(model.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);

        if(downflg == 0){
            try {
                CourseDetailInfo courseDetailInfo = courseDetailInfoDao.queryCourseId(model.courseId);
                if(courseDetailInfo == null){
                    courseDetailInfo = new CourseDetailInfo();
                    courseDetailInfo.setCourseId(model.courseId);
                    courseDetailInfo.setCourseJson(model.json);
                    courseDetailInfoDao.save(courseDetailInfo);
                }else {
                    String courseJson = courseDetailInfo.getCourseJson();
                    CourseDetailGetBean courseDetailGetBeanOld = new Gson().fromJson(courseJson, CourseDetailGetBean.class);
                    List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListOld = courseDetailGetBeanOld.getResultObject().getLessons();
                    CourseDetailGetBean courseDetailGetBeanNew = new Gson().fromJson(model.json, CourseDetailGetBean.class);
                    List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListNew = courseDetailGetBeanNew.getResultObject().getLessons();
                    Map<Integer,CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListMapNew = new TreeMap<>();
                    Map<Integer,CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListMapOld = new TreeMap<>();
                    List<CourseDownloadInfo> courseDownloadInfoListOld = courseDownloadInfoDao.queryAllByCourseId(model.courseId);
                    Set<Integer> lessonSetOld = new HashSet<Integer>();

                    if(courseDownloadInfoListOld != null && courseDownloadInfoListOld.size() > 0){
                        for(CourseDownloadInfo courseDownloadInfo : courseDownloadInfoListOld){
                            lessonSetOld.add(courseDownloadInfo.getLessonId());
                        }
                    }

                    for(CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean : lessonsBeanListNew){
                        lessonsBeanListMapNew.put(lessonsBean.getId(), lessonsBean);
                    }

                    for(CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean : lessonsBeanListOld){
                        lessonsBeanListMapOld.put(lessonsBean.getId(), lessonsBean);
                    }

                    Iterator<Map.Entry<Integer, CourseDetailGetBean.ResultObjectBean.LessonsBean>> it = lessonsBeanListMapNew.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Integer, CourseDetailGetBean.ResultObjectBean.LessonsBean> entry = it.next();
                        if(lessonsBeanListMapOld.containsKey(entry.getKey())){
                            CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBeanOld = lessonsBeanListMapOld.get(entry.getKey());
                            CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBeanNew = entry.getValue();
                            if(lessonSetOld.contains(entry.getKey())) {
                                lessonsBeanOld.setThumb(lessonsBeanNew.getThumb());
                                lessonsBeanOld.setTitle(lessonsBeanNew.getTitle());
                            }
                            else{
                                lessonsBeanListMapOld.remove(entry.getKey());
                                lessonsBeanListMapOld.put(entry.getKey(), lessonsBeanNew);
                            }
                        } else {
                            lessonsBeanListMapOld.put(entry.getKey(),entry.getValue());
                        }
                    }
                    courseDetailGetBeanNew.getResultObject().setLessons(new ArrayList<>(lessonsBeanListMapOld.values()));
                    String newjson = new Gson().toJson(courseDetailGetBeanNew);
                    courseDetailInfo.setCourseJson(newjson);
                    courseDetailInfoDao.update(courseDetailInfo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            int fileCount = model.audiosBeanList.size();
            int index = 0;
            List<CourseDownloadInfo> courseDownloadInfoList1 = new ArrayList<>();
            for(CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean audioListBean :model.audiosBeanList) {
                CourseDownloadInfo courseDownloadInfo = new CourseDownloadInfo();
                courseDownloadInfo.seturl(audioListBean.getOrigUrl());
                courseDownloadInfo.setfileName(audioListBean.getVid() + audioListBean.getVideoName());
                courseDownloadInfo.setLessonId(model.id);
                courseDownloadInfo.setProgress((1000 / fileCount) * index);
                courseDownloadInfo.setTotle(1000);
                courseDownloadInfo.setVid(audioListBean.getVid());
                courseDownloadInfo.setState(3);
                courseDownloadInfo.setFilePath(String.valueOf(model.id));
                courseDownloadInfo.setErrorMessage("");
                courseDownloadInfo.setFileCount(fileCount);
                courseDownloadInfo.setIndex(index);
                courseDownloadInfo.setCourseId(model.courseId);
                index ++;
                courseDownloadInfoList1.add(courseDownloadInfo);

                try {
                    courseDownloadInfoDao.save(courseDownloadInfo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            mActivity.mDownloadService.addWaitDownload(courseDownloadInfoList1);
            progress.setVisibility(View.GONE);
            downloadText.setVisibility(View.VISIBLE);
            downloadText.setText("正在等待...");
            downloadTextTip.setVisibility(View.VISIBLE);
            downloadTextTip.setText("点击暂停下载");

            //获得图片的地址
            if(model.courselistImg != null && !model.courselistImg.isEmpty()){
                //Target
                Target target = new Target(){

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int height = DensityUtil.px2dip(v.getContext(), bitmap.getHeight());
                        int width = DensityUtil.px2dip(v.getContext(), bitmap.getWidth());
                        int defalt = 100;
                        int start = height <= width ? width : height;
                        while (start > 200){
                            start = start / 2;
                            defalt = defalt / 2;
                        }

                        final ByteArrayOutputStream os = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, defalt, os);

                        try {
                            ImgUrlInfo imgUrlInfo = imgUrlInfoDao.queryUrl(model.courselistImg);
                            if(imgUrlInfo == null){
                                imgUrlInfo = new ImgUrlInfo();
                                imgUrlInfo.setUrl(model.courselistImg);
                                imgUrlInfo.setBlob(os.toByteArray());
                                imgUrlInfoDao.save(imgUrlInfo);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                //Picasso下载
                Picasso.with(v.getContext()).load(model.courselistImg).into(target);
            }
        } else if(downflg == 1){
            progress.setVisibility(View.VISIBLE);
            downloadText.setVisibility(View.GONE);
            downloadTextTip.setVisibility(View.GONE);
            progress.setBackgroundResource(R.mipmap.xiazaizanting);
            mActivity.mDownloadService.stopDownload(courseDownloadInfoList);
        } else if (downflg == 2){
            progress.setVisibility(View.GONE);
            downloadText.setVisibility(View.VISIBLE);
            downloadText.setTextColor(Color.parseColor("#969696"));
            downloadText.setText("正在等待...");
            downloadTextTip.setVisibility(View.VISIBLE);
            downloadTextTip.setText("点击暂停下载");
            for(CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList){
                if(courseDownloadInfo.getState() != 4){
                    courseDownloadInfo.setState(3);
                    try {
                        courseDownloadInfoDao.update(courseDownloadInfo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            mActivity.mDownloadService.addWaitDownload(courseDownloadInfoList);
        } else if (downflg == 3){
            progress.setVisibility(View.VISIBLE);
            downloadText.setVisibility(View.GONE);
            downloadTextTip.setVisibility(View.GONE);
            progress.setBackgroundResource(R.mipmap.xiazaizanting);
            mActivity.mDownloadService.stopDownload(courseDownloadInfoList);
        } else if (downflg == 5){
            progress.setVisibility(View.GONE);
            downloadText.setVisibility(View.VISIBLE);
            downloadText.setTextColor(Color.parseColor("#969696"));
            downloadText.setText("正在等待...");
            downloadTextTip.setVisibility(View.VISIBLE);
            downloadTextTip.setText("点击暂停下载");
            for(CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList){
                if(courseDownloadInfo.getState() != 4){
                    courseDownloadInfo.setState(3);
                    try {
                        courseDownloadInfoDao.update(courseDownloadInfo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            mActivity.mDownloadService.addWaitDownload(courseDownloadInfoList);
        }
    }

    public void gotoK1_2(View v,CourseItem model){
        Intent intent = new Intent(v.getContext(), MediaActivity.class);
        Bundle bundle = new Bundle();
        List<Integer> lessonIDList=new ArrayList<>();
        for(CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean : model.lessonsBeanList){
            lessonIDList.add(lessonsBean.getId());
        }
        CommonMediaOption option = new CommonMediaOption();
        option.setType(CommonMediaOption.AUTO);
        option.setId(model.id);
        option.setShowDownload(true);
        if(model.courselistDraw == 0){
            option.setShowRepeat(false);
        }
        option.setCourseName(model.title);
        option.setLessonIDList(lessonIDList);
        bundle.putSerializable("option", option);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
        mActivity.unbindService(mActivity.mDownloadServiceConnection);
    }

    private static String formatSecond(int msecsecond){
        int second = msecsecond / 1000;
        String  html="";

            Integer hours =(second/(60*60));
            Integer minutes = (second/60-hours*60);
            Integer seconds = (second-minutes*60-hours*60*60);
            if(hours>0){
                html = hours +":"+addZero(minutes)+": "+addZero(seconds);
            }else if(minutes>0){
                html = addZero(minutes)+": "+addZero(seconds);
            }else {
                html = "00: " + addZero(seconds);
            }

        return html;
    }

    private static String addZero(int time){
        if(time < 10){
            return "0" + time;
        }
        return String.valueOf(time);
    }
}


