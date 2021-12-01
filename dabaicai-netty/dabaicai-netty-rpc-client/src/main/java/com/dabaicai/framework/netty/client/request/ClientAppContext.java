package com.dabaicai.framework.netty.client.request;

import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.common.beans.PackageScanner;
import com.dabaicai.framework.netty.annotation.Request;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.client.RpcNettyClient;
import com.dabaicai.framework.netty.client.proxy.RequestProxy;
import com.dabaicai.framework.netty.enums.NettyMessageType;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author zhangyanbing
 * @Description: 上下文
 * @date 2021/11/29 20:17
 */
public class ClientAppContext {


    /**
     * 目的端口
     */
    private static Integer port;

    /**
     * 目标服务器
     */
    private static String serverIp;

    /**
     * 扫描包路径
     */
    private static String basePackage;

    /**
     * rpc调用对象
     */
    private static RpcNettyClient rpcNettyClient;

    /**
     * 单例
     */
    private final static ClientAppContext APP_CONTEXT = new ClientAppContext();

    /**
     * 扫描的代理对象 key为接口全类名， value 为代理对象
     */
    private static Map<String, Object> requestMap;

    public static ClientAppContext getAppContext() {
        return APP_CONTEXT;
    }

    private ClientAppContext() {
    }

    /**
     * 初始话  必须调用此方法
     */
    public static boolean  init() {
        if (serverIp == null || port == null || basePackage == null) {
            return false;
        }
        requestMap = scanRequest(basePackage);
        // 创建客户端链接对象
        rpcNettyClient = new RpcNettyClient(serverIp, port);
        return true;
    }

    /**
     * 扫描处理器
     * @return
     */
    private static Map<String, Object> scanRequest(String basePackage) {
        Map<String, Object> map = new HashMap<>();
        List<Class<? extends Annotation>> classes = Arrays.asList(Request.class);
        new PackageScanner(basePackage, classes) {
            @Override
            public void dealClass(Class<?> klass) {
                RequestProxy requestProxy = new RequestProxy(klass);
                map.put(klass.getName(), requestProxy.getProxy());
            }
        }.packageScan();
        return map;
    }

    /**
     * 发送一个rpc Message
     * @param message
     */
    public static void writeMessage(RpcMessage message) {
        rpcNettyClient.writeMessage(message);
    }

    /**
     * 发送二进制消息
     * @param bytes
     */
    public static void writeBytes(byte[] bytes) {
        rpcNettyClient.writeBytes(bytes);
    }

    /**
     * 获取requestBean
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return requestMap.get(name);
    }

    /**
     * 获取requestBean
     * @param zClass 接口class
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> zClass) {
        return (T)getBean(zClass.getName());
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        ClientAppContext.port = port;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String serverIp) {
        ClientAppContext.serverIp = serverIp;
    }

    public static String getBasePackage() {
        return basePackage;
    }

    public static void setBasePackage(String basePackage) {
        ClientAppContext.basePackage = basePackage;
    }
}
