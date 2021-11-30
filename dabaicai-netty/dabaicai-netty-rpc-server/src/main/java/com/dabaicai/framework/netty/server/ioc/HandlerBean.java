package com.dabaicai.framework.netty.server.ioc;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author zhangyanbing
 * @Description: 处理器类
 * @date 2021/11/29 21:09
 */
@Data
public class HandlerBean {

    /**
     * 父url
     */
    private String baseUrl;


    /**
     * 处理器执行类
     */
    private Object handler;

}
