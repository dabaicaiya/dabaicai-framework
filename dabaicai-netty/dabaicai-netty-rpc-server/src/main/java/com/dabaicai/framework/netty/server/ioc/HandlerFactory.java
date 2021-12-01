package com.dabaicai.framework.netty.server.ioc;

import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyanbing
 * @Description: 处理器工厂
 * @date 2021/11/29 21:45
 */
public class HandlerFactory {

    /**
     * 单利模式
     */
    private static HandlerFactory handlerFactory = new HandlerFactory();

    public static HandlerFactory getInst() {
        return handlerFactory;
    }


    /**
     * 方法处理工厂
     */
    private Map<String, RpcHandler> rpcHandlerFactory = new HashMap<>();

    private HandlerFactory() {
        ServerAppContext appContext = ServerAppContext.getAppContext();
        List<HandlerBean> handlerList = appContext.getHandlerList();
        for (HandlerBean handlerBean : handlerList) {
            // 处理器
            Object handler = handlerBean.getHandler();
            Method[] handlerMethods = handler.getClass().getDeclaredMethods();
            for (Method handlerMethod : handlerMethods) {
                RpcHandler rpcHandlerBean = new RpcHandler();
                rpcHandlerBean.setHandlerBean(handlerBean);
                String url = UrlUtils.generateUrl(handlerBean.getBaseUrl(), handlerMethod.getName());
                rpcHandlerBean.setUrl(url);
                rpcHandlerBean.setHandlerMethod(handlerMethod);
                rpcHandlerFactory.put(rpcHandlerBean.getUrl(), rpcHandlerBean);
            }
        }
    }

    /**
     * 执行处理器
     * @param reqMessage
     * @return
     */
    public Object invokeHandler(RpcMessage reqMessage) {
        String url = reqMessage.getUrl();
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        // 不存在处理器
        if (!this.containsUrl(url)) {
            return null;
        }
        // 处理器
        RpcHandler rpcHandlerBean = this.get(url);
        return rpcHandlerBean.invokeMethod(reqMessage.getData());
    }

    public RpcHandler get(String url) {

        return rpcHandlerFactory.get(url);
    }

    public boolean containsUrl(String url) {
        return rpcHandlerFactory.containsKey(url);
    }
}
