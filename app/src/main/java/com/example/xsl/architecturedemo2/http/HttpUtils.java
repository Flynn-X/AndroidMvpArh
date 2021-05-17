package com.example.xsl.architecturedemo2.http;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import retrofit2.Retrofit;

/**
 * Created by Zhou on 2017/4/10.
 */

public class HttpUtils{

    public static ApiService apiService = null;

    public static ApiService getService(Retrofit retrofit){
        if (apiService == null){
            apiService = retrofit.create(ApiService.class);
        }
       return apiService;
    }


    /**
     * 接口请求异常判断
     * @param context 上下文对象
     * @param code 错误码
     * @param msg 服务器对错误描述
     * @return
     */
    public static boolean checkCode(final Context context, int code, String msg){
        boolean bool;
        String hint = null;
        switch (code){
            case 0://成功
                bool = true;
                break;
            case 1:
                bool = false;
                hint = "Token失效或不合法";
//                if (!LoginActivity.isAction) {
//                    LoginActivity.isAction = true;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //跳转到登录界面
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            context.startActivity(intent);
//                        }
//                    },500);
//                }
                break;
            case 2:
                bool = false;
                hint = "失败或异常";
                break;
            case 3:
                bool = false;
                hint = "数据类型不合法";
                break;
            case 4:
                bool = false;
                hint = "数据不能为空";
                break;
            case 5:
                bool = false;
                hint = "已激活，申请试用用户资料不完整";
                break;
            case 6:
                bool = false;
                hint = "邮箱已存在";
                break;
            case 7:
                bool = false;
                hint = "用户未激活";
                break;
            case 8:
                bool = false;
                hint = "密码错误";
                break;
            case 9:
                bool = false;
                hint = "邮箱不存在";
                break;
            case 10:
                bool = false;
                hint = "用户被冻结";
                break;
            case 11:
                bool = false;
                hint = "系统服务已结束";
                break;
            case 12:
                bool = false;
                hint = "返回0条数据";
                break;
            case 13:
                bool = false;
                hint = "数据没有更新";
                break;
            default:
                bool = false;
                hint = "服务器未知错误";
                break;
        }

        if (!bool){
            try {
                Toast.makeText(context, TextUtils.isEmpty(msg) ? hint : msg, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return bool;
    }


}
