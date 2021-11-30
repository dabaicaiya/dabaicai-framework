package com.dabaicai.framework.netty.client.proxy;


import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.netty.annotation.Request;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.utils.UrlUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhangyanbing
 * @Description: 响应代理接口
 * @date 2021/11/29 21:28
 */
public class RequestProxy implements InvocationHandler {

    private Class<?> zClass;

    public RequestProxy(Class<?> zClass) {
        this.zClass = zClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request annotation = ((Proxy)proxy).getClass().getAnnotation(Request.class);
        String baseUrl = annotation.value();
        // 请求地址
        String url = UrlUtils.generateUrl(baseUrl, method.getName());
        RpcMessage rpcMes = new RpcMessage();
        rpcMes.setUrl(url);
        if (args.length > 0) {
            rpcMes.setData(JSONObject.toJSONString(args[0]));
        }
        return null;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] {zClass}, this);
    }
}
