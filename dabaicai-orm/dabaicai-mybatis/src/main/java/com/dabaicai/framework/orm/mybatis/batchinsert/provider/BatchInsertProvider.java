package com.dabaicai.framework.orm.mybatis.batchinsert.provider;

import com.dabaicai.framework.common.lambda.SFunction;
import com.dabaicai.framework.orm.common.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 批量插入
 *
 * @author zhangyanbing
 * Date: 2022/1/13 10:07
 */
@Slf4j
public class BatchInsertProvider<T>{

    /**
     * 批量插入sql语句生成
     * @param recordList
     * @param fns
     * @return
     */
    public String batchInsert(@Param("recordList") List<T> recordList, @Param("properties") SFunction<T, Object>... fns) {
        if (recordList.isEmpty()) {
            log.error("批量插入的数量为空, sql语句生成失败");
            return "";
        }
        Class<?> entityClass = recordList.get(0).getClass();
        StringBuilder sql = new StringBuilder();

        // java属性名与数据库字段名对应关系
        Map<String, String> columnMap = EntityUtils.getColumnMap(entityClass);
        // 表名
        String tableName = EntityUtils.getTableName(entityClass);
        sql.append("insert into ").append(tableName);
        sql.append("(");
        String properties = Arrays.stream(fns).map(this::getProperty).map(this::resolveFieldName).map(columnMap::get).collect(Collectors.joining(","));
        sql.append(properties);
        sql.append(")");
        sql.append("VALUES ");
        String values = "(" + Arrays.stream(fns).map(this::getProperty).map(this::resolveFieldName).map(propertyName -> "#{recordList[%d]." + propertyName + "}").collect(Collectors.joining(",")) + ")";
        AtomicInteger currIndex = new AtomicInteger(0);
        String insertList = recordList.stream().map(e -> {
            Integer[] indexArray = new Integer[fns.length];
            for (int i = 0; i < indexArray.length; i++) {
                indexArray[i] = currIndex.get();
            }
            String format = String.format(values, indexArray);
            currIndex.incrementAndGet();
            return format;
        }).collect(Collectors.joining(","));
        sql.append(insertList);
        return sql.toString();
    }


    /**
     * 获取lambda表达式属性名
     * @return
     */
    private String getProperty(SFunction<T, Object> fn) {
        return Optional.ofNullable(getSerializedLambda(fn)).map(SerializedLambda::getImplMethodName).orElse("");
    }

    /**
     * 获取lambda表达式序列化对象
     * @return
     */
    private SerializedLambda getSerializedLambda(SFunction<T, Object> fn) {
        //writeReplace改了好像会报异常
        Method write = null;
        try {
            write = fn.getClass().getDeclaredMethod("writeReplace");
            write.setAccessible(true);
            return (SerializedLambda) write.invoke(fn);
        } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
            return null;
        }
    }


    /**
     * get方法名转属性名
     * @param getMethodName
     * @return
     */
    private String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith("is")) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        return firstToLowerCase(getMethodName);
    }

    private String firstToLowerCase(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }
}
