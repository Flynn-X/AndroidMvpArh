package com.example.xsl.corelibrary.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by xsl on 2016/7/2.
 * @author Celery
 */
public abstract class CeleryResponseBody extends ResponseBody {

    //实际的待包装响应体
    private final ResponseBody responseBody;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    /**
     * 构造函数，赋值
     * @param responseBody 待包装的响应体
     */
    public CeleryResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }


    /**
     * 重写调用实际的响应体的contentType
     * @return MediaType
     */
    @Override public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * 重写进行包装source
     * @return BufferedSource
     * @throws IOException 异常
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));

        }
        return bufferedSource;
    }



    /**
     * 读取，回调进度接口
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            long lastTime = 0;
            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                /**
                 *  根据系统版本不同设置不同的进度读取精度，防止卡顿内存溢出
                 */
                long currentTimeMillis = System.currentTimeMillis();
                /**
                 * 5.0以下系统每秒读取1次
                 */
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
                    if (currentTimeMillis-lastTime>=1000) {
                        lastTime = currentTimeMillis;
                        Message message = handler.obtainMessage();
                        message.what = 200;
                        message.arg1 = (int) bytesRead;
                        message.obj = totalBytesRead;
                        message.sendToTarget();
                    }
                }

                /**
                 * 5.0-7.0每秒读取2次
                 */
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT<= Build.VERSION_CODES.N){
                    if (currentTimeMillis-lastTime >= 500) {
                        lastTime = currentTimeMillis;
                        Message message = handler.obtainMessage();
                        message.what = 200;
                        message.arg1 = (int) bytesRead;
                        message.obj = totalBytesRead;
                        message.sendToTarget();
                    }
                }

                /**
                 * 7.0+ 以上系统每秒读取4次
                 */
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N ){
                    if (currentTimeMillis-lastTime >= 250) {
                        lastTime = currentTimeMillis;
                        Message message = handler.obtainMessage();
                        message.what = 200;
                        message.arg1 = (int) bytesRead;
                        message.obj = totalBytesRead;
                        message.sendToTarget();
                    }
                }

//                //回调，如果contentLength()不知道长度，会返回-1
//               onResponseProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }

    /**
     * handler 用于切换到UI线程，实现回调方法里可操作UI控件效果
     */
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    float percent = (((float) (long) msg.obj / responseBody.contentLength()) * 100);
                    //回调，如果contentLength()不知道长度，会返回-1
                    onResponseProgress(percent, msg.arg1 == -1);
                    break;
            }
        }
    };

    public abstract void onResponseProgress(float percent, boolean done);


}
