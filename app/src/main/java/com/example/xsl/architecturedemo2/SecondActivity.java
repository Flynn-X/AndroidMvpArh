package com.example.xsl.architecturedemo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xsl.architecturedemo2.bean.User;
import com.example.xsl.architecturedemo2.di.component.DaggerActivityComponent;
import com.example.xsl.architecturedemo2.di.module.ActivityModule;
import com.example.xsl.architecturedemo2.mvp.contract.SecondActivityContract;
import com.example.xsl.architecturedemo2.mvp.presenter.SecondActivityPresenter;
import com.example.xsl.corelibrary.CeleryBaseActivity;
import com.example.xsl.corelibrary.CoreLibrary;
import com.example.xsl.corelibrary.utils.L;
import com.example.xsl.corelibrary.utils.PictureFromSysUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends CeleryBaseActivity implements SecondActivityContract.View {

    @Inject
    SecondActivityPresenter secondActivityPresenter;

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.button2)
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build().inject(this);

        Map<String, String> map = new HashMap<>();
        map.put("cookie", "test");
        map.put("cookie2", "test2");
        map.put("User-Agent", "app.");

        CoreLibrary.initHeaders(map);
        secondActivityPresenter.getData();
    }

    @Override
    public void backData(User user) {
        L.e("返回了");
        textView.setText(user.getCreated_at());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("onDestroy");
    }



    @OnClick({R.id.textView, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView:

                break;
            case R.id.button2:


                PictureFromSysUtil.OpenCarmera(SecondActivity.this);

                break;
        }
    }


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
//                    //获取选择的图片文件
//                    File album = PictureFromSysUtil.albumOnActivityResult(mContext, data);
//

                    break;
                case PictureFromSysUtil.TAKE_PHOTO:
                    //拍照跳转到系统剪切
                    PictureFromSysUtil.carmeraOnActivityResultStartPhotoZoom(mContext, data);
//                    //获取拍照照片文件
//                    File carmera = PictureFromSysUtil.carmeraOnActivityResult(mContext, data);
                    break;
                case PictureFromSysUtil.CROP_PICTURE:

                   String path =  PictureFromSysUtil.outPutUri.getPath();

                    secondActivityPresenter.upLoad(path);

                    break;
            }
        }
    }
}
