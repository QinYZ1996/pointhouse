package com.pointhouse.chiguan.k1_2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pointhouse.chiguan.Application.GlobalApplication;
import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.RoundProgressBar.RoundProgressBar;
import com.pointhouse.chiguan.common.base.HttpActivityBase;
import com.pointhouse.chiguan.common.jsonObject.CourseDetailGetBean;
import com.pointhouse.chiguan.common.layout.BackBtnViewLayout;
import com.pointhouse.chiguan.common.layout.MyScrollView;
import com.pointhouse.chiguan.common.mydialog.MyDialog;
import com.pointhouse.chiguan.common.util.Constant;
import com.pointhouse.chiguan.common.util.DensityUtil;
import com.pointhouse.chiguan.common.util.IntentUriUtil;
import com.pointhouse.chiguan.common.util.ToolUtil;
import com.pointhouse.chiguan.db.CourseDownloadInfo;
import com.pointhouse.chiguan.db.CourseDownloadInfoDao;
import com.pointhouse.chiguan.db.PayInfo;
import com.pointhouse.chiguan.db.PayInfoDao;
import com.pointhouse.chiguan.k1_10.CourseAuditionActivity;
import com.pointhouse.chiguan.k1_4.PayActivity;
import com.pointhouse.chiguan.k1_8.TeacherDetailActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static com.pointhouse.chiguan.common.util.SharedPreferencesUtil.readToken;

/**
 * Created by ljj on 2017/7/14.
 */

public class CourseDetailsActivity  extends HttpActivityBase implements View.OnClickListener,AdapterView.OnItemClickListener, MyScrollView.OnScrollListener{

    TextView courseIntroduction;
    CourseDetailGetBean courseDetailGetBean;
    View courseIntroductionLine;
    TextView allCourses;
    CourseDetailsAdapter adapter;
    View allCoursesLine;
    BackBtnViewLayout backBtnViewLayout;
    ListView viewCourseList;
    public MyScrollView scrollView;
    public DownloadService mDownloadService;
    private CourseDownloadInfoDao courseDownloadInfoDao;
    public Map<Integer,RoundProgressBar> progressBarList = new HashMap<Integer,RoundProgressBar>();
    public Map<Integer,TextView> progressTxtList = new HashMap<Integer,TextView>();
    public Map<Integer,TextView> progressTxtTipList = new HashMap<Integer,TextView>();
    List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanList;
    private boolean isFavorite = false;
    String id;
    private PayInfoDao payInfoDao;
    public int isPaid;

    protected ServiceConnection mDownloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadService = ((DownloadService.MsgBinder) service).getService();
            mDownloadService.setOnProgressListener(mProgressListener);
        }
    };

    /**
     * 回调接口的实现类
     */
    private DownloadService.OnProgressListener mProgressListener =
            new DownloadService.OnProgressListener() {

                @Override
                public void onNext(CourseDownloadInfo downloadInfo) {
                      RoundProgressBar progressBar = progressBarList.get(downloadInfo.getLessonId());
                     if(progressBar != null){
                         int flg = -1;
                         try {
                             List<CourseDownloadInfo> courseDownloadInfoList = courseDownloadInfoDao.queryLessonId(downloadInfo.getLessonId());
                             flg = DownloadStateUtil.getDownloadState(courseDownloadInfoList);
                         } catch (SQLException e) {
                             e.printStackTrace();
                         }
                            progressBar.setProgress((int)downloadInfo.getProgress());
                            if(flg == 4){
                                android.util.Log.d("k1_2_","onComplete");
                                progressBar.setProgress(1000);
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setBackgroundResource(R.mipmap.xiazaiwanbi);
                                TextView progressTxt = progressTxtList.get(downloadInfo.getLessonId());
                                progressTxt.setVisibility(View.GONE);
                                TextView progressTxtTip =  progressTxtTipList.get(downloadInfo.getLessonId());
                                progressTxtTip.setVisibility(View.GONE);
                            } else if (flg == 1) {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setBackgroundResource(R.mipmap.xiazaizhong);
                                TextView progressTxt = progressTxtList.get(downloadInfo.getLessonId());
                                progressTxt.setVisibility(View.GONE);
                                TextView progressTxtTip =  progressTxtTipList.get(downloadInfo.getLessonId());
                                progressTxtTip.setVisibility(View.GONE);
                            } else if (flg == 5){
                                progressBar.setVisibility(View.GONE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView progressTxt = progressTxtList.get(downloadInfo.getLessonId());
                                        progressTxt.setVisibility(View.VISIBLE);
                                        //progressTxt.setText(downloadInfo.getErrorMessage());
                                        progressTxt.setText("下载失败...");
                                        progressTxt.setTextColor(Color.RED);
                                        TextView progressTxtTip =  progressTxtTipList.get(downloadInfo.getLessonId());
                                        progressTxtTip.setVisibility(View.VISIBLE);
                                        progressTxtTip.setText("点击重新下载");
                                    }});
                            }
                  }
                  }
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.k1_2_activity_coursedetails);
        courseDownloadInfoDao = new CourseDownloadInfoDao(GlobalApplication.sContext);
        payInfoDao = new PayInfoDao(GlobalApplication.sContext);
        viewCourseList = (ListView) findViewById(R.id.viewCourseList);
        viewCourseList.setFocusable(false);
        adapter = new CourseDetailsAdapter(this, viewCourseList);
        viewCourseList.setAdapter(adapter);
        viewCourseList.setOnItemClickListener(this);
        backBtnViewLayout = (BackBtnViewLayout) findViewById(R.id.BackBtnViewLayout);
        backBtnViewLayout.setTitleClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //当前页面销毁
                finish();
                //创建一个事件
                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK);
                //设置activity监听此事件，返回true只监听一次，如果返回false则继续监听
                onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
            }
        });
        scrollView = (MyScrollView) findViewById(R.id.ScrollView);
        scrollView.setOnScrollListener(this);
        //试听区域
        RelativeLayout audition = (RelativeLayout)findViewById(R.id.RelativeLayout05);
        audition.setVisibility(View.GONE);
        //购买区域
        RelativeLayout purchaseLayout = (RelativeLayout)findViewById(R.id.PurchaseTextRL);
        purchaseLayout.setVisibility(View.GONE);

        Button backbtn = (Button)findViewById(R.id.findBackBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //当前页面销毁
                finish();
                //创建一个事件
                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK);
                //设置activity监听此事件，返回true只监听一次，如果返回false则继续监听
                onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
            }
        });

        TextView titleText = (TextView)findViewById(R.id.title_course) ;
        titleText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //当前页面销毁
                finish();
                //创建一个事件
                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK);
                //设置activity监听此事件，返回true只监听一次，如果返回false则继续监听
                onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);
            }
        });
        bindViews();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume(){
        backBtnViewLayout.setAlpha((float)((scrollView.getScrollY()-150)*0.01));
        Log.d("k1_2","onResume");
        Intent it = getIntent();
        Uri uri = it.getData();
        if(uri != null){
            id = uri.getQueryParameter("id");
        }
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(id);

        //绑定下载服务
        bindService(new Intent(this, DownloadService.class),
                mDownloadServiceConnection,
                Context.BIND_AUTO_CREATE);
        getAsynHttp(Constant.URL_BASE +"courseDetail", jSONArray.toString() ,readToken(this),1);
        super.onResume();
    }

    private void showLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.common_dialog, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tvTitle.setText("请加入课程互动群");
        final TextView btnYes = (TextView) view.findViewById(R.id.f_quchecbutton_btn_queding);
        btnYes.setText("稍后");
        final TextView btlNo = (TextView) view.findViewById(R.id.f_quchecbutton_btn_quxiao);
        btlNo.setText("加入");
        final MyDialog builder = new MyDialog(this,0,0,view,R.style.DialogTheme);

        builder.setCancelable(false);
        builder.show();
        //设置对话框显示的View
        //点击确定是的监听
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });


        btlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(courseDetailGetBean.getGroup().getTid());
                getAsynHttp(Constant.URL_BASE +"groupApply", jSONArray.toString() ,readToken(view.getContext()),4);
            }
        });

    }

    @Override
    protected void onDestroy() {
        unbindService(mDownloadServiceConnection);
        super.onDestroy();
    }


    @Override
    public void getAsynHttpResponse(Call call, String json, int flg,int error) {
        RelativeLayout collectionRl = (RelativeLayout)findViewById(R.id.Collection);
        if(error == -1) {
            return;
        }
        JSONObject jSONObject;
        if(flg == 1)
        {
            courseDetailGetBean = new CourseDetailGetBean(json);
            if(courseDetailGetBean.gethasError()) return;
            reload(json);
        }
        else if(flg == 2)
        {
            // TODO: 2017/7/27  未收藏点完后
            JSONTokener jsonParser = new JSONTokener(json);
            try {
                jSONObject = (JSONObject) jsonParser.nextValue();
                TextView collectionText = (TextView)findViewById(R.id.CollectionText);
                if(jSONObject.getInt("resultCode") == 1){
                    //TODO: 2017/7/27
                    isFavorite = true;
                    if(isFavorite){
                        collectionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,11);
                        collectionText.setText("已收藏");
                        collectionRl.setBackgroundResource(R.mipmap.yishoucang);
                        Toast.makeText(getApplicationContext(), "收藏成功" , Toast.LENGTH_SHORT).show();
                    } else {
                        collectionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
                        collectionText.setText("收藏");
                        collectionRl.setBackgroundResource(R.mipmap.shoucang);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(flg == 3)
        {
            // TODO: 2017/7/27  已收藏点完后
            JSONTokener jsonParser = new JSONTokener(json);
            try {
                jSONObject = (JSONObject) jsonParser.nextValue();
                TextView collectionText = (TextView)findViewById(R.id.CollectionText);
                if(jSONObject.getInt("resultCode") == 1){
                    //TODO: 2017/7/27
                    isFavorite = false;
                    if(isFavorite){
                        collectionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,11);
                        collectionText.setText("已收藏");
                        collectionRl.setBackgroundResource(R.mipmap.yishoucang);
                    } else {
                        collectionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
                        collectionText.setText("收藏");
                        collectionRl.setBackgroundResource(R.mipmap.shoucang);
                        Toast.makeText(getApplicationContext(), "取消收藏成功" , Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void postAsynHttpResponse(Call call, String json, int flg) {

    }


    private void bindViews() {

        courseIntroduction = (TextView) findViewById(R.id.CourseIntroduction);
        courseIntroductionLine = (View) findViewById(R.id.CourseIntroductionLine);
        allCourses = (TextView) findViewById(R.id.AllCourses);
        allCoursesLine = (View) findViewById(R.id.AllCoursesLine);

        courseIntroduction.setOnClickListener(this);
        allCourses.setOnClickListener(this);

        setSelected(true);
    }

    //重置所有文本的选中状态
    private void setSelected(boolean first) {
        courseIntroduction.setSelected(first);
        courseIntroductionLine.setSelected(first);
        allCourses.setSelected(!first);
        allCoursesLine.setSelected(!first);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CourseIntroduction:
                setSelected(true);
                findViewById(R.id.RelativeLayout06).setVisibility(View.VISIBLE);
                findViewById(R.id.RelativeLayout07).setVisibility(View.GONE);
                break;
            case R.id.AllCourses:
                setSelected(false);
                findViewById(R.id.RelativeLayout06).setVisibility(View.GONE);
                findViewById(R.id.RelativeLayout07).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void reload(String json){
        isPaid = courseDetailGetBean.getIsPaid();
        isFavorite = courseDetailGetBean.getIsFavorite() == 1;
        RelativeLayout toteacherImg = (RelativeLayout)findViewById(R.id.RelativeLayout03);

        toteacherImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TeacherDetailActivity.class);
                intent.putExtra("id", courseDetailGetBean.getCourseBean().getTid());
                v.getContext().startActivity(intent);
            }
        });

        TextView titleText = (TextView)findViewById(R.id.title_course) ;
        titleText.setText(courseDetailGetBean.getCourseBean().getTitle());

        TextView collectionText = (TextView)findViewById(R.id.CollectionText);
        RelativeLayout collectionRl = (RelativeLayout)findViewById(R.id.Collection);
        if(isFavorite){
            collectionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,11);
            collectionText.setText("已收藏");
            collectionRl.setBackgroundResource(R.mipmap.yishoucang);
        } else {
            collectionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
            collectionText.setText("收藏");
            collectionRl.setBackgroundResource(R.mipmap.shoucang);
        }


        TextView classification_course = (TextView)findViewById(R.id.classification_course);
        classification_course.setText(courseDetailGetBean.getCourseBean().getCategoryTitle());

        TextView price = (TextView)findViewById(R.id.Price);
        TextView membershipFree = (TextView)findViewById(R.id.MembershipFree);
        if(courseDetailGetBean.getCourseBean().getType() == 0){
            price.setText("免费");
            price.setTextColor(Color.parseColor("#3c5fc5"));
        }else if(courseDetailGetBean.getCourseBean().getType() == 1){
            price.setText("¥"+ ToolUtil.doubleToString(courseDetailGetBean.getCourseBean().getPrice()));
        }else if(courseDetailGetBean.getCourseBean().getType() == 2){
            price.setText("¥"+ ToolUtil.doubleToString(courseDetailGetBean.getCourseBean().getPrice()));
            membershipFree.setText("(会员免费)");
        }

        boolean hasfree = false;
        //是否有试听
        lessonsBeanList = courseDetailGetBean.getLessonsBeanList();

        for (CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean: lessonsBeanList){
            if(lessonsBean.getType() == 0) continue;
            hasfree = true;
        }

        //试听区域
        RelativeLayout audition = (RelativeLayout)findViewById(R.id.RelativeLayout05);

        //购买区域
        RelativeLayout purchaseLayout = (RelativeLayout)findViewById(R.id.PurchaseTextRL);
        boolean isCourseFree = false;
        if(courseDetailGetBean.getCourseBean().getType() == 0){
            isCourseFree = true;
        }

        if(readToken(this) == null || readToken(this).equals("")){
            if(hasfree) {
                audition.setVisibility(View.VISIBLE);
                audition.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // TODO: 2017/7/27 跳登录
                        startActivity(IntentUriUtil.getIntent("l1","k1_2"+","+id));
                        finish();
                    }
                });
            }
            if(!isCourseFree){
                purchaseLayout.setVisibility(View.VISIBLE);
                purchaseLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // TODO: 2017/7/27 跳登录
                        startActivity(IntentUriUtil.getIntent("l1","k1_2"+","+id));
                        finish();
                        return;
                    }
                });
            }
        } else {
            if(courseDetailGetBean.getIsPaid() == 0){
                if(!isCourseFree) {
                    purchaseLayout.setVisibility(View.VISIBLE);
                    purchaseLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                PayInfo payInfo = payInfoDao.queryCourseId(1, courseDetailGetBean.getCourseBean().getId());
                                if(payInfo != null) {
                                    payInfoDao.delete(payInfo);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(v.getContext(), PayActivity.class);
                            intent.putExtra("type", 1);
                            intent.putExtra("courseId", courseDetailGetBean.getCourseBean().getId());
                            intent.putExtra("courseName", courseDetailGetBean.getCourseBean().getTitle());
                            intent.putExtra("price", courseDetailGetBean.getCourseBean().getPrice());
                            v.getContext().startActivity(intent);
                            unbindService(mDownloadServiceConnection);
                        }
                    });
                }
                if(hasfree){
                    audition.setVisibility(View.VISIBLE);
                    audition.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), CourseAuditionActivity.class);
                            List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanList = courseDetailGetBean.getLessonsBeanList();
                            for(CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean :lessonsBeanList){
                                if(lessonsBean.getType() == 1){
                                    intent.putExtra("lessonId", lessonsBean.getId());
                                }
                            }
                            v.getContext().startActivity(intent);
                            unbindService(mDownloadServiceConnection);
                        }
                    });
                }
            } else if(courseDetailGetBean.getIsPaid() == 1){
                purchaseLayout.setVisibility(View.GONE);
                audition.setVisibility(View.GONE);
            }
        }

        WebView courseIntroductionText = (WebView)findViewById(R.id.CourseIntroductionText);
        String abouts = courseDetailGetBean.getCourseBean().getAbouts();
        if(abouts != null && !abouts.isEmpty()){
            courseIntroductionText.getSettings().setDefaultTextEncodingName("UTF -8");
            courseIntroductionText.loadData(abouts.replace("<img", "<img style=\"width:100%;\""),"text/html; charset=UTF-8", null);
        }else {
            courseIntroductionText.loadData(getString(R.string.media_nodata), "text/html; charset=UTF-8", null);
        }

        courseIntroductionText.setFocusable(false);

        ImageView courselistDraw1 = (ImageView) findViewById(R.id.course_icon1);
        ImageView courselistDraw2 = (ImageView) findViewById(R.id.course_icon2);
        ImageView courselistDraw3 = (ImageView) findViewById(R.id.course_icon3);

        int lessonType = courseDetailGetBean.getCourseBean().getLessonType();
        if(lessonType == 0){
            courselistDraw1.setBackgroundResource(R.mipmap.wenzhang);
        } else if (lessonType == 1) {
            courselistDraw1.setBackgroundResource(R.mipmap.yinpin);
        }else if(lessonType == 2){
            courselistDraw1.setBackgroundResource(R.mipmap.shipin);
        }else if (lessonType == 3){
            courselistDraw1.setBackgroundResource(R.mipmap.yinpin);
            courselistDraw2.setBackgroundResource(R.mipmap.wenzhang);
        }else if (lessonType == 4){
            courselistDraw1.setBackgroundResource(R.mipmap.shipin);
            courselistDraw2.setBackgroundResource(R.mipmap.wenzhang);
        }else if (lessonType == 5){
            courselistDraw1.setBackgroundResource(R.mipmap.shipin);
            courselistDraw2.setBackgroundResource(R.mipmap.yinpin);
        }else if (lessonType == 6){
            courselistDraw1.setBackgroundResource(R.mipmap.shipin);
            courselistDraw2.setBackgroundResource(R.mipmap.yinpin);
            courselistDraw3.setBackgroundResource(R.mipmap.wenzhang);
        }

        // 设置背景
        ImageView iv = (ImageView)findViewById(R.id.background_img);
        iv.setBackgroundResource(R.drawable.kv_default);
        if(courseDetailGetBean.getCourseBean().getThumb() != null && !courseDetailGetBean.getCourseBean().getThumb().isEmpty()){
            Picasso.with(getApplicationContext()).load(courseDetailGetBean.getCourseBean().getThumb()).placeholder(R.drawable.kv_default).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    int defalt = 100;
                    int start = height <= width ? width : height;
                    while (start > 1000){
                        start = start / 2;
                        defalt = defalt / 2;
                    }
                    final ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, defalt, os);
                    os.toByteArray();

                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);

                    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                    int iVWidth = dm.widthPixels;
                    int iVHeight = DensityUtil.dip2px(getApplicationContext(), 110);
                    int bitmapWidth = bitmap2.getWidth();
                    int bitmapHeight = bitmap2.getHeight();

                    float WidthHeight = iVWidth / iVHeight;
                    float bitmapHeightf = bitmapWidth / WidthHeight;

                    if (bitmapHeightf > bitmapHeight){
                        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmapWidth, bitmapHeight);
                        Drawable bitmapDraw = new BitmapDrawable(getResources(), resizedBitmap);
                        iv.setImageDrawable(bitmapDraw);
                    } else {
                        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap2, 0, (bitmapHeight - (int)bitmapHeightf)/2, bitmapWidth, (int)bitmapHeightf);
                        Drawable bitmapDraw = new BitmapDrawable(getResources(), resizedBitmap);
                        iv.setImageDrawable(bitmapDraw);
                    }
                    iv.setBackgroundResource(0);
                    // ==================special code for resize bitmap===========END.
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.kv_default);
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int iVWidth = dm.widthPixels;
            int iVHeight = DensityUtil.dip2px(getApplicationContext(), 110);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            float WidthHeight = iVWidth / iVHeight;
            float bitmapHeightf = bitmapWidth / WidthHeight;

            if (bitmapHeightf > bitmapHeight){
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight);
                Drawable bitmapDraw = new BitmapDrawable(getResources(), resizedBitmap);
                iv.setImageDrawable(bitmapDraw);
            } else {
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, (bitmapHeight - (int)bitmapHeightf)/2, bitmapWidth, (int)bitmapHeightf);
                Drawable bitmapDraw = new BitmapDrawable(getResources(), resizedBitmap);
                iv.setImageDrawable(bitmapDraw);
            }
            iv.setBackgroundResource(0);
        }

        //设定头像
        CircleImageView headportrait = (CircleImageView)findViewById(R.id.Headportrait);
        if(courseDetailGetBean.getTeacherBean().getAvatar() != null && !courseDetailGetBean.getTeacherBean().getAvatar().isEmpty()){
            Picasso.with(this).load(courseDetailGetBean.getTeacherBean().getAvatar()).placeholder(R.drawable.icon2_default).into(headportrait);
        }else {
            Picasso.with(this).load(R.drawable.icon2_default).into(headportrait);
        }

        TextView teacher =  (TextView)findViewById(R.id.Teachername);
        teacher.setText(courseDetailGetBean.getTeacherBean().getNickname());

        RelativeLayout collection = (RelativeLayout)findViewById(R.id.Collection);
        collection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONArray jSONArray = new JSONArray();
                jSONArray.put("1").put(String.valueOf(courseDetailGetBean.getCourseBean().getId()));
                if(!isFavorite){
                    if(readToken(v.getContext()) == null || readToken(v.getContext()).equals("")){
                        startActivity(IntentUriUtil.getIntent("l1","k1_2"+","+id));
                        finish();
                        return;
                    }

                    // TODO: 2017/7/27  未收藏
                    getAsynHttp(Constant.URL_BASE +"favorite", jSONArray.toString(),readToken(v.getContext()),2);
                } else {
                    if(readToken(v.getContext()) == null || readToken(v.getContext()).equals("")){
                        startActivity(IntentUriUtil.getIntent("l1","k1_2"+","+id));
                        finish();
                        return;
                    }

                    // TODO: 2017/7/27  已收藏
                    getAsynHttp(Constant.URL_BASE +"favoriteDel", jSONArray.toString(),readToken(v.getContext()),3);
                }
            }
        });

        adapter.clean();
        loadDate(json);
        if(adapter.getCount() == 0){
            viewCourseList.setVisibility(View.GONE);
            LinearLayout viewCourseNodata = (LinearLayout)findViewById(R.id.viewCourseNodata);
            viewCourseNodata.setVisibility(View.VISIBLE);
        } else {
            LinearLayout viewCourseNodata = (LinearLayout)findViewById(R.id.viewCourseNodata);
            viewCourseNodata.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

        try {
            if(!id.isEmpty()) {
                Log.d("start",new Date() +"");
                PayInfo payInfo = payInfoDao.queryCourseId(1, Integer.valueOf(id));
                Log.d("start1",new Date() +"");
                if (payInfo != null && payInfo.getIsBuy() && !payInfo.getIsPrompt()) {
                    payInfo.setIsPrompt(true);
                    payInfoDao.update(payInfo);
                    if (courseDetailGetBean.getGroup() != null) {
                        showLoginDialog();
                    }
                }
                Log.d("start2",new Date() +"");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDate(String json) {
        List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanList = courseDetailGetBean.getLessonsBeanList();

        List<CourseDetailGetBean.ResultObjectBean.LessonsBean> lessonsBeanListNotFree = new ArrayList<>();


        for(CourseDetailGetBean.ResultObjectBean.LessonsBean lessonsBean : lessonsBeanList){
            int learnTime = 0;

            lessonsBeanListNotFree.add(lessonsBean);
            List<CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean> audioList = lessonsBean.getAudioList();
            for(CourseDetailGetBean.ResultObjectBean.LessonsBean.VideoListBean video : lessonsBean.getVideoList()){
                CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean audio = new CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean();
                audio.setOriginal(video.getOriginal());
                audio.setOrigUrl(video.getOrigUrl());
                audio.setVid(video.getVid());
                audio.setVideoName(video.getVideoName());
                audio.setDurationMsec(video.getDurationMsec());
                audio.setInitialSize(video.getInitialSize());
                audioList.add(audio);
            }
            for(CourseDetailGetBean.ResultObjectBean.LessonsBean.AudioListBean audioListBean : audioList){
                learnTime += audioListBean.getDurationMsec();
            }

            adapter.addCourse(lessonsBean.getId(),courseDetailGetBean.getCourseBean().getThumb(),lessonsBean.getTitle(),lessonsBean.getLessonType(),
                    learnTime,lessonsBean.getCourseId(),audioList,json,courseDetailGetBean.getCourseBean().getThumb(),lessonsBeanListNotFree,
                    courseDetailGetBean.getCourseBean().getTitle());
        }

        ViewGroup.LayoutParams layoutParams = viewCourseList.getLayoutParams();
        int count = lessonsBeanList.size();
        float height = 81f * count;
        layoutParams.height = DensityUtil.dip2px(getApplicationContext(), height);
        viewCourseList.setLayoutParams(layoutParams);
    }

    public void finishActivity(){
        if(readToken(this) == null || readToken(this).equals("")) {
            startActivity(IntentUriUtil.getIntent("l1","k1_2"+","+id));
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void checkLessonDetail(int lessonId,View v,CourseDetailsAdapter.CourseItem model){
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(String.valueOf(lessonId));
        setPara(v,model);
        //getAsynHttp(Constant.URL_BASE +"lessonDetail", jSONArray.toString() ,readToken(this),4);
        adapter.gotoK1_2(adapterv,adaptermodel);
    }

    private View adapterv;
    private CourseDetailsAdapter.CourseItem adaptermodel;
    private void setPara(View v,CourseDetailsAdapter.CourseItem model){
        adapterv = null;
        adaptermodel = null;
        adapterv = v;
        adaptermodel = model;
    }

    public void checkLessonDetail2(int lessonId, View v, CourseDetailsAdapter.CourseItem model, RoundProgressBar progress, TextView downloadText,TextView downloadTextTip){
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(String.valueOf(lessonId));
        setPara2(v,model,progress,downloadText,downloadTextTip);
        //getAsynHttp(Constant.URL_BASE +"lessonDetail", jSONArray.toString() ,readToken(this),5);
        adapter.downLoadFile(adapterv2,adaptermodel2,adapterprogress2,adapterdownloadText2,downloadTextTip2);
    }

    private View adapterv2;
    private CourseDetailsAdapter.CourseItem adaptermodel2;
    private RoundProgressBar adapterprogress2;
    private TextView adapterdownloadText2;
    private TextView downloadTextTip2;
    private void setPara2(View v, CourseDetailsAdapter.CourseItem model, RoundProgressBar progress, TextView downloadText, TextView downloadTextTip){
        adapterv2 = null;
        adaptermodel2 = null;
        adapterprogress2 = null;
        adapterdownloadText2 = null;
        downloadTextTip2 = null;
        adapterv2 = v;
        adaptermodel2 = model;
        adapterprogress2 = progress;
        adapterdownloadText2 = downloadText;
        downloadTextTip2 = downloadTextTip;
    }

    @Override
    public void onScroll(int scrollY) {
        backBtnViewLayout.setAlpha((float)((scrollY-150)*0.01));
    }
}
