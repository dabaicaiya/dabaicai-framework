package com.dabaicai.framework.netty.utils;

/**
 * @author zhangyanbing
 * Date: 2021/11/30 13:49
 */
public class UrlUtils {

    /**
     * 生成链接地址
     * @param baseUrl 基础url
     * @param methodName 方法名称
     * @return
     */
    public static String generateUrl(String baseUrl, String methodName) {
        String newBaseUrl = "";
        if (baseUrl != null && baseUrl.length() > 0) {
            if (baseUrl.charAt(0) != '/') {
                newBaseUrl += "/";
            }
            newBaseUrl += baseUrl;
        }
        return newBaseUrl + "/" + methodName;
    }
}
