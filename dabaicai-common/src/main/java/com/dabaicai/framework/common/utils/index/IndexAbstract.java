package com.dabaicai.framework.common.utils.index;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    private Map<Function<V, Object>, Map<Object, Object>> indexFiledMap = new HashMap();



    @Override
    public void createIndex(Function<V, Object> function) {
        indexFiledMap.put(function, getMap());
    }


    @Override
    public boolean put(V v) {
        if (rootMap.containsKey(v.getId())) {
            return false;
        }
        rootMap.put(v.getId(), v);
        // 添加每一个 非聚簇索引的新索引
        for (Map.Entry<Function<V, Object>, Map<Object, Object>> functionMapEntry : indexFiledMap.entrySet()) {
            Function<V, Object> function = functionMapEntry.getKey();
            // 获取属性
            Object propertyValue = function.apply(v);
            functionMapEntry.getValue().put(propertyValue, v.getId());
        }
        return true;
    }


    @Override
    public V get(Object id) {
        if (id == null) return null;
       return rootMap.get(id);
    }


    @Override
    public V get(Function<V, Object> propertyFunction, Object value) {
        Map<Object, Object> keyMap = indexFiledMap.get(propertyFunction);
        if (keyMap == null) return null;
        // 获取属性对应的key
        Object key = keyMap.get(value);
        return get(key);
    }


    @Override
    public boolean removeByProperty(Function<V, Object> propertyFunction, Object property) {
        Map<Object, Object> keyMap = indexFiledMap.get(propertyFunction);
        if (keyMap == null) return false;
        Object key = keyMap.get(property);
        // 获取全部数据
        V v = rootMap.get(key);
        // 删除主数据
        rootMap.remove(key);
        // 删除索引树
        for (Map.Entry<Function<V, Object>, Map<Object, Object>> functionMapEntry : indexFiledMap.entrySet()) {
            Function<V, Object> function = functionMapEntry.getKey();
            // 获取属性
            Object propertyValue = function.apply(v);
            functionMapEntry.getValue().remove(propertyValue);
        }
        return true;
    }


    /**
     * 获取一个索引
     * @return
     */
    public abstract Map getMap();


}
