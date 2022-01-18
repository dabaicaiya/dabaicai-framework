package com.dabaicai.framework.common.annotation;

import com.dabaicai.framework.common.base.ICommonEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置枚举值注解
 *
 * @author zhangyanbing
 * Date: 2022/1/13 17:11
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoSetEnumValue {

    /**
     * 枚举字段
     *
     * @return
     */
    Class<? extends Enum<? extends ICommonEnum>> enumClass();

}
