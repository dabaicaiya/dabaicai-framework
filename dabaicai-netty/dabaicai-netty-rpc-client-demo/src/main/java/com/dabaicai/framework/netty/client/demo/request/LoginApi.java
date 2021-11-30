package com.dabaicai.framework.netty.client.demo.request;

import com.dabaicai.framework.netty.annotation.Request;
import com.dabaicai.framework.netty.client.demo.req.LoginReq;

/**
 * 登录接口
 *
 * @author zhangyanbing
 * Date: 2021/11/30 14:38
 */
@Request("login")
public interface LoginApi {

    void login(LoginReq loginReq);
}
