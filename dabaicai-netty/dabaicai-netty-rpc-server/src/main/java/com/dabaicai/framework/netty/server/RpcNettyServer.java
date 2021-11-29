package com.dabaicai.framework.netty.server;

import com.dabaicai.framework.netty.NettyServer;
import com.dabaicai.framework.netty.server.handler.RpcNettyHandle;

/**
 * @author zhangyanbing
 * @Description: 远程调用服务器
 * @date 2021/11/29 20:14
 */
public class RpcNettyServer implements Runnable{

    private String scanPackage;

    private int port;

    private NettyServer nettyServer;

    public RpcNettyServer(String scanPackage, int port) {
        this.scanPackage = scanPackage;
        this.port = port;
        nettyServer = new NettyServer(new RpcNettyHandle(), port);
    }


    @Override
    public void run() {
        nettyServer.run();
    }
}
