package com.dabaicai.framework.netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zhangyanbing
 * @Description: netty的事件处理
 * @date 2021/11/29 20:17
 */
@ChannelHandler.Sharable
public class RpcNettyHandle extends ChannelInboundHandlerAdapter {

    /**
     * 接收请求后的处理类
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

    }
}
