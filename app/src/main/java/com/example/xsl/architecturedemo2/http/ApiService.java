package com.example.xsl.architecturedemo2.http;


import com.example.xsl.architecturedemo2.bean.PayDepositBean;
import com.example.xsl.architecturedemo2.bean.User;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Zhou on 2017/4/6.
 * 统一接口定义，采用retrofit2，配合Rxjava 实现调用。
 */
public interface ApiService {

    /**
     *  南海项目测试地址
     */
    String baseUrl = "https://api.github.com/users/list?sort=desc";


    /***********************************************************************【资讯Start】************************************************************************************************/

    //获取资讯
    @GET(baseUrl + "/users/list?sort=desc")
    Call<User> getUsers();


    @Multipart
    @POST("https://ydtest.e-lingcloud.com/yanda_staff/member/uploadHeadImg.ihtml")
    Call<User> upload(@Part("type") RequestBody body, @Part MultipartBody.Part part);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("https://ydtest.e-lingcloud.com/yanda_staff/deposit/getUnpaidCustomer.ihtml")
    Observable<PayDepositBean> intentClient();


}
