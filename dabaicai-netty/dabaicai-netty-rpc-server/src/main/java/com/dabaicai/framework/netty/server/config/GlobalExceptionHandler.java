package com.dabaicai.framework.netty.server.config;

/**
 * 全局异常处理器
 *
 * @author zhangyanbing
 * Date: 2021/12/6 10:40
 */
public interface GlobalExceptionHandler <T extends Throwable> {

    /**
     * 收到异常后的处理
     * @param t
     * @return
     */
    Object handler(T t);

    /**
     * 获取异常类型
     * @return
     */
    Class<T> getExceptionClass();

}
