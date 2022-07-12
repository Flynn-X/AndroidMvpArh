package com.example.xsl.corelibrary.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.example.xsl.corelibrary.utils.L;
import com.example.xsl.corelibrary.utils.NetworkUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xsl on 2017/3/20.
 * @version 1.0
 * 通过okhttp3 封装上传下载文件进度
 */
public class OkhttpClientUtils {

    //服务器异常
    public final static int SERVER_EXCEPTION_CODE = 500;
    //无网络
    public final static int NO_NETWORK_CODE = 400;
    //服务器连接异常描述
    public final static String SERVER_EXCEPTION = "服务器连接异常";
    //无网络描述
    public final static String NO_NETWORK = "网络不可用";

    private OkHttpClient okHttpClient = null;
    private Handler handler = new Handler(Looper.getMainLooper());


    private OkHttpClient getOkHttpClientInstance(Context context){
        if (okHttpClient == null) {
            //保存sesion统一
            okHttpClient = RetrofitClientUtil.getClient(context.getApplicationContext());

        }
        return okHttpClient;
    }


    /**
     * 上传文件
     * @param context 上下文
     * @param params 参数
     * @param fileKey 文件上传参数定义
     * @param file 文件
     * @param url 上传完整地址
     * @param upLoadProgressListener 上传进度监听接口
     */
    public void upLoad(Context context, HashMap<String,String> params, String fileKey,File file, String url, final UpLoadProgressListener upLoadProgressListener){
        OkHttpClient okHttpClient = getOkHttpClientInstance(context.getApplicationContext());
        MultipartBody.Builder builder= new MultipartBody.Builder().setType(MultipartBody.FORM);
        //添加其他参数要在文件之前
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                builder.addFormDataPart(entry.getKey(),  URLEncoder.encode(entry.getValue(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        builder.addFormDataPart(!TextUtils.isEmpty(fileKey)?fileKey:"file", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data;boundary=\"+BOUNDARY"), file));

        Request request = new Request.Builder()
                .tag(context.getApplicationContext())
                .url(url)
                .post(new CeleryRequestBody(builder.build()) {
                    @Override
                    public void loading(float percent, boolean done) {
                        upLoadProgressListener.progress(percent, done,file.getAbsolutePath().toString());
                    }
                })
                .build();

        L.e("request UpLoad" +  request.toString());

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                try {
                    call.cancel();
                    NetworkUtil.pingBaiDu(new NetworkUtil.pingListerner() {
                        @Override
                        public void onSuccess(boolean bool) {
                            upLoadProgressListener.onFailure(SERVER_EXCEPTION_CODE, SERVER_EXCEPTION, e,file.getAbsolutePath().toString());
                        }

                        @Override
                        public void onFailure(int code, boolean bool) {
                            upLoadProgressListener.onFailure(NO_NETWORK_CODE, NO_NETWORK, null,file.getAbsolutePath().toString());
                        }
                    }, handler);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String body = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful() && response.code() == 200) {
                                upLoadProgressListener.onSuccess(true, body,file.getAbsolutePath());
                                upLoadProgressListener.progress(100, true,file.getAbsolutePath().toString());
                            } else {
                                upLoadProgressListener.onFailure(response.code(), response.message(), null,file.getAbsolutePath().toString());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下载文件
     * @param context 上下文
     * @param url 下载地址
     * @param downLoadProgressListener 下载进度接口监听
     */
    public void downLoad(Context context, String url, final DownLoadProgressListener downLoadProgressListener) {
        final Request request = new Request.Builder()
                .tag(context.getApplicationContext())
                .url(url)
                .build();

        L.e("request DownLoad" + request.toString());

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context.getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new CeleryResponseBody(originalResponse.body()) {
                    @Override
                    public void onResponseProgress(float percent, boolean done) {
                        downLoadProgressListener.progress(percent, done,url);
                    }
                }).build();
            }
        }).connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();

        final OkHttpClient finalOkHttpClient = okHttpClient;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = finalOkHttpClient.newCall(request).execute();
                    if (response.code() == 200) {
                        long total = response.body().contentLength();
                        L.e("total------>" + total);
                        InputStream is = response.body().byteStream();
                        downLoadProgressListener.write(is,url);
                        if (is != null) {
                            is.close();
                        }
                        //进度展示在UI线程中操作
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                downLoadProgressListener.progress(100, true,url);
                            }
                        });

                    } else {
                        //下载失败展示在UI主线程操作
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                downLoadProgressListener.onFailure(response.code(), response.message(), null,url);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

}
