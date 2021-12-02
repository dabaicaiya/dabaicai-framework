package com.dabaicai.framework.common.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author zhangyanbing
 * @Description: 支持序列化的方法
 * @date 2021/12/2 20:24
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {

    /**
     * 这个方法返回的SerializedLambda是重点
     * @return
     * @throws Exception
     */
    default SerializedLambda getSerializedLambda() throws Exception {
        //writeReplace改了好像会报异常
        Method write = this.getClass().getDeclaredMethod("writeReplace");
        write.setAccessible(true);
        return (SerializedLambda) write.invoke(this);
    }

    /**
     * 获取类名
     * @return
     */
    default String getImplClass() {
        try {
            return getSerializedLambda().getImplClass();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取方法名
     * @return
     */
    default String getImplMethodName() {
        try {
            return getSerializedLambda().getImplMethodName();
        } catch (Exception e) {
            return null;
        }
    }
}
