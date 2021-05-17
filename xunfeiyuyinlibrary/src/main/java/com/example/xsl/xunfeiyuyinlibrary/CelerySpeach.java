package com.example.xsl.xunfeiyuyinlibrary;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/18
 * @description 讯飞文字转语音
 */
public class CelerySpeach {

    private static SpeechSynthesizer mTts = CelerySpeachLibrary.mTts;
    // 引擎类型
    private static String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人:小琪
    private static String voicer = "xiaoqi";

    private static CelerySpeachListener xfYunListener;


    /**
     * 文字转语音播放
     * @param context 上下文对象
     * @param tag 标记tag
     * @param content 播放所需文字
     * @param yunListener 播放监听
     */
    public static void startSpeach(Context context, String tag, String content,CelerySpeachListener yunListener){
        if (mTts == null){
            return ;
        }
        xfYunListener = yunListener;
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(context.getApplicationContext(), tag);
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(content, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                //未安装则跳转到提示安装页面
//                mInstaller.install();
            }else {
                Toast.makeText(context.getApplicationContext(),"语音合成失败,错误码: " + code,Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 增加动态改变语音播报人
     * @param context 上下文对象
     * @param tag tag标记
     * @param dfvoicer 播报人
     * @param content 播报内容
     * @param yunListener 播报监听
     */
    public static void startSpeach(Context context, String tag,String dfvoicer, String content,CelerySpeachListener yunListener){
        if (mTts == null){
            return ;
        }
        if (!TextUtils.isEmpty(dfvoicer)){
            voicer = dfvoicer;
        }
        xfYunListener = yunListener;
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(context.getApplicationContext(), tag);
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(content, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                //未安装则跳转到提示安装页面
//                mInstaller.install();
            }else {
                Toast.makeText(context.getApplicationContext(),"语音合成失败,错误码: " + code,Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 参数设置
     * @param
     * @return
     */
    private static void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH,"50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    /**
     * 合成回调监听。
     */
    private static SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
            if (xfYunListener != null) {
                xfYunListener.onSpeakBegin();
            }
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
            if (xfYunListener != null) {
                xfYunListener.onSpeakPaused();
            }
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
            if (xfYunListener != null) {
                xfYunListener.onSpeakResumed();
            }
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
          if (xfYunListener != null) {
              xfYunListener.onBufferProgress(percent, beginPos, endPos, info);
          }
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
            if (xfYunListener != null) {
                xfYunListener.onSpeakProgress(percent, beginPos, endPos);
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
//            if (error == null) {
//                showTip("播放完成");
//            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
//            }

            if (xfYunListener != null && error == null) {
                xfYunListener.onCompleted();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 取消合成
     */
    public static void stopSpeaking(){
        if (mTts != null){
            mTts.stopSpeaking();
        }

    }

    /**
     * 暂停播放
     */
    public static void pauseSpeaking(){
        if (mTts != null){
            mTts.pauseSpeaking();
        }
    }

    /**
     * 继续播放
     */
    public static void resumeSpeaking(){
        if (mTts != null){
            mTts.resumeSpeaking();
        }
    }


    /**
     * 获取所有的语音播报人
     * @return
     */
    public static Map<String,String> getAllSpeachPerson(){
        Map<String,String> map = new HashMap<>();
        map.put("小燕—女青、中英、普通话","xiaoyan");
        map.put("小宇—男青、中英、普通话","xiaoyu");
        map.put("凯瑟琳—女青、英","catherine");
        map.put("亨利—男青、英","henry");
        map.put("玛丽—女青、英","vimary");
        map.put("小研—女青、中英、普通话","vixy");
        map.put("小琪—女青、中英、普通话","xiaoqi");
        map.put("小峰—男青、中英、普通话","vixf");
        map.put("小梅—女青、中英、粤语","xiaomei");
        map.put("小莉—女青、中英、台湾普通话","xiaolin");
        map.put("小蓉—女青、中、四川话","xiaorong");
        map.put("小芸—女青、中、东北话","xiaoqian");
        map.put("小坤—男青、中、河南话","xiaokun");
        map.put("小强—男青、中、湖南话","xiaoqiang");
        map.put("小莹—女青、中、陕西话","vixying");
        map.put("小新—男童、中、普通话","xiaoxin");
        map.put("楠楠—女童、中、普通话","nannan");
        map.put("老孙—男老、中、普通话","vils");
        return map;
    }


}