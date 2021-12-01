package com.dabaicai.framework.netty.server.handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * rpc调用使用的线程池
 *
 * @author zhangyanbing
 * Date: 2021/12/1 10:04
 */
public class RpcHandleThreadPool {

    /**
     * 线程池
     */
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(
                20,
                40,
                0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(20));
    }


    public static void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

}
