package com.example.xsl.corelibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Window;
import com.example.xsl.corelibrary.R;
import com.example.xsl.corelibrary.mvp.presenter.AlertDialogOnclickListener;
import com.necer.ndialog.NDialog;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 * AlertDialog 对话框通用4.0到最新Android版本，dz 5.0设计样式）
 *
 * @author xsl
 * @version 2.0
 * @date 2017/11/4
 * 增加了弹出动画
 * 调整弹出框，使原来弹出框可以设置字体大小等动作
 */
public class CeleryAlertDialog {

    /**
     * 弹出提示对话框
     * @param context 上下文对象
     * @param tittle 标题
     * @param content 提示内容
     * @param alertDialogOnclickListener 接口监听
     */
    public static void show(Context context,String tittle, String content, final AlertDialogOnclickListener alertDialogOnclickListener) {
        NDialog  nDialog = new NDialog(context);
        nDialog.setTitle(tittle)
                .setTitleColor(CeleryAlertDialogUtils.titleColor>0?CeleryAlertDialogUtils.titleColor:Color.parseColor("#000000"))
                .setTitleSize(CeleryAlertDialogUtils.titleSize>0?CeleryAlertDialogUtils.titleSize:17)
                .setTitleCenter(CeleryAlertDialogUtils.alertDialogCenter)
                .setMessageCenter(CeleryAlertDialogUtils.alertDialogCenter)
                .setMessage(content)
                .setMessageSize(CeleryAlertDialogUtils.messageSize>0?CeleryAlertDialogUtils.messageSize:15)
                .setMessageColor(CeleryAlertDialogUtils.messageColor>0?CeleryAlertDialogUtils.messageColor:Color.parseColor("#999999"))
                .setNegativeTextColor(CeleryAlertDialogUtils.negativeTextColor>0?CeleryAlertDialogUtils.negativeTextColor:Color.parseColor("#666666"))
                .setPositiveTextColor(CeleryAlertDialogUtils.positiveTextColor>0?CeleryAlertDialogUtils.positiveTextColor:Color.parseColor("#333333"))
                .setNegativeButtonText(CeleryAlertDialogUtils.negativeButtonText)
                .setPositiveButtonText(CeleryAlertDialogUtils.positiveButtonText)
                .setButtonCenter(CeleryAlertDialogUtils.alertDialogCenter)
                .setButtonSize(CeleryAlertDialogUtils.buttonSize>0?CeleryAlertDialogUtils.buttonSize:14)
                .setCancleable(false)//设置点击外面不可取消
                .setOnConfirmListener(new NDialog.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        //which,0代表NegativeButton，1代表PositiveButton
                        if (which == 0) {
                            alertDialogOnclickListener.negativeClick(which,"");
                        }
                        if (which == 1) {
                            alertDialogOnclickListener.positiveClick(which,"");
                        }
                    }
                });
        AlertDialog alertDialog = nDialog.create(NDialog.CONFIRM);
        alertDialog.show();
        //设置弹出动画
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.celeryalertdialog_style);

    }

    /**
     * 弹出输入对话框
     * @param context 上下文对象
     * @param tittle 标题
     * @param hint 提示语句
     * @param inputText 历史输入内容
     * @param inputType 输入限制
     *  例子：InputType.TYPE_CLASS_TEXT (输入文字)
     * @param alertDialogOnclickListener
     */
    public static void showEdit(Context context,String tittle, String hint,String inputText,int inputType, final AlertDialogOnclickListener alertDialogOnclickListener) {
        NDialog  nDialog = new NDialog(context);
        nDialog.setTitle(tittle)
                .setInputHintText(hint)
                .setInputHintTextColor(CeleryAlertDialogUtils.hintTextColor>0?CeleryAlertDialogUtils.hintTextColor:Color.parseColor("#999999"))
                .setInputText(inputText)
                .setInputTextColor(CeleryAlertDialogUtils.messageColor>0?CeleryAlertDialogUtils.messageColor:Color.parseColor("#666666"))
                .setInputTextSize(CeleryAlertDialogUtils.messageSize>0?CeleryAlertDialogUtils.messageSize:15)
                .setInputType(inputType>0?inputType:InputType.TYPE_NULL)
                .setInputLineColor(CeleryAlertDialogUtils.inputLineColor>0?CeleryAlertDialogUtils.inputLineColor:Color.parseColor("#666666"))
                .setPositiveButtonText(CeleryAlertDialogUtils.positiveButtonText)
                .setNegativeButtonText(CeleryAlertDialogUtils.negativeButtonText)
                .setNegativeTextColor(CeleryAlertDialogUtils.negativeTextColor>0?CeleryAlertDialogUtils.negativeTextColor:Color.parseColor("#666666"))
                .setPositiveTextColor(CeleryAlertDialogUtils.positiveTextColor>0?CeleryAlertDialogUtils.positiveTextColor:Color.parseColor("#333333"))
                .setCancleable(true)
                .setOnInputListener(new NDialog.OnInputListener() {
                    @Override
                    public void onClick(String inputText, int which) {
                        //which,0代表NegativeButton，1代表PositiveButton
                        if (which == 0) {
                            alertDialogOnclickListener.negativeClick(which,inputText);
                        }
                        if (which == 1) {
                            alertDialogOnclickListener.positiveClick(which,inputText);
                        }
                    }
                });
        AlertDialog alertDialog = nDialog.create(NDialog.INPUT);
        alertDialog.show();
        //设置弹出动画
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.celeryalertdialog_style);
    }



}
