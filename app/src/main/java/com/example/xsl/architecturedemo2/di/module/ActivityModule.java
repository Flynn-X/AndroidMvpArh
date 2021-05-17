package com.example.xsl.architecturedemo2.di.module;

import android.app.Activity;
import android.content.Context;

import com.example.xsl.corelibrary.mvp.BaseIView;

import dagger.Module;
import dagger.Provides;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/24
 * @description
 */
@Module
public class ActivityModule {

    private Activity activity;
    private BaseIView baseIView;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    public ActivityModule(Activity activity, BaseIView baseIView) {
        this.activity = activity;
        this.baseIView = baseIView;
    }

    @Provides
    Activity provideActivity(){
        return activity;
    }

    @Provides
    Context provideContext(){
        return activity;
    }

    @Provides
    BaseIView provideBaseIView(){
        return baseIView;
    }

}