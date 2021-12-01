package com.dabaicai.framework.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.nio.ByteOrder;


/**
 * @author zhangyanbing
 * @Description: 服务器
 * @时间： 2020年8月7日08:31:16
 */
public class NettyServer implements Runnable {
    private static final boolean SSL = System.getProperty("ssl") != null;

    private int port = 60099;

    /**
     * 数据处理
     */
    private ChannelInboundHandlerAdapter channelInboundHandlerAdapter;

    public NettyServer(ChannelInboundHandlerAdapter channelInboundHandlerAdapter) {
        this.channelInboundHandlerAdapter = channelInboundHandlerAdapter;
    }

    public NettyServer(ChannelInboundHandlerAdapter channelInboundHandlerAdapter, int port) {
        this.channelInboundHandlerAdapter = channelInboundHandlerAdapter;
        this.port = port;
    }

    /**
     * 启动netty 等待连接
     *
     * @throws Exception
     */
    private void startNetty() throws Exception {
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup(10);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            p.addFirst(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,100000000,0,4,0,4, true));
                            p.addLast(new ByteArrayDecoder());
                            p.addLast(new ByteArrayEncoder());
                            p.addLast(channelInboundHandlerAdapter);
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(port).sync();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public void run() {
        try {
            startNetty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


