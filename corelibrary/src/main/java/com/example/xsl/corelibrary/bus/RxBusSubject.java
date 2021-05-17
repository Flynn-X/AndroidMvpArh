package com.example.xsl.corelibrary.bus;


import com.example.xsl.corelibrary.utils.L;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;


/**
 * @author xsl
 * @version 1.0
 * @date 2017/4/11
 * @description
 * 考虑异常情况，订阅失败重新订阅
 */
public abstract class RxBusSubject extends Subject {
    private EventBase eventBase;

    @Override
    protected void subscribeActual(Observer observer) {

    }

    @Override
    public boolean hasObservers() {
        return false;
    }

    @Override
    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return false;
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }



    @Override
    public void onSubscribe(Disposable d) {
        L.e("RxBus onSubscribe");
    }



    @Override
    public void onNext(Object value) {
        this.eventBase = (EventBase) value;
        L.e(eventBase.getData().toString());
    }

//    @Override
//    public void onNext(T value) {
//        this.eventBase = (EventBase) value;
//        L.e(eventBase.getData().toString());
//    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //订阅失败，重新订阅
        RxBus.getInstance().post(eventBase);
    }

    @Override
    public void onComplete() {
        L.e("RxBus onComplete");
//        //订阅失败，重新订阅
//        RxBus.getInstance().post(value);
    }

}
