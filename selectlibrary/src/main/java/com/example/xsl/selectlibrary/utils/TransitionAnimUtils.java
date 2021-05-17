package com.example.xsl.selectlibrary.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.xsl.selectlibrary.R;

/**
 * Created by xieshuilin on 2018/1/22.
 * 图片浏览进入动画
 */
public class TransitionAnimUtils {

    //动画执行时间
    private static final int DURATION = 200;
    //屏幕宽、高
    public static int mScreenWidth;
    public static int mScreenHeight;
    private static Handler handler = new Handler();




    /**
     * 打开动画
     * @param context
     * @param resource 显示原图图片信息
     * @param bundle 点击的图片位置信息
     * @param big_path 大图
     * @param thumb_path 小图
     * @param view 图片容器
     * @param viewGroup 背景用于渐变动画
     */
    public static void enter(final Activity context, final Drawable resource, Bundle bundle, final String big_path, final String thumb_path, final View view, ViewGroup viewGroup) {
        initDisplay(context);
        Rect rect = bundle.getParcelable("bounds");

        // 获取上一个界面传入的信息
        int x = rect.left;
        int y = rect.top ;
        int width = rect.width();
        int height = rect.height();


        float mWidthScale,mHeightScale;
        //缩放基准点计算,分两种情况
        final Rect bounds = resource.getBounds();
        if (bounds.height()>=bounds.width()) {
            mWidthScale = (float) (width*bounds.height()) / (bounds.width()*mScreenHeight);
            mHeightScale = (float) height / mScreenHeight;

        }else {
            mWidthScale = (float) width / mScreenWidth;
            mHeightScale = (float) (height*bounds.width()) / (bounds.height()*mScreenWidth);
        }


        //平移基准点计算
        //坐标的获取设置
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);


        //计算起始点坐标
        float mLeftDelta = (x+(float)width/2) - (float)(mScreenWidth/2);
        float mTopDelta = (y+(float)height/2)-(float)(mScreenHeight/2);



        /*
        *  创建一个AnimationSet，它能够同时执行多个动画效果
        *  构造方法的入参如果是“true”，则代表使用默认的interpolator，如果是“false”则代表使用自定义interpolator
        */
        AnimationSet animationSet = new AnimationSet(true);

        /*
         *  创建一个半透明效果的动画对象，效果从完全透明到完全不透明
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);


        /*
         *  设置动画的持续时间
         */
        alphaAnimation.setDuration(DURATION);

        //设置硬件加速
        viewGroup.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        //去除缓存
        viewGroup.setPersistentDrawingCache(ViewGroup.PERSISTENT_NO_CACHE);
        //背景颜色渐变
        viewGroup.startAnimation(alphaAnimation);




        /*
         *  创建一个缩放效果的动画
         *  入参列表含义如下：
         *  fromX：x轴的初始值
         *  toX：x轴缩放后的值
         *  fromY：y轴的初始值
         *  toY：y轴缩放后的值
         *  pivotXType：x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
         *  pivotXValue：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
         *  pivotYType：y轴坐标的类型
         *  pivotYValue：轴的值，0.5f表明是以自身这个控件的一半长度为y轴
         */
        ScaleAnimation scaleAnimation = new ScaleAnimation(mWidthScale, 1.0f, mHeightScale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        /*
         *  设置动画的持续时间
         */
        scaleAnimation.setDuration(DURATION);

        /*
         *  创建一个移动动画效果
         *  入参的含义如下：
         *  fromXType：移动前的x轴坐标的类型
         *  fromXValue：移动前的x轴的坐标
         *  toXType：移动后的x轴的坐标的类型
         *  toXValue：移动后的x轴的坐标
         *  fromYType：移动前的y轴的坐标的类型
         *  fromYValue：移动前的y轴的坐标
         *  toYType：移动后的y轴的坐标的类型
         *  toYValue：移动后的y轴的坐标
         */
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE,mLeftDelta,Animation.ABSOLUTE,0,Animation.ABSOLUTE,mTopDelta,Animation.ABSOLUTE,0);

        /*
         *  设置动画的持续时间
         */
        translateAnimation.setDuration(DURATION);



//         /*
//         *  创建一个旋转动画对象
//         *  入参列表含义如下：
//         *  1.fromDegrees：从哪个角度开始旋转
//         *  2.toDegrees：旋转到哪个角度结束
//         *  3.pivotXType：旋转所围绕的圆心的x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
//         *  4.pivotXValue：旋转所围绕的圆心的x轴坐标,0.5f表明是以自身这个控件的一半长度为x轴
//         *  5.pivotYType：y轴坐标的类型
//         *  6.pivotYValue：y轴坐标
//         */
//        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//
//        /*
//         *  设置动画的持续时间
//         */
//        rotateAnimation.setDuration(DURATION);



         /*
         *  将两种动画效果放入同一个AnimationSet中
         */
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        //设置硬件加速
        view.setLayerType(View.LAYER_TYPE_HARDWARE,null);

         /*
         *  同时执行多个动画效果
         */
        view.startAnimation(animationSet);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新加载原图
                if (!TextUtils.isEmpty(big_path)) {
                    CeleryImageLoader.displayImage(context,big_path,(ImageView) view,null);
                }
            }
        },DURATION);

    }



    /**
     * 退出动画
     * @param context
     * @param bitmapRect 当前图片信息
     * @param view 图片显示容器
     * @param position 当前图片所处位置
     * @param bundle 传入参数
     * @param viewGroup 图片容器背景，用于渐变动画
     */
    public static void exit(final Activity context, Rect bitmapRect, View view, int position, Bundle bundle,ViewGroup viewGroup){
        initDisplay(context);

        //读取传入参数
        Rect rect = bundle.getParcelable("bounds");
        int indexPosition = bundle.getInt("indexPosition",0);
        int numColumns = bundle.getInt("numColumns",1);
        //获取gridview 分割线的宽高
        int horizontalSpacing = bundle.getInt("horizontalSpacing",0);
        int verticalSpacing = bundle.getInt("verticalSpacing",0);

        // 获取上一个界面传入的信息
        int x = rect.left;
        int y = rect.top;
        int width = rect.width();
        int height = rect.height();


        /**
         * 动态计算X,Y位置信息
         * 计算规则：
         * x位置计算：根据当前位置position除余列数-参考位置indexPosition除余列数，然后乘以宽度，就是当前应该返回的x位置点
         * y位置计算：根据当前位置position除以列数-参考位置indexPosition除以列数，然后乘以高度，就是当前应该返回的y位置点
         */
        x = x + ((position)%numColumns - (indexPosition)%numColumns)*(width+horizontalSpacing);
        y = y + ((position)/numColumns-(indexPosition)/numColumns)*(height+verticalSpacing);



        /**
         * 缩放基准点计算,分两种情况
         * 计算规则如下：
         * 1、当图片的高大于等于宽时，缩放宽的其实比例计算为：
         */
        float mWidthScale,mHeightScale;
        if (bitmapRect.height()>= bitmapRect.width()) {
            mWidthScale = (float) (width*bitmapRect.height()) / (bitmapRect.width()*mScreenHeight);
            mHeightScale = (float) height /mScreenHeight;
        }else {
            mWidthScale = (float) width / mScreenWidth;
            mHeightScale = (float) (height*bitmapRect.width()) / (bitmapRect.height()*mScreenWidth);
        }


        //平移基准点计算
        //坐标的获取设置
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);



        //计算起始点坐标
        float mLeftDelta = (x+(float)width/2) - (float)(mScreenWidth/2);
        float mTopDelta = (y+(float)height/2)-(float)(mScreenHeight/2);



        /*
        *  创建一个AnimationSet，它能够同时执行多个动画效果
        *  构造方法的入参如果是“true”，则代表使用默认的interpolator，如果是“false”则代表使用自定义interpolator
        */
        AnimationSet animationSet = new AnimationSet(true);

        /*
         *  创建一个半透明效果的动画对象，效果从完全不透明到完全透明
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);


        /*
         *  设置动画的持续时间
         */
        alphaAnimation.setDuration(DURATION);
        //设置硬件加速
        viewGroup.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        //去除缓存
        viewGroup.setPersistentDrawingCache(ViewGroup.PERSISTENT_NO_CACHE);
        //背景颜色渐变动画
        viewGroup.startAnimation(alphaAnimation);



        /*
         *  创建一个缩放效果的动画
         *  入参列表含义如下：
         *  fromX：x轴的初始值
         *  toX：x轴缩放后的值
         *  fromY：y轴的初始值
         *  toY：y轴缩放后的值
         *  pivotXType：x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
         *  pivotXValue：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
         *  pivotYType：y轴坐标的类型
         *  pivotYValue：轴的值，0.5f表明是以自身这个控件的一半长度为y轴
         */
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, mWidthScale, 1.0f, mHeightScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        /*
         *  设置动画的持续时间
         */
        scaleAnimation.setDuration(DURATION);

        /*
         *  创建一个移动动画效果
         *  入参的含义如下：
         *  fromXType：移动前的x轴坐标的类型
         *  fromXValue：移动前的x轴的坐标
         *  toXType：移动后的x轴的坐标的类型
         *  toXValue：移动后的x轴的坐标
         *  fromYType：移动前的y轴的坐标的类型
         *  fromYValue：移动前的y轴的坐标
         *  toYType：移动后的y轴的坐标的类型
         *  toYValue：移动后的y轴的坐标
         */
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,mLeftDelta,Animation.ABSOLUTE,0,Animation.ABSOLUTE,mTopDelta);

        /*
         *  设置动画的持续时间
         */
        translateAnimation.setDuration(DURATION);



         /*
         *  将两种动画效果放入同一个AnimationSet中
         */
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        //设置硬件加速
        view.setLayerType(View.LAYER_TYPE_HARDWARE,null);

         /*
         *  同时执行多个动画效果
         */
        view.startAnimation(animationSet);


        //动画完成退出当前页面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.finish();
            }
        },DURATION-20);

    }


    /**
     * 控件底部进入
     * @param aView
     * @param tView
     */
    public static void bottomEnter(final View aView, final View tView){

        /**
         * 属性动画设置成全透明
         */
        ObjectAnimator
                .ofFloat(aView,"alpha",0f,1f)
                .setDuration(DURATION)
                .start();

        aView.setVisibility(View.VISIBLE);
        tView.setVisibility(View.VISIBLE);

        /*
         *  创建一个移动动画效果
         *  入参的含义如下：
         *  fromXType：移动前的x轴坐标的类型
         *  fromXValue：移动前的x轴的坐标
         *  toXType：移动后的x轴的坐标的类型
         *  toXValue：移动后的x轴的坐标
         *  fromYType：移动前的y轴的坐标的类型
         *  fromYValue：移动前的y轴的坐标
         *  toYType：移动后的y轴的坐标的类型
         *  toYValue：移动后的y轴的坐标
         */
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0f);

                /*
                 *  设置动画的持续时间
                 */
        translateAnimation.setDuration(DURATION);

        tView.startAnimation(translateAnimation);
    }

    /**
     * 底部退出
     * @param aView
     * @param tView
     */
    public static void bottomExit(final View aView, final View tView) {

        /**
         * 属性动画设置成半透明
         */
        ObjectAnimator
                .ofFloat(aView,"alpha",1f,0f)
                .setDuration(DURATION)
                .start();

        /*
         *  创建一个移动动画效果
         *  入参的含义如下：
         *  fromXType：移动前的x轴坐标的类型
         *  fromXValue：移动前的x轴的坐标
         *  toXType：移动后的x轴的坐标的类型
         *  toXValue：移动后的x轴的坐标
         *  fromYType：移动前的y轴的坐标的类型
         *  fromYValue：移动前的y轴的坐标
         *  toYType：移动后的y轴的坐标的类型
         *  toYValue：移动后的y轴的坐标
         */
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,1.0f);

        /*
         *  设置动画的持续时间
         */
        translateAnimation.setDuration(DURATION);

        tView.startAnimation(translateAnimation);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tView.setVisibility(View.INVISIBLE);
                aView.setVisibility(View.INVISIBLE);
            }
        },DURATION);
    }




    /**
     * 获取屏幕宽、高
     * @param activity 页面
     */
    public static void initDisplay(Activity activity){
        if (mScreenWidth<=0 || mScreenHeight<=0) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mScreenWidth = size.x;
            mScreenHeight = size.y;
        }
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
           return 0;
        }
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


}
