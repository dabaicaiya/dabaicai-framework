package com.dabaicai.framework.netty.server.ioc;

import com.dabaicai.framework.netty.annotation.Handler;
import com.dabaicai.framework.netty.annotation.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author zhangyanbing
 * @Description: 上下文
 * @date 2021/11/29 20:17
 */
public class AppContext {

    /**
     * 扫描包路径
     */
    private String basePackage = "com.dabaicai";

    private static AppContext appContext = null;

    /**
     * 处理器扫描类
     */
    private List<HandlerBean> handlerList = null;

    public static AppContext getAppContext() {
        if (appContext == null) {
            appContext = new AppContext();
        }
        return appContext;
    }

    public AppContext() {
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        this.handlerList = scanHandler();
    }

    /**
     * 扫描处理器
     * @return
     */
    public List<HandlerBean> scanHandler() {
        List<HandlerBean> handlerList = new ArrayList<>();
        List<Class<? extends Annotation>> classes = Arrays.asList(Handler.class);
        new PackageScanner(basePackage, classes) {
            @Override
            public void dealClass(Class<?> klass) {
                HandlerBean handlerBean = new HandlerBean();
                try {
                    Object handler = klass.newInstance();
                    handlerBean.setHandler(handler);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                Handler handler = klass.getAnnotation(Handler.class);
                String baseUrl = handler.value();
                handlerBean.setBaseUrl(baseUrl);
                handlerList.add(handlerBean);
            }
        }.packageScan();
        return handlerList;
    }

    /**
     * 扫描处理器
     * @return
     */
    public Map<String, Object> scanResponse() {
        Map<String, Object> map = new HashMap<>();
        List<Class<? extends Annotation>> classes = Arrays.asList(Request.class);
        new PackageScanner(basePackage, classes) {
            @Override
            public void dealClass(Class<?> klass) {
                map.put(klass.getName(), Proxy.newProxyInstance(klass.getClassLoader(),
                        new Class[]{klass}, new ResponseProxy()));
            }
        }.packageScan();
        return map;
    }


    public List<HandlerBean> getHandlerList() {
        return handlerList;
    }
}
