package com.example.xsl.corelibrary.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;



import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifyRetriever {

    private static NotifyRetriever INSTANCE = new NotifyRetriever();
    private Context context;
    private NotificationManager manger;
    private NotificationCompat.Builder builder;
    private NotificationChannel channel;
    //true 为每一个通知都会是独立的，false为通一类通知覆盖消息，比如同一个人的聊天对话
    private boolean notifyTag;

    public static final int TYPE_Normal = 1;
    public static final int TYPE_Progress = 2;
    public static final int TYPE_BigText = 3;
    public static final int TYPE_Inbox = 4;
    public static final int TYPE_BigPicture = 5;
    public static final int TYPE_Hangup = 6;
    public static final int TYPE_Media = 7;
    public static final int TYPE_Customer = 8;


//    android.support.v4.media.app.NotificationCompat.MediaStyle mediaStyle
//            = new android.support.v4.media.app.NotificationCompat.MediaStyle();

    protected static NotifyRetriever get() {
        return INSTANCE;
    }

    /**
     * 初始化
     * @param context
     * @param showTop 是否在状态栏顶部弹出（需要清单文件里配置权限）
     *                <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
     * @return
     */
    protected NotifyRetriever init(Context context, boolean showTop) {
        notifyTag = false;
        this.context = context;
        manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "";
            if (showTop){
                channelId = "channel_2";
                channel = new NotificationChannel(channelId, "重要消息", NotificationManager.IMPORTANCE_HIGH);
            }else {
                channelId = "channel_1";
                channel = new NotificationChannel(channelId, "普通消息", NotificationManager.IMPORTANCE_DEFAULT);
            }
            manger.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context,channelId);
        }else {
            if (showTop) {
                builder = new NotificationCompat.Builder(context,"channel_2");
                builder.setPriority(Notification.PRIORITY_HIGH);
            }else {
                builder = new NotificationCompat.Builder(context,"channel_1");
                builder.setPriority(Notification.PRIORITY_DEFAULT);
            }
        }
        return this;
    }


    /**
     * 状态栏显示的提示
     * @param ticker
     * @return
     */
    public NotifyRetriever setTicker(String ticker){
        //Ticker是状态栏显示的提示
        builder.setTicker(ticker);
        return this;
    }


    public NotifyRetriever setContentTitle(String contentTitle){
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle(contentTitle);
        return this;
    }

    public NotifyRetriever setContentText(String contentText){
        //第二行内容 通常是通知正文
        builder.setContentText(contentText);
        return this;
    }

    public NotifyRetriever setSmallIcon(int smallIcon){
        builder.setSmallIcon(smallIcon);
        return this;
    }

    public NotifyRetriever setLargeIcon(int largeIcon){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),largeIcon,options));
        return this;
    }

    public NotifyRetriever setIntent(Intent intent){
        PendingIntent pIntent = PendingIntent.getActivity(context,1,intent,0);
        builder.setContentIntent(pIntent);
        return this;
    }

    public NotifyRetriever setNotifyTag(boolean bool){
        if (bool) {
            notifyTag = true;
        }else {
            notifyTag = false;
        }
        return this;
    }

    /**
     * 简单通知
     * @param subText 第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
     */
    public void showSimpleNotity(String subText){
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        if (!TextUtils.isEmpty(subText)) {
            builder.setSubText(subText);
        }
        builder.setAutoCancel(true);
        builder.setNumber(2);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        if (notifyTag) {
            manger.notify(System.currentTimeMillis() + "", TYPE_Normal, notification);
        }else {
            manger.notify(TYPE_Normal, notification);
        }
    }

    /**
     * BigTextStyle
     * @param bigText 正文内容
     * @param bigContentTitle 点击后的标题
     * @param summaryText 末尾只一行的文字内容
     */
    public void showBigTextStyleNotity(String bigText, String bigContentTitle, String summaryText){
        android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
        if (!TextUtils.isEmpty(bigText)) {
            style.bigText(bigText);
        }
        //点击后的标题
        if (!TextUtils.isEmpty(bigContentTitle)) {
            style.setBigContentTitle(bigContentTitle);
        }
        //末尾只一行的文字内容
        if (!TextUtils.isEmpty(summaryText)) {
            style.setSummaryText(summaryText);
        }
        builder.setStyle(style);
        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        if (notifyTag) {
            manger.notify(System.currentTimeMillis() + "", TYPE_BigText, notification);
        }else {
            manger.notify(TYPE_BigText, notification);
        }
    }

    /**
     * 折叠文字通知栏
     * @param bigContentTitle
     * @param summaryText
     * @param strings 最多显示五行 再多会有截断
     */
    public void showinBoxStyleNotity(String bigContentTitle, String summaryText, String...strings){
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app.NotificationCompat.InboxStyle();
        if (!TextUtils.isEmpty(bigContentTitle)) {
            style.setBigContentTitle(bigContentTitle);
        }
        if (!TextUtils.isEmpty(summaryText)) {
            style.setSummaryText(summaryText);
        }
        for (int i=0;i<strings.length;i++){
            style.addLine(strings[i]);
        }
        builder.setStyle(style);
        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        if (notifyTag) {
            manger.notify(System.currentTimeMillis() + "", TYPE_Inbox, notification);
        }else {
            manger.notify(TYPE_Inbox, notification);
        }
    }

    /**
     * 带图片通知栏显示
     * @param bigContentTitle
     * @param summaryText
     * @param bitmap 图片 BitmapFactory.decodeResource(getResources(),R.drawable.small)
     */
    public void showBigPictureStyleNotity(String bigContentTitle, String summaryText, Bitmap bitmap){
        android.support.v4.app.NotificationCompat.BigPictureStyle style = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        if (!TextUtils.isEmpty(bigContentTitle)) {
            style.setBigContentTitle(bigContentTitle);
        }
        if (!TextUtils.isEmpty(summaryText)) {
            style.setSummaryText(summaryText);
        }
        if (bitmap != null) {
            style.bigPicture(bitmap);
        }
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        if (notifyTag) {
            manger.notify(System.currentTimeMillis() + "", TYPE_BigPicture, notification);
        }else {
            manger.notify(TYPE_BigPicture, notification);
        }
    }



}
