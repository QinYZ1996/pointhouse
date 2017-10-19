package com.pointhouse.chiguan.s1_2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.RoundProgressBar.RoundProgressBar;
import com.pointhouse.chiguan.common.sdcard.SDCardHelper;
import com.pointhouse.chiguan.common.swipelistview.SwipeListLayout;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.k1_2.DownloadService;
import com.pointhouse.chiguan.k1_2.DownloadStateUtil;
import com.pointhouse.chiguan.s1.WalkmanItem;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pointhouse.chiguan.R.id.downloadText;

/**
 * 随时学下载中数据填充
 * Created by Maclaine on 2017/7/26.
 */

public class WalkmanDownloadingAdapter extends BaseAdapter {
    private final static String TAG = "WalkmanDownloading";

    private Context mContext;
    private List<WalkmanItem> mList;
    private Set<SwipeListLayout> sets = new HashSet<>();
    private Integer layoutID;
    private CallBack deleteListener;
    private CourseDownloadInfoDao courseDownloadInfoDao;
    private DownloadService mDownloadService;
    private ServiceConnection mDownloadServiceConnection;
    Map<Integer, CallBack2> progressMap = new HashMap<>();

/*    WalkmanDownloadingAdapter(Context context, List<WalkmanItem> list) {
        this.mContext = context;
        this.mList = list;
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
    }*/

    public WalkmanDownloadingAdapter(Context context, List<WalkmanItem> list, int layoutID) {
        this.mContext = context;
        this.mList = list;
        this.layoutID = layoutID;
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
        mDownloadServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mDownloadService = ((DownloadService.MsgBinder) service).getService();
                mDownloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                    @Override
                    public void onNext(CourseDownloadInfo downloadInfo) {
                        try {
                            CourseDownloadInfo courseDownloadInfo = courseDownloadInfoDao.queryById(downloadInfo.getId());
                            CallBack2 callBack2 = progressMap.get(courseDownloadInfo.getLessonId());
                            if (callBack2 != null) {
                                callBack2.accept(downloadInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };
        mContext.bindService(new Intent(mContext, DownloadService.class), mDownloadServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= new ViewHolder();
        /*if (convertView == null) {
            if (layoutID == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.s1_2_item_walkmanlesson_downloading, parent, false);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
            }
            holder = new ViewHolder();
            holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
            holder.txtDownload = (TextView) convertView.findViewById(R.id.txtDownload);
            holder.rpDownload = (RoundProgressBar) convertView.findViewById(R.id.rpDownload);
            holder.viewDownload = convertView.findViewById(R.id.viewDownload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/
        //图片显示问题无法复用
        if (layoutID == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.s1_2_item_walkmanlesson_downloading, parent, false);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        }
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
        holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
        holder.txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);
        holder.txtDownload = (TextView) convertView.findViewById(R.id.txtDownload);
        holder.rpDownload = (RoundProgressBar) convertView.findViewById(R.id.rpDownload);
        holder.viewDownload = convertView.findViewById(R.id.viewDownload);
        WalkmanItem item = mList.get(position);
        if (item.getThumb() != null) {
            holder.imgThumb.setImageBitmap(item.getThumb());
        }
        holder.txtName.setText(item.getName());
        holder.rpDownload.setMax(1000);
        try {
            List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId((int) item.getId());
            int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
            switch (downflg) {
                case 1://下载中
                    int progress2 = 0;
                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                        if (courseDownloadInfo.getState() == 4) {
                            progress2 = (int) courseDownloadInfo.getProgress();
                        }
                        if (courseDownloadInfo.getState() == 2) {
                            if ((int) courseDownloadInfo.getProgress() != 0) {
                                progress2 = (int) courseDownloadInfo.getProgress();
                            }
                            break;
                        }
                    }
                    holder.rpDownload.setProgress(progress2);
                    holder.txtDownload.setVisibility(View.GONE);
                    holder.rpDownload.setVisibility(View.VISIBLE);
                    holder.rpDownload.setBackgroundResource(R.mipmap.xiazaizhong);
                    break;
                case 2://暂停中
                    holder.txtDownload.setVisibility(View.GONE);
                    holder.rpDownload.setVisibility(View.VISIBLE);
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
                    holder.rpDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                    holder.rpDownload.setProgress(progress1);
                    break;
                case 3://等待中
                    holder.rpDownload.setVisibility(View.GONE);
                    holder.txtDownload.setText(Html.fromHtml(mContext.getString(R.string.walkman_download_waiting)));
//                    holder.txtDownload.setTextColor(ContextCompat.getColor(mContext, R.color.color_969696));
                    holder.txtDownload.setVisibility(View.VISIBLE);
                    break;
                case 4://下载结束
                    //tan90°
                    Log.w(TAG, "不应检测到下载结束状态,但是检测到");
                    break;
                case 5://下载失败
                    holder.rpDownload.setVisibility(View.GONE);
                    holder.txtDownload.setText(Html.fromHtml(mContext.getString(R.string.walkman_download_fail)));
//                    holder.txtDownload.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff0000));
                    holder.txtDownload.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RxView.clicks(holder.viewDownload)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    ((WalkmanLessonActivity)mContext).permissions(() -> {
                        List<CourseDownloadInfo> courseDownloadInfoList = null;
                        try {
                            courseDownloadInfoList = courseDownloadInfoDao.queryLessonId((int) item.getId());
                            int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                            switch (downflg) {
                                case 1://下载中
                                    holder.rpDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                                    mDownloadService.stopDownload(courseDownloadInfoList);
                                    break;
                                case 2://暂停中
                                    holder.rpDownload.setVisibility(View.GONE);
                                    holder.txtDownload.setVisibility(View.VISIBLE);
//                                    holder.txtDownload.setTextColor(ContextCompat.getColor(mContext, R.color.color_969696));
                                    holder.txtDownload.setText(Html.fromHtml(mContext.getString(R.string.walkman_download_waiting)));
                                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                                        if (courseDownloadInfo.getState() != 4) {
                                            courseDownloadInfo.setState(3);
                                            try {
                                                courseDownloadInfoDao.update(courseDownloadInfo);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    mDownloadService.addWaitDownload(courseDownloadInfoList);
                                    break;
                                case 3://等待中
                                    holder.rpDownload.setVisibility(View.VISIBLE);
                                    holder.txtDownload.setVisibility(View.GONE);
                                    holder.rpDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                                    mDownloadService.stopDownload(courseDownloadInfoList);
                                    break;
                                case 4://下载结束
                                    //tan90
                                    break;
                                case 5://下载失败
                                    holder.rpDownload.setVisibility(View.GONE);
                                    holder.txtDownload.setVisibility(View.VISIBLE);
//                                    holder.txtDownload.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff0000));
                                    holder.txtDownload.setText(Html.fromHtml(mContext.getString(R.string.walkman_download_fail)));
                                    for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                                        if (courseDownloadInfo.getState() != 4) {
                                            courseDownloadInfo.setState(3);
                                            try {
                                                courseDownloadInfoDao.update(courseDownloadInfo);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    mDownloadService.addWaitDownload(courseDownloadInfoList);
                                    break;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                });
        progressMap.put((Integer) item.getId(), downloadInfo -> {
            if (holder.rpDownload != null) {
                holder.rpDownload.setProgress((int) downloadInfo.getProgress());
            }
            try {
                List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId((int) item.getId());
                int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                switch (downflg) {
                    case 1://下载中
                        holder.txtDownload.setVisibility(View.GONE);
                        holder.rpDownload.setVisibility(View.VISIBLE);
                        holder.rpDownload.setBackgroundResource(R.mipmap.xiazaizhong);
                        break;
                    case 2://暂停中
                        holder.txtDownload.setVisibility(View.GONE);
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
                        holder.rpDownload.setVisibility(View.VISIBLE);
                        holder.rpDownload.setBackgroundResource(R.mipmap.xiazaizanting);
                        holder.rpDownload.setProgress(progress1);
                        break;
                    case 3://等待中
                        holder.rpDownload.setVisibility(View.GONE);
                        holder.txtDownload.setText(Html.fromHtml(mContext.getString(R.string.walkman_download_waiting)));
//                        holder.txtDownload.setTextColor(ContextCompat.getColor(mContext, R.color.color_969696));
                        holder.txtDownload.setVisibility(View.VISIBLE);
                        break;
                    case 4://下载结束
                        // mList.remove(position);
                        ((WalkmanLessonActivity) mContext).removeDownloading(position);
                        break;
                    case 5://下载失败
                        holder.rpDownload.setVisibility(View.GONE);
                        holder.txtDownload.setText(Html.fromHtml(mContext.getString(R.string.walkman_download_fail)));
//                        holder.txtDownload.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff0000));
                        holder.txtDownload.setVisibility(View.VISIBLE);
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        final SwipeListLayout slView = (SwipeListLayout) convertView;
        slView.setOnSwipeStatusListener(new MyOnSlipStatusListener(slView));
        RxView.clicks(holder.txtDelete)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    try {
                        List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId((int) item.getId());
                        int downflg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                        switch (downflg) {
                            case 1://下载中
                            case 3://等待中
                                mDownloadService.stopDownload(courseDownloadInfoList);
                                break;
                        }
                        Observable.timer(500, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    if (courseDownloadInfoList != null) {
                                        for (CourseDownloadInfo courseDownloadInfo : courseDownloadInfoList) {
                                            SDCardHelper.removeFileFromRom(courseDownloadInfo.getFilePath() + "/" + courseDownloadInfo.getfileName());
                                        }
                                    }
                                    courseDownloadInfoDao.deleteByLessonId((Integer) item.getId());
                                    ((WalkmanLessonActivity) mContext).removeDownloading(position);
                                })
                        ;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                })
        ;
        return convertView;
    }


    static class ViewHolder {
        ImageView imgThumb;
        TextView txtName, txtDelete, txtDownload;
        RoundProgressBar rpDownload;
        View viewDownload;
    }

    public void setDeleteListener(CallBack callBack) {
        this.deleteListener = callBack;
    }

    public void onDestory() {
        mContext.unbindService(mDownloadServiceConnection);
    }

    public interface CallBack {
        public void accept(List<?> list, int position);
    }

    interface CallBack2 {
        void accept(CourseDownloadInfo downloadInfo);
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
}
