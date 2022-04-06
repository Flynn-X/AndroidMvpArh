package com.example.xsl.architecturedemo2;

import android.app.Application;

import com.example.sharelibrary.CeleryShareUtils;
import com.example.xsl.corelibrary.CoreLibrary;
import com.example.xsl.corelibrary.widgets.alertdialog.CeleryAlertDialogOptions;

/**
 * Created by Zhou on 2017/4/6.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //弹出框配置
        CeleryAlertDialogOptions dialogOptions = new CeleryAlertDialogOptions()
                .setInputLineColor(0xffe60000)
                .setPositiveButtonText("Sure")
                .setNegativeButtonText("Cancel")
                .setAlertDialogCenter(true);

        CoreLibrary.init(this,true)
                .baseUrl("http://api.hahah.com/")
                .celeryBaseActivityAutoSize(false)
                .dialogOption(dialogOptions);

        new CeleryShareUtils.Build()
                .init(this,"5971bd78717c19308c000653",true)
                .qqConfig("1106228145","gW03DyVxrOThC7Yt")
                .shareChannel(CeleryShareUtils.SHARE_CHANNEL.QQ, CeleryShareUtils.SHARE_CHANNEL.QZONE
                        , CeleryShareUtils.SHARE_CHANNEL.WEIXIN, CeleryShareUtils.SHARE_CHANNEL.WEIXIN_CIRCLE,
                        CeleryShareUtils.SHARE_CHANNEL.SINA);

    }


}
