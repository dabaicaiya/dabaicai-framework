package com.dabaicai.framework.netty.server.ioc;

import lombok.Data;

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
     * 处理请求的处理器
     */
    private HandlerBean handlerBean;

}
