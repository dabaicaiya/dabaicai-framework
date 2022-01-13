package com.dabaicai.framework.common.base;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2021/3/30 15:22
 */

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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
     * 根据value获取enum
     * @param key
     * @return
     */
    static <E extends Enum<E> & ICommonEnum> E getEnumByKey(Integer key, Class<E> clazz) {
        if (key == null) {
            return null;
        }
        EnumSet<E> all = EnumSet.allOf(clazz);
        return all.stream().filter(e -> e.getKey().equals(key) ).findFirst().orElse(null);
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    static <E extends Enum<E> & ICommonEnum>  String getValueByKey(Integer key, Class<E> clazz) {
        E enumByKey = getEnumByKey(key, clazz);
        if (enumByKey == null) {
            return null;
        }
        return enumByKey.getValue();
    }

    static <E extends Enum<E> & ICommonEnum> List<EnumVO> getEnumVOList(Class<E> clazz) {
        EnumSet<E> all = EnumSet.allOf(clazz);
        return all.stream().map(e->new EnumVO(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
