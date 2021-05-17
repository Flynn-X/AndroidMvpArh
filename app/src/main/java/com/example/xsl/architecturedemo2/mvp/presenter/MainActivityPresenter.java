package com.example.xsl.architecturedemo2.mvp.presenter;import android.app.Activity;import com.example.xsl.architecturedemo2.MainActivity;import com.example.xsl.architecturedemo2.bean.PayDepositBean;import com.example.xsl.architecturedemo2.bean.User;import com.example.xsl.architecturedemo2.http.ApiService;import com.example.xsl.architecturedemo2.http.HttpUtils;import com.example.xsl.architecturedemo2.mvp.contract.MainActivityContract;import com.example.xsl.corelibrary.mvp.BaseIView;import com.example.xsl.corelibrary.mvp.biz.BasePresenterlmpl;import com.example.xsl.corelibrary.utils.L;import com.google.gson.Gson;import javax.inject.Inject;import io.reactivex.Observer;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.disposables.Disposable;import io.reactivex.schedulers.Schedulers;import okhttp3.MediaType;import okhttp3.RequestBody;import retrofit2.Call;import retrofit2.Callback;import retrofit2.Response;/** * Created by xsl on 2017/12/20. */public class MainActivityPresenter extends BasePresenterlmpl implements MainActivityContract.Presenter {    private MainActivity mainActivity;    private ApiService apiService = HttpUtils.getService(retrofit);    private MainActivityContract.View view;    @Inject    public MainActivityPresenter(Activity activity, BaseIView baseIView) {        super(activity);        if (activity instanceof MainActivity){            mainActivity = (MainActivity) activity;        }        view = (MainActivityContract.View) baseIView;    }    @Override    public void getUsers() {//        Call<User> call = apiService.getUsers();//        call.enqueue(new Callback<User>() {//            @Override//            public void onResponse(Call<User> call, Response<User> response) {////            }////            @Override//            public void onFailure(Call<User> call, Throwable t) {////            }//        });////        view.bindLifecycle();        apiService.intentClient()                .compose(view.bindLifecycle())                .subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribeWith(new Observer<PayDepositBean>() {                    @Override                    public void onSubscribe(Disposable d) {                    }                    @Override                    public void onNext(PayDepositBean response) {                        if (response.getCode() == 200) {                        } else {                            L.e(response.getMsg());                        }                    }                    @Override                    public void onError(Throwable e) {                        e.printStackTrace();                    }                    @Override                    public void onComplete() {                    }                });    }}