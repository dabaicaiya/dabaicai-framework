package com.dabaicai.framework.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.SneakyThrows;

import javax.net.ssl.SSLException;
import java.nio.ByteOrder;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhangyanbing
 * @Description 客户端
 * @date 2020/7/11 16:55
 */
public class NettyClient implements Runnable {

    private static final boolean SSL = System.getProperty("ssl") != null;

    /**
     * 链接的目的地址
     */
    private String host;

    /**
     * 目的端口
     */
    private Integer port;

    /**
     * 事件处理
     */
    private ChannelInboundHandlerAdapter channelInboundHandlerAdapter;


    /**
     * 用于线程同步， netty启动结束后会进入阻塞状态  所以在子线程中创建，然后在主线程进行等待连接成功
     */
    private CountDownLatch countDownLatch = new CountDownLatch(2);


    public void start() {
        //设置处理器
        new Thread(this).start();
        countDownLatch.countDown();
        //等待连接成功
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startClient() throws InterruptedException, SSLException {
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        if (sslCtx != null) {
                            p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                        }
                        p.addFirst(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 100000000, 0, 4, 0, 4, true));
                        p.addLast(new ByteArrayDecoder());
                        p.addLast(new ByteArrayEncoder());
                        p.addLast(channelInboundHandlerAdapter);
                    }
                });

        // Start the client.
        ChannelFuture f = b.connect(host, port).sync();
        System.out.println("Client  ServerBootstrap配置启动完成");
        countDownLatch.countDown();
        try {
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }


    @SneakyThrows
    @Override
    public void run() {
        startClient();
    }
}
