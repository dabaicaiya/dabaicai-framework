package com.dabaicai.framework.orm.mybatis.batchinsert;

import com.dabaicai.framework.common.lambda.SFunction;
import com.dabaicai.framework.orm.mybatis.batchinsert.provider.BatchInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批量插入数据库通用操作
 *
 * @author zhangyanbing
 * Date: 2022/1/13 10:03
 */
public interface BatchInsertMapper<T> {

    /**
     * 批量插入方法
     * @param recordList 记录
     * @param fns 字段列表
     * @return
     */
    @InsertProvider(type = BatchInsertProvider.class, method = "batchInsert")
    int batchInsert(@Param("recordList") List<T> recordList, @Param("properties") SFunction<T,Object>... fns);

}
