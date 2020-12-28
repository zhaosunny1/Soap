package com.zyy.soap;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.zyy.soap.factory.DefaultCallFactory;
import com.zyy.soap.factory.ICallFactory;
import com.zyy.soap.improved.RxLog;
import com.zyy.soap.improved.RxService;
import com.zyy.soap.interfaces.ISoapRequest;
import com.zyy.soap.result.ResultUtil;
import com.zyy.soap.service.SoapService;
import com.zyy.soap.utils.AnnonationsUtil;
import com.zyy.soap.utils.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;


/**
 * 用于Webservice protocol
 * <example>
 * Soap soap =new Soap.Builder()
 * .baseUrl("http://127.0.0.1")
 * .create();
 * Api api = soap.create(Api.class);
 * api.login("username","password","name")
 * .....
 * ......
 * </example>
 * Created by zhaoyang on 2017/1/19.
 */
@SuppressWarnings("unchecked")
public final class Soap {

    private String baseUrl = "";
    private static boolean DEBUG = false;

    //用于配置旧的Ksoap2util,rxapiutil,rxlog
    public static String NAMESPACE = "";
    public static String ENDPOINT = "";
    private ICallFactory callFactory;
    private OkHttpClient okHttpClient;
    private SYSTEM system;

    public enum SYSTEM {MINBAO, JDYZB}


    Soap(String baseUrl, ICallFactory callFactory, boolean isNew,
         OkHttpClient okHttpClient, SYSTEM system) {
        this.baseUrl = baseUrl;
        this.callFactory = callFactory;
        RxService.isNew = isNew;
        this.okHttpClient = okHttpClient;
        this.system = system;
    }

    public static void setDEBUG(boolean DEBUG) {
        RxLog.setDEBUG(DEBUG);
        Soap.DEBUG = DEBUG;
    }

    public static final class Builder {
        private String baseUrl;
        private ICallFactory callFactory;
        private boolean isNew = false;
        private OkHttpClient okHttpClient;
        private SYSTEM builderSystem = SYSTEM.MINBAO;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder isNewSoap(boolean isNew) {
            this.isNew = isNew;
            return this;
        }

        public Builder system(SYSTEM sys) {
            this.builderSystem = sys;
            return this;
        }


        public Builder callFactory(ICallFactory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder client(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Soap build() {
            if (null == callFactory) {
                return new Soap(baseUrl, DefaultCallFactory.create(),
                        isNew, okHttpClient, builderSystem);
            } else return new Soap(baseUrl, callFactory, isNew,
                    okHttpClient, builderSystem);
        }
    }

    /**
     * 基本的URL
     */

    public <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, final Method method, final Object... args)
                            throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        if (callFactory == null) {
                            throw new IllegalArgumentException("call facotry must be instantce");
                        }
                        Class<?> responseClass = method.getReturnType();
                        if (responseClass == Observable.class) {
                            return callFactory.convert(createObservable(service, method, baseUrl, args));
                        } else if (responseClass == Flowable.class) {
                            return callFactory.convert(createFloawable(service, method, baseUrl, args));
                        } else if (responseClass == String.class) {
                            ISoapRequest mSoapRequest = AnnonationsUtil.
                                    transformInvokeToRequest(service, method, args, baseUrl);
                            return RxService.call(
                                    mSoapRequest.getNameSpace(),
                                    mSoapRequest.getEndPoint(),
                                    mSoapRequest.getMethodName(),
                                    mSoapRequest.getParams(), okHttpClient);
                        } else {
                            throw new IllegalArgumentException("unknown return type,you must use Observable,Flowable,String");
                        }
                    }
                });
    }

    private Observable createObservable(final Class<?> service, final Method method,
                                        final String baseUrl, final Object... args) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Type responseType = Utils.getCallResponseType(method.getGenericReturnType());
                try {
                    ISoapRequest mSoapRequest = AnnonationsUtil.
                            transformInvokeToRequest(service, method, args, baseUrl);
                    String result = RxService.call(
                            mSoapRequest.getNameSpace(),
                            mSoapRequest.getEndPoint(),
                            mSoapRequest.getMethodName(),
                            mSoapRequest.getParams(), okHttpClient);
                    if (!ResultUtil.isError(result)) {
                        Gson gson = new Gson();
                        if (responseType != String.class) {
                            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(responseType));
                            if (!emitter.isDisposed()) {
                                emitter.onNext(adapter.fromJson(result));
                            }
                        } else {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(result);
                            }
                        }
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }

                    } else {
                        if (!emitter.isDisposed()) {
                            emitter.onError(new Throwable(result));
                        }
                    }
                } catch (Exception e) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        });
    }

    Flowable createFloawable(final Class<?> service, final Method method,
                             final String baseUrl, final Object... args) {
        return Flowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
                Type responseType = Utils.getCallResponseType(method.getGenericReturnType());
                try {
                    ISoapRequest mSoapRequest = AnnonationsUtil.
                            transformInvokeToRequest(service, method, args, baseUrl);
                    String result = RxService.call(
                            mSoapRequest.getNameSpace(),
                            mSoapRequest.getEndPoint(),
                            mSoapRequest.getMethodName(),
                            mSoapRequest.getParams(), okHttpClient);
                    if (!ResultUtil.isError(result)) {
                        Gson gson = new Gson();
                        if (responseType != String.class) {
                            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(responseType));
                            if (!emitter.isCancelled()) {
                                emitter.onNext(adapter.fromJson(result));
                            }
                        } else {
                            if (!emitter.isCancelled()) {
                                emitter.onNext(result);
                            }
                        }
                        if (!emitter.isCancelled()) {
                            emitter.onComplete();
                        }

                    } else {
                        if (!emitter.isCancelled()) {
                            emitter.onError(new Throwable(result));
                        }
                    }
                } catch (Exception e) {
                    if (!emitter.isCancelled()) {
                        emitter.onError(e);
                    }
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}



