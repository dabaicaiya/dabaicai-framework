package com.dabaicai.framework.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyanbing
 * @Description: 二进制流处理器
 * @date 2021/11/30 21:59
 */
public class ByteNettyHandler {

    /**
     * 处理函数监听列表
     */
    private static List<ByteNettyListener> byteNettyListenerList = new ArrayList<>();


    /**
     * 添加一个监听器
     * @param byteNettyListener
     */
    public static synchronized void addByteNettyListener(ByteNettyListener byteNettyListener) {
        byteNettyListenerList.add(byteNettyListener);
    }

    /**
     * 读取函数
     * @param ctx
     * @param bytes
     * @param offset
     * @param len
     */
    public static void channelRead(ChannelHandlerContext ctx, byte[] bytes, int offset, int len) {
        for (ByteNettyListener byteNettyListener : byteNettyListenerList) {
            byteNettyListener.channelRead(ctx, bytes, offset, len);
        }
    }

}
