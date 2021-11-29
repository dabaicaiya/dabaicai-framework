package com.dabaicai.framework.orm.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dabaicai.framework.orm.common.QueryBuilderAbstract;

/**
 * @author zhangyanbing
 * @Description: 构建mybatis-plus的查询器
 * @date 2021/11/28 21:55
 */
public class BuildQueryWrapper extends QueryBuilderAbstract<QueryWrapper> {

    public BuildQueryWrapper() {
    }


    @Override
    protected void queryNe(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.ne(fieldName, value);
    }

    @Override
    protected void queryLt(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.lt(fieldName, value);
    }

    @Override
    protected void queryLike(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.like(fieldName, value);
    }

    @Override
    protected void queryLe(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.le(fieldName, value);
    }

    @Override
    protected void queryGt(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.gt(fieldName, value);
    }

    @Override
    protected void queryGe(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.ge(fieldName, value);
    }

    @Override
    protected void queryEquals(QueryWrapper queryWrapper, String fieldName, Object value) {
        queryWrapper.eq(fieldName, value);
    }
}
