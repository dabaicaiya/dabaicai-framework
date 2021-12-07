package com.dabaicai.framework.netty.server.config;

/**
 * 全局异常处理器
 *
 * @author zhangyanbing
 * Date: 2021/12/6 10:40
 */
public interface GlobalExceptionHandler {

    /**
     * 收到异常后的处理
     * @param throwable
     * @return
     */
    Object handler(Throwable throwable);

}
