package com.example.xsl.corelibrary.notify;


import android.content.Context;


/**
 * 封装的通知栏组件
 */
public class NotifyComponent {


    /**
     * 通知
     * @param context
     * @param showTop 通知是否从顶部弹出
     * @return
     */
    public static NotifyRetriever with(Context context, boolean showTop) {
        NotifyRetriever retriever = NotifyRetriever.get();
        return retriever.init(context,showTop);
    }

}
