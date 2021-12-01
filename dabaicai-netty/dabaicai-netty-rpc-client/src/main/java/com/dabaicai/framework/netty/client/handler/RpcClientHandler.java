package com.dabaicai.framework.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @author zhangyanbing
 * @Description: 客户端监听处理
 * @date 2021/11/29 20:17
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端连接服务器后调用，简单的就ctx.writeAndFlush(ByteBuf)，复杂点就自定义编解码器
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)  {


    }

    /**
     * 接收到数据后调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

    }

    /**
     * 完成时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("channelReadComplete");
        ctx.flush();
    }

    /**
     * 发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
