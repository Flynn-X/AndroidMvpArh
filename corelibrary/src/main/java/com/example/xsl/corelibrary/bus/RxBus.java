package com.example.xsl.corelibrary.bus;

import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 * 用rxjava 2.0 封装的RxBus用于代替eventbus功能实现事件传递
 * 默认情况下在UI线程中执行，有需要耗时操作，请在接收数据后自行开启子线程执行。
 *
 * 使用方法：
 * 一、发送数据： RxBus.getInstance().post(new EventBase("RxBus","RxBus 更新"));
 * 二、接收数据：
 * 1、注册：  RxBus.getInstance().register(this);
 * 2、接收数据：@RxBusEvent(value = "")
 *              public void onEvent(EventBase eventBase){
 *              textView.setText(eventBase.getData().toString());
 *              }
 * 3、注销订阅： RxBus.getInstance().unRegister(this);
 * 三、注意：同事提议我考虑订阅失败要重新订阅，因为目前未发现失败的情况，即使失败也有可能无限失败，因为操作是在UI线程，为防止死循环
 * 有订阅失败请自行查找日志，并找出原因。
 * （如发现特殊情况下连续失败请替换成系统广播或者 eventbus 实现事件传递，此封装控件并不会和任何第三方控件冲突，也并不依赖除rxjava基础库以外任何第三方控件）
 */
public class RxBus {

//    RxBus.getInstance().register(this);
//    @RxBusEvent(value = "")
//    public void onEvent(EventBase eventBase) {
//        textView.setText(eventBase.getData().toString());
//    }

    private final Subject rxBus = PublishSubject.create().toSerialized();
    private Map<String,Disposable> mDisposables = new HashMap<>();

    public static RxBus getInstance() {
        return RxBusInstance.instance;
    }


    private static class RxBusInstance{
        private static final RxBus instance = new RxBus();
    }

    /**
     * 发送信息
     * @param eventBase
     */
    public void post(EventBase eventBase){
        rxBus.onNext(eventBase);
    }



    /**
     * 注册订阅
     * @param obj
     */
    public void register(final Object obj){
        if(obj == null) {
            return;
        }
        Disposable disposable =  rxBus.ofType(EventBase.class)
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventBase>() {
                    @Override
                    public void accept(EventBase eventBase) throws Exception {
                        RxBus.this.call(obj, eventBase);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });

        putDisposable(obj, disposable);
    }



    private void call(Object obj, EventBase eventBase){
        Class<?> cls = obj.getClass();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RxBusEvent.class)) {
                RxBusEvent event = method.getAnnotation(RxBusEvent.class);
                String value = event.value();
                String tag = eventBase.getTag();
                try {
                    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(value)) {
                        method.setAccessible(true);
                        method.invoke(obj, eventBase);
                    } else {
                        if (tag.equals(value)) {
                            method.setAccessible(true);
                            method.invoke(obj, eventBase);
                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注销订阅
     * @param obj
     */
    public void unRegister(Object obj){
        if(obj == null)
            return;
        Disposable subscription = getDisposable(obj);
        if(subscription != null && !subscription.isDisposed()){
            subscription.dispose();
        }
    }

    /**
     * map推入订阅
     * @param obj
     * @param disposable
     */
    private void putDisposable(Object obj,Disposable disposable){
        String className = obj.getClass().getName();
        for (Map.Entry<String, Disposable> entry : mDisposables.entrySet()) {
            if(className.equals(entry.getKey())){
                if(!entry.getValue().isDisposed()){
                    entry.getValue().dispose();
                }
                mDisposables.remove(className);
                break;
            }
        }
        mDisposables.put(className,disposable);
    }

    /**
     * 获取订阅
     * @param obj
     * @return
     */
    private Disposable getDisposable(Object obj){
        String className = obj.getClass().getName();
        for (Map.Entry<String, Disposable> entry : mDisposables.entrySet()) {
            if(className.equals(entry.getKey())){
                Disposable value = entry.getValue();
                mDisposables.remove(entry.getKey());
                return value;
            }
        }
        return null;
    }

}