package com.dabaicai.framework.common.utils.index;

/**
 * 想要使用本类下面的索引 需要在实体类里面实现这个接口
 * @author zhangyanbing
 * Date: 2021/12/2 14:57
 */
public interface IndexEntry {

    /**
     * 获取id
     * @return
     */
     Object getId();

}
