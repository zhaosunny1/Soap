package com.zyy.soap.improved;

import com.zyy.soap.result.ResultUtil;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.OkHttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * RxCallWebService
 * Created by zhaoyang on 2017/3/28.
 */

public class RxService {
    /**
     * 配置是采用新的框架
     */
    public static boolean isNew = false;

    /**
     * 实际请求，加入client,加入剧毒/民爆系统的判断不同
     *
     * @param namespace
     * @param endPoint
     * @param methodName
     * @param params
     * @param client
     * @return
     */
    public static String call(String namespace, String endPoint, String methodName,
                              Map<String, Object> params, OkHttpClient client) throws Exception {
        try {
            SoapPrimitive resultObject = null;
            // 指定WebService的命名空间和调用的方法名
            SoapObject request = new SoapObject(namespace, methodName);

            // 设置需调用WebService接口需要传入的两个参数
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, Object> e : params.entrySet()) {
                    if (e.getValue() instanceof byte[]) {
                        byte[] d = (byte[]) e.getValue();
                        String data = Base64.encode(d);
                        request.addProperty(e.getKey(), data);
                    } else {
                        request.addProperty(e.getKey(), e.getValue());
                    }
                }
            }
            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = false;
            OkHttpTransportSE ht = new OkHttpTransportSE(client, null, endPoint,
                    ServiceConnection.DEFAULT_TIMEOUT, -1);
            if (!isNew) {
                ht.call(namespace + methodName, envelope);
            } else {
                ht.call(null, envelope);
            }
            if (envelope.getResponse() != null) {
                resultObject = (SoapPrimitive) envelope.getResponse();
            } else {
                resultObject = null;
            }
            String resultStr = (null == resultObject ? "" : resultObject.toString());
            if (null == resultStr || resultStr.length() == 0) {
                resultStr = ResultUtil.convertNoData();
            }
            RxLog.log(namespace, endPoint, methodName, params, resultStr);
            return resultStr;
        } catch (Exception e) {
            String returnStr = ResultUtil.convertExceptionToString(e);
            RxLog.log(namespace, endPoint, methodName, params, returnStr, e);
            throw e;
        }
    }

}
