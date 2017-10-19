package com.pointhouse.chiguan.common.http;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpBase
{

    private static OkHttpClient client = null;

    private HttpBase() {

    }

    //获取okhttpclient单例对象
    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpBase.class) {
                if (client == null)
                    client = new OkHttpClient();
            }
        }
        return client;
    }

//    /**
//     * 从网络获取json数据,(String byte[})
//
//     * @param path
//     * @return
//     */
//    public static String getJsonByInternet(String path){
//        try {
//            URL url = new URL(path.trim());
//            //打开连接
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//            if(200 == urlConnection.getResponseCode()){
//                //得到输入流
//                InputStream is =urlConnection.getInputStream();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while(-1 != (len = is.read(buffer))){
//                    baos.write(buffer,0,len);
//                    baos.flush();
//                }
//                return baos.toString("utf-8");
//            }
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    //获取其他页面的数据
//    /**
//     * POST请求获取数据
//     */
//    public static String postDownloadJson(String path,String post){
//        URL url = null;
//        try {
//            url = new URL(path);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("POST");// 提交模式
//            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
//            // conn.setReadTimeout(2000);//读取超时 单位毫秒
//            // 发送POST请求必须设置如下两行
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//            // 发送请求参数
//            printWriter.write(post);//post的参数 xx=xx&yy=yy
//            // flush输出流的缓冲
//            printWriter.flush();
//            //开始获取数据
//            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            int len;
//            byte[] arr = new byte[1024];
//            while((len=bis.read(arr))!= -1){
//                bos.write(arr,0,len);
//                bos.flush();
//            }
//            bos.close();
//            return bos.toString("utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /**
     * Get请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }




    /**
     * Post请求发送键值对数据
     *
     * @param url
     * @param mapParams
     * @param callback
     */
    public static void doPost(String url, Map<String, String> mapParams, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            builder.add(key, mapParams.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }


    /**
     * Post请求发送JSON数据
     *
     * @param url
     * @param jsonParams
     * @param callback
     */
    public static void doPost(String url, String jsonParams, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }




}