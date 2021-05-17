package com.example.xsl.corelibrary.http;

/**
 * Created by Zhou on 2017/3/20.
 */

public interface UpLoadProgressListener {

    /**
     * 上传进度监听
     * @param percent 上传进度百分比
     * @param done 是否上传完成
     */
    void progress(float percent, boolean done, String tag);

    /**
     * 上传成功后返回
     * @param bool 是否上传成功
     * @param response 上传成功后返回字符串
     */
    void onSuccess(boolean bool, String response, String tag);

    /**
     * 上传失败
     * @param code 失败错误码
     * @param Msg 错误信息内部描述
     * @param e 异常
     */
    void onFailure(int code, String Msg, Exception e, String tag);
}
