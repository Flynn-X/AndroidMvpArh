package com.example.xsl.corelibrary.http;

import java.io.InputStream;

/**
 * Created by Zhou on 2017/3/20.
 */

public interface DownLoadProgressListener {
    /**
     * 下载进度监听
     * @param percent 下载进度百分比
     * @param done 是否下载到100%（下载完成）
     */
    void progress(float percent, boolean done, String tag);

    /**
     * 数据流，这里操作数据流写入文件过程
     * @param inputStream 数据流
     */
    void write(InputStream inputStream, String tag);

    /**
     * 下载失败
     * @param code 错误码
     * @param Msg 错误描述
     * @param e 异常
     */
    void onFailure(int code, String Msg, Exception e, String tag);
}
