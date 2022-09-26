package com.example.xsl.corelibrary.widgets.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.xsl.corelibrary.R;

/**
 * @author xsl
 * @date 2022/9/14 13:53
 * @email 2596449399@qq.com
 * @describe TODO
 * 权限说明弹窗
 */
public class PermissionDescriptionDialog {

    private static Dialog dialog = null;
    private static TextView HintTv;

    /**
     * 弹出框
     * @param context
     */
    public static void show(Activity context, String description){
        if (dialog == null || !dialog.isShowing()) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.permission_description_dialog, null);
            HintTv = dialogView.findViewById(R.id.hint_tv);
            if (description != null) {
                HintTv.setText(description);
            }
            dialog = new Dialog(context, R.style.bottomDialog);
            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(dip2px(context,15), 0, dip2px(context,15), 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //背景全透明
            window.setDimAmount(0f);
            window.setAttributes(lp);
            window.setGravity(Gravity.TOP);
            window.setWindowAnimations(R.style.top_style);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!((Activity) context).isDestroyed()) {
                    dialog.show();
                }
            }else {
                dialog.show();
            }
        }else {
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    if (description != null) {
                        HintTv.setText(description);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isDestroyed()) {
                            dialog.show();
                        }
                    }else {
                        dialog.show();
                    }
                }else {
                    if (description != null) {
                        HintTv.setText(description);
                    }
                }
            }
        }
    }

    public static void dismiss(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
