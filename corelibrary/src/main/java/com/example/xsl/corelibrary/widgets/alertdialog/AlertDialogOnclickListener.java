package com.example.xsl.corelibrary.widgets.alertdialog;

/**
 * Created by xsl on 2017/3/16.
 * AlertDialog 回调接口定义
 */
public interface AlertDialogOnclickListener {
    void positiveClick(int which, String content);
    void negativeClick(int which, String content);
}
