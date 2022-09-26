package com.example.xsl.architecturedemo2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.sharelibrary.CeleryShareDialog;
import com.example.xsl.architecturedemo2.di.component.DaggerActivityComponent;
import com.example.xsl.architecturedemo2.di.module.ActivityModule;
import com.example.xsl.architecturedemo2.mvp.contract.MainActivityContract;
import com.example.xsl.architecturedemo2.mvp.presenter.MainActivityPresenter;
import com.example.xsl.corelibrary.CeleryBaseActivity;
import com.example.xsl.corelibrary.CoreLibrary;
import com.example.xsl.corelibrary.bus.EventBase;
import com.example.xsl.corelibrary.bus.RxBus;
import com.example.xsl.corelibrary.bus.RxBusEvent;
import com.example.xsl.corelibrary.http.DownLoadProgressListener;
import com.example.xsl.corelibrary.http.OkhttpClientUtils;
import com.example.xsl.corelibrary.http.retrofiturlmanager.RetrofitUrlManager;
import com.example.xsl.corelibrary.imageloader.ImageLoader;
import com.example.xsl.corelibrary.utils.CelerySpUtils;
import com.example.xsl.corelibrary.utils.CeleryToast;
import com.example.xsl.corelibrary.utils.CeleryToolsUtils;
import com.example.xsl.corelibrary.utils.CoreConstants;
import com.example.xsl.corelibrary.utils.L;

import com.example.xsl.corelibrary.utils.PermissionUtils;
import com.example.xsl.corelibrary.utils.PictureFromSysUtil;
import com.example.xsl.corelibrary.widgets.alertdialog.AvatarDialog;
import com.example.xsl.corelibrary.widgets.alertdialog.PermissionDescriptionDialog;
import com.example.xsl.selectlibrary.aty.CelerySelectPictureActivity;
import com.example.xsl.selectlibrary.utils.SelectLibrary;
import com.example.xsl.xunfeiyuyinlibrary.CelerySpeach;
import com.example.xsl.xunfeiyuyinlibrary.CelerySpeachLibrary;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@Route(path = "/main/test")
public class MainActivity extends CeleryBaseActivity implements MainActivityContract.View{

    @Inject
    MainActivityPresenter secondActivityPresenter;

    private LinearLayout activityMain;
    private TextView textView;
    private Button button,button7,button8;
    private ImageView imageView;

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this, this)).build().inject(this);
        assignViews();
        RxBus.getInstance().register(this);
        CelerySpeachLibrary.init(this);
        RxPermissions rxPermissions = new RxPermissions(this);

        //请求头token设置
        Map<String,String> map = new HashMap<>();
        map.put("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1NTE3ODAzNzAwMjYsImlhdCI6MTU1MTE3NTU3MDAyNiwibG9naW5OYW1lIjoiYWRtaW4ifQ.mEBy7vOqbpQsT4FZglmhhMrV59pKjRXX0o1sviBR_Dg");
        map.put("sysUserId","1");
        CoreLibrary.initHeaders(map);

//        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(granted -> {
//                    if (granted) {
//                        // All requested permissions are granted
//                        L.e("动态权限请求成功");
//                    } else {
//                        // At least one permission is denied
//                        L.e("动态权限请求失败");
//                    }
//                });

//        secondActivityPresenter.getUsers();

    }

    private void assignViews() {
        activityMain = (LinearLayout) findViewById(R.id.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        imageView = (ImageView) findViewById(R.id.image);


        button.setOnClickListener(onClickListener);
        button7.setOnClickListener(onClickListener);
        button8.setOnClickListener(onClickListener);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }

        CommonTabLayout  tablayout = (CommonTabLayout) findViewById(R.id.tablayout);
        tablayout.setTabData(mTabEntities);

        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });



//        if(Build.VERSION.SDK_INT>=23){
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW
//                    ,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS,Manifest.permission.CAMERA};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);
//        }

    }

    int index = 0;

    /**
     * 单击事件
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.button:

//                    PermissionDescriptionDialog.show(MainActivity.this, getString(R.string.READ_PHONE_STATE_DES));

                    String[] permissions = new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                    };
                    String[] descriptions = new String[]{
                            getString(R.string.READ_PHONE_STATE_DES),
                    };
                    PermissionUtils.requestMultiplePermission(MainActivity.this
                        , permissions, descriptions, new PermissionUtils.OnRequestPermissionListener() {
                        @Override
                        public void finish(boolean allGranted) {
                            if (allGranted){
                                L.e("用户同意了权限：");

                            }else {
                                CeleryToast.showShort(MainActivity.this,getString(R.string.PERMISSIONS_NOT_GRANTED));
                            }
                        }
                    });







//                    ++index;
//                    RetrofitUrlManager.getInstance().setGlobalDomain("https://flynnx.com/" + index);
//                    secondActivityPresenter.getUsers();

//                    AvatarDialog.show(MainActivity.this);

//                    intent = new Intent(mContext,SecondActivity.class);
//                    startActivity(intent);

//                    new OkhttpClientUtils().downLoad(MainActivity.this
//                            , "https://pro-app-tc.fir.im/491298456433ed1e30bc06862c0439c3f5c2fef5.apk?sign=6fe0efe3983aa040d44daeda62da09de&t=5a61620e"
//                            , new DownLoadProgressListener() {
//                        @Override
//                        public void progress(float percent, boolean done) {
//                            if (!done) {
//                                L.e("下载进度：" + percent);
//                            }else {
//                                L.e("下载完成：");
//                            }
//                        }
//
//                        @Override
//                        public void write(InputStream inputStream) {
//                           String path =  CeleryToolsUtils.savaFile(inputStream,"test/a/b",System.currentTimeMillis()+".apk");
//                            CeleryToolsUtils.installApp(MainActivity.this,new File(path),100);
//                           L.e("保存路径：" + path);
//                        }
//
//
//                        @Override
//                        public void onFailure(int code, String Msg, Exception e) {
//                            L.e(Msg);
//                        }
//                    });



//                    new RxPermissions(MainActivity.this)
//                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                            .subscribe(grand ->{
//                                if (grand){
//                                    CeleryShareDialog.show(MainActivity.this, "标题", "http://www.baidu.com", "描述", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513855037916&di=1e1072282e68e22985c1bcbce1c8f2e3&imgtype=0&src=http%3A%2F%2Fwww.visionunion.com%2Fdata%2Ffile%2Fimg%2F20130715%2F20130715000803.jpg", new UMShareListener() {
//                                        @Override
//                                        public void onStart(SHARE_MEDIA share_media) {
//                                            L.e("onStart");
//                                        }
//
//                                        @Override
//                                        public void onResult(SHARE_MEDIA share_media) {
//                                            L.e("onResult");
//                                        }
//
//                                        @Override
//                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                                            L.e("onError");
//                                        }
//
//                                        @Override
//                                        public void onCancel(SHARE_MEDIA share_media) {
//                                            L.e("onCancel");
//                                        }
//                                    });
//
//                                }else {
//
//                                }
//
//                            });


//                    intent = new Intent(MainActivity.this,SecondActivity.class);
//                    startActivity(intent);


//                    CeleryAlertDialog.show(MainActivity.this, "提示", "确定要删除？" + System.currentTimeMillis(), new AlertDialogOnclickListener() {
//                        @Override
//                        public void positiveClick(int which, String content) {
//
//                        }
//
//                        @Override
//                        public void negativeClick(int which, String content) {
//
//                        }
//                    });


//                    // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
//                    ARouter.getInstance().build("/moduel/test").navigation();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            RxBus.getInstance().post(new EventBase("11","rxbus"));
//                        }
//                    }).start();
                    break;
                case R.id.button7:
//                    startActivity(new Intent(MainActivity.this,SecondActivity.class));
//                    intent = new Intent(MainActivity.this, CelerySelectPictureActivity.class);
//                    intent.putExtra("isStatusBarDarkText", true);
//                    intent.putExtra("exResId", R.mipmap.ic_launcher);
//                    intent.putExtra("exTittleColor", Color.GREEN);
//                    intent.putExtra("exConfirmTextColor", Color.CYAN);
//                    intent.putExtra("limit", 9);
//                    intent.putExtra("data", (Serializable) list);
//                    startActivityForResult(intent, 100);


                    SelectLibrary.openList(MainActivity.this)
                            .setStatusBarDarkText(true)
                            .setLimit(4)
                            .setConfirmTextColor(0xffe60000)
                            .startForResult(100);

//                    SelectLibrary.openList(MainActivity.this)
//                            .setBackResId(R.mipmap.celery_left)
//                            .setTitleColor(0xffffffff)
//                            .setConfirmTextColor(0xffffffff)
//                            .setLimit(9)
//                            .setStatusBarDarkText(false)
//                            .setSelectedList(list)
//                            .setTabBgColor(R.color.colorAccent)
//                            .setTabTextColor(R.color.white)
//                            .startForResult(100);


//                    LoadingDialog.show(MainActivity.this,"进度2-----222");

//                    CeleryAlertDialog.showEdit(MainActivity.this, "提示", "请输入你的名字","",0, new AlertDialogOnclickListener() {
//                        @Override
//                        public void positiveClick(int which, String content) {
//
//                        }
//
//                        @Override
//                        public void negativeClick(int which, String content) {
//
//                        }
//                    });
//                    ARouter.getInstance().build("/moduel02/test").navigation();
//                    Intent intent = new Intent(MainActivity.this, CelerySelectPictureActivity.class);
//                    intent.putExtra("data", (Serializable) list);
//                    startActivityForResult(intent,101);
                    break;
                case R.id.button8:
                    String content = "韩联社4月17日报道称，据消息人士17日透露，朝鲜驻华、驻俄大使在朝鲜重要节日太阳节（已故领导人金日成生日）前已返回朝鲜。\n" +
                            "　　上述报道称，两名大使并未出席在当地举行的庆祝太阳节的晚宴，据此推测两人已归朝。朝鲜驻华使馆每年举办庆祝太阳节的宴会，但上周末朝鲜驻华大使池在龙没有现身，而是由朝鲜驻华大使馆公使朴明浩作为“临时代办”出席活动。根据规定，大使离开所驻国时应向所驻国政府通报“临时代办”人选，预计池在龙可能在12日前返回朝鲜。\n" +
                            "　　另外，朝鲜驻俄使馆在美国国务卿蒂勒森访俄第二天的13日在莫斯科举办庆祝太阳节宴会，朝鲜驻俄大使金亨俊当天也未出席。有消息称，他已于本月11日途经北京回到朝鲜。\n" +
                            "　　中国外交部发言人陆慷17日表示，留意到朝鲜16日再次试射弹道导弹，重申当前半岛局势已经高度敏感复杂及高危，呼吁有关各方避免采取相互刺激火上加油的行动，应致力降低紧张的局势。陆慷还指出，中国不是半岛核问题的始作俑者，不是问题产生的症结和关键，因此解决问题需要所有关键方双向而行。";
                    CelerySpeach.startSpeach(MainActivity.this, "button8" ,content , null);

                    break;
            }
        }
    };



//    private   List<String> list = new ArrayList<>();
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        CeleryShareDialog.onActivityResult(MainActivity.this,requestCode,resultCode,data);
//        if (resultCode == RESULT_OK){
//            switch (requestCode){
//                case 100:
//                    list = (List<String>) data.getSerializableExtra("data");
//                    for (String string:list){
//                        Log.e("返回图片路径",string);
//                    }
//                    break;
//            }
//        }
//    }


    /**
     * onActivityResult使用方法示例
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PictureFromSysUtil.SELECT_PICTURES:
                    //跳转到系统剪切
                    PictureFromSysUtil.albumOnActivityResultStartPhotoZoom(mContext, data);
                    //获取选择的图片文件
                    File album = PictureFromSysUtil.albumOnActivityResult(mContext, data);
                    break;
                case PictureFromSysUtil.TAKE_PHOTO:
                    //拍照跳转到系统剪切
                    PictureFromSysUtil.carmeraOnActivityResultStartPhotoZoom(mContext, data);
                    //获取拍照照片文件
                    File carmera = PictureFromSysUtil.carmeraOnActivityResult(mContext, data);
                    break;
                case PictureFromSysUtil.CROP_PICTURE:
                    //获取剪切后文件
                    Bitmap bitmap  = BitmapFactory.decodeFile(PictureFromSysUtil.outPutUri.getPath());
                    imageView.setImageBitmap(bitmap);
                    break;
            }
        }
    }


    @RxBusEvent(value = "11")
    public void onEvent(final EventBase eventBase){
        L.e(eventBase.getData().toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(eventBase.getData().toString());
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }


    @Override
    public LifecycleTransformer bindLifecycle() {
        return this.bindToLifecycle();
    }
}
