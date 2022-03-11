package com.dabaicai.framework.orm.common.base;

import lombok.Data;

import java.util.Map;

/**
 * @author zhangyanbing
 * @Description: 数据库表属性
 * @date 2022/3/11 13:21
 */
@Data
public class TableDetail {


    /**
     * 数据库表名
     */
    private String tableName;

    /**
     * 数据库实体类class
     */
    private Class<?> entityClass;

    /**
     * java字段对应数据库字段
     */
    private Map<String, String> columnMap;

}
