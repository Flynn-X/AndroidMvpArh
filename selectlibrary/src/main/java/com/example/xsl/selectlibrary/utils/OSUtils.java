package com.example.xsl.selectlibrary.utils;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xsl on 2017/10/31.
 * @version 1.0
 * @des 沉浸栏适配解决方案
 * 在corelibrary 1.1.0.5 中增加此功能
 *
 * 使用方法：OSUtils.setStatusBarDarkText(this,true,false);
 *
 */
public class OSUtils {

    //MIUI标识
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    //EMUI标识
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    //Flyme标识
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    private static final String KEY_FLYME_ID_FALG_VALUE_KEYWORD = "Flyme";
    private static final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
    private static final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
    private static final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";

    /**
     * @param
     * @return ROM_TYPE ROM类型的枚举
     * @description获取ROM类型: MIUI_ROM, FLYME_ROM, EMUI_ROM, OTHER_ROM
     */

    public static ROM_TYPE getRomType() {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;
        try {
            BuildProperties buildProperties = BuildProperties.getInstance();

            if (buildProperties.containsKey(KEY_EMUI_VERSION_CODE) || buildProperties.containsKey(KEY_EMUI_API_LEVEL) || buildProperties.containsKey(KEY_MIUI_INTERNAL_STORAGE)) {
                return ROM_TYPE.EMUI;
            }
            if (buildProperties.containsKey(KEY_MIUI_VERSION_CODE) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME)) {
                return ROM_TYPE.MIUI;
            }
            if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) || buildProperties.containsKey(KEY_FLYME_SETUP_FALG) || buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                return ROM_TYPE.FLYME;
            }
            if (buildProperties.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                String romName = buildProperties.getProperty(KEY_FLYME_ID_FALG_KEY);
                if (!TextUtils.isEmpty(romName) && romName.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD)) {
                    return ROM_TYPE.FLYME;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rom_type;
    }

    public enum ROM_TYPE {
        MIUI,
        FLYME,
        EMUI,
        OTHER
    }


    /**
     * 动态修改状态栏文字颜色
     * @param activity
     * @param dark 是否设置成黑色
     * @param fullScreen 是否全屏不需要状态栏，true布局延伸到状态栏
     */
    public static void setStatusBarDarkText(Activity activity, boolean dark,boolean fullScreen){
        /**
         * 小于Android 4.4 不做下列适配
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return;
        }

        /**
         * Android 6.0+ 系统提供修改方法
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {//黑色
                if (fullScreen){
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }else {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }else {//白色
                if (fullScreen){
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            }
            return;
        }

        /**
         * 魅族EMUI （flyme4+ 适配4.4.4 开始）
         */
        if (OSUtils.getRomType().equals(OSUtils.ROM_TYPE.FLYME) && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (setMeizuStatusBarDarkIcon(activity,dark)){
                return;
            }
        }

        /**
         * 小米MIUI（miui 6+  Android 5.0 开始）【小米的适配先判断是否可用miui提供方法，如果不可以再根据Android 6.0+适配Android原生方法】
         * 【小米公司原话】：基于以上背景，我们决定兼容 Android 的方法，舍弃 MIUI 的自己的实现方法。这个改动将会在 7.7.13 公测开发版开始生效（内测版本已生效），之后随稳定版外发。
         * 非常抱歉给各位带来麻烦，但长远来看，兼容 Android 的标准，减少了开发者的适配成本，对整个 Android 生态也更为有利
         * （http://www.miui.com/thread-8946673-1-1.html）
         */
        if (OSUtils.getRomType().equals(OSUtils.ROM_TYPE.MIUI) && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (setMiuiStatusBarDarkMode(activity,dark)) {
                return;
            }
        }

        /**
         * 其他系统
         */
        if (OSUtils.getRomType().equals(OSUtils.ROM_TYPE.OTHER)){
            return;
        }
    }


    /**
     * flyme设置黑色状态栏文字颜色（flyme4+）
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


    /**
     * 设置状态栏字体图标为深色，需要MIUI6以上
     * @param activity
     * @param darkmode
     * @return
     */
    private static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
