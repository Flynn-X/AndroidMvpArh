package com.example.xsl.selectlibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.xsl.selectlibrary.R;

/**
 * Created by xsl on 2018/2/9.
 */

public class CeleryImageLoader {

    private  static Handler handler = new Handler();

   private static RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.mipmap.ci_image_default)
            .error(R.mipmap.ci_image_default)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.NORMAL);

    /**
     * 显示小图
     * @param context 上下文对象
     * @param path 图片路径
     * @param imageView 展示图片
     */
    public static void displayImageThumbnail(Context context, String path, final ImageView imageView, final RequestListener requestListener){
        if (TextUtils.isEmpty(path)){
            return;
        }

        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.fillInStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (requestListener != null) {
                            requestListener.onResourceReady(resource);
                        }
                        return false;
                    }
                }).into(imageView);
    }

    /**
     * 加载图片原图
     * @param context 上下文对象
     * @param path 图片路径
     * @param imageView 展示图片
     */
    public static void displayImage(final Context context, final String path, final ImageView imageView, final RequestListener requestListener){
        if (TextUtils.isEmpty(path)){
            return;
        }

        //显示原图配置override
        RequestOptions requestOptions = new RequestOptions()
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .placeholder(imageView.getDrawable())
                .error(imageView.getDrawable())
                .dontTransform()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.NORMAL);

        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        /**
                         * 对长图显示做特殊处理
                         */
                        if (resource.getMinimumHeight()>resource.getMinimumWidth()*3){
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }else {
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                        if (requestListener != null) {
                            requestListener.onResourceReady(resource);
                        }

                        /**
                         * 原图大于限制则，进行压缩尺寸加载
                         */
                        if ( resource.getMinimumWidth()>GLESTextureLimit.getSize() || resource.getMinimumHeight()>GLESTextureLimit.getSize()){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int width = resource.getMinimumWidth()>GLESTextureLimit.getSize()?GLESTextureLimit.getSize(): resource.getMinimumWidth();
                                    int heigh = resource.getMinimumHeight()>GLESTextureLimit.getSize()?GLESTextureLimit.getSize(): resource.getMinimumHeight();
                                    displayImageLimitSize(context,path,imageView,width,heigh);
                                }
                            },50);
                        }
                        return false;
                    }
                }).into(imageView);
    }

    /**
     * 根据具体尺寸显示图片
     * @param context
     * @param path
     * @param imageView
     * @param width
     * @param height
     */
    private static void displayImageLimitSize(Context context, String path, ImageView imageView, int width, int height ){
        if (TextUtils.isEmpty(path)){
            return;
        }

        RequestOptions requestOptions = new RequestOptions()
                .override(width,height)
                .placeholder(imageView.getDrawable())
                .error(imageView.getDrawable())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.NORMAL);

        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .into(imageView);

    }









}
