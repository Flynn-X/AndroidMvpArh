package com.example.xsl.selectlibrary.utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by xsl on 2018/2/27.
 */
public class SelectLibrary{


    /**
     * 打开图片选择界面
     * @param activity
     * @return
     */
    public static RequestListRetriever openList(Activity activity) {
        RequestListRetriever requestListRetriever = RequestListRetriever.get();
        return requestListRetriever.initAty(activity);
    }

    /**
     * 打开浏览界面
     * @param activity
     * @return
     */
    public static RequestScanRetriever openScan(Activity activity){
        RequestScanRetriever requestScanRetriever = RequestScanRetriever.get();
        return requestScanRetriever.initAty(activity);
    }



    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }



}
