package com.example.xsl.architecturedemo2.di.module;



import android.app.Fragment;
import dagger.Module;
import dagger.Provides;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/24
 * @description
 */
@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment provideFragment(){
        return fragment;
    }

}