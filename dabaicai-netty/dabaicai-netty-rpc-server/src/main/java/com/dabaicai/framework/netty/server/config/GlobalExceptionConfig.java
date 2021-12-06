package com.dabaicai.framework.netty.server.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常配置
 * @author zhangyanbing
 * Date: 2021/12/6 10:08
 */
public class GlobalExceptionConfig {

    /**
     * 全局异常处理
     */
    private static Map<Class<? extends Throwable>, GlobalExceptionHandler<? extends Throwable>> exceptionMap = new LinkedHashMap<>();

    /**
     * 添加一个异常捕获
     * @param globalException
     */
    public static void addGlobalException(GlobalExceptionHandler<? extends Throwable> globalException) {
        Class<? extends Throwable> exceptionClass = globalException.getExceptionClass();
        exceptionMap.put(exceptionClass, globalException);
    }

    /**
     * 获取一个异常捕获处理器
     * @param dstException
     * @return
     */
    public static GlobalExceptionHandler getTryException(Throwable dstException) {
        GlobalExceptionHandler<? extends Throwable> globalExceptionHandler = exceptionMap.get(dstException.getClass());
        if (globalExceptionHandler != null) {
            return globalExceptionHandler;
        }
        List<GlobalExceptionHandler> list = new ArrayList<>();
        for (GlobalExceptionHandler<? extends Throwable> exceptionHandler : exceptionMap.values()) {

        }
        return null;
    }

}
