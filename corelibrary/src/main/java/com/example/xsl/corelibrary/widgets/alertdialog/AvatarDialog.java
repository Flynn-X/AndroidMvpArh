package com.example.xsl.corelibrary.widgets.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.xsl.corelibrary.R;
import com.example.xsl.corelibrary.utils.PictureFromSysUtil;



/**
 * Created by Celery on 2016/10/25.
 * @version 1.0
 * @author xsl
 * @des 使用方法
 *
 */

//    /**
//     * onActivityResult使用方法示例
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK){
//            switch (requestCode){
//                case PictureFromSysUtil.SELECT_PICTURES:
//                    //跳转到系统剪切
//                    PictureFromSysUtil.albumOnActivityResultStartPhotoZoom(mContext, data);
//                    //获取选择的图片文件
//                    File album = PictureFromSysUtil.albumOnActivityResult(mContext, data);
//                    break;
//                case PictureFromSysUtil.TAKE_PHOTO:
//                    //拍照跳转到系统剪切
//                    PictureFromSysUtil.carmeraOnActivityResultStartPhotoZoom(mContext, data);
//                    //获取拍照照片文件
//                    File carmera = PictureFromSysUtil.carmeraOnActivityResult(mContext, data);
//                    break;
//                case PictureFromSysUtil.CROP_PICTURE:
//                    //获取剪切后文件
//                    Bitmap bitmap  = BitmapFactory.decodeFile(PictureFromSysUtil.outPutUri.getPath());
//                    imageView.setImageBitmap(bitmap);
//                    break;
//            }
//        }
//    }

public class AvatarDialog {

    /**
     * 弹出框
     * @param context
     */
    public static void show(Activity context){
        View dialogView = LayoutInflater.from(context).inflate(R.layout.avatar_dialog, null);
        final Dialog dialog = new Dialog(context,R.style.bottomDialog);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.mystyle);

        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.picture) {
                    PictureFromSysUtil.OpenAlbum(context);
                    dialog.dismiss();
                } else if (i == R.id.takephoto) {
                    PictureFromSysUtil.OpenCarmera(context);
                    dialog.dismiss();
                } else if (i == R.id.cancel) {
                    dialog.dismiss();
                }
            }
        };

        dialogView.findViewById(R.id.picture).setOnClickListener(onclick);
        dialogView.findViewById(R.id.takephoto).setOnClickListener(onclick);
        dialogView.findViewById(R.id.cancel).setOnClickListener(onclick);
        dialog.show();
    }




}
