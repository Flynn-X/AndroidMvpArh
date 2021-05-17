package com.example.xsl.corelibrary.notify;

import java.io.Serializable;

public class ProgressInfo implements Serializable {

    private float percent;//进度百分比
    private String url;//下载地址
    private String filePath;//文件保存的绝对路径

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
