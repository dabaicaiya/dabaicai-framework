package com.dabaicai.framework.orm.common;

import com.dabaicai.framework.orm.common.base.TableDetail;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import org.apache.commons.lang3.StringUtils;

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

    public static final char UNDERLINE = '_';
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
        Map<String, String> columnMap = Arrays.stream(fields).collect(Collectors.toMap(e -> e.getName(), e -> camelToUnderline(e.getName())));
        tableDetail.setColumnMap(columnMap);
        return tableDetail;
    }


    /**
     * 字符串驼峰转下划线格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    private static String camelToUnderline(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

}
