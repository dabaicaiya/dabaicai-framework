package com.dabaicai.framework.orm.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dabaicai.framework.orm.common.annotation.Equals;
import com.dabaicai.framework.orm.common.annotation.Like;
import lombok.Data;

/**
 * @author zhangyanbing
 * @Description: 构造查询器工具类
 * @date 2021/11/28 22:12
 */
public class BuildQueryWrapperUtils {

    /**
     * mybatis-plus 查询构造器
     */
    private static final BatisQueryWrapper buildQueryWrapper = new BatisQueryWrapper();

    /**
     * 构建一个查询器
     *
     * @param query 请求参数
     * @param doClass do实体类
     * @param <T>
     * @return 查询构造器
     */
    public static <T> QueryWrapper<T> build(Object query, Class<T> doClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        buildQueryWrapper.build(query, queryWrapper, doClass);
        return queryWrapper;
    }

    /**
     * 构建一个Lambda查询器
     *
     * @param <T>
     * @param query 请求参数
     * @param doClass do实体类
     * @return 查询构造器
     */
    public static <T> LambdaQueryWrapper<T> buildLambda(Object query, Class<T> doClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        buildQueryWrapper.build(query, queryWrapper, doClass);
        return queryWrapper.lambda();
    }

    public static void main(String[] args) {
        TestRequest routeQuery = new TestRequest();
        routeQuery.setName("sdasda");
        routeQuery.setId(1323123);
        // new QueryWrapper<>(routeQuery)
        QueryWrapper<RouteEntity> query = BuildQueryWrapperUtils.build(routeQuery, RouteEntity.class);
        System.out.println(query);

    }

    @Data
    static
    class TestRequest {

        @Like
        private String name;

        @Equals
        private Integer id;
    }

    class RouteEntity{
        @Like
        private String name;

        @Equals
        private Integer id;
    }

}
