package com.example.xsl.selectlibrary.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xsl.selectlibrary.R;
import com.example.xsl.selectlibrary.adapter.PhotoViewPager;
import com.example.xsl.selectlibrary.adapter.PictrueScanAdapter;
import com.example.xsl.selectlibrary.utils.OSUtils;
import com.example.xsl.selectlibrary.utils.OnkeyBackListener;

import java.util.ArrayList;
import java.util.List;

public class CeleryPictrueScanActivity extends AppCompatActivity {


    PhotoViewPager viewPager;
    LinearLayout cursorGroup;
    TextView countTextView;
    //图片地址信息集合
    private List<String> list = new ArrayList<>();
    //当前选中位置
    private int indexPostion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 【非flyme和miui系统只有Android 6.0+才适配 沉浸状态栏】
         * flyme5+ 和 miui6+开始适配Android沉浸栏
         * 其他机型从Android 6.0+开始适配沉浸栏
         */
        if ((OSUtils.getRomType().equals(OSUtils.ROM_TYPE.OTHER)|| OSUtils.getRomType().equals(OSUtils.ROM_TYPE.EMUI))
                &&  Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            initStatusBar(true);
        }else {
            initStatusBar(false);
        }
        setContentView(R.layout.activity_celery_pictrue_scan);
        init();
    }


    /**
     * 初始化界面
     */
    private void init(){
        // 获取上一个界面传入的信息
        Bundle bundle = getIntent().getBundleExtra("bundle");
        indexPostion = bundle.getInt("indexPosition", 0);
        list = bundle.getStringArrayList("imgs");


        viewPager = (PhotoViewPager) findViewById(R.id.viewPager);
        cursorGroup = (LinearLayout) findViewById(R.id.cursor_group);
        countTextView = (TextView) findViewById(R.id.countTextView);

        if (list.size()>9){
            countTextView.setVisibility(View.VISIBLE);
            cursorGroup.setVisibility(View.GONE);
            countTextView.setText((indexPostion+1)+"/" + list.size());
        }else {
            countTextView.setVisibility(View.GONE);
            cursorGroup.setVisibility(View.VISIBLE);
            addView(indexPostion);
        }

        viewPager.setAdapter(new PictrueScanAdapter(CeleryPictrueScanActivity.this,viewPager,bundle));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (list.size()>9) {
                    countTextView.setText((position+1) + "/" + list.size());
                }else {
                    addView(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (list.size()>0) {
            viewPager.setCurrentItem(indexPostion);
        }

        viewPager.getCurrentItem();
    }


    /**
     * 动态改变cursor
     */
    private void addView(int currentSelect){
        cursorGroup.removeAllViews();
        for (int i=0;i<list.size();i++) {
            if (i==currentSelect){
                View dotViewNel = new View(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(this,8),dip2px(this,8));
                layoutParams.setMargins(dip2px(this,5),0,0,0);
                dotViewNel.setLayoutParams(layoutParams);
                dotViewNel.setBackgroundResource(R.drawable.dotview_nel);
                cursorGroup.addView(dotViewNel);
            }else {
                View dotViewNor = new View(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(this,8), dip2px(this,8));
                layoutParams.setMargins(dip2px(this,5),0,0,0);
                dotViewNor.setLayoutParams(layoutParams);
                dotViewNor.setBackgroundResource(R.drawable.dotview_nor);
                cursorGroup.addView(dotViewNor);
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 是否需要状态栏，不需要布局将会延伸到状态栏
     * @param isNeed
     */
    protected void initStatusBar(boolean isNeed) {
        if (isNeed) {
            //需要用到系统状态栏，统一设置成黑色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.BLACK);
            }

        } else {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }



    /**
     * 按键触发的事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            //调用返回
            if (onKeyBackListener != null) {
                onKeyBackListener.onBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回监听回调
     */
    private static OnkeyBackListener onKeyBackListener;
    public static void setOnKeyBackListener(OnkeyBackListener obk){
        onKeyBackListener = obk;
    }

    @Override
    public void finish() {
        super.finish();
        //去除退出转场动画
        overridePendingTransition(0,0);
    }
}
