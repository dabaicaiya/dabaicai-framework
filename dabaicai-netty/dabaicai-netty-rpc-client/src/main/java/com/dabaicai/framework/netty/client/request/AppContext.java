package com.dabaicai.framework.netty.client.request;

import com.dabaicai.framework.common.beans.PackageScanner;
import com.dabaicai.framework.netty.annotation.Handler;
import com.dabaicai.framework.netty.annotation.Request;
import com.dabaicai.framework.netty.client.proxy.RequestProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author zhangyanbing
 * @Description: 上下文
 * @date 2021/11/29 20:17
 */
public class AppContext {


    private static AppContext appContext = new AppContext();

    private static Map<String, Object> requestMap;

    public static AppContext getAppContext() {
        return appContext;
    }

    private AppContext() {
        requestMap = scanRequest("com.dabaicai");
    }

    /**
     * 扫描处理器
     * @return
     */
    public Map<String, Object> scanRequest(String basePackage) {
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
     * 获取requestBean
     * @param name
     * @return
     */
    public Object getBean(String name) {
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

}
