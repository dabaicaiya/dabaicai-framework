package com.dabaicai.framework.netty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2021/11/29 20:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Indexed
public @interface Handler {

    /**
     * baseUrl
     * @return
     */
    String value();
}
