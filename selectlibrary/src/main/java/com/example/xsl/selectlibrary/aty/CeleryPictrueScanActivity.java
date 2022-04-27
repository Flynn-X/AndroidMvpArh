package com.example.xsl.selectlibrary.aty;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xsl.selectlibrary.R;
import com.example.xsl.selectlibrary.adapter.PhotoViewPager;
import com.example.xsl.selectlibrary.adapter.PictrueScanAdapter;
import com.example.xsl.selectlibrary.utils.OSUtils;
import com.example.xsl.selectlibrary.utils.OnkeyBackListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CeleryPictrueScanActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static int REQUEST_PERMISSION_CODE = 0x001;

    PhotoViewPager viewPager;
    LinearLayout cursorGroup;
    TextView countTextView;
    LinearLayout save_layout;
    //图片地址信息集合
    private List<String> list = new ArrayList<>();
    //当前选中位置
    private int indexPostion;
    //滚动的当前位置
    private int currentPosition;
    //是否显示下载按钮，默认不显示
    private boolean showSave = false;


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
        showSave = bundle.getBoolean("showSave");

        viewPager = (PhotoViewPager) findViewById(R.id.viewPager);
        cursorGroup = (LinearLayout) findViewById(R.id.cursor_group);
        countTextView = (TextView) findViewById(R.id.countTextView);
        save_layout = (LinearLayout) findViewById(R.id.save_layout);
        save_layout.setVisibility(showSave?View.VISIBLE:View.GONE);

        save_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(CeleryPictrueScanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CeleryPictrueScanActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    }else {
                        saveImage();
                    }
                }
            }
        });

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
                currentPosition = position;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                saveImage();
            }else {
                Toast.makeText(this,"请授权文件读取权限！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 保存图片
     */
    private void saveImage(){
        Glide.with(CeleryPictrueScanActivity.this).asBitmap().load(list.get(currentPosition)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                InputStream inputStream = Bitmap2IS(bitmap);
                //图片格式不要动
                String path = saveFile(inputStream, Environment.DIRECTORY_DCIM, System.currentTimeMillis()+".jpg");
                //发送广播更新相册信息
                refreshAlbum(path,false);
            }
        });
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

    private InputStream  Bitmap2IS(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    /**
     * 将数据流保存在sd卡
     * @param inputStream
     * @param folderName 文件夹路径，可包含多层,最后不需要加斜杠（例如："test/a/b"）
     * @param fileName 包括后缀 (例如：ab123.rar)
     * @return 保存的全路径
     */
    private String saveFile(InputStream inputStream, String folderName, String fileName){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("", "SD卡不存在");
            return null;
        }
        File file = null;
        //表示是内部存储文件夹路径
        if (folderName.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())){
            file = new File(folderName);
        }else {
            //可以在这里自定义路径
            file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        }
        if (!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(file.getPath()+ File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            fileOutputStream = new FileOutputStream(file1);
            long current = 0;
            while ((len = inputStream.read(buf)) != -1) {
                current += len;
                fileOutputStream.write(buf, 0, len);
            }
            fileOutputStream.flush();
            return file1.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 通知相册更新
     */
    private MediaScannerConnection mMediaScanner;
    private void refreshAlbum(final String fileAbsolutePath, final boolean isVideo) {
        mMediaScanner = new MediaScannerConnection(CeleryPictrueScanActivity.this,
                new MediaScannerConnection.MediaScannerConnectionClient() {

                    @Override
                    public void onMediaScannerConnected() {
                        if (isVideo) {
                            mMediaScanner.scanFile(fileAbsolutePath, "video/mp4");
                        } else {
                            mMediaScanner.scanFile(fileAbsolutePath, "image/jpeg");
                        }
                        Toast.makeText(CeleryPictrueScanActivity.this,"下载成功：" + fileAbsolutePath,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        mMediaScanner.disconnect();
                    }
                });
        mMediaScanner.connect();
    }

}
