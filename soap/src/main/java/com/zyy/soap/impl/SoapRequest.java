package com.zyy.soap.impl;

import com.zyy.soap.interfaces.ISoapRequest;

import java.util.LinkedHashMap;

/**
 * ksoap2 帮住类，实现webserivce调用 //issue: The given SOAPAction does not match an
 * operation.' faultactor: 'null' detail: null //ht.call(namespace + methodName,
 * envelope); 新框架有变化
 *
 * @author ftl
 */
public class SoapRequest implements ISoapRequest {

    private String nameSpace;
    private String endPoint;
    private String methodName;
    private LinkedHashMap<String, Object> params;

    @Override
    public void init(String nameSpace, String endPoint, String methodName, LinkedHashMap<String, Object> params) {
        this.nameSpace = nameSpace;
        this.endPoint = endPoint;
        this.methodName = methodName;
        this.params = params;
        this.endPoint = endPoint;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getMethodName() {
        return methodName;
    }

    public LinkedHashMap<String, Object> getParams() {
        return params;
    }
}
