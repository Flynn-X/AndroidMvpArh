package com.example.xsl.corelibrary.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telephony.TelephonyManager;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by xsl on 2015/6/24.
 * 网络判断
 */
public class NetworkUtil {

    /**
     * 检查当前网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    //只判断net和WiFi是否可用
                    if (networkInfo[0].getState() == NetworkInfo.State.CONNECTED ||
                            networkInfo[1].getState() == NetworkInfo.State.CONNECTED ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * ping 百度，判断是服务器连接不上还是网络问题
     * 百度挂了，此方法无效
     * Handler 传入是使操作在同一个UI线程中执行
     * @return
     */
    public static void pingBaiDu(final pingListerner pingListerner, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.baidu.com/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 设置连接网络的超时时间
                    httpURLConnection.setConnectTimeout(30000);
                    httpURLConnection.setReadTimeout(30000);
                    httpURLConnection.setDoInput(true);
                    // 表示设置本次http请求使用GET方式请求
                    httpURLConnection.setRequestMethod("GET");
                    final int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pingListerner.onSuccess(true);
                            }
                        });

                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pingListerner.onFailure(responseCode, false);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pingListerner.onFailure(400, false);
                        }
                    });
                }
            }
        }).start();
    }

    public interface pingListerner{
        void onSuccess(boolean bool);
        void onFailure(int code, boolean bool);
    }

    /**
     * 判断gps是否打开
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 判断WiFi是否打开
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否是3G网络
     * @param context
     * @return
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }


    /**
     * 判断是wifi还是3g网络,
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     *  唯一的设备ID：
     *  GSM手机的 IMEI 和 CDMA手机的 MEID.
     * @param context
     * @return
     */
    public static String getIMEI(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 唯一的用户ID：
     * 例如：IMSI(国际移动用户识别码) for a GSM phone.
     * 需要权限：READ_PHONE_STATE
     * @param context
     * @return
     */
    public static String getIMSI(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

}
