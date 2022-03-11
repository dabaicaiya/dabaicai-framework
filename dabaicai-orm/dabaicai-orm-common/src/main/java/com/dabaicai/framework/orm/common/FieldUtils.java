package com.dabaicai.framework.orm.common;


import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyanbing
 * @Description: 字段工具类
 * @date 2021/3/26 15:29
 */
public class FieldUtils {

    private static Map<Class<?>, List<Field>> declaredFieldsCache = new ConcurrentLinkedHashMap.Builder<Class<?>, List<Field>>()
            .maximumWeightedCapacity(500).weigher(Weighers.singleton()).build();
    /**
     * 获取类的所有属性 包含父类的属性
     * @param clazz
     */
    public static List<Field> scanFields(Class<?> clazz) {
        Class targetClass = clazz;
        List<Field> classFieldList = new ArrayList<>();
        do {
            Field[] fields = getDeclaredFields(targetClass);
            classFieldList.addAll(Arrays.asList(fields));
            targetClass = targetClass.getSuperclass();
        } while(targetClass != null && targetClass != Object.class);
        return classFieldList;
    }


    private static Field[] getDeclaredFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }
}
