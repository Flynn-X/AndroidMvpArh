package com.example.xsl.corelibrary.widgets.alertdialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Window;

import com.example.xsl.corelibrary.R;
import com.example.xsl.corelibrary.utils.CoreLibraryRetriever;



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
 *
 * @author xsl
 * @version 2.0
 * @date 2018/5/4
 *  调整 AlertDialogOptions 获取方式从底层包初始化中设置，优化结构
 *
 */
public class CeleryAlertDialog {

    private static CeleryAlertDialogOptions dialogOptions = CoreLibraryRetriever.getDialogOption();

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
                .setTitleColor(dialogOptions.getTitleColor()>0? dialogOptions.getTitleColor():Color.parseColor("#000000"))
                .setTitleSize(dialogOptions.getitleSize()>0? dialogOptions.getitleSize():17)
                .setTitleCenter(dialogOptions.getAlertDialogCenter())
                .setMessageCenter(dialogOptions.getAlertDialogCenter())
                .setMessage(content)
                .setMessageSize(dialogOptions.getMessageSize()>0?dialogOptions.getMessageSize():15)
                .setMessageColor(dialogOptions.getMessageColor()>0?dialogOptions.getMessageColor():Color.parseColor("#999999"))
                .setNegativeTextColor(dialogOptions.getNegativeTextColor()>0?dialogOptions.getNegativeTextColor():Color.parseColor("#666666"))
                .setPositiveTextColor(dialogOptions.getPositiveTextColor()>0?dialogOptions.getPositiveTextColor():Color.parseColor("#333333"))
                .setNegativeButtonText(dialogOptions.getNegativeButtonText())
                .setPositiveButtonText(dialogOptions.getPositiveButtonText())
                .setButtonCenter(dialogOptions.getAlertDialogCenter())
                .setButtonSize(dialogOptions.getButtonSize()>0? dialogOptions.getButtonSize():14)
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
                .setInputHintTextColor(dialogOptions.getHintTextColor()>0? dialogOptions.getHintTextColor():Color.parseColor("#999999"))
                .setInputText(inputText)
                .setInputTextColor(dialogOptions.getMessageColor()>0? dialogOptions.getMessageColor():Color.parseColor("#666666"))
                .setInputTextSize(dialogOptions.getMessageSize()>0? dialogOptions.getMessageSize():15)
                .setInputType(inputType>0?inputType:InputType.TYPE_NULL)
                .setInputLineColor(dialogOptions.getInputLineColor()>0? dialogOptions.getInputLineColor():Color.parseColor("#666666"))
                .setPositiveButtonText(dialogOptions.getPositiveButtonText())
                .setNegativeButtonText(dialogOptions.getNegativeButtonText())
                .setNegativeTextColor(dialogOptions.getNegativeTextColor()>0? dialogOptions.getNegativeTextColor():Color.parseColor("#666666"))
                .setPositiveTextColor(dialogOptions.getPositiveTextColor()>0? dialogOptions.getPositiveTextColor():Color.parseColor("#333333"))
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
