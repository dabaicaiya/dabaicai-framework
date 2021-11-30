package com.dabaicai.framework.netty.enums;


import com.dabaicai.framework.common.base.ICommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * netty消息类型
 *
 * @author zhangyanbing
 * Date: 2021/11/30 10:32
 */
@Getter
@AllArgsConstructor
public enum NettyMessageType  implements ICommonEnum {

    /**
     * rpc远程调用
     */
    RPC(1, "rpc远程调用"),
    /**
     * 二进制消息
     */
    BYTE(10, "二进制消息");

    private int key;

    private String value;

    public static NettyMessageType getEnumByKey(int key) {
        for (NettyMessageType value : values()) {
            if (value.getKey() == key) {
                return value;
            }
        }
        return null;
    }

}
