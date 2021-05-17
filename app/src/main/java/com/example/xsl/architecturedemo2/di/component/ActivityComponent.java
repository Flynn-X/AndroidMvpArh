package com.example.xsl.architecturedemo2.di.component;







import com.example.xsl.architecturedemo2.MainActivity;
import com.example.xsl.architecturedemo2.SecondActivity;
import com.example.xsl.architecturedemo2.di.module.ActivityModule;

import dagger.Component;

/**
 * Created by xsl on 2017/4/10.
 */
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(SecondActivity activity);

}

