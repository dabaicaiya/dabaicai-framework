package com.dabaicai.framework.orm.common;

import com.dabaicai.framework.common.utils.cache.LruCache;
import com.dabaicai.framework.orm.common.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangyanbing
 * @Description: 查询构造器的抽象类
 * @date 2021/11/28 21:38
 */
public abstract class QueryBuilderAbstract<T> {

    /**
     * 与查询相关的注解
     */
    private static Map<String, String> annotationList = new HashMap<>();

    /**
     * 字段详情缓存
     */
    private static Map<String, List<FieldDetails>> fieldDetailsCache = new LruCache<>(100);

    /**
     * 往t里面设置查询条件
     * @param queryParams
     * @param t
     * @param doClass
     * @return
     */
    public T build(Object queryParams, T t, Class<?> doClass) {
        List<FieldDetails> classFieldDetails = getClassFieldDetails(queryParams.getClass());
        // 获取do的所有属性
        Map<String, List<Field>> doFieldMap = FieldUtils.scanFields(doClass).stream().collect(Collectors.groupingBy(Field::getName));
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
                    queryEquals(t, classFieldDetail.getFieldName(), value);
                } else {
                    String annotationName = annotation.getClass().getName();
                    if (annotationName.equals(Equals.class.getName())) {
                        queryEquals(t, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Ge.class.getName())) {
                        queryGe(t, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Gt.class.getName())) {
                        queryGt(t, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Le.class.getName())) {
                        queryLe(t, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Like.class.getName())) {
                        queryLike(t, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Lt.class.getName())) {
                        queryLt(t, classFieldDetail.getFieldName(), value);
                    } else if (annotationName.equals(Ne.class.getName())) {
                        queryNe(t, classFieldDetail.getFieldName(), value);
                    }
                }
            }
        }
        return t;
    }

    /**
     * 不等于
     *
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryNe(T t, String fieldName, Object value);

    /**
     * 小于
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryLt(T t, String fieldName, Object value);

    /**
     * 模糊查询
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryLike(T t, String fieldName, Object value);

    /**
     * 小于等于
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryLe(T t, String fieldName, Object value);

    /**
     * 大于
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryGt(T t,String fieldName, Object value);

    /**
     * 大于等于
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryGe(T t, String fieldName, Object value);

    /**
     * 相等比较
     * @param t
     * @param fieldName
     * @param value
     */
    protected abstract void queryEquals(T t, String fieldName, Object value);

    /**
     * 获取类相关属性
     *
     * @return
     */
    private static List<FieldDetails> getClassFieldDetails(Class aClass) {
        if (fieldDetailsCache.containsKey(Object.class.getName())) {
            return fieldDetailsCache.get(Object.class.getName());
        }
        // 获取所有字段
        List<Field> fields = FieldUtils.scanFields(aClass);
        List<FieldDetails> resList = new ArrayList<>();
        for (Field field : fields) {
            FieldDetails fieldDetails = new FieldDetails();
            // 属性名称
            fieldDetails.setFieldName(field.getName());
            // 扫描类属性及与构造查询条件相关注解信息
            Annotation[] annotations = field.getAnnotations();
            // 注解列表
            List<Annotation> annotationsList = Arrays.asList(annotations);
            for (Annotation annotation : annotationsList) {
                String annotationName = annotation.getClass().getTypeName();
                if (annotationList.containsKey(annotationName)) {
                    fieldDetails.setAnnotation(annotation);
                    break;
                }
            }
            fieldDetails.setField(field);
            resList.add(fieldDetails);
        }
        return resList;
    }

    /**
     * 获取类相关属性
     *
     * @param queryParams
     * @return
     */
    private static List<FieldDetails> getClassFieldDetails(Object queryParams) {
        return getClassFieldDetails(queryParams.getClass());
    }
}
