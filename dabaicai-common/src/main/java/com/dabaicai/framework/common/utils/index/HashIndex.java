package com.dabaicai.framework.common.utils.index;


import java.util.HashMap;
import java.util.Map;

/**
 * 支持多索引搜索
 * @author zhangyanbing
 * Date: 2021/12/2 14:26
 */
public class HashIndex<V extends IndexEntry> extends IndexAbstract<V>{


    @Override
    public Map getMap() {
        return new HashMap();
    }

}


