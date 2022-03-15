package com.dabaicai.framework.orm.common;

import com.dabaicai.framework.common.beans.BeanUtils;
import com.dabaicai.framework.orm.common.handler.QueryHandler;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyanbing
 * @Description: 查询构造器的抽象类 可用于mybatis es等查询构造 具体查询什么子类实现对应的处理器
 * @date 2021/11/28 21:38
 */
public abstract class QueryBuilderAbstract<T> {

    /**
     * 字段详情缓存
     */
    private static final Map<String, List<QueryFieldDetails>> FIELD_DETAILS_CACHE = new ConcurrentLinkedHashMap.Builder<String, List<QueryFieldDetails>>()
            .maximumWeightedCapacity(500).weigher(Weighers.singleton()).build();

    /**
     * 往t里面设置查询条件
     * @param queryParams
     * @param t
     * @param doClass
     * @return
     */
    public T build(Object queryParams, T t, Class<?> doClass) {
        List<QueryFieldDetails> classFieldDetails = getClassFieldDetails(queryParams.getClass());
        Map<Class<? extends Annotation>, QueryHandler<Object, T>> queryHandler = getQueryHandler();
        // 获取do的所有属性
        Map<String, List<Field>> doFieldMap = FieldUtils.scanFields(doClass).stream().collect(Collectors.groupingBy(Field::getName));
        // 循环设置属性
        for (QueryFieldDetails classFieldDetail : classFieldDetails) {
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
                    continue;
                }
                // 获取注解的代理接口
                Class<?> target = BeanUtils.getJdkTarget(annotation);
                if (target == null) {
                    continue;
                }
                if (queryHandler.containsKey(target)) {
                    queryHandler.get(target).handler(annotation, t, queryParams, classFieldDetail, Collections.emptyMap());
                }
            }
        }
        return t;
    }

    /**
     * 获取类相关属性
     *
     * @return
     */
    private List<QueryFieldDetails> getClassFieldDetails(Class aClass) {
        if (FIELD_DETAILS_CACHE.containsKey(Object.class.getName())) {
            return FIELD_DETAILS_CACHE.get(Object.class.getName());
        }
        Map<Class<? extends Annotation>, QueryHandler<Object, T>> queryHandler = getQueryHandler();
        // 获取所有字段
        List<Field> fields = FieldUtils.scanFields(aClass);
        List<QueryFieldDetails> resList = new ArrayList<>();
        for (Field field : fields) {
            QueryFieldDetails fieldDetails = new QueryFieldDetails();
            // 属性名称
            fieldDetails.setFieldName(field.getName());
            // 扫描类属性及与构造查询条件相关注解信息
            Annotation[] annotations = field.getAnnotations();
            // 注解列表
            for (Class<? extends Annotation> queryClass : queryHandler.keySet()) {
                Annotation[] annotationsByType = field.getAnnotationsByType(queryClass);
                if (annotationsByType.length > 0) {
                    fieldDetails.setAnnotation(annotationsByType[0]);
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
    private List<QueryFieldDetails> getClassFieldDetails(Object queryParams) {
        return getClassFieldDetails(queryParams.getClass());
    }


    /**
     * 获取查询注解相关的处理器
     * @return
     */
    public  abstract <G> Map<Class<? extends Annotation>, QueryHandler<G, T>> getQueryHandler();
}
