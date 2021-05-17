package com.example.xsl.selectlibrary.views;

import android.content.Context;
import android.util.AttributeSet;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xsl on 2018/2/1.
 * 重写PhotoView模拟PhotoView单击事件
 */
public class MyPhotoView extends PhotoView {

    private PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener;

    public MyPhotoView(Context context) {
        super(context);
    }

    public MyPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public MyPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        super.setOnPhotoTapListener(listener);
        this.onPhotoTapListener = listener;
    }


    /**
     * 模拟PhotoView单击事件
     * @return
     */
    public boolean performPhotoTapClick(){
        if (onPhotoTapListener != null) {
            onPhotoTapListener.onOutsidePhotoTap();
        }
        return true;
    }
}
