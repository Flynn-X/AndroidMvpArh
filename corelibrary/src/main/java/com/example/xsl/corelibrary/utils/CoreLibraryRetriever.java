package com.example.xsl.corelibrary.utils;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.xsl.corelibrary.CrashHandler;
import com.example.xsl.corelibrary.widgets.alertdialog.CeleryAlertDialogOptions;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;




/**
 * Created by xsl on 2018/5/3.
 */

public class CoreLibraryRetriever {





    private static final CoreLibraryRetriever INSTANCE = new CoreLibraryRetriever();

    private Application AtContext;
    /**
     * 是否要打印日志
     */
    private static boolean showLog = false;
    /**
     * http基础地址
     */
    public static String baseUrl = "http://api.k780.com:88/";
    /**
     *  AlertDialog配置
     */
    private  static CeleryAlertDialogOptions celeryAlertDialogOptions;

    /**
     * 标记CeleryBaseActivity支持屏幕适配，默认不支持
     */
    public static boolean mCeleryBaseActivityAutoSize = false;



    /**
     * Retrieves and returns the RequestManagerRetriever singleton.
     */
    public static CoreLibraryRetriever get() {
        return INSTANCE;
    }


    public CoreLibraryRetriever init(Application context, boolean isDebug) {
        AtContext = context;
        //是否显示log
        showLog = isDebug;
        //arouter 顺序必须是这样
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.printStackTrace();
        ARouter.init(context); // 尽可能早，推荐在Application中初始化

        if (isDebug) {
            //日志打印初始化
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(0)         // (Optional) How many method line to show. Default 2
                    .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                    .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                    .tag("CeleryLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        }
        return this;
    }


    /**
     * 初始化retrofit基础请求地址：url
     * @param url
     */
    public CoreLibraryRetriever baseUrl(String url){
        if (!TextUtils.isEmpty(url)) {
            baseUrl = url;
        }
        return this;
    }

    /**
     * CeleryBaseActivity屏幕适配设置
     * @param celeryBaseActivityAutoSize
     * 2021-5-12 不再配置后续将删除
     * @return
     */
    @Deprecated
    public CoreLibraryRetriever celeryBaseActivityAutoSize(boolean celeryBaseActivityAutoSize){
        mCeleryBaseActivityAutoSize = celeryBaseActivityAutoSize;
        return this;
    }


    /**
     * 配置自定义dialogOptions
     * @param dialogOptions
     * @return
     */
    public CoreLibraryRetriever dialogOption(CeleryAlertDialogOptions dialogOptions){
        celeryAlertDialogOptions = dialogOptions;
        return this;
    }

//    /**
//     * 是否支持屏幕适配
//     * @param AutoSize
//     * @return
//     */
//    public CoreLibraryRetriever autosize(boolean AutoSize){
//        AutoSizeConfig.getInstance()
//                .getUnitsManager()
//                .setSupportDP(true)
//                .setSupportSP(true)
//                .setSupportSubunits(Subunits.MM);
//        return this;
//    }

    /**
     * 全局异常捕获初始化
     * @return
     */
    public CoreLibraryRetriever crash(){
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(AtContext);
        return this;
    }

    public static CeleryAlertDialogOptions getDialogOption(){
        if (celeryAlertDialogOptions == null){
            celeryAlertDialogOptions = new CeleryAlertDialogOptions();
        }
        return celeryAlertDialogOptions;
    }

    public static boolean getshowLog(){
        return showLog;
    }


}
