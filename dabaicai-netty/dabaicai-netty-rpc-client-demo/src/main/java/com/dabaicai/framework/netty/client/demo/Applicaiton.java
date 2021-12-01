package com.dabaicai.framework.netty.client.demo;

import com.dabaicai.framework.netty.client.demo.req.LoginReq;
import com.dabaicai.framework.netty.client.demo.request.LoginApi;
import com.dabaicai.framework.netty.client.request.ClientAppContext;

/**
 * @author zhangyanbing
 * Date: 2021/11/30 14:35
 */
public class Applicaiton {

    public static void main(String[] args) {
        ClientAppContext appContext = ClientAppContext.getAppContext();
        LoginApi loginApi = appContext.getBean(LoginApi.class);
        System.out.println(loginApi);
        loginApi.login(new LoginReq());
    }
    
}
