package com.dabaicai.framework.netty.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.common.utils.IntUtils;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.enums.NettyMessageType;
import com.dabaicai.framework.netty.server.ioc.HandlerFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangyanbing
 * @Description: netty的事件处理
 * @date 2021/11/29 20:17
 */
@ChannelHandler.Sharable
public class RpcNettyHandle extends ChannelInboundHandlerAdapter {

    /**
     * 处理器仓库
     */
    private HandlerFactory handler = HandlerFactory.getInst();

    /**
     * 接收请求后的处理类
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        byte[] bytesMessage = (byte[]) msg;
        // 获取请求数据类型
        int messageType = IntUtils.byteArrayToInt(bytesMessage, true);
        // 获取消息类型
        NettyMessageType nettyMessageType = NettyMessageType.getEnumByKey(messageType);
        if (nettyMessageType == null) {
            return;
        }
        switch (nettyMessageType) {
            case RPC: {
                String message = new String(bytesMessage, 4, bytesMessage.length - 4);
                RpcMessage rpcMessage = JSONObject.parseObject(message, RpcMessage.class);
                rpcChannelRead(ctx, rpcMessage);
                break;
            }
            case BYTE: {
                return;
            }
            default: return;
        }
    }

    /**
     * rpc 调用处理方法
     * @param ctx 请求的通道
     */
    public void rpcChannelRead(ChannelHandlerContext ctx, RpcMessage reqMessage) {
        // 处理请求
        Object response = handler.invokeHandler(reqMessage);
        if (response == null) {
            return;
        }
        // 处理响应
        invokeResponse(ctx, reqMessage, response);
    }

    /**
     * 处理响应
     * @param ctx
     * @param reqMessage
     * @param response
     */
    private void invokeResponse(ChannelHandlerContext ctx, RpcMessage reqMessage, Object response) {
        // 发送响应信息
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setUrl(reqMessage.getUrl() + "#response");
        rpcMessage.setData(JSONObject.toJSONString(response));
        byte[] messageType = IntUtils.intToByteLittle(NettyMessageType.RPC.getKey());
        byte[] bytes = JSONObject.toJSONString(rpcMessage).getBytes(StandardCharsets.UTF_8);
        byte[] messageLen = IntUtils.intToByteLittle(bytes.length + 4);
        ctx.write(messageLen);
        ctx.write(messageType);
        ctx.writeAndFlush(bytes);
    }
}
