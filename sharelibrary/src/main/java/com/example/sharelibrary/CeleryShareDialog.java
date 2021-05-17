package com.example.sharelibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sharelibrary.CeleryShareUtils.SHARE_CHANNEL;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

/**
 * Created by xsl on 2017/11/7.
 * @version 1.1
 * @author xsl
 * @date 2017/12/21
 * @des
 * 友盟分享通用弹出框
 * 关于动态权限适配，请在调用当前·app前做
 * 特别注意：
 * 1、在分享的 onActivityResult 加上  UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);才能走回调
 * 2、 <data android:scheme="tencent1106228145"  要和初始化中  .qqConfig("1106228145","gW03DyVxrOThC7Yt") 中appid相同分享成功后才能回调。
 */
public class CeleryShareDialog {

    private static Context context;

    /**
     * 在activity中onActivityResult方法中回调
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(Activity activity,int requestCode ,int resultCode,Intent data){
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 分享dialog弹窗、
     * @param activity
     * @param title 标题
     * @param url 分享链接
     * @param description 描述
     * @param imageUrl 分享缩略图url
     */
    public static void show(Activity activity,String title,String url,String description,String imageUrl,UMShareListener umShareListener){
        UMImage thumb =  new UMImage(activity,imageUrl);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述

//        new ShareAction(activity)
//                .withText(title)
//                .withMedia(web)
//                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
//                .setCallback(umShareListener)
//                .open();

        showDialog(activity,title,web,umShareListener);

    }

    /**
     * 分享dialog弹窗
     * @param activity
     * @param title 标题
     * @param url 分享链接
     * @param description 描述
     * @param resId 分享缩略图R.mipmap.image 地址
     */
    public static void show(Activity activity,String title,String url,String description,int resId,UMShareListener umShareListener){
        context = activity;
        UMImage thumb =  new UMImage(activity,resId);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述

        showDialog(activity,title,web,umShareListener);
    }

    /**
     * 分享dialog弹窗
     * @param activity
     * @param title 标题
     * @param url 分享链接
     * @param description 描述
     * @param bitmap  缩略图
     */
    public static void show(Activity activity,String title,String url,String description,Bitmap bitmap,UMShareListener umShareListener){
        context = activity;
        UMImage thumb =  new UMImage(activity,bitmap);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述

        showDialog(activity,title,web,umShareListener);
    }

    /**
     * 分享dialog弹窗
     * @param activity
     * @param title 标题
     * @param url 分享链接
     * @param description 描述
     * @param file  缩略图
     */
    public static void show(Activity activity,String title,String url,String description,File file,UMShareListener umShareListener){
        context = activity;
        UMImage thumb =  new UMImage(activity,file);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述

        showDialog(activity,title,web,umShareListener);
    }

    /**
     * 分享dialog弹窗
     * @param activity
     * @param title 标题
     * @param url 分享链接
     * @param description 描述
     * @param bytes  缩略图
     */
    public static void show(Activity activity,String title,String url,String description,byte[] bytes,UMShareListener umShareListener){
        context = activity;
        UMImage thumb =  new UMImage(activity,bytes);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述

        showDialog(activity,title,web,umShareListener);
    }


    /**
     * 分享（自定义面板）
     * @param context actvity
     * @param title 分享标题
     * @param web 分享内容
     * @param umShareListener 回调监听
     */
    private static void showDialog(final Activity context, final String title, final UMWeb web, final UMShareListener umShareListener){
        View dialogView = LayoutInflater.from(context).inflate(R.layout.celery_share_dialog, null);
        GridView gridView = (GridView) dialogView.findViewById(R.id.gridView);
        TextView textView = (TextView) dialogView.findViewById(R.id.textView);
        final Dialog dialog = new Dialog(context,R.style.bottomDialog);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.mystyle);
        //适配器
        gridView.setAdapter(new ShareAdapter(context,CeleryShareUtils.share_list));
        //点击分享事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SHARE_CHANNEL shareChannel = CeleryShareUtils.share_list.get(position).getShareChannel();
                switch (shareChannel){
                    case QQ:
                        new ShareAction(context)
                                .setPlatform(SHARE_MEDIA.QQ)//传入平台
                                .withText(title)//分享内容
                                .withMedia(web)
                                .setCallback(umShareListener)//回调监听器
                                .share();
                        break;
                    case QZONE:
                        new ShareAction(context)
                                .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                                .withText(title)//分享内容
                                .withMedia(web)
                                .setCallback(umShareListener)//回调监听器
                                .share();
                        break;
                    case WEIXIN:
                        new ShareAction(context)
                                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                                .withText(title)//分享内容
                                .withMedia(web)
                                .setCallback(umShareListener)//回调监听器
                                .share();
                        break;
                    case WEIXIN_CIRCLE:
                        new ShareAction(context)
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                                .withText(title)//分享内容
                                .withMedia(web)
                                .setCallback(umShareListener)//回调监听器
                                .share();
                        break;
                    case SINA:
                        new ShareAction(context)
                                .setPlatform(SHARE_MEDIA.SINA)//传入平台
                                .withText(title)//分享内容
                                .withMedia(web)
                                .setCallback(umShareListener)//回调监听器
                                .share();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        //点击取消分享
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
