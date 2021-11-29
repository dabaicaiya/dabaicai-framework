package com.dabaicai.framework.common.utils;

import java.util.UUID;

/**
 * @author Administrator
 * @date 2020/7/14 22:23
 */
public class StringUtils {

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        str = str.replace("-", "");
        return str;
    }
}
