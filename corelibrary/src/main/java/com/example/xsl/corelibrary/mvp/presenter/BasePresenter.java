package com.example.xsl.corelibrary.mvp.presenter;


import android.content.Context;

/**
 * Created by xsl on 2017/3/15.
 * 基础Presenter接口方法定义基础方法
 */
public interface BasePresenter {

    /*********************************************【提示通用方法 Start】******************************/

    /**
     * 弹出提示对话框
     * @param tittle 标题
     * @param content 提示内容
     * @param alertDialogOnclickListener 接口监听
     */
    void showAlertDialog(String tittle, String content, AlertDialogOnclickListener alertDialogOnclickListener);

    /**
     * 弹出输入对话框
     * @param tittle 标题
     * @param hint 提示语句
     * @param inputText 历史输入内容
     * @param inputType 输入限制
     *  例子：InputType.TYPE_CLASS_TEXT (输入文字)
     * @param alertDialogOnclickListener
     */
    void showAlertDialogEdit(String tittle, String hint,String inputText,int inputType, final AlertDialogOnclickListener alertDialogOnclickListener);

    /**
     * 弹出提示信息
     * @param content 提示内容
     */
    void showToast(String content);



    /**
     * 显示进度不带提示
     */
    void showLoadingDialog();

    /**
     * 显示进度不带提示（过时）
     * @param context
     */
    @Deprecated
    void showLoadingDialog(Context context);


    /**
     * 带提示的进度
     * @param hint
     */
    void showLoadingDialog(String hint);
    /**
     * 带提示的进度（过时）
     * @param context
     * @param hint
     */
    @Deprecated
    void showLoadingDialog(Context context,String hint);

    /**
     * 取消进度显示
     */
    void dismissLoadingDialog();

    /*********************************************【提示通用方法 End】******************************/



}
