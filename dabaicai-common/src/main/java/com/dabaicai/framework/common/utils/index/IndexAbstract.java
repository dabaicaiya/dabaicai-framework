package com.dabaicai.framework.common.utils.index;


import com.dabaicai.framework.common.lambda.SFunction;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象索引
 *
 * @author zhangyanbing
 * Date: 2021/12/2 14:40
 */
public  abstract class IndexAbstract<V extends IndexEntry> implements Index<V> {

    /**
     * 主索引 对应这mysql中的聚簇索引
     */
    private final Map<Object, V> rootMap = getMap();


    /**
     * 非聚簇索引 每一项都是索引 索引位 v里面的属性到k的映射
     */
    private Map<String, PropertyIndex<V>> indexFiledMap = new HashMap();


    @Override
    public void createIndex(SFunction<V, Object> function) {
        PropertyIndex<V> newIndex = new PropertyIndex<>();
        newIndex.setFunction(function);
        newIndex.setIndexMap(getMap());
        newIndex.setIndexName(function.getImplMethodName());
        indexFiledMap.put(newIndex.getIndexName(), newIndex);
    }


    @Override
    public boolean put(V v) {
        rootMap.put(v.getId(), v);
        // 添加每一个 非聚簇索引的新索引
        for (PropertyIndex<V> propertyIndex : indexFiledMap.values()) {
            // 获取属性
            Object propertyValue = propertyIndex.getFunction().apply(v);
            propertyIndex.getIndexMap().put(propertyValue, v.getId());
        }
        return true;
    }


    @Override
    public V get(Object id) {
        if (id == null) return null;
       return rootMap.get(id);
    }


    @Override
    public V get(SFunction<V, Object> propertyFunction, Object value) {
        PropertyIndex<V> propertyIndex = indexFiledMap.get(propertyFunction.getImplMethodName());
        if (propertyIndex == null) return null;
        // 获取属性对应的key
        Object key = propertyIndex.getIndexMap().get(value);
        return get(key);
    }


    @Override
    public <T> boolean removeByProperty(SFunction<V, T> propertyFunction, T property) {
        PropertyIndex<V> propertyIndex = indexFiledMap.get(propertyFunction.getImplMethodName());
        if (propertyIndex == null) return false;
        Object key = propertyIndex.getIndexMap().get(property);
        // 获取全部数据
        V v = rootMap.get(key);
        if (v == null) return false;
        // 删除主数据
        rootMap.remove(key);
        // 删除索引树
        for (PropertyIndex<V> vPropertyIndex : indexFiledMap.values()) {
            // 获取属性
            Object propertyValue = vPropertyIndex.getFunction().apply(v);
            vPropertyIndex.getIndexMap().remove(propertyValue);
        }
        return true;
    }


    /**
     * 获取一个索引
     * @return
     */
    public abstract Map getMap();


    /**
     * 不同的索引区分key
     */
    @Data
    public static class PropertyIndex<VV> {

        /**
         * 索引名称
         */
        private String indexName;

        /**
         * 获取属性值的方法
         */
        private SFunction<VV, Object> function;

        /**
         * 索引
         */
        private Map<Object, Object> indexMap;
    }


}
