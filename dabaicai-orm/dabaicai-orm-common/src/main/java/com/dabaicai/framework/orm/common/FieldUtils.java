package com.dabaicai.framework.orm.common;


import com.dabaicai.framework.common.utils.cache.LruCache;
import sun.misc.LRUCache;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhangyanbing
 * @Description: 字段工具类
 * @date 2021/3/26 15:29
 */
public class FieldUtils {

    private static Map<Class<?>, List<Field>> declaredFieldsCache = new LruCache<>(100);

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
