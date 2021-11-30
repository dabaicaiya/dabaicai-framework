package com.dabaicai.framework.common.utils.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * lru缓存实现
 *
 * @author: zhangyanbing
 * Date: 2021/11/30 10:23
 */
public class LruCache<K, V> {

    private LinkedHashMap<K, V> linkedHashMap;

    public LruCache(int capacity) {
        linkedHashMap = new CacheLinkedHashMap<K, V>(capacity);
    }

    public V get(K key) {
        return linkedHashMap.get(key);
    }

    public void put(K key, V value) {
        linkedHashMap.put(key, value);
    }
}

class CacheLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private int capacity;

    public CacheLinkedHashMap(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}