package com.example.xsl.corelibrary.mvp.biz;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import com.example.xsl.corelibrary.http.RetrofitClientUtil;
import com.example.xsl.corelibrary.mvp.presenter.AlertDialogOnclickListener;
import com.example.xsl.corelibrary.mvp.presenter.BasePresenter;
import com.example.xsl.corelibrary.utils.CeleryAlertDialog;
import com.example.xsl.corelibrary.utils.CeleryToast;
import com.example.xsl.corelibrary.widgets.LoadingDialog;

import java.util.Map;

import javax.inject.Inject;
import retrofit2.Retrofit;

/**
 * Created by xsl on 2017/3/15.
 * 对BasePresenter定义的接口进行实现
 *
 * @author xsl
 * @version 2.0
 * @date 2017/11/4
 * @description
 * 1、调整类名规范：BasePresenterlmpl
 * 2、AlertDialog 调取方式调整
 * 3、增加showAlertDialogEdit
 *
 * @author xsl
 * @version 2.1
 * @date 2017/12/19
 * @description
 * 1、增加生命周期监听
 * 2、当前页面生命周期结束的时候
 *
 */
public class BasePresenterlmpl implements BasePresenter {

    protected Activity mContext;
    private ViewGroup viewGroup;
    protected Retrofit retrofit = null;


    @Inject
    public BasePresenterlmpl(Activity activity) {
        this.mContext = activity;
        retrofit = RetrofitClientUtil.getRetrofit(activity);
    }


    @Override
    public void showAlertDialog(String tittle, String content, final AlertDialogOnclickListener alertDialogOnclickListener) {
        CeleryAlertDialog.show(mContext,tittle, content, new AlertDialogOnclickListener() {
            @Override
            public void positiveClick(int which, String content) {
                alertDialogOnclickListener.positiveClick(which,content);
            }

            @Override
            public void negativeClick(int which, String content) {
                alertDialogOnclickListener.negativeClick(which,content);
            }
        });
    }

    @Override
    public void showAlertDialogEdit(String tittle, String hint, String inputText, int inputType, final AlertDialogOnclickListener alertDialogOnclickListener) {
        CeleryAlertDialog.showEdit(mContext, tittle, hint,inputText,inputType, new AlertDialogOnclickListener() {
            @Override
            public void positiveClick(int which, String content) {
                alertDialogOnclickListener.positiveClick(which,content);
            }

            @Override
            public void negativeClick(int which, String content) {
                alertDialogOnclickListener.negativeClick(which,content);
            }
        });
    }

    @Override
    public void showToast(String content) {
        CeleryToast.showShort(mContext,content);
    }


    @Override
    public void showLoadingDialog() {
        LoadingDialog.show(mContext,"正在加载...");
    }

    /**
     * 过时api，请参考 showLoadingDialog()；
     * @param context
     */
    @Deprecated
    @Override
    public void showLoadingDialog(Context context) {
        LoadingDialog.show(context,"正在加载...");
    }

    @Override
    public void showLoadingDialog(String hint) {
        LoadingDialog.show(mContext,hint);
    }

    /**
     * 过时api 请参考 showLoadingDialog(String hint)；
     * @param context
     * @param hint
     */
    @Deprecated
    @Override
    public void showLoadingDialog(Context context, String hint) {
        LoadingDialog.show(context,hint);
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.dismiss();
    }


}
