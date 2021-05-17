package com.example.xsl.corelibrary.widgets.alertdialog;

/**
 * Created by xsl on 2017/11/4.
 */

public class CeleryAlertDialogOptions {

    /**
     * 弹出框alertDialog字体大小、颜色
     */
    private int titleColor ,titleSize,messageColor,messageSize,buttonSize,negativeTextColor,positiveTextColor,hintTextColor,inputLineColor;
    /**
     * 弹出框alertDialog 内容是否居中（默认否）
     */
    private boolean alertDialogCenter = false;

    private String positiveButtonText = "确定",negativeButtonText = "取消";




    /**
     * 设置AlertDialog 标题字体颜色
     * @param adTitleColor
     * @return
     */
    public CeleryAlertDialogOptions setTitleColor(int adTitleColor){
        titleColor = adTitleColor;
        return this;
    }

    public int getTitleColor(){
        return titleColor;
    }

    /**
     * 设置AlertDialog 标题字体颜色
     * @param adTitleSize
     * @return
     */
    public CeleryAlertDialogOptions setTitleSize(int adTitleSize){
        titleSize = adTitleSize;
        return this;
    }

    public int getitleSize(){
        return titleSize;
    }

    /**
     *  设“内容”按钮字体颜色
     * @param adMessageColor
     * @return
     */
    public CeleryAlertDialogOptions setMessageColor(int adMessageColor){
        messageColor = adMessageColor;
        return this;
    }

    public int getMessageColor(){
        return messageColor;
    }

    /**
     * 设“内容”按钮字体大小
     * @param adMessageSize
     * @return
     */
    public CeleryAlertDialogOptions setMessageSize(int adMessageSize){
        messageSize = adMessageSize;
        return this;
    }

    public int getMessageSize(){
        return messageSize;
    }

    /**
     * 设“确定”、“取消“按钮字体大小
     * @param adButtonSize
     * @return
     */
    public CeleryAlertDialogOptions setButtonSize(int adButtonSize){
        buttonSize = adButtonSize;
        return this;
    }

    public int getButtonSize(){
        return buttonSize;
    }

    /**
     * 设置“取消”按钮字体颜色
     * @param adNegativeTextColor
     * @return
     */
    public CeleryAlertDialogOptions setNegativeTextColor(int adNegativeTextColor){
        negativeTextColor = adNegativeTextColor;
        return this;
    }

    public int getNegativeTextColor(){
        return negativeTextColor;
    }

    /**
     * 设置“确定”按钮字体颜色
     * @param adPositiveTextColor
     * @return
     */
    public CeleryAlertDialogOptions setPositiveTextColor(int adPositiveTextColor){
        positiveTextColor = adPositiveTextColor;
        return this;
    }

    public int getPositiveTextColor(){
        return positiveTextColor;
    }

    /**
     * 设置“确定”按钮文字
     * @param adPositiveButtonText
     * @return
     */
    public CeleryAlertDialogOptions setPositiveButtonText(String adPositiveButtonText){
        positiveButtonText = adPositiveButtonText;
        return this;
    }

    public String getPositiveButtonText(){
        return positiveButtonText;
    }

    /**
     * 设置“取消”按钮文字
     * @param adNegativeButtonText
     * @return
     */
    public CeleryAlertDialogOptions setNegativeButtonText(String adNegativeButtonText){
        negativeButtonText = adNegativeButtonText;
        return this;
    }

    public String getNegativeButtonText(){
        return negativeButtonText;
    }

    /**
     * 设置内容是否居中
     * @param adalertDialogCenter 默认false
     * @return
     */
    public CeleryAlertDialogOptions setAlertDialogCenter(boolean adalertDialogCenter){
        alertDialogCenter = adalertDialogCenter;
        return this;
    }

    public boolean getAlertDialogCenter(){
        return alertDialogCenter;
    }

    /**
     * 设置提示问题颜色
     * @param adhintTextColor
     * @return
     */
    public CeleryAlertDialogOptions setHintTextColor(int adhintTextColor){
        hintTextColor = adhintTextColor;
        return this;
    }

    public int getHintTextColor(){
        return hintTextColor;
    }

    /**
     * 输入框下划线颜色
     * @param adinputLineColor
     * @return
     */
    public CeleryAlertDialogOptions setInputLineColor(int adinputLineColor){
        inputLineColor = adinputLineColor;
        return this;
    }

    public int getInputLineColor(){
        return inputLineColor;
    }


}
