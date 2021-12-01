package com.dabaicai.framework.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.common.utils.IntUtils;
import com.dabaicai.framework.netty.NettyClient;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.client.handler.RpcClientHandler;
import com.dabaicai.framework.netty.client.request.ClientAppContext;
import com.dabaicai.framework.netty.enums.NettyMessageType;
import io.netty.channel.Channel;
import javafx.application.Application;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangyanbing
 * @Description: 远程调用客户端
 * @date 2021/11/29 20:14
 */
public class RpcNettyClient implements Runnable{

    private NettyClient nettyClient;

    public RpcNettyClient(String serverIp, int port) {
        ClientAppContext.setServerIp(serverIp);
        ClientAppContext.setPort(port);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        // 事件处理器
        RpcClientHandler rpcClientHandler = new RpcClientHandler();
        nettyClient = new NettyClient(ClientAppContext.getServerIp(), ClientAppContext.getPort(), rpcClientHandler);
    }

    /**
     * 写锁
     */
    private final ReentrantLock writeLock = new ReentrantLock();

    /**
     * 发送一个rpc Message
     * @param message
     */
    public void writeMessage(RpcMessage message) {
        byte[] bytes = JSONObject.toJSONString(message).getBytes(StandardCharsets.UTF_8);
        writeAndFlush(NettyMessageType.RPC.getKey(), bytes);
    }

    /**
     * 发送二进制消息
     * @param bytes
     */
    public void writeBytes(byte[] bytes) {
        writeAndFlush(NettyMessageType.BYTE.getKey(), bytes);
    }

    /**
     * 发送数据
     * @param type 类型
     * @param bytes 具体数据
     */
    private void writeAndFlush(int type, byte[] bytes) {
        Channel channel = nettyClient.getChannel();
        if (channel == null) {
            return;
        }
        byte[] messageLen = IntUtils.intToByteLittle(bytes.length + 4);
        byte[] messageType = IntUtils.intToByteLittle(type);
        writeLock.lock();
        try {
            channel.write(messageLen);
            channel.write(messageType);
            channel.writeAndFlush(bytes);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void run() {
        nettyClient.run();
    }
}
