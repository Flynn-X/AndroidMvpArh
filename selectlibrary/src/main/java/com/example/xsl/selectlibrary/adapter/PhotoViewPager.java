package com.example.xsl.selectlibrary.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/5/26
 * @description
 * 解决  photoview 与viewpager 组合时 图片缩放的错误 ；异常：.IllegalArgumentException: pointerIndex out of range
 */
public class PhotoViewPager extends ViewPager {


    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

}