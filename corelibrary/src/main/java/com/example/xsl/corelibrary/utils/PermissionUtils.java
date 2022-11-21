package com.example.xsl.corelibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.xsl.corelibrary.widgets.alertdialog.PermissionDescriptionDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

/**
 * @author xsl
 * @date 2022/9/17 16:11
 * @email 2596449399@qq.com
 * @describe TODO
 */
public class PermissionUtils {

    //下面是使用示例

//    String[] permissions = new String[]{
//            Manifest.permission.READ_PHONE_STATE,
//    };
//    String[] descriptions = new String[]{
//            getString(R.string.READ_PHONE_STATE_DES),
//    };
//    PermissionUtils.requestMultiplePermission(this
//        , permissions, descriptions, new PermissionUtils.OnRequestPermissionListener() {
//        @Override
//        public void finish(boolean allGranted) {
//            if (allGranted){
//
//
//            }else {
//                CeleryToast.showShort(activity,getString(R.string.PERMISSIONS_NOT_GRANTED));
//            }
//        }
//    });

    private static String TAG = PermissionUtils.class.getSimpleName();

    private static boolean allGranted = true;

    /**
     * 请求单个权限
     * @param activity
     * @param permissions
     * @param descriptions
     * @param onRequestPermissionListener
     */
    public static void requestSinglePermission(Activity activity, String permissions, String descriptions, OnRequestPermissionListener onRequestPermissionListener){
        allGranted = true;
        if (permissions == null || descriptions == null){
            CeleryToast.showShort(activity,"权限和权限说明不能为空!");
            return;
        }
        new RxPermissions(activity).requestEach(permissions)
                .subscribe(new io.reactivex.Observer<Permission>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        //判定是否展示请求权限的意图
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions)) {
                            PermissionDescriptionDialog.show(activity, descriptions);
                        }
                    }
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Permission permission) {
                        if (permission.granted){
                            //用户同意了权限
                        }else if (permission.shouldShowRequestPermissionRationale){
                            //用户拒绝了权限，没有选中【不再询问】，下次启动时还会提示请求对话框。
                            allGranted = false;
                        }else {
                            //用户拒绝了权限，并选中【不再询问】。
                            allGranted = false;
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        PermissionDescriptionDialog.dismiss();
                        if (onRequestPermissionListener != null){
                            onRequestPermissionListener.finish(allGranted);
                        }
                    }

                    @Override
                    public void onComplete() {
                        PermissionDescriptionDialog.dismiss();
                        if (onRequestPermissionListener != null){
                            onRequestPermissionListener.finish(allGranted);
                        }
                    }
                });
    }

    /**
     * 请求多个权限
     * @param activity
     * @param permissions
     * @param descriptions
     * @param onRequestPermissionListener
     */
    public static void requestMultiplePermission(Activity activity, String[] permissions, String[] descriptions, OnRequestPermissionListener onRequestPermissionListener){
        allGranted = true;
        if (permissions == null || descriptions == null){
            CeleryToast.showShort(activity,"权限和权限说明不能为空!");
            return;
        }
        if (permissions.length != descriptions.length){
            CeleryToast.showShort(activity,"权限和权限需一一对应数量相等!");
            return;
        }
        multiplePermission(activity,permissions,descriptions,0,onRequestPermissionListener);
    }

    /**
     * 多个权限处理
     * @param activity
     * @param permissions
     * @param descriptions
     * @param index
     * @param onRequestPermissionListener
     */
    private static void multiplePermission(Activity activity, String[] permissions, String[] descriptions, int index, OnRequestPermissionListener onRequestPermissionListener){
        if (permissions.length == index){
            //这里说明全部权限已经处理完了
            if (onRequestPermissionListener != null){
                onRequestPermissionListener.finish(allGranted);
            }
            return;
        }
        new RxPermissions(activity).requestEach(permissions[index])
                .subscribe(new io.reactivex.Observer<Permission>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                       //判定是否展示请求权限的意图
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[index])) {
                            PermissionDescriptionDialog.show(activity,descriptions[index]);
                        }
                    }
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Permission permission) {
                        if (permission.granted){
                            //用户同意了权限
                            Log.e(TAG,"通过的权限：" + permission.name);
                        }else if (permission.shouldShowRequestPermissionRationale){
                            //用户拒绝了权限，没有选中【不再询问】，下次启动时还会提示请求对话框。
                            allGranted = false;
                            Log.e(TAG,"拒绝的权限：" + permission.name);
                        }else {
                            //用户拒绝了权限，并选中【不再询问】。
                            allGranted = false;
                            Log.e(TAG,"拒绝的权限：" + permission.name);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        PermissionDescriptionDialog.dismiss();
                        multiplePermission(activity,permissions,descriptions,index + 1,onRequestPermissionListener);
                    }

                    @Override
                    public void onComplete() {
                        PermissionDescriptionDialog.dismiss();
                        multiplePermission(activity,permissions,descriptions,index + 1,onRequestPermissionListener);
                    }
                });
    }


    public interface OnRequestPermissionListener{
        void finish(boolean allGranted);
    }

}
