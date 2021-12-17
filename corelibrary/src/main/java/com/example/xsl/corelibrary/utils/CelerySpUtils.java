package com.example.xsl.corelibrary.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.xsl.corelibrary.CoreLibrary;

import java.util.Set;

/**
 * Created by xsl on 2017/11/13.
 * @version 1.0
 * @author xsl
 * @des 底层 SharedPreferences 存储
 * 1.1.0.4 中增加此功能
 */
public class CelerySpUtils {

    private static SharedPreferences sharedPreferences = null;
    //空间命名
    public static final String spName = CoreLibrary.AtContext.getPackageName();

    private static SharedPreferences getmSharedPreferences(){
        if (sharedPreferences == null){
            sharedPreferences = CoreLibrary.AtContext.getSharedPreferences(spName, Activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 清除所有SharedPreferences数据
     */
    public static void clearAllCache(){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 保存boolen类型数据
     * @param key 名称
     * @param bool 值
     */
    public static void putBoolen(String key,boolean bool){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.putBoolean(key, bool);
        editor.commit();
    }

    /**
     * 获boolen类型值
     * @param key 名称
     * @return
     */
    public static boolean getBoolen(String key){
        return getmSharedPreferences().getBoolean(key, false);
    }


    public static void putString(String key,String value){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getString(String key){
        return getmSharedPreferences().getString(key,"");
    }


    public static void putInt(String key, int value){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static int getInt(String key){
        return getmSharedPreferences().getInt(key,-1);
    }

    public static void putFloat(String key, float value){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.putFloat(key, value);
        editor.commit();
    }


    public static float getFloat(String key){
        return getmSharedPreferences().getFloat(key,-1);
    }


    public static void putLong(String key, long value){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }


    public static long getLong(String key){
        return getmSharedPreferences().getLong(key,-1);
    }


    public static void putStringSet(String key,Set<String> values){
        SharedPreferences.Editor editor = getmSharedPreferences().edit();
        editor.putStringSet(key, values);
        editor.commit();
    }


    public static Set<String> getStringSet(String key){
        return getmSharedPreferences().getStringSet(key,null);
    }



}
