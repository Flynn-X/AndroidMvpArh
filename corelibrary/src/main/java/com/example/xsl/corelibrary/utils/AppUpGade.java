package com.example.xsl.corelibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * root app自动升级工具类
 */
public class AppUpGade {



    public static void  excutesucmd(Context context, String currenttempfilepath) {

        new Thread(){
            @Override
            public void run() {
                super.run();

                Process process = null;
                OutputStream out = null;
                InputStream in = null;
                try {
                    // 请求root
                    process = Runtime.getRuntime().exec("su");
                    out = process.getOutputStream();
                    // 调用安装
                    out.write(("pm install -r " + currenttempfilepath + "\n").getBytes());
                    in = process.getInputStream();
                    int len = 0;
                    byte[] bs = new byte[256];
                    while (-1 != (len = in.read(bs))) {
                        String state = new String(bs, 0, len);
                        if (state.equals("success\n")) {
                            //安装成功后的操作
                            //静态注册自启动广播
                            Intent intent=new Intent();
                            //与清单文件的receiver的anction对应
                            intent.setAction("android.intent.action.PACKAGE_REPLACED");
                            //发送广播
                            context.sendBroadcast(intent);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            }
        }.start();
    }


    // 执行指定命令
    public static String execCommand(String... command) {
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        String result = "";

        try {
            process = new ProcessBuilder().command(command).start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            result = new String(baos.toByteArray());
            if (inIs != null)
                inIs.close();
            if (errIs != null)
                errIs.close();
            process.destroy();
        } catch (IOException e) {
            Log.e("execCommand", e.getLocalizedMessage());
            result = e.getMessage();
        }
        return result;
    }




}
