package com.example.xsl.xunfeiyuyinlibrary;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/18
 * @description
 */
public interface CelerySpeachListener {
    void onSpeakBegin();
    void onSpeakPaused();
    void onSpeakResumed();
    void onBufferProgress(int percent, int beginPos, int endPos, String info);
    void onSpeakProgress(int percent, int beginPos, int endPos);
    void onCompleted();
}
