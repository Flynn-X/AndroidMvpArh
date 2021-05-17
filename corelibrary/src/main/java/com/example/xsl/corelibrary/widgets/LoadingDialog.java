package com.example.xsl.corelibrary.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xsl.corelibrary.R;


/**
 * Created by Celery on 2016/9/7.
 * https://github.com/Celery1025
 * superzilin1025@gmail.com
 * 通用加载进度条
 *
 * @version 2.0
 * @date 2017/11/4
 * @author xsl
 * @描述
 * 1、修改显示逻辑，原有方法不再保留
 */
public class LoadingDialog {

    private static Dialog dialog = null;
    private static TextView textView;
    private static ImageView imageView;
    private static AnimationDrawable anim;

    /**
     * 弹出Loading对话框
     * @param context
     * @param hint
     */
    public static void show(Context context,String hint){
        if (dialog == null || !dialog.isShowing()) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog_common, null);
            dialog = new Dialog(context, R.style.loading_dialog);
            //去除标题
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            textView = (TextView) view.findViewById(R.id.loadhint);
            textView.setText(hint);
            Window win = dialog.getWindow();
            win.setContentView(view);
//            WindowManager m = context.getWindowManager();
//            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
//            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
//            p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.9
//            dialog.getWindow().setAttributes(p); //设置生效

            imageView = (ImageView) view.findViewById(R.id.image);
            anim = (AnimationDrawable) imageView.getDrawable();

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (imageView != null && anim != null) {
                        anim.stop();
                    }
                }
            });
            anim.start();
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }
        }else {
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    textView.setText(hint);
                    anim.start();
                    if (!((Activity) context).isFinishing()) {
                        dialog.show();
                    }
                }else {
                    textView.setText(hint);
                }
            }
        }
    }

    public static void dismiss(){
        if (dialog != null && dialog.isShowing()) {
            if (imageView != null && anim != null) {
                anim.stop();
            }
            dialog.dismiss();
        }
    }



}
