package com.dabaicai.framework.orm.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author zhangyanbing
 * @Description: 构造查询器工具类
 * @date 2021/11/28 22:12
 */
public class BuildQueryWrapperUtils {

    /**
     * mybatis-plus 查询构造器
     */
    private static final BuildQueryWrapper buildQueryWrapper = new BuildQueryWrapper();

    /**
     * 构建一个查询器
     *
     * @param t 请求参数
     * @param doClass do实体类
     * @param <T>
     * @return 查询构造器
     */
    public static <T> QueryWrapper<T> build(T t, Class<?> doClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        buildQueryWrapper.build(t, queryWrapper, doClass);
        return queryWrapper;
    }

}
