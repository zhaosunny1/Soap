package com.zyy.soap.interfaces;

import java.util.LinkedHashMap;

/**
 * Created by zhaoyang on 2016/12/17.
 */

public interface ISoapRequest {

    /**
     * 初始化信息
     */
     void init(String nameSpace, String endPoint, String methodName, LinkedHashMap<String, Object> params);

     String getNameSpace();

     String getEndPoint();

     String getMethodName();

     LinkedHashMap<String, Object> getParams();
}
