package com.dabaicai.framework.netty.server.ioc;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author zhangyanbing
 * @Description: Rpc处理器
 * @date 2021/11/29 21:47
 */
@Data
public class RpcHandlerBean {

    /**
     * 请求url
     */
    private String url;


    /**
     * 执行的方法
     */
    private Method handlerMethod;

    /**
     * 处理请求的处理器
     */
    private HandlerBean handlerBean;

}
