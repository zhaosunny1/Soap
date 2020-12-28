package com.zyy.soap.utils;


import com.zyy.soap.annonations.WebParam;
import com.zyy.soap.annonations.WebService;
import com.zyy.soap.impl.SoapRequest;
import com.zyy.soap.interfaces.ISoapRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;


/**
 * AnnonationsUtil工具类
 * Created by zhaoyang on 2016/12/17.
 */

public class AnnonationsUtil {

    public static ISoapRequest transformInvokeToRequest(
            Class<?> target, Method method,
             Object[] args, String baseUrl) {
        ISoapRequest soapRequest = new SoapRequest();
        String nameSpace = "";
        String endPoint = "";
        String methodName = "";
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        WebService service = createWebService(target);
        Annotation[][] annotations = method.getParameterAnnotations();

        if (annotations != null && annotations.length > 0) {
            for (Annotation[] paramAnnonation : annotations) {
                for (Annotation annotation : paramAnnonation) {
                    if (annotation instanceof WebParam) {
                        WebParam path = ((WebParam) annotation);
                        params.put(path.name(), args[params.size()]);
                    } else {
                        throw new RuntimeException(
                                "params must instance of Object and with an annonation called " +
                                        "WebParam.The args must be not a null");
                    }
                }
            }

        }
        nameSpace = service.targetNamespace();
        endPoint = Utils.validateUrl(baseUrl, service.targetEndPoint());
        methodName = method.getName();
        soapRequest.init(nameSpace, endPoint, methodName, params);
        return soapRequest;
    }


    private static WebService createWebService(Class<?> T) {
        WebService service = T.getAnnotation(WebService.class);
        return service;
    }

    private static WebParam createWebParam(Class<?> T, Method method) {
        WebParam param = method.getAnnotation(WebParam.class);
        return param;
    }



}
