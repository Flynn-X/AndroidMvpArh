package com.example.xsl.selectlibrary.adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.xsl.selectlibrary.R;
import com.example.xsl.selectlibrary.aty.CeleryPictrueScanActivity;
import com.example.xsl.selectlibrary.utils.CeleryImageLoader;
import com.example.xsl.selectlibrary.utils.OnkeyBackListener;
import com.example.xsl.selectlibrary.utils.TransitionAnimUtils;
import com.example.xsl.selectlibrary.views.MyPhotoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/20
 * @description 图片浏览viewpager适配器
 */
public class PictrueScanAdapter extends PagerAdapter {

    private Activity context;
    private int indexPostion;
    //大图片信息集合
    private List<String> list = new ArrayList<>();
    //小图地址集合
    private List<String> thumb_list = new ArrayList<>();
    private  Bundle bundle;
    //MyPhotoView map集合
    private Map<Integer,View> viewMap = new HashMap<>();
    private Handler handler = new Handler();

    public PictrueScanAdapter(Activity context, final ViewPager viewPager, Bundle bundle){
        this.context = context;
        this.bundle = bundle;
        indexPostion = bundle.getInt("indexPosition",0);
        list = bundle.getStringArrayList("imgs");
        thumb_list = bundle.getStringArrayList("thumb_imgs");

        /**
         * 实现返回按钮点击事件
         */
        CeleryPictrueScanActivity.setOnKeyBackListener(new OnkeyBackListener() {
            @Override
            public void onBack() {
                if (viewMap != null && viewMap.size()>0 ) {
                    MyPhotoView myPhotoView = (MyPhotoView) viewMap.get(viewPager.getCurrentItem());
                    if (myPhotoView != null) {
                        //模拟点击事件
                        myPhotoView.performPhotoTapClick();
                    }
                }
            }
        });
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_celery_pictrue_scan_item, null);
        final FrameLayout layout = (FrameLayout) view.findViewById(R.id.layout);
        final FrameLayout framelayout = (FrameLayout) view.findViewById(R.id.framelayout);
        final MyPhotoView photo_view = (MyPhotoView) view.findViewById(R.id.photo_view);


        String thumbnail_path = (thumb_list == null || TextUtils.isEmpty(thumb_list.get(position)))?list.get(position):thumb_list.get(position);
        if (position == indexPostion) {
            layout.setVisibility(View.INVISIBLE);
            CeleryImageLoader.displayImageThumbnail(context, thumbnail_path
                    ,photo_view, new com.example.xsl.selectlibrary.utils.RequestListener() {
                @Override
                public boolean onResourceReady(final Drawable resource) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.setVisibility(View.VISIBLE);
                            //启动动画
                            TransitionAnimUtils.enter(context,resource,bundle,list.get(position),null,photo_view,framelayout);
                        }
                    },50);
                    return false;
                }
            });
        }else {
            /**
             * 直接显示图片，动画播放完成后
             */
            CeleryImageLoader.displayImageThumbnail(context, thumbnail_path
                    ,photo_view, new com.example.xsl.selectlibrary.utils.RequestListener() {
                        @Override
                        public boolean onResourceReady(Drawable resource) {
                            //加载大图
//                            if (!TextUtils.isEmpty(list.get(position))) {
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        CeleryImageLoader.displayImage(context, list.get(position),photo_view,null);
//                                    }
//                                },50);
//
//                            }
                            return false;
                        }
                    });
        }


        /**
         * photoview 单击事件
         */
        photo_view.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                Rect bitmapRect  = photo_view.getDrawable().getBounds();
                TransitionAnimUtils.exit(context,bitmapRect,photo_view,position,bundle,framelayout);
            }

            @Override
            public void onOutsidePhotoTap() {
                Rect bitmapRect  = photo_view.getDrawable().getBounds();
                TransitionAnimUtils.exit(context,bitmapRect,photo_view,position,bundle,framelayout);
            }
        });

        viewMap.put(position,photo_view);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
        viewMap.remove(position);
    }



}