package com.dabaicai.framework.common.utils;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * list工具类
 *
 * @author: zhangyanbing
 * Date: 2021/11/29 15:26
 */
public class ListUtils {

    /**
     * 批量操作list
     * @param list 原数组
     * @param batchNum 一次处理的大小
     * @param consumer 具体处理函数
     * @param <T>
     */
    public static <T> void batchList(List<T> list, int batchNum, Consumer<List<T>> consumer) {
        int size = list.size();
        int allNum = size / batchNum;
        allNum = size % batchNum > 0 ? allNum + 1 : allNum;
        for (int i = 1; i <= allNum; i++) {
            if (size < batchNum) {
                consumer.accept(list);
            } else {
                int end = i * batchNum;
                if (i == allNum) {
                    end = size;
                }
                List<T> subList = list.subList((i - 1) * batchNum, end);
                consumer.accept(subList);
            }
        }
    }

    /**
     * 使用线程池批量操作
     * @param list
     * @param batchNum
     * @param threadPoolSize
     * @param consumer
     * @param <T>
     */
    public static <T> void batchListPool(List<T> list, int batchNum, int threadPoolSize, Consumer<List<T>> consumer) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(threadPoolSize));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 使用线程池操作
        batchList(list, batchNum, e-> threadPoolExecutor.execute(()-> consumer.accept(e)));
        threadPoolExecutor.shutdown();
        try {
            // 等待所有线程执行完毕
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
