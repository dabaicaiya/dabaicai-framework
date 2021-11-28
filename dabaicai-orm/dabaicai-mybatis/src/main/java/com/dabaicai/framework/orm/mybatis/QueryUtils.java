package com.dabaicai.framework.orm.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.dabaicai.framework.orm.common.FieldDetails;
import com.dabaicai.framework.orm.common.FieldUtils;
import com.dabaicai.framework.orm.common.annotation.Ge;
import com.dabaicai.framework.orm.common.annotation.Le;
import com.dabaicai.framework.orm.common.annotation.Like;
import com.dabaicai.framework.orm.common.annotation.Ne;
import com.sun.org.apache.xpath.internal.operations.Equals;
import com.sun.org.apache.xpath.internal.operations.Gt;
import com.sun.org.apache.xpath.internal.operations.Lt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyanbing
 * @Description: 查询工具类
 * @date 2021/11/28 21:40
 */
public class QueryUtils {

    /**
     * 与查询相关的注解
     */
    private static Map<String, String> annotationList = new HashMap<>();

    static {
        annotationList.put(Equals.class.getName(), "");
        annotationList.put(Ge.class.getName(), "");
        annotationList.put(Gt.class.getName(), "");
        annotationList.put(Le.class.getName(), "");
        annotationList.put(Like.class.getName(), "");
        annotationList.put(Lt.class.getName(), "");
        annotationList.put(Ne.class.getName(), "");
    }

    public static <T> QueryWrapper<T> build(Object queryParams, Class<T> aClass) {
        List<FieldDetails> classFieldDetails = getClassFieldDetails(queryParams.getClass());
        // 构建查询条件
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 获取do的所有属性
        Map<String, List<Field>> doFieldMap = FieldUtils.scanFields(aClass).stream().collect(Collectors.groupingBy(Field::getName));
        // 循环设置属性
        for (FieldDetails classFieldDetail : classFieldDetails) {
            if (doFieldMap.containsKey(classFieldDetail.getFieldName())) {
                // 查询属性名在  数据库实体类中存在
                Annotation annotation = classFieldDetail.getAnnotation();
                Object value = null;
                // 属性值
                try {
                    classFieldDetail.getField().setAccessible(true);
                    value = classFieldDetail.getField().get(queryParams);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value == null) {
                    continue;
                }
                if (annotation == null) {
                    queryEquals(queryWrapper, classFieldDetail.getFieldName(), value);
                } else {
                    String annotationName = annotation.getClass().getName();
                    if (annotationName.equals(Equals.class.getName())) {
                        queryEquals(queryWrapper, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Ge.class.getName())) {
                        queryGe(queryWrapper, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Gt.class.getName())) {
                        queryGt(queryWrapper, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Le.class.getName())) {
                        queryLe(queryWrapper, classFieldDetail.getFieldName(), value);
                    }else if (annotationName.equals(Like.class.getName())) {
                        queryLike(queryWrapper, classFieldDetail.getFieldName(), value);
                    }else if (annotationName.equals(Lt.class.getName())) {
                        queryLt(queryWrapper, classFieldDetail.getFieldName(), value);
                    }else if (annotationName.equals(Ne.class.getName())) {
                        queryNe(queryWrapper, classFieldDetail.getFieldName(), value);
                    }
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 执行相等判断
     * @param queryWrapper
     * @param fieldName
     * @param value
     * @param <T>
     */
    private static <T> void queryEquals(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.eq(fieldName, value);
    }

    private static <T> void queryNe(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.ne(fieldName, value);
    }

    private static <T> void queryLt(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.lt(fieldName, value);
    }

    private static <T> void queryLike(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.like(fieldName, value);
    }

    private static <T> void queryLe(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.le(fieldName, value);
    }

    private static <T> void queryGt(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.gt(fieldName, value);
    }

    private static <T> void queryGe(QueryWrapper<T> queryWrapper, String fieldName, Object value) {
        queryWrapper.ge(fieldName, value);
    }



    /**
     * 字段详情缓存
     */
    private static Map<String, List<FieldDetails>> fieldDetailsCache = new HashMap<>();

    /**
     * 获取类相关属性
     * @param queryParams
     * @return
     */
    private static List<FieldDetails> getClassFieldDetails(Object queryParams) {
        return getClassFieldDetails(queryParams.getClass());
    }



    public static void main(String[] args) {
        FieldDetails fieldDetails = new FieldDetails();
        SFunction<FieldDetails, String> getFieldName = FieldDetails::getFieldName;
        String apply = getFieldName.apply(fieldDetails);
        System.out.println(apply);
    }

}
