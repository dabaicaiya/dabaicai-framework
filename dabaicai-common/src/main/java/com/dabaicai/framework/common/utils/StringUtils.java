package com.dabaicai.framework.common.utils;

import java.util.UUID;

/**
 * @author Administrator
 * @date 2020/7/14 22:23
 */
public class StringUtils {

    public static final char UNDERLINE = '_';

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



    /**
     * 字符串驼峰转下划线格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
