package com.dabaicai.framework.common.utils;

import com.dabaicai.framework.common.annotation.AutoSetEnumValue;
import com.dabaicai.framework.common.base.ICommonEnum;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 枚举工具类
 *
 * @author zhangyanbing
 * Date: 2022/1/13 16:50
 */
public class EnumUtils {


    /**
     * 枚举缓存 lru淘汰策略
     */
    private static final Map<String, Map<Integer, String>> ENUM_CACHE = new ConcurrentLinkedHashMap.Builder<String, Map<Integer, String>>()
            .maximumWeightedCapacity(500).weigher(Weighers.singleton()).build();

    /**
     * 设置对象枚举字段对应的属性字段值
     * @see AutoSetEnumValue 在属性上面打上这个注解 会将传入对象 属性名+“Name” 自动设置为枚举的value
     * @param object
     */
    public static void setEntityEnumValue(Object object) {
        if (object == null) {
            return;
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Map<String, Field> fieldNameMap = Arrays.stream(declaredFields).collect(Collectors.toMap(Field::getName, e -> e));
        for (Field declaredField : declaredFields) {
            AutoSetEnumValue annotation = declaredField.getAnnotation(AutoSetEnumValue.class);
            if (annotation == null) {
                continue;
            }
            // 注解上面的枚举类型
            Class<? extends Enum> enumClass = annotation.enumClass();
            // 获取枚举的键值对
            Map<Integer, String> enumMap;
            if (ENUM_CACHE.containsKey(enumClass.getName())) {
                enumMap = ENUM_CACHE.get(enumClass.getName());
            } else {
                EnumSet<?> enumSet = EnumSet.allOf(enumClass);
                enumMap = enumSet.stream().map(e->(ICommonEnum)e).collect(Collectors.toMap(ICommonEnum::getKey, ICommonEnum::getValue));
                ENUM_CACHE.put(enumClass.getName(), enumMap);
            }
            try {
                // 设置枚举字段对应的枚举value
                String valueFieldName = declaredField.getName() + "Name";
                if (fieldNameMap.containsKey(valueFieldName)) {
                    Field valueField = fieldNameMap.get(valueFieldName);
                    valueField.setAccessible(true);
                    declaredField.setAccessible(true);
                    valueField.set(object, enumMap.get(declaredField.get(object)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
