package com.example.xsl.corelibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.example.xsl.corelibrary.notify.DownloadService;

import java.io.File;



/**
 * apk 自动更新安装工具类
 */
public class ApkUtils {

    /**
     * apk下载
     * @param context
     * @param newVersion 当前服务器最新版本
     * @param url
     */
    public static void downLoad(Context context, String newVersion,String url){
        String archiveFilePath = CeleryToolsUtils.getSystemFilePath(context, Environment.DIRECTORY_DOWNLOADS) + File.separator + CeleryToolsUtils.getAppName(context) + ".apk";
        if (hasLocalLasterApk(context,newVersion)){
            //跳转安装app
            CeleryToolsUtils.installApk(context,new File(archiveFilePath));
        }else {
            //说明本地没有现成的安装包，启动下载
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(CoreConstants.DOWNLOAD_URL,url);
            intent.putExtra(CoreConstants.DOWNLOAD_SAVE_FOlDER,CeleryToolsUtils.getSystemFilePath(context, Environment.DIRECTORY_DOWNLOADS));
            intent.putExtra(CoreConstants.DOWNLOAD_SAVE_FILE_NAME,CeleryToolsUtils.getAppName(context)+".apk");
            intent.putExtra(CoreConstants.DOWNLOAD_NOTITY,true);
            context.startService(intent);
        }
    }

    /**
     * 检查本地是否有最新版apk安装包
     * @param context
     * @param newVersion 服务器最新版本
     * @return
     */
    public static boolean hasLocalLasterApk(Context context, String newVersion){
        PackageManager packageManager = context.getPackageManager();
        String archiveFilePath = CeleryToolsUtils.getSystemFilePath(context, Environment.DIRECTORY_DOWNLOADS) + File.separator + CeleryToolsUtils.getAppName(context) + ".apk";
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(archiveFilePath , PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            try {
                int index = CeleryToolsUtils.compareVersion(packageInfo.versionName,newVersion);
                if (index>=0){
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }






}
