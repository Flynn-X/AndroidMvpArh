package com.example.xsl.architecturedemo2.mvp.contract;

import com.example.xsl.corelibrary.mvp.BaseIView;

/**
 * Created by xsl on 2017/12/20.
 */

public interface MainActivityContract {
    interface Model {
    }

    interface View extends BaseIView{
    }

    interface Presenter {

        void getUsers();
    }
}
