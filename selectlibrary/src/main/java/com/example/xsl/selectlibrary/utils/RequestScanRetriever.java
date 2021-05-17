package com.example.xsl.selectlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.example.xsl.selectlibrary.aty.CeleryPictrueScanActivity;

import java.util.ArrayList;

/**
 * Created by xsl on 2018/2/27.
 * 打开浏览界面所传参数配置
 */
public class RequestScanRetriever {

    private static final RequestScanRetriever INSTANCE = new RequestScanRetriever();

    private Activity mContext;
    private Intent intent;
    private Bundle bundle;


    /**
     * Retrieves and returns the RequestManagerRetriever singleton.
     */
    public static RequestScanRetriever get() {
        return INSTANCE;
    }

    protected RequestScanRetriever initAty(Activity context) {
        this.mContext = context;
        intent = new Intent(context,CeleryPictrueScanActivity.class);
        bundle = new Bundle();
        return this;
    }

    /**
     * 点击当前的view（一般为imageview也可以自定义的imageview）
     * @param view
     * @return
     */
    public RequestScanRetriever withView(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        bundle.putParcelable("bounds", rect);
        return this;
    }

    /**
     * 点击当前gridview或者recycleview位置数
     * @param position
     * @return
     */
    public RequestScanRetriever position(int position) {
        bundle.putInt("indexPosition", position);
        return this;
    }

    /**
     * 小图数组（可以没有）
     * @param thumb_list
     * @return
     */
    public RequestScanRetriever thumbImgs(ArrayList<String> thumb_list) {
        //小图数组传递
        bundle.putStringArrayList("thumb_imgs", thumb_list);
        return this;
    }

    /**
     * 大图数组（必须有）
     * @param normal_list
     * @return
     */
    public RequestScanRetriever normalImgs(ArrayList<String> normal_list) {
        //小图数组传递
        bundle.putStringArrayList("imgs", normal_list);
        return this;
    }

    /**
     * 记录当前gridview或者recyclerview列数
     * @param numColumns 列数
     * @return
     */
    public RequestScanRetriever numColumns(int numColumns) {
        //小图数组传递
        bundle.putInt("numColumns",numColumns);
        return this;
    }

    /**
     * 记录横向分割分割线宽度
     * @param hspacing
     * @return
     */
    public RequestScanRetriever horizontalSpacing(int hspacing) {
        //横向分割空间
        bundle.putInt("horizontalSpacing",hspacing);
        return this;
    }

    /**
     * 记录纵向分割分割线宽度
     * @param vspacing
     * @return
     */
    public RequestScanRetriever verticalSpacing(int vspacing) {
        //横向分割空间
        bundle.putInt("verticalSpacing",vspacing);
        return this;
    }


    public void start(){
        if (bundle != null && intent != null) {
            intent.putExtra("bundle",bundle);
            mContext.startActivity(intent);
            // 屏蔽 Activity 默认转场效果
            mContext.overridePendingTransition(0, 0);
        }
    }




}
