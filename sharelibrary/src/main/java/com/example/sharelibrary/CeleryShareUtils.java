package com.example.sharelibrary;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xsl on 2017/11/7.
 * 分享第三方登录初始化配置
 */
public class CeleryShareUtils {

    /**
     * 分享可配置渠道
     */
    public enum SHARE_CHANNEL{
        QQ,
        QZONE,
        WEIXIN,
        WEIXIN_CIRCLE,
        SINA
    }

    /**
     * 已配置渠道列表
     */
    public static List<ShareItem> share_list = new ArrayList<>();

    public static class Build{

        /**
         * 初始化
         * @param isDebug 是否打开调试
         * @return
         */
        public Build init(Application ctx,String appKey,boolean isDebug){

            if (isDebug) {
                Config.DEBUG = true;
            }else {
                Config.DEBUG = false;
            }
            //授权登录调试
            UMShareConfig config = new UMShareConfig();
            config.isNeedAuthOnGetUserInfo(true);
            config.isOpenShareEditActivity(true);
            config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
            config.setFacebookAuthType(UMShareConfig.AUTH_TYPE_SSO);
            config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);
            UMShareAPI.get(ctx).setShareConfig(config);

            /**
             * 动态改变已存在的友盟appkey
             */
            try {
                ApplicationInfo appi = ctx.getPackageManager().getApplicationInfo(
                        ctx.getPackageName(), PackageManager.GET_META_DATA);
                appi.metaData.putString("UMENG_APPKEY", appKey);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }

            return this;
        }

        /**
         * 配置qq
         * @param id
         * @param key
         * @return
         */
        public Build qqConfig(String id,String key){
            PlatformConfig.setQQZone(id,key);
            return this;
        }

        /**
         * 配置微信
         * @param id
         * @param secret
         * @return
         */
        public Build wxConfig(String id,String secret){
            PlatformConfig.setWeixin(id,secret);
            return this;
        }

        /**
         * 配置微博
         * @param key
         * @param secret
         * @param redirectUrl
         * @return
         */
        public Build weiboConfig(String key, String secret, String redirectUrl){
            PlatformConfig.setSinaWeibo(key,secret,redirectUrl);
            return this;
        }


        /**
         * 配置可分享渠道（必须配置）
         * 渠道配置的顺序决定显示的顺序
         * @param list 渠道名称
         * @return
         */
        public Build shareChannel(SHARE_CHANNEL...list){
          for (int i=0;i<list.length;i++){
              ShareItem shareItem = new ShareItem();
              switch (list[i]){
                  case QQ:
                      shareItem.setShareChannel(list[i]);
                      shareItem.setName("QQ");
                      shareItem.setResId(R.drawable.qq_share);
                      share_list.add(shareItem);
                      break;
                  case QZONE:
                      shareItem.setShareChannel(list[i]);
                      shareItem.setName("QQ空间");
                      shareItem.setResId(R.drawable.qzone_share);
                      share_list.add(shareItem);
                      break;
                  case WEIXIN:
                      shareItem.setShareChannel(list[i]);
                      shareItem.setName("微信");
                      shareItem.setResId(R.drawable.wechat_share);
                      share_list.add(shareItem);
                      break;
                  case WEIXIN_CIRCLE:
                      shareItem.setShareChannel(list[i]);
                      shareItem.setName("微信朋友圈");
                      shareItem.setResId(R.drawable.wechat_friend_share);
                      share_list.add(shareItem);
                      break;
                  case SINA:
                      shareItem.setShareChannel(list[i]);
                      shareItem.setName("微博");
                      shareItem.setResId(R.drawable.weibo_share);
                      share_list.add(shareItem);
                      break;
                  default:
                      break;
              }
          }
            return this;
        }

    }
}
