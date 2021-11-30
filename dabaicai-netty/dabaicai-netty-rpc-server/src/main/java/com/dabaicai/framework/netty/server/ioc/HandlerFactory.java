package com.dabaicai.framework.netty.server.ioc;

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
     * 方法处理工厂
     */
    private Map<String, RpcHandlerBean> rpcHandlerFactory = new HashMap<>();

    public HandlerFactory() {
        AppContext appContext = AppContext.getAppContext();
        List<HandlerBean> handlerList = appContext.getHandlerList();
        for (HandlerBean handlerBean : handlerList) {
            String newBaseUrl = "";
            String baseUrl = handlerBean.getBaseUrl();
            if (baseUrl != null && baseUrl.length() > 0) {
                if (handlerBean.getBaseUrl().charAt(0) != '/') {
                    newBaseUrl += "/";
                }
                newBaseUrl += baseUrl;
            }
            // 处理器
            Object handler = handlerBean.getHandler();
            Method[] handlerMethods = handler.getClass().getDeclaredMethods();
            for (Method handlerMethod : handlerMethods) {
                RpcHandlerBean rpcHandlerBean = new RpcHandlerBean();
                rpcHandlerBean.setHandlerBean(handlerBean);
                rpcHandlerBean.setUrl(newBaseUrl + "/" + handlerMethod.getName());
                rpcHandlerBean.setHandlerMethod(handlerMethod);
                rpcHandlerFactory.put(rpcHandlerBean.getUrl(), rpcHandlerBean);
            }
        }
    }
}
