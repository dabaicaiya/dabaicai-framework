package com.dabaicai.framework.orm.common.handler;

import com.dabaicai.framework.orm.common.QueryFieldDetails;

import java.util.Map;

/**
 * 查询处理器
 * @param <T> 注解类型
 * @param <G> 后置处理的类型
 */
public interface QueryHandler <T, G>{

    /**
     * 处理方法
     * @param t 注解实例
     * @param value 属性值
     * @param params 其他相关参数
     */
    void handler(T t, G g, Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params);

    /**
     * 获取枚举的class
     * @return
     */
    Class<T> getQueryClass();

}
