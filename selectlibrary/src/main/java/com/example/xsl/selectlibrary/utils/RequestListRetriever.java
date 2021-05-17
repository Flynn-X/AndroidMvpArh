package com.example.xsl.selectlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.xsl.selectlibrary.aty.CelerySelectPictureActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xsl on 2018/2/28.
 * 打开列表界面所传参数配置
 */
public class RequestListRetriever {

    private static final RequestListRetriever INSTANCE = new RequestListRetriever();

    private Activity mContext;
    private Intent intent;
    private Bundle bundle;
    private int enterAnim,exitAnim;


    /**
     * Retrieves and returns the RequestManagerRetriever singleton.
     */
    protected static RequestListRetriever get() {
        return INSTANCE;
    }

    protected RequestListRetriever initAty(Activity context) {
        mContext = context;
        intent = new Intent(context,CelerySelectPictureActivity.class);
        bundle = new Bundle();
        return this;
    }



    /**
     * 设置返回按钮图标
     * @param resId
     * @return
     */
    public RequestListRetriever setBackResId(int resId){
        bundle.putInt("exResId",resId);
        return this;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public RequestListRetriever setTitle(String title){
        bundle.putString("exTittle",title);
        return this;
    }

    /**
     * 设置标题颜色
     * @param titleColor
     * @return
     */
    public RequestListRetriever setTitleColor(int titleColor){
        bundle.putInt("exTittleColor",titleColor);
        return this;
    }

    /**
     * 确认按钮文字
     * @param confirmText
     * @return
     */
    public RequestListRetriever setConfirmText(String confirmText){
        bundle.putString("exConfirmText",confirmText);
        return this;
    }

    /**
     * 确认按钮颜色
     * @param confirmTextColor
     * @return
     */
    public RequestListRetriever setConfirmTextColor(int confirmTextColor){
        bundle.putInt("exConfirmTextColor",confirmTextColor);
        return this;
    }


    /**
     * 设置可选择最大数量
     * @param limit
     * @return
     */
    public RequestListRetriever setLimit(int limit){
        bundle.putInt("limit",limit);
        return this;
    }

    /**
     * 设置已选择的图片
     * @param selectList
     * @return
     */
    public RequestListRetriever setSelectedList(List<String> selectList){
        bundle.putSerializable("data", (Serializable) selectList);
        return this;
    }

    /**
     * 设置状态栏图标颜色（白色或者黑色）
     * @param bool 默认白色
     * @return
     */
    public RequestListRetriever setStatusBarDarkText(boolean bool){
        bundle.putBoolean("isStatusBarDarkText", bool);
        return this;
    }

    /**
     * 设置底部相册选择背景颜色
     * @param tabBgColor
     * @return
     */
    public RequestListRetriever setTabBgColor(int tabBgColor){
        bundle.putInt("tabBgColor", tabBgColor);
        return this;
    }

    /**
     * 设置选择相册字体颜色
     * @param tabTextColor
     * @return
     */
    public RequestListRetriever setTabTextColor(int tabTextColor){
        bundle.putInt("tabTextColor", tabTextColor);
        return this;
    }


    /**
     * 设置转场动画
     * @param enterAnim 进入动画
     * @param exitAnim 退出动画
     * @return
     */
    public RequestListRetriever overridePendingTransition(int enterAnim, int exitAnim){
        if (enterAnim>=0) {
            this.enterAnim = enterAnim;
        }
        if (exitAnim>=0) {
            this.exitAnim = exitAnim;
        }
        return this;
    }


    public void startForResult(int requestCode){
        if (bundle != null && intent != null) {
            intent.putExtra("bundle",bundle);
            mContext.startActivityForResult(intent,requestCode);
            if (enterAnim > 0 || exitAnim > 0) {
                //设置Activity转场动画
                mContext.overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }


}
