package com.dabaicai.framework.orm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangyanbing
 * @Description: 小于等于
 * @date 2021/3/25 10:46
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Le {

    /**
     * 字段名称
     * @return
     */
    String filedName() default "";

}
