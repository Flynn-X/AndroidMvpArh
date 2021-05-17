package com.example.xsl.corelibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.example.xsl.corelibrary.CoreLibrary;

import java.io.File;

/**
 * 从系统中获取图片工具类
 * 兼容4.4 以及android N 以上版本
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

public class PictureFromSysUtil {

    public static final int SELECT_PICTURES = 0x0111;
    public static final int TAKE_PHOTO = 0x0112;
    public static final int CROP_PICTURE = 0x0113;

    private static File mCameraFile;//照相机的File对象
    //裁剪后的File对象
    private static File mCropFile = new File(CeleryToolsUtils.getSystemFilePath(CoreLibrary.AtContext, Environment.DIRECTORY_PICTURES), "camera_crop.jpg");
    public static Uri outPutUri = Uri.fromFile(mCropFile);

    /**
     * 打开相机拍照
     */
    public static void OpenCarmera(Activity context) {
        if (CeleryToolsUtils.hasSdcard()) {
            mCameraFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+Environment.DIRECTORY_DCIM, System.currentTimeMillis() + "_celery.jpg");
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mCameraFile);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
            }
            context.startActivityForResult(intentFromCapture, TAKE_PHOTO);
        } else {
            CeleryToast.showShort(context, "未找到存储卡，无法存储照片！");
        }
    }


    /**
     * 打开相册
     */
    public static void OpenAlbum(Activity context) {
        File mGalleryFile = new File(CeleryToolsUtils.getSystemFilePath(context, Environment.DIRECTORY_PICTURES), "gallery.jpg");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mGalleryFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        context.startActivityForResult(intent, SELECT_PICTURES);
    }


    /**
     * 相册选择单张图片后onActivityResult 返回文件
     * @param context
     * @param data
     * @return
     */
    public static File albumOnActivityResult(Context context, Intent data) {
        return new File(ImageTool.getImageAbsolutePath(context, data.getData()));
    }

    /**
     * 单张拍照后onActivityResult 返回文件
     * @param context
     * @param data
     * @return
     */
    public static File carmeraOnActivityResult(Context context, Intent data) {
        sysAlbumUpdateBroadcast(context,mCameraFile);
        return mCameraFile;
    }

    /**
     * 相册选择单张图片后onActivityResult 跳转到系统截切
     * @param context
     * @param data
     * @return
     */
    public static void albumOnActivityResultStartPhotoZoom(Activity context, Intent data) {
        Uri dataUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File imgUri = new File(ImageTool.getImageAbsolutePath(context, data.getData()));
            dataUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imgUri);
        } else {
            dataUri = data.getData();
        }
        startPhotoZoom(context,dataUri);
    }


    /**
     * 拍照单张图片后onActivityResult 跳转到系统截切
     * @param context
     * @param data
     */
    public static void carmeraOnActivityResultStartPhotoZoom(Activity context, Intent data) {
        Uri inputUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            inputUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mCameraFile);
        } else {
            inputUri = Uri.fromFile(mCameraFile);
        }
        sysAlbumUpdateBroadcast(context,mCameraFile);
        startPhotoZoom(context,inputUri);
    }


    /**
     * 裁剪图片方法实现
     * @param context
     * @param inputUri
     */
    private static void startPhotoZoom(Activity context, Uri inputUri) {
        if (inputUri == null) {
            L.i("The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            //去除默认的人脸识别，否则和剪裁匡重叠
            intent.putExtra("noFaceDetection", false);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                String url = ImageTool.getImageAbsolutePath(context, inputUri);
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //这里就将裁剪后的图片的Uri返回了
        context.startActivityForResult(intent, CROP_PICTURE);
    }

    /**
     * 系统广播通知相册更新
     * @param context
     * @param file
     */
    private static void sysAlbumUpdateBroadcast(Context context,File file){
        // 这里我们发送广播让MediaScanner 扫描我们制定的文件
        //这样在系统的相册中我们就可以找到我们拍摄的照片了
        Intent broadcastIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        broadcastIntent.setData(Uri.fromFile(file));
        context.sendBroadcast(broadcastIntent);
    }


}
