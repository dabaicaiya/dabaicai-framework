package com.dabaicai.framework.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.common.utils.IntUtils;
import com.dabaicai.framework.netty.NettyClient;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.client.handler.RpcClientHandler;
import com.dabaicai.framework.netty.enums.NettyMessageType;
import io.netty.channel.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangyanbing
 * @Description: 远程调用服务器
 * @date 2021/11/29 20:14
 */
public class RpcNettyClient implements Runnable{

    private int port;

    private String serverIp;

    private NettyClient nettyClient;

    public RpcNettyClient(String scanPackage, String serverIp, int port) {
        System.setProperty("dabaicai.netty.scanPackage", scanPackage);
        this.port = port;
        this.serverIp = serverIp;
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        // 事件处理器
        RpcClientHandler rpcClientHandler = new RpcClientHandler();
        nettyClient = new NettyClient(serverIp, port, rpcClientHandler);
    }

    /**
     * 发送一个rpc Message
     * @param message
     */
    public void writeMessage(RpcMessage message) {
        Channel channel = nettyClient.getChannel();
        if (channel == null) {
            return;
        }
        byte[] messageType = IntUtils.intToByteLittle(NettyMessageType.RPC.getKey());
        byte[] bytes = JSONObject.toJSONString(message).getBytes(StandardCharsets.UTF_8);
        byte[] messageLen = IntUtils.intToByteLittle(bytes.length + 4);
        channel.write(messageLen);
        channel.write(messageType);
        channel.writeAndFlush(bytes);
    }

    /**
     * 发送二进制消息
     * @param bytes
     */
    public void writeBytes(byte[] bytes) {
        Channel channel = nettyClient.getChannel();
        if (channel == null) {
            return;
        }
        byte[] messageType = IntUtils.intToByteLittle(NettyMessageType.RPC.getKey());
        channel.write(bytes.length + 4);
        channel.write(messageType);
        channel.writeAndFlush(bytes);
    }

    @Override
    public void run() {
        nettyClient.run();
    }
}
