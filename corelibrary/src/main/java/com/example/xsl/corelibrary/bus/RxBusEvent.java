package com.example.xsl.corelibrary.bus;

import android.support.v7.app.AppCompatActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 * value 填写过滤固定消息
 * value 不填接收所有消息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RxBusEvent {
    String value();
}