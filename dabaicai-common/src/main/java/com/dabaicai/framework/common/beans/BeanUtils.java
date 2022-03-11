package com.dabaicai.framework.common.beans;

import java.lang.reflect.Field;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2022/2/15 16:30
 */
public class BeanUtils {

    public static Class<?> getTarget(Object proxy) {
        Field field = null;
        try {
            field = proxy.getClass().getDeclaredField("h");
             return field.get(proxy).getClass();
        } catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
