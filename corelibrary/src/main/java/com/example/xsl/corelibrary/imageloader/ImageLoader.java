package com.example.xsl.corelibrary.imageloader;

import android.content.Context;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/12/26
 * @description
 * 图片加载简单封装，方便后面替换图片加载第三方矿建
 * 简单加载使用，如果是特殊要求的加载，请调用 glide 原始方法
 */
public class ImageLoader {

    public static ImageLoaderRetriever with(Context context) {
        ImageLoaderRetriever retriever = ImageLoaderRetriever.get();
        return retriever.init(context);
    }

}