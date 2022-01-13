package com.dabaicai.framework.common.base;


/**
 * @author zhangyanbing
 * @Description: 枚举VO
 * @date 2020/12/9 14:00
 */
public class EnumVO {
    private Object key;
    private String value;

    public EnumVO() {
    }

    public EnumVO(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public EnumVO(Object key, String value) {
        this.key = key;
        this.value = value;
    }


    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EnumVO{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }
}
