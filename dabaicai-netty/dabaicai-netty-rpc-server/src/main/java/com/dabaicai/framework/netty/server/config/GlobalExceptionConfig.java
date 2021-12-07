package com.dabaicai.framework.netty.server.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常配置
 * @author zhangyanbing
 * Date: 2021/12/6 10:08
 */
public class GlobalExceptionConfig {

    /**
     * 全局异常处理
     */
    private static Map<Class<? extends Throwable>, GlobalExceptionHandler> exceptionMap = new LinkedHashMap<>();

    /**
     * 添加一个异常捕获
     * @param globalException
     */
    public static void addGlobalException(Class<? extends Throwable> e, GlobalExceptionHandler globalException) {
        exceptionMap.put(e, globalException);
    }

    /**
     * 获取一个异常捕获处理器
     * @param dstException
     * @return
     */
    public static GlobalExceptionHandler getTryException(Class<? extends Throwable> dstException) {
        GlobalExceptionHandler globalExceptionHandler = exceptionMap.get(dstException);
        if (globalExceptionHandler != null) {
            return globalExceptionHandler;
        }

        List<Class<? extends Throwable>> filterList = exceptionMap.keySet()
                .stream().filter(e -> e.isAssignableFrom(dstException))
                .sorted(new ExceptionDepthComparator(dstException)).
                collect(Collectors.toList());
        if (!filterList.isEmpty()) {
            return exceptionMap.get(filterList.get(0));
        }
        return null;
    }

}
