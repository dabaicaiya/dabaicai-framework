package com.dabaicai.framework.netty.demo;

import com.alibaba.fastjson.JSONObject;
import com.dabaicai.framework.netty.bean.RpcMessage;
import com.dabaicai.framework.netty.demo.req.LoginReq;
import com.dabaicai.framework.netty.server.ioc.HandlerFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author: zhangyanbing
 * Date: 2021/11/30 9:39
 */
public class Application {

    public static void main(String[] args) {
        HandlerFactory handlerFactory = HandlerFactory.getInst();
        RpcMessage rpcMes = new RpcMessage();
        rpcMes.setUrl("/login/logout");
        LoginReq login = new LoginReq();
        login.setName("你好啊");
        login.setPassword("111111");
        rpcMes.setData(JSONObject.toJSONString(login));
        Object o = handlerFactory.invokeHandler(rpcMes);
        System.out.println(o);
    }


}
