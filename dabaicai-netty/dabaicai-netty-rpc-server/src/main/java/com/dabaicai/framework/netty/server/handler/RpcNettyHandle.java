package com.dabaicai.framework.netty.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.common.utils.IntUtils;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.enums.NettyMessageType;
import com.dabaicai.framework.netty.server.ioc.HandlerFactory;
import com.dabaicai.framework.netty.server.ioc.RpcHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;

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
        switch (nettyMessageType) {
            case RPC: {
                String message = new String(bytesMessage, 4, bytesMessage.length - 4);
                RpcMessage rpcMessage = JSONObject.parseObject(message, RpcMessage.class);
                rpcChannelRead(ctx, rpcMessage.getUrl(), rpcMessage.getData());
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
     * @param url 请求地址
     * @param data 请求数据区域
     */
    public void rpcChannelRead(ChannelHandlerContext ctx, String url, String data) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        // 不存在处理器
        if (!handler.containsUrl(url)) {
            return;
        }
        // 处理器
        RpcHandler rpcHandlerBean = handler.get(url);
        Object response = rpcHandlerBean.invokeMethod(data);
        if (response == null) {
            // 如果没有响应信息
            return;
        }
        // 发送响应信息
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setUrl(url + "#Response");
        rpcMessage.setData(JSONObject.toJSONString(response));
        byte[] messageType = IntUtils.intToByteLittle(NettyMessageType.RPC.getKey());
        byte[] bytes = JSONObject.toJSONString(rpcMessage).getBytes(StandardCharsets.UTF_8);
        byte[] messageLen = IntUtils.intToByteLittle(bytes.length + 4);
        ctx.write(messageLen);
        ctx.write(messageType);
        ctx.writeAndFlush(bytes);
    }
}
