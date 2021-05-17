package com.example.xsl.corelibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 * toast控制器
 */
public class CeleryToast {

    private static Toast toast;

    /**
     * short Toast
     * @param context 上下文对象
     * @param content 提示内容
     */
    public static void showShort(Context context,String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * long Toast
     * @param context 上下文对象
     * @param content 提示内容
     */
    public static void showLong(Context context,String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
