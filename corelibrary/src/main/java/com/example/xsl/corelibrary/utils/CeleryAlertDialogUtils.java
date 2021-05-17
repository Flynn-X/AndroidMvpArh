package com.example.xsl.corelibrary.utils;

/**
 * Created by xsl on 2017/11/4.
 */

public class CeleryAlertDialogUtils {

    /**
     * 弹出框alertDialog字体大小、颜色
     */
    public static int titleColor ,titleSize,messageColor,messageSize,buttonSize,negativeTextColor,positiveTextColor,hintTextColor,inputLineColor;
    /**
     * 弹出框alertDialog 内容是否居中（默认否）
     */
    public static boolean alertDialogCenter = false;

    public static String positiveButtonText = "确定",negativeButtonText = "取消";

    public static class Builder{

        /**
         * 设置AlertDialog 标题字体颜色
         * @param adTitleColor
         * @return
         */
        public Builder setTitleColor(int adTitleColor){
            titleColor = adTitleColor;
            return this;
        }

        /**
         * 设置AlertDialog 标题字体颜色
         * @param adTitleSize
         * @return
         */
        public Builder setTitleSize(int adTitleSize){
            titleSize = adTitleSize;
            return this;
        }

        /**
         *  设“内容”按钮字体颜色
         * @param adMessageColor
         * @return
         */
        public Builder setMessageColor(int adMessageColor){
            messageColor = adMessageColor;
            return this;
        }

        /**
         * 设“内容”按钮字体大小
         * @param adMessageSize
         * @return
         */
        public Builder setMessageSize(int adMessageSize){
            messageSize = adMessageSize;
            return this;
        }

        /**
         * 设“确定”、“取消“按钮字体大小
         * @param adButtonSize
         * @return
         */
        public Builder setButtonSize(int adButtonSize){
            buttonSize = adButtonSize;
            return this;
        }

        /**
         * 设置“取消”按钮字体颜色
         * @param adNegativeTextColor
         * @return
         */
        public Builder setNegativeTextColor(int adNegativeTextColor){
            negativeTextColor = adNegativeTextColor;
            return this;
        }

        /**
         * 设置“确定”按钮字体颜色
         * @param adPositiveTextColor
         * @return
         */
        public Builder setPositiveTextColor(int adPositiveTextColor){
            positiveTextColor = adPositiveTextColor;
            return this;
        }

        /**
         * 设置“确定”按钮文字
         * @param adPositiveButtonText
         * @return
         */
        public Builder setPositiveButtonText(String adPositiveButtonText){
            positiveButtonText = adPositiveButtonText;
            return this;
        }

        /**
         * 设置“取消”按钮文字
         * @param adNegativeButtonText
         * @return
         */
        public Builder setNegativeButtonText(String adNegativeButtonText){
            negativeButtonText = adNegativeButtonText;
            return this;
        }

        /**
         * 设置内容是否居中
         * @param adalertDialogCenter 默认false
         * @return
         */
        public Builder setAlertDialogCenter(boolean adalertDialogCenter){
            alertDialogCenter = adalertDialogCenter;
            return this;
        }

        /**
         * 设置提示问题颜色
         * @param adhintTextColor
         * @return
         */
        public Builder setHintTextColor(int adhintTextColor){
            hintTextColor = adhintTextColor;
            return this;
        }

        /**
         * 输入框下划线颜色
         * @param adinputLineColor
         * @return
         */
        public Builder setInputLineColor(int adinputLineColor){
            inputLineColor = adinputLineColor;
            return this;
        }

    }

}
