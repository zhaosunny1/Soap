package com.zyy.soap.improved;

import android.util.Log;

import java.util.Map;

/**
 * Rx日志记录
 * Created by zhaoyang on 2017/3/28.
 */

public class RxLog {
    private static String TAG = "SoapLog ";

    private static boolean DEBUG = false;

    public static void setDEBUG(boolean DEBUG) {
        RxLog.DEBUG = DEBUG;
    }

    public static void setDEBUG(boolean DEBUG, String DEBUG_TAG) {
        RxLog.DEBUG = DEBUG;
        RxLog.TAG = DEBUG_TAG;
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void log(String namespace,
                           String endPoint,
                           String methodName,
                           Map<String, Object> params, String result) {
        log(namespace, endPoint, methodName, params, result, null);
    }

    public static void log(String namespace,
                           String endPoint,
                           String methodName,
                           Map<String, Object> params, String result, Exception e) {
        if (DEBUG) {
            Log.d(TAG, "");
            Log.d(TAG, "=================开始=====================");
            //如果为调试模式，则打印数据
            Log.d(TAG, "1.命名空间：" + namespace);
            Log.d(TAG, "2.访问地址：" + endPoint);
            Log.d(TAG, "3.接口名称：" + methodName);
            Log.d(TAG, "4.参数信息：");
            if (null != params && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Log.d(TAG, "  [" + entry.getKey() + "," + String.valueOf(entry.getValue()) + "]");
                }
            } else {
                Log.d(TAG, "4.没有参数");
            }
            Log.d(TAG, "5.返回结果：" + result);
            if (null != e) {
                log(e);
            }
            Log.d(TAG, "=================结束=====================");
            Log.d(TAG, "");

        }
    }

    public static void log(Exception e) {
        Log.e(TAG, "请求数据异常：", e);
    }
}
