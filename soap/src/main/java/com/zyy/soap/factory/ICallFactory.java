package com.zyy.soap.factory;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Java App ,Android App use different Factory
 * Created by zhaoyang on 2017/3/31.
 */

public interface ICallFactory {


    <T> Observable<T> convert(Observable<T> observable);

    <T> Flowable<T> convert(Flowable<T> flowable);
}
