package com.example.xsl.corelibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.annotations.Nullable;

/**
 * Created by Celery on 2016/8/30.
 * https://github.com/Celery1025
 * superzilin1025@gmail.com
 * 工具类
 *
 * @version 2.0
 * 在版本 1.1.0.5 中增加此功能
 *
 * @version 2.0.1
 * 1、增加文件保存到sdk功能
 * 2、版本对比
 * 3、跳转apk安装
 *
 */
public class CeleryToolsUtils {

    /**
     * 隐藏虚拟键盘
     * @param v
     */
    public static void HideKeyboard(View v) {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive()) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
        }
    }

    /**
     * 显示虚拟键盘
     * @param v
     */
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制显示或者关闭系统键盘
     * @param txtSearchKey
     * @param isOpen 是否强制打开
     */
    public static void KeyBoard(final EditText txtSearchKey, final boolean isOpen) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(isOpen) {
                    m.showSoftInput(txtSearchKey,InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    /**
     * 通过定时器强制隐藏虚拟键盘
     * @param v
     */
    public static void TimerHideKeyboard(final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive()) {
                    imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
                }
            }
        }, 10);
    }



    /**
     * 输入法是否显示着
     * @param edittext
     * @return
     */
    public static boolean KeyBoard(EditText edittext) {
        boolean bool = false;
        InputMethodManager imm = ( InputMethodManager ) edittext.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive()) {
            bool = true;
        }
        return bool;
    }


    /**
     * 跳转到WIFI设置界面
     * @param context
     */
    public static void startActivityWifiSetting(Context context){
        Intent intent =  new Intent(Settings. ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到蓝牙设置界面
     * @param context
     */
    public static void startActivityBlueToothSetting(Context context){
        Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivity(intent);
    }


    /**
     * 将字符串中大写字母转成小写
     * @param str
     * @return
     */
    public static String exChangeLower(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    /**
     * 将字符串中小写字母转成大写
     * @param str
     * @return
     */
    public static String exChangeUpper(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isLowerCase(c)){
                    sb.append(Character.toUpperCase(c));
                }else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    /**
     * 获取系统缓存路径
     * @param context
     * @param s
     * @return
     */
    public static String getSystemFilePath(Context context,String s) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(s).getAbsolutePath();
//            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//            cachePath = context.getExternalCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
//            cachePath = context.getCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        }
        return cachePath;
    }


    /**
     * 判断是否有sd卡
     * @return
     */
    public static boolean hasSdcard(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return true;
        }
        return false;
    }


    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App的名称
     * @param context 上下文
     * @return 名称
     */
    public static String getAppName(Context context){
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            //获取应用 信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取albelRes
            int labelRes = applicationInfo.labelRes;
            //返回App的名称
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序的进程名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }



    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            return imei;
        }
        return "";
    }

    /**
     * 获取手机IMSI号
     */
    public static String getIMSI(Context context){
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = mTelephonyMgr.getSubscriberId();
        return imsi ;
    }


    /**
     * 日期字符串转时间戳
     * @param dateStr 如 2018-05-17 16:07 或者 2018/05/17 16:07
     * @param tim  定义dateStr格式 如 "yyyy-MM-dd HH:mm"，"yyy/MM/dd HH:mm" 等等
     * @return
     */
    public static long getStringToDateTime(String dateStr,String tim){
        SimpleDateFormat sdf=new SimpleDateFormat(tim);
        Date date =null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date == null ? 0 : date.getTime();
    }


    /**
     * 时间戳转换成字符窜
     * @param time
     * @param tim 格式 如 "yyyy-MM-dd"，"yyy/MM/dd" 等等
     * @return
     */
    public static String getDateToString(long time,String tim) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(tim);
        return sf.format(d);
    }




    /**
     * 验证手机号
     * @param phoneNumber 传入电话号码
     * @return 返回true 为验证通过
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        //正则表达式
        String phone="^1[345789]\\d{9}$" ;
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 金钱保留小数
     * @param d
     * @param count 保留位数
     * @return
     */
    public static String decimalFormat(double d,int count){
        StringBuffer stringBuffer = new StringBuffer("#.");
        for (int i=0;i<count;i++){
            stringBuffer.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(stringBuffer.toString());
        if (d<1){
            return "0" + decimalFormat.format(d);
        }
        return decimalFormat.format(d);
    }


    /**
     * 判断邮箱是否合法
     * @param email 传入邮箱地址
     * @return 返回true为验证通过
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 判断身份证号码是否合法
     * @param idCard 身份证号码
     * @return
     */
    public static boolean isValidator(String idCard) {
        return IDCardUtil.IDCardValidate(idCard).equals("YES");
    }


    /**
     * 重启app
     * @param context
     */
    public static void  restartApplication(Context context) {
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 处理与时间相关的字符串

     1.今年
     1> 今天
     * 1分内： 刚刚
     * 1分~59分内：xx分钟前
     * 大于60分钟：xx小时前

     2> 昨天
     * 昨天 xx:xx

     3> 其他
     * xx-xx xx:xx

     2.非今年
     1> xxxx-xx-xx

     * @param str
     * @return 返回格式化时间
     */
    public static String backFormatDateTime(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(str);
            //将日期转换成时间戳
            long timestamp = date.getTime();
            //获取当前系统时间戳
            long currentTimestamp = new Date().getTime();
            long dValue = currentTimestamp - timestamp;
            if (dValue>=0 && dValue < 1000*60){
                return " 刚刚";
            }

            if (dValue>= 1000*60 && dValue < 1000*60*60){
                return dValue/(1000*60) + " 分钟前";
            }

            String timeFormat = format.format(date).split(" ")[0];
            String currentFormat = format.format(new Date()).split(" ")[0];
            //判断是否当前时间还是在今天
            if (dValue>= 1000*60*60 && timeFormat.equals(currentFormat)){
                return dValue/(1000*60*60) + " 小时前";
            }

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            timestamp = format1.parse(timeFormat).getTime();
            currentTimestamp = format1.parse(currentFormat).getTime();
            //判断是否在昨天
            if (currentTimestamp - timestamp>= 1000*60*60*24 && currentTimestamp - timestamp< 1000*60*60*48){
                return "昨天 " + (format.format(date).split(" ")[1]);
            }

            //判断当前时间是否还在今年
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy");
            if (format2.format(timestamp).equals(format2.format(currentTimestamp))){
                SimpleDateFormat format3 = new SimpleDateFormat("MM-dd HH:mm");
                return format3.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str.split(" ")[0];
    }


    /**
     * 将数据流保存在sd卡
     * @param inputStream
     * @param folderName 文件夹路径，可包含多层,最后不需要加斜杠（例如："test/a/b"）
     * @param fileName 包括后缀 (例如：ab123.rar)
     * @return 保存的全路径
     */
    public static String savaFile(InputStream inputStream, String folderName, String fileName){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("", "SD卡不存在");
            return null;
        }
        File file = null;
        //表示是内部存储文件夹路径
        if (folderName.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())){
            file = new File(folderName);
        }else {
            //可以在这里自定义路径
            file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        }
        if (!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(file.getPath()+ File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            fileOutputStream = new FileOutputStream(file1);
            long current = 0;
            while ((len = inputStream.read(buf)) != -1) {
                current += len;
                fileOutputStream.write(buf, 0, len);
            }
            fileOutputStream.flush();
            return file1.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * 规则是按日期订的例如：2.10.15
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        for(int i = 0 ; i<versionArray1.length ; i++){ //如果位数只有一位则自动补零（防止出现一个是04，一个是5 直接以长度比较）
            if(versionArray1[i].length() == 1){
                versionArray1[i] = "0" + versionArray1[i];
            }
        }
        String[] versionArray2 = version2.split("\\.");
        for(int i = 0 ; i<versionArray2.length ; i++){//如果位数只有一位则自动补零
            if(versionArray2[i].length() == 1){
                versionArray2[i] = "0" + versionArray2[i];
            }
        }
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }


    /**
     * 自动跳转安装app
     * @param context 上下文对象
     * @param file 本地文件
     */
    public static void installApk(Context context, File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider7.getUriForFile(context,file);
        FileProvider7.grantPermissions(context,intent,uri,true);
        FileProvider7.setIntentDataType(context,intent
                ,"application/vnd.android.package-archive",file,true);
        context.startActivity(intent);
    }



    /**
     * 文件重命名
     * @param filePath 文件绝对路径
     * @param newFileName 新文件完整名字（包括后缀）
     * @return 返回重命名后文件完整路径
     */
    public static String renameFile(String filePath, String newFileName) {
        if(TextUtils.isEmpty(filePath)) {
            return null;
        }

        if(TextUtils.isEmpty(newFileName)) {
            return null;
        }
        File file = new File(filePath);
        String[] strings = filePath.split("/");
        //获取完整文件名
        String fileName = strings[strings.length-1];
        //获取到当前文件所属文件夹完整路径
        String substring = filePath.substring(0,filePath.length()-fileName.length());
        //重命名后文件完整路径
        String newPath = substring + newFileName;
        file.renameTo(new File(newPath));
        return newPath;
    }


    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable CharSequence str){
        if (str != null && !TextUtils.isEmpty(str)){
            return false;
        }
        return true;
    }

    /**
     * 判断是不是baseUrl
     * @param urls
     * @return
     */
    public static boolean isBaseUrl(String urls){
        if ((urls.startsWith("http://") || urls.startsWith("https://"))
                && urls.contains(".") && urls.length()> 10){
            return true;
        }else {
            return false;
        }
    }

}
