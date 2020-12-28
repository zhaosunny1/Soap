package com.zyy.soap.factory;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by zhaoyang on 2017/3/31.
 */

public class DefaultCallFactory implements ICallFactory {

    public static DefaultCallFactory create() {
        return new DefaultCallFactory();
    }

    @Override
    public Observable convert(Observable observable) {
        return observable;
    }

    @Override
    public Flowable convert(Flowable flowable) {
        return flowable;
    }
}
