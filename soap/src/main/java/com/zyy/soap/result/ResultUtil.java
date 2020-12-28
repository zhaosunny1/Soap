package com.zyy.soap.result;

import org.ksoap2.SoapFault;
import org.ksoap2.SoapFault12;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Error 帮助类
 *
 * @author zhaoyang
 * @time 2016年09月01日
 * @date 2016年11月1日 修改返回错误信息，以兼容过去的版本；
 */
public class ResultUtil {


    /**
     * 判断是否为错误信息
     *
     * @param data
     * @return
     */
    public static boolean isError(String data) {
        //数据为空时，也为错误数据
        if (null == data || data.length() == 0)
            return true;
        if (data.startsWith("err-"))
            return true;
        else
            return false;
    }
    /**
     * 是否成功
     *
     * @param data
     * @return
     */
    public static boolean isOk(String data) {
        if (null != data && data.equals("ok")) {
            return true;
        }
        return false;
    }

    /**
     * 异常转换
     *
     * @param e
     * @return
     */
    public static String convertExceptionToString(Exception e) {
        String suffix = "err-3333:";
        if (e instanceof SocketTimeoutException) {
            return suffix + "网络连接超时";
        } else if (e instanceof XmlPullParserException) {
            return suffix + "XML文件解析失败";
        } else if (e instanceof SoapFault12 || e instanceof SoapFault) {
            return suffix + "服务器出现异常";
        } else if (e instanceof IOException) {
            return suffix + "网络连接异常";
        }

        return suffix + e.getLocalizedMessage();
    }

    /**
     * 没有数据
     *
     * @return
     */
    public static String convertNoData() {
        String suffix = "err-3333:";
        return suffix + "没有数据";
    }


}
