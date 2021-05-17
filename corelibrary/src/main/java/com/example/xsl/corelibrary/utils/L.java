package com.example.xsl.corelibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.xsl.corelibrary.CoreLibrary;
import com.orhanobut.logger.Logger;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 * Log 控制器
 *
 * @@version 1.1
 * @date 2017/12/18
 * @description
 * 1、增加 Logger 打印
 * 2、增加对msg判断，防止打印log为空情况下空指针异常。
 *
 */
public class L {

    /**
     * 传统打印方法
     * @param msg
     */
    public static void v(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Log.v("CeleryLog",checkMsg(msg));
        }
    }

    public static void d(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Log.d("CeleryLog",checkMsg(msg));
        }
    }
    public static void i(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Log.i("CeleryLog",checkMsg(msg));
        }
    }

    public static void w(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Log.w("CeleryLog",checkMsg(msg));
        }
    }

    public static void e(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Log.e("CeleryLog",checkMsg(msg));
        }
    }


    /**
     * Logger 打印方法
     * @param msg
     */
    public static void vs(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.v(checkMsg(msg));
        }
    }

    public static void ds(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.d(checkMsg(msg));
        }
    }
    public static void is(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.i(checkMsg(msg));
        }
    }

    public static void ws(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.w(checkMsg(msg));
        }
    }

    public static void es(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.e(checkMsg(msg));
        }
    }

    /**
     * 打印json
     * @param msg
     */
    public static void json(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.json(checkMsg(msg));
        }
    }

    /**
     * 打印xml
     * @param msg
     */
    public static void xml(String msg){
        if (CoreLibraryRetriever.getshowLog()) {
            Logger.xml(checkMsg(msg));
        }
    }


    /**
     * 检查log信息，防止报错空指针
     * @param msg
     * @return
     */
    private static String checkMsg(String msg){
        if (TextUtils.isEmpty(msg)){
            return "log信息为空";
        }
        return msg;
    }



}
