package com.dabaicai.framework.netty.demo.handler;

import com.dabaicai.framework.netty.annotation.Handler;
import com.dabaicai.framework.netty.demo.req.LoginReq;
import com.dabaicai.framework.netty.demo.req.LogoutReq;

/**
 * 登录处理
 *
 * @author: zhangyanbing
 * Date: 2021/11/30 9:37
 */
@Handler("login")
public class LoginHandler {

    public String login(LoginReq loginReq) {
        System.out.println(loginReq);
        return "success";
    }

    public String logout(LogoutReq loginReq) {
        System.out.println(loginReq);
        return "success";
    }
}
