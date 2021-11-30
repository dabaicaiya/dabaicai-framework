package com.dabaicai.framework.netty.server.ioc;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhangyanbing
 * @Description: Rpc处理器
 * @date 2021/11/29 21:47
 */
@Data
public class RpcHandler {

    /**
     * 请求url
     */
    private String url;

    /**
     * 执行的方法
     */
    private Method handlerMethod;

    /**
     * 处理请求的处理器
     */
    private HandlerBean handlerBean;

    /**
     * 执行方法
     * @param jsonData
     * @return
     */
    public Object invokeMethod(String jsonData) {
        Class<?>[] parameterTypes = handlerMethod.getParameterTypes();
        try {
            Object handler = handlerBean.getHandler();
            if (parameterTypes.length == 0) {
                // 无参调用
                return handlerMethod.invoke(handler);
            } else {
                // 带参数调用
                return handlerMethod.invoke(handler, JSONObject.parseObject(jsonData, parameterTypes[0]));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

}
