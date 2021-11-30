package com.dabaicai.framework.netty.demo;

import com.dabaicai.framework.netty.server.ioc.HandlerFactory;

/**
 * @author: zhangyanbing
 * Date: 2021/11/30 9:39
 */
public class Application {

    public static void main(String[] args) {
        HandlerFactory handlerFactory = HandlerFactory.getInst();

        handlerFactory.getClass();
    }


}
