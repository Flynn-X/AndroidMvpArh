package com.example.xsl.corelibrary.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.xsl.corelibrary.R;


/**
 * Created by xsl on 2018/5/3.
 */

public class ImageLoaderRetriever {

    private static final ImageLoaderRetriever INSTANCE = new ImageLoaderRetriever();

    private Context context;
    /**
     * 加载地址
     */
    private Object objectUrl;
    /**
     * 配置
     */
    private RequestOptions requestOptions = null;

    /**
     * 配置通用glide RequestOptions
     * @return
     */
    private RequestOptions getRequestOptions(){
        if (requestOptions == null) {
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.core_image_default)
                    .error(R.drawable.core_image_default)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);
        }
        return requestOptions;
    }


    /**
     * Retrieves and returns the RequestManagerRetriever singleton.
     */
    protected static ImageLoaderRetriever get() {
        return INSTANCE;
    }

    protected ImageLoaderRetriever init(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 图片加载地址
     * @param object
     * @return
     */
    public ImageLoaderRetriever load(Object object){
        objectUrl = object;
        return this;
    }

    /**
     * 不添加为默认值
     * @param requestOptions
     * @return
     */
    public ImageLoaderRetriever apply(RequestOptions requestOptions){
        this.requestOptions = requestOptions;
        return this;
    }

    /**
     * 显示
     * @param imageView
     */
    public void into(ImageView imageView){
        Glide.with(context)
                .load(objectUrl)
                .apply(requestOptions==null?getRequestOptions():requestOptions)
                .into(imageView);
    }





}
