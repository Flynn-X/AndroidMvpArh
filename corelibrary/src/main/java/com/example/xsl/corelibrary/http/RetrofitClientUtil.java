package com.example.xsl.corelibrary.http;

import android.content.Context;
import android.util.Log;


import com.example.xsl.corelibrary.CoreLibrary;
import com.example.xsl.corelibrary.R;
import com.example.xsl.corelibrary.http.retrofiturlmanager.RetrofitUrlManager;
import com.example.xsl.corelibrary.utils.CelerySpUtils;
import com.example.xsl.corelibrary.utils.CeleryToolsUtils;
import com.example.xsl.corelibrary.utils.CoreConstants;
import com.example.xsl.corelibrary.utils.CoreLibraryRetriever;
import com.example.xsl.corelibrary.utils.L;
import com.example.xsl.corelibrary.utils.NetworkUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;

/**
 * Created by xieshuilin on 2017/2/16.
 * @version 1.0
 * @author xsl
 * @des 通过Rxjava 和 retrofit2 结合封装 http请求
 * 实现如下功能
 * 1、请求参数（get、post表单、post Json字符串）打印
 * 2、实现json数据解析
 * 3、动态添加请求头
 * 4、动态替换baseurl
 * 5、日志打印窗口实现json数据自动解析显示
 * 6、单例模式实现对象获取
 * 7、等等整合。
 */
public class RetrofitClientUtil {


    protected static Retrofit retrofit = null;
    private static OkHttpClient  client = null;


    /**
     * 初始化Retrofit
     */
    public static Retrofit getRetrofit(Context context){
        if (retrofit == null) {
            if (CeleryToolsUtils.isBaseUrl(CelerySpUtils.getString(CoreConstants.SP_BASE_URL))) {
                //获取实例
                retrofit = new Retrofit.Builder()
                        //设置OKHttpClient,如果不设置会提供一个默认的
                        .client(getClient(context))
                        //设置baseUrl
                        .baseUrl(CelerySpUtils.getString(CoreConstants.SP_BASE_URL))//post 方法
                        //添加Gson转换器
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            } else {
                //获取实例
                retrofit = new Retrofit.Builder()
                        //设置OKHttpClient,如果不设置会提供一个默认的
                        .client(getClient(context))
                        //设置baseUrl
                        .baseUrl(CoreLibraryRetriever.baseUrl)//post 方法
                        //添加Gson转换器
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }


    /**
     * log日志拦截
     */
    private static class LoggingInterceptor implements Interceptor {

        private Context mContext;
        public  LoggingInterceptor(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();

            long t1 = System.nanoTime();//请求发起的时间

//            String sendLog = String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers());

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间

            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            long bytelg = response.body().contentLength()>0?response.body().contentLength():(1024*1024);
            ResponseBody responseBody = response.peekBody(bytelg);

            String content = responseBody.string();
            String reciveLog = String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n",
                    CeleryToolsUtils.URLDecoderForUtf8(response.request().url().toString()),
                    content,
                    (t2 - t1) / 1e6d);

            /**
             * 回调拦截的body（用于全局拦截）
             */
            if (CoreLibrary.responseBodyInterceptorListener != null){
                CoreLibrary.responseBodyInterceptorListener.responseBody(response.request().url().toString(),response.headers().toString(),content);
            }

            L.d(reciveLog);
            L.d("响应时间："+((t2 - t1)/1e6d)+"");
            L.json(content);

            int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();

//            if (NetworkUtil.isNetworkAvailable(mContext)) {
//                int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
//                response.newBuilder()
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                        .build();
//            } else {
//                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//                response.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("Pragma")
//                        .build();
//            }

            return response;
        }
    }

    /**
     * 请求拦截器，修改请求header
     */
    private static class RequestInterceptor implements Interceptor{

        private Context mContext;
        CacheControl controlCache = null;
        public RequestInterceptor(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            controlCache = CacheControl.FORCE_NETWORK;
//            if (NetworkUtil.isNetworkAvailable(mContext)) {
//                controlCache = CacheControl.FORCE_NETWORK;
//            }else {
//                controlCache = CacheControl.FORCE_CACHE;
//            }

            Request.Builder builder = chain.request()
                    .newBuilder();
            builder.cacheControl(controlCache);

            //添加请求头
            Map<String, String> maps = CoreLibrary.getHeaders();
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                builder.header(entry.getKey(),entry.getValue());
            }


            Request request = builder.build();
//            L.d(URLDecoderForUtf8(request.toString()));
            //post请求打印请求参数
            if(request.method().equals("POST")){
                StringBuilder sb = new StringBuilder();
                if ((request.body() instanceof FormBody)) {
                    FormBody body = (FormBody) request.body();
                    for (int i = 0; i < body.size(); i++) {
                        sb.append( "\n" + body.encodedName(i) + "=" + CeleryToolsUtils.URLDecoderForUtf8(body.encodedValue(i)));
                    }
                    L.d("表单提交请求参数：\n"+ "{" +  sb.toString() + "\n}");
                }else if (request.body() instanceof MultipartBody){
                    MultipartBody body = (MultipartBody) request.body();
                    for (int i = 0; i < body.size(); i++) {
                        sb.append( "\n" + getParamContent(body.part(i).body()));
                    }
                    L.d("文件上传参数：\n"+ "{" +  sb.toString() + "\n}");

                } else {
                    //buffer流
                    Buffer buffer = new Buffer();
                    request.body().writeTo(buffer);
                    String oldParamsJson = buffer.readUtf8();
                    L.d("Json传递请求参数："+ CeleryToolsUtils.URLDecoderForUtf8(oldParamsJson));
                }
            }
            L.d("headers：" + request.headers().toString());
            return chain.proceed(request);
        }
    }

    /**
     * 获取常规post请求参数
     */
    private static String getParamContent(RequestBody body)  {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.readUtf8();
    }





    public static OkHttpClient getClient(Context mContext){
        if (client == null) {
            ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
            File cacheFile = new File(mContext.getApplicationContext().getCacheDir(), "celery_retrofit");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //100Mb
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                    .addInterceptor(new LoggingInterceptor(mContext.getApplicationContext()))
//                    .addInterceptor(new RequestInterceptor(mContext.getApplicationContext()))
                    .cookieJar(cookieJar)
                    .cache(cache)
                    .connectTimeout(Long.parseLong(mContext.getResources().getString(R.string.core_connect_time_out)), TimeUnit.SECONDS)
                    .readTimeout(Long.parseLong(mContext.getResources().getString(R.string.core_read_time_out)), TimeUnit.SECONDS)
                    .writeTimeout(Long.parseLong(mContext.getResources().getString(R.string.core_write_time_out)), TimeUnit.SECONDS);

            //支持动态改变baseUrl
            client = RetrofitUrlManager.getInstance().with(builder)
                    //网络拦截（这里必须用网络拦截，否则可能导致抢号登录一直token失效）
                    .addNetworkInterceptor(new RequestInterceptor(mContext.getApplicationContext()))
                    //应用拦截
                    .addInterceptor(new LoggingInterceptor(mContext.getApplicationContext()))
                    .build();
        }
        return client;
    }




    /**
     * 组装字符串数据
     * @param paramsMap
     * @return
     */
    public static RequestBody getRequestBody(HashMap<String,String> paramsMap){
        StringBuilder jsonStr = new StringBuilder("");
        for (String key : paramsMap.keySet()) {
            jsonStr.append(key).append("=").append(paramsMap.get(key)).append("&");
        }
        jsonStr.delete(jsonStr.length()-1,jsonStr.length());
        System.out.print(jsonStr.toString());
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr.toString());
        return body;
    }


}
