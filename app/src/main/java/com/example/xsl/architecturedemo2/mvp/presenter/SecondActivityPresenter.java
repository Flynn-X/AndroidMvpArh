package com.example.xsl.architecturedemo2.mvp.presenter;

import android.app.Activity;

import com.example.xsl.architecturedemo2.SecondActivity;
import com.example.xsl.architecturedemo2.bean.User;
import com.example.xsl.architecturedemo2.http.ApiService;
import com.example.xsl.architecturedemo2.http.HttpUtils;
import com.example.xsl.architecturedemo2.mvp.contract.SecondActivityContract;
import com.example.xsl.corelibrary.mvp.biz.BasePresenterlmpl;
import com.example.xsl.corelibrary.utils.L;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xsl on 2017/12/20.
 */

public class SecondActivityPresenter extends BasePresenterlmpl implements SecondActivityContract.Presenter {

    private SecondActivity secondActivity;
    private ApiService apiService = HttpUtils.getService(retrofit);

    @Inject
    public SecondActivityPresenter(Activity activity) {
        super(activity);
        if (activity instanceof SecondActivity){
            secondActivity = (SecondActivity) activity;
        }
    }

    @Override
    public void getData() {
        Call<User> call = apiService.getUsers();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    secondActivity.backData(response.body());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                L.e(t.getMessage());
            }
        });

    }

    @Override
    public void upLoad(String path) {

        File file = new File(path);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "1";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Call<User> call = apiService.upload(description,body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                L.e(t.getMessage());
            }
        });

    }
}
