package com.dabaicai.framework.common.utils.index;


import com.dabaicai.framework.common.lambda.SFunction;

/**
 * 索引顶层接口
 *
 * @author zhangyanbing
 * Date: 2021/12/2 15:12
 */
public interface Index<V extends IndexEntry> {

    /**
     * 添加属性索引，不要添加主键索引，默认已经创建
     * @param function
     */
    void createIndex(SFunction<V, Object> function);

    /**
     * 添加一个元素
     * @param v
     * @return
     */
    boolean put(V v);

    /**
     * 根据k查找value
     * @param id
     * @return
     */
    V get(Object id);

    /**
     * 根据 value查找数据
     * @param propertyFunction 索引字段
     * @param value 属性值
     * @return
     */
     V get(SFunction<V, Object> propertyFunction, Object value);

    /**
     * 根据 属性 删除元素
     * @param propertyFunction 索引字段
     * @param property 属性值
     * @return
     */
    <T> boolean  removeByProperty(SFunction<V, T> propertyFunction, T property);
}
