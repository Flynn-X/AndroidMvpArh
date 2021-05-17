package com.example.xsl.corelibrary.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.List;

/**
 * 共享文件适配
 * 支持Android 7.0、8.0+ 以上系统
 */
public class FileProvider7 {

    /**
     * 获取 uri 适配android N+
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file){
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            fileUri = getUriForFile24(context,file);
        }else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * android N+ 获取Uri方法
     * @param context
     * @param file
     * @return
     */
    private static Uri getUriForFile24(Context context,File file){
        Uri uri = android.support.v4.content.FileProvider.getUriForFile(context
                ,context.getPackageName()+".provider",file);
        return uri;
    }


    /**
     * 适配android 7.0、8.0+ setDataAndType
     * @param context
     * @param intent
     * @param type
     * @param file
     * @param writeAble
     */
    public static void setIntentDataType(Context context, Intent intent,String type,File file,boolean writeAble){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.setDataAndType(getUriForFile(context,file),type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble){
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }else {
            intent.setDataAndType(Uri.fromFile(file),type);
        }
    }

    /**
     * 获取安装未知应用权限
     * @param context
     * @param intent
     * @param uri
     * @param writeAble
     */
    public static void grantPermissions(Context context,Intent intent,Uri uri,boolean writeAble){
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble){
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent
                , PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo:resolveInfoList){
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName,uri,flag);
        }
    }

}
