package com.example.xsl.corelibrary.http;

/**
 * Created by xsl on 2017/12/7.
 * 请求返回数据拦截
 */
public interface onResponseBodyInterceptorListener {
    /**
     * 请求返回数据拦截
     * @param url 响应地址
     * @param headers 响应头
     * @param string 响应字符串数据拦截
     */
    void responseBody(String url, String headers, String string);


}
