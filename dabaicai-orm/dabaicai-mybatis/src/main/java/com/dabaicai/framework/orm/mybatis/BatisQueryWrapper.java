package com.dabaicai.framework.orm.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dabaicai.framework.orm.common.QueryBuilderAbstract;
import com.dabaicai.framework.orm.common.QueryFieldDetails;
import com.dabaicai.framework.orm.common.annotation.*;
import com.dabaicai.framework.orm.common.handler.QueryHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyanbing
 * @Description: 构建mybatis-plus的查询器
 * @date 2021/11/28 21:55
 */
public class BatisQueryWrapper extends QueryBuilderAbstract<QueryWrapper> {

    public BatisQueryWrapper() {
    }

    /**
     * 处理器列表
     */
    private List<QueryHandler<?, QueryWrapper<?>>> handlers = Arrays.asList(
            new EqualsHandler(),
            new GeHandler(),
            new GtHandler(),
            new LeHandler(),
            new LikeHandler(),
            new LtHandler(),
            new NeHandler()

    );

    private Map<Class<?>, QueryHandler<?, QueryWrapper<?>>> queryHandlerMap;
    {
        queryHandlerMap = handlers.stream().collect(Collectors.toMap(QueryHandler::getQueryClass, e->e));
    }

    @Override
    public <T> Map<Class<? extends Annotation>, QueryHandler<T, QueryWrapper>> getQueryHandler() {
        return (Map)queryHandlerMap;
    }
}

class EqualsHandler implements QueryHandler<Equals, QueryWrapper<?>>{

    @Override
    public void handler(Equals equals, QueryWrapper<?> queryWrapper,  Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        queryWrapper.eq(FieldUtils.getFieldName(equals.filedName(), queryFieldDetails.getField()), queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Equals> getQueryClass() {
        return Equals.class;
    }
}

class GeHandler implements QueryHandler<Ge, QueryWrapper<?>>{

    @Override
    public void handler(Ge ge, QueryWrapper<?> queryWrapper, Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        queryWrapper.ge(FieldUtils.getFieldName(ge.filedName(), queryFieldDetails.getField()), queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Ge> getQueryClass() {
        return Ge.class;
    }
}

class GtHandler implements QueryHandler<Gt, QueryWrapper<?>>{

    @Override
    public void handler(Gt gt, QueryWrapper<?> queryWrapper, Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        queryWrapper.gt(FieldUtils.getFieldName(gt.filedName(), queryFieldDetails.getField()), queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Gt> getQueryClass() {
        return Gt.class;
    }
}

class LeHandler implements QueryHandler<Le, QueryWrapper<?>>{

    @Override
    public void handler(Le le, QueryWrapper<?> queryWrapper, Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        String fieldName = FieldUtils.getFieldName(le.filedName(), queryFieldDetails.getField());
        queryWrapper.gt(fieldName, queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Le> getQueryClass() {
        return Le.class;
    }
}

class LikeHandler implements QueryHandler<Like, QueryWrapper<?>>{

    @Override
    public void handler(Like like, QueryWrapper<?> queryWrapper, Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        String fieldName = FieldUtils.getFieldName(like.filedName(), queryFieldDetails.getField());
        queryWrapper.likeLeft(fieldName, queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Like> getQueryClass() {
        return Like.class;
    }
}

class LtHandler implements QueryHandler<Lt, QueryWrapper<?>>{


    @Override
    public void handler(Lt lt, QueryWrapper<?> queryWrapper, Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        queryWrapper.gt(FieldUtils.getFieldName(lt.filedName(), queryFieldDetails.getField()), queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Lt> getQueryClass() {
        return Lt.class;
    }
}

class NeHandler implements QueryHandler<Ne, QueryWrapper<?>>{



    @Override
    public void handler(Ne ne, QueryWrapper<?> queryWrapper,  Object queryParams, QueryFieldDetails queryFieldDetails, Map<Object, Object> params) {
        queryWrapper.ne(FieldUtils.getFieldName(ne.filedName(), queryFieldDetails.getField()), queryFieldDetails.getFieldValue(queryParams));
    }

    @Override
    public Class<Ne> getQueryClass() {
        return Ne.class;
    }
}

class FieldUtils {

    public static String getFieldName(String fieldName, Field field) {
        if (!StringUtils.isEmpty(fieldName)) {
            return fieldName;
        }
        return com.dabaicai.framework.common.utils.StringUtils.camelToUnderline(field.getName());
    }

}