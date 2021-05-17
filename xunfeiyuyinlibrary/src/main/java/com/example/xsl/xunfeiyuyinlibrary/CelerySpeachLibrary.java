package com.example.xsl.xunfeiyuyinlibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/18
 * @description
 */
public class CelerySpeachLibrary {

    private static String TAG = "XFYunLibrary";
    private static Context cxt;
    // 语音合成对象
    public static SpeechSynthesizer mTts = null;


    /**
     * 讯飞语音初始化
     * // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
     */
    public static void init(Context context){
        if (mTts == null) {
            SpeechUtility.createUtility(context.getApplicationContext(), "appid=" + context.getString(R.string.xfyun_app_id));
            cxt = context.getApplicationContext();
            // 初始化合成对象
            mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        }
    }

    /**
     * 讯飞语音初始化
     * // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
     */
    public static void init(Context context,String appid){
        if (mTts == null) {
            SpeechUtility.createUtility(context.getApplicationContext(), "appid=" + appid);
            cxt = context.getApplicationContext();
            // 初始化合成对象
            mTts = SpeechSynthesizer.createSynthesizer(context.getApplicationContext(), mTtsInitListener);
        }
    }




    /**
     * 讯飞语音释放
     */
    public static void destroy(){
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }


    /**
     * 初始化监听。
     */
    private static InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(cxt,"语音合成初始化失败,错误码："+code ,Toast.LENGTH_SHORT).show();
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

}