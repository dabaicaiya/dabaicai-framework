package com.dabaicai.framework.orm.common;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author zhangyanbing
 * @Description: 类属性详情
 * @date 2021/3/25 10:16
 */
@Data
public class FieldDetails {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 与构造查询条件相关的注解
     */
    private Annotation annotation;

    /**
     * 对应的字段
     */
    private Field field;

}
