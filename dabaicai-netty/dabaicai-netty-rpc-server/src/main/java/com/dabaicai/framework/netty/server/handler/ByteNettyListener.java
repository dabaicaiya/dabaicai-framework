package com.dabaicai.framework.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2021/11/30 21:57
 */
public interface ByteNettyListener {

    /**
     * 读取函数
     * @param ctx 通道
     * @param bytes 读取到的数据
     * @param offset 偏移地址
     * @param len 长度
     */
    void channelRead(ChannelHandlerContext ctx, byte[] bytes, int offset, int len);

}
