package com.dabaicai.framework.netty.bean;

import lombok.Data;

/**
 * 远程调用message
 *
 * @author: zhangyanbing
 * Date: 2021/11/30 9:18
 */
@Data
public class RpcMessage {

    /**
     * 请求id
     */
    private String messageId;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求的数据区
     */
    private String data;

    public RpcMessage() {
        // todo: 生成一个 messageId

    }
}
