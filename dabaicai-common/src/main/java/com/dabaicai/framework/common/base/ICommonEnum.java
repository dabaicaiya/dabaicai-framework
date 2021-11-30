package com.dabaicai.framework.common.base;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2021/3/30 15:22
 */

import java.util.EnumSet;

public interface ICommonEnum<E extends Enum<E> & ICommonEnum> {

    /**
     * 获取value
     * @return
     */
    String getValue();

    /**
     * 获取key
     * @return
     */
    Integer getKey();

    /**
     * 获取枚举的类型
     * @return
     */
    default Class<E> getEnumClass() {
        return null;
    }

    /**
     * 根据value获取enum
     * @param value
     * @return
     */
    default  E getEnumByValue(String value) {
        EnumSet<E> all = EnumSet.allOf(getEnumClass());
        return all.stream().filter(e -> e.getValue().equals(value)).findFirst().orElse(null);
    }

    /**
     * 根据key获取Enum
     * @param key
     * @return
     */
    default  E getEnumByKey(Integer key) {
        EnumSet<E> all = EnumSet.allOf(getEnumClass());
        return all.stream().filter(e -> e.getKey().equals(key)).findFirst().orElse(null);
    }
}
