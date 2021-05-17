package com.example.xsl.corelibrary.http;

import android.os.Build;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by xsl on 2016/7/1.
 * @author  Celery
 */
public abstract class CeleryRequestBody extends RequestBody {

    private final RequestBody requestBody;
    private BufferedSink bufferedSink;

    public CeleryRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
        bufferedSink.close();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long current,total,lastTime;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (total == 0) {
                    total = contentLength();
                }
                current += byteCount;


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
                        message.obj = current;
                        if (total == current){
                            message.arg1 = 1;
                        }else {
                            message.arg1 = 0;
                        }
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
                        message.obj = current;
                        if (total == current){
                            message.arg1 = 1;
                        }else {
                            message.arg1 = 0;
                        }
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
                        message.obj = current;
                        if (total == current){
                            message.arg1 = 1;
                        }else {
                            message.arg1 = 0;
                        }
                        message.sendToTarget();
                    }
                }



            }
        };
    }

    /**
     * handler 用于切换到UI线程，实现回调方法里可操作UI控件效果
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    try {
                        float percent = (((float) (long) msg.obj / contentLength()) * 100);
                        loading(percent, msg.arg1==1 ? true:false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    public abstract void loading(float percent, boolean done);
}
