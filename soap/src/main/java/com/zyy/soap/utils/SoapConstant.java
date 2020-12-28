package com.zyy.soap.utils;

/**
 * Format SoapRequest
 * Created by zhaoyang on 2017/1/2.
 */

public class SoapConstant {

    public static final String requestFormat = "%s%s%s";
    public static final String requestHeaderPrefix = "<soapenv:Envelope xmlns:soapenv=\"" +
            "http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:dao=\"http://dao.ws.cbsw.cn/\">\n" +
            "   <soapenv:Header>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>";

    public static final String requestHeaderSuffix = " </soapenv:Body>\n" +
            "</soapenv:Envelope>";


    public static final String requestBodyFormat = "<dao:%s>%s</dao:%s>";

    public static final String requestParamsFormat = "<%s>%s</%s>";


}
