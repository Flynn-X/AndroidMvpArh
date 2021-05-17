package com.example.sharelibrary;

/**
 * Created by xsl on 2017/12/21.
 */

public class ShareItem {

   private CeleryShareUtils.SHARE_CHANNEL shareChannel;
   private String name;
   private int resId;


    public CeleryShareUtils.SHARE_CHANNEL getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(CeleryShareUtils.SHARE_CHANNEL shareChannel) {
        this.shareChannel = shareChannel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
