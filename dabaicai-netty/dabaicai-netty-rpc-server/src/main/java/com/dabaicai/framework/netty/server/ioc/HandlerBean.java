package com.dabaicai.framework.netty.server.ioc;

import lombok.Data;

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
     * 接口名称
     */
    private String interfaceName;

    /**
     * 处理器执行类
     */
    private Object handler;

    /**
     * 消息响应发送
     */
    private Object response;

}
