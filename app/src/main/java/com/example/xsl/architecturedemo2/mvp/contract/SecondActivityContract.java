package com.example.xsl.architecturedemo2.mvp.contract;

import com.example.xsl.architecturedemo2.bean.User;

/**
 * Created by xsl on 2017/12/20.
 */

public interface SecondActivityContract {
    interface Model {
    }

    interface View {

        void backData(User user);




    }

    interface Presenter {

        void getData();


        void upLoad(String path);
    }
}
