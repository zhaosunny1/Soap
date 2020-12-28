package com.zyy.soap.annonations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhaoyang on 2016/12/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EndPoint {
    String targetEndPoint();
}
