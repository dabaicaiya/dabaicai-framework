package com.dabaicai.framework.orm.common;

import com.dabaicai.framework.common.utils.StringUtils;
import com.dabaicai.framework.orm.common.base.TableDetail;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2022/3/11 13:04
 */
public class EntityUtils {

    /**
     * 数据库实体类缓存
     */
    private static Map<Class<?>, TableDetail> tableDetailCache = new ConcurrentLinkedHashMap.Builder<Class<?>, TableDetail>()
            .maximumWeightedCapacity(500).weigher(Weighers.singleton()).build();


    /**
     * 获取数据表java属性名和数据库属性名对应关系
     * @param entityClass
     * @return
     */
    public static Map<String, String> getColumnMap(Class<?> entityClass) {
        return getTableDetail(entityClass).getColumnMap();
    }

    /**
     * 获取表名
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) {
        return getTableDetail(entityClass).getTableName();
    }

    /**
     * 获取数据表属性
     * @param entityClass
     * @return
     */
    public static  TableDetail getTableDetail(Class<?> entityClass) {
        TableDetail tableDetail = tableDetailCache.get(entityClass);
        if (tableDetail != null) {
            return tableDetail;
        }
        TableDetail buildTableDetail = buildTableDetail(entityClass);
        tableDetailCache.put(entityClass, buildTableDetail);
        return buildTableDetail;
    }


    /**
     * 创建数据表属性
     * @param entityClass
     * @return
     */
    private static TableDetail buildTableDetail(Class<?> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("请在实体类上用 javax.persistence.Table 注解标注表名");
        }
        TableDetail tableDetail = new TableDetail();
        tableDetail.setTableName(table.name());
        tableDetail.setEntityClass(entityClass);
        Field[] fields = entityClass.getDeclaredFields();
        Map<String, String> columnMap = Arrays.stream(fields).collect(Collectors.toMap(e -> e.getName(), e -> StringUtils.camelToUnderline(e.getName())));
        tableDetail.setColumnMap(columnMap);
        return tableDetail;
    }


}
