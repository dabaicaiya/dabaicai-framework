package com.dabaicai.framework.common.utils;

/**
 * @author zhangyanbing
 * @Description:
 * @date 2021/3/6 14:26
 */
public class ByteUtils {

    public static String to16String(byte[] bytes, int lastIndex) {
        return to16String(bytes, 0, lastIndex);
    }

    /**
     * 将字节转为16进制字符
     * @param bytes
     * @return
     */
    public static String to16String(byte[] bytes, int startIndex, int lastIndex) {
        StringBuffer sb = new StringBuffer();
        for (int i = startIndex; i < lastIndex; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
            sb.append(' ');
            if (i % 16 == 0) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public static String toCharString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append((char)bytes[i]);
        }
        return sb.toString();
    }

    /**
     * 前缀匹配 要求 prefix的全部内容在 data的头部
     * @param prefix 前缀
     * @param data 数据区
     * @return
     */
    public static boolean prefixMatching(char[] prefix, byte[] data) {
        if (data.length < prefix.length) {
            return false;
        }
        boolean isOk = true;
        for (int j = 0; j < prefix.length; j++) {
            if (prefix[j] != data[j]) {
                isOk = false;
                break;
            }
        }
        return isOk;
    }

    public static void printBytes(byte[] bytes) {
        if (bytes == null) {
            return;
        }
        for (byte aByte : bytes) {
            System.out.print(aByte);
        }
        System.out.println();
    }

    /**
     * ip字符串转字节数组
     * @param addr
     * @return
     */
    public static byte[] toArrayAddr(String addr) {
        if (addr == null) {
            return null;
        }
        String[] splitIp = addr.split("\\.");
        byte[] arrayAddr = new byte[splitIp.length];
        for (int i = 0; i < splitIp.length; i++) {
            arrayAddr[i] = (byte)Integer.parseInt(splitIp[i]);
        }
        return arrayAddr;
    }

    /**
     * 字符数组转ip字符串
     * @param addr
     */
    public static String toStringAddr(char[] addr, int startIndex) {
        if (addr.length < 4) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append((int)addr[startIndex]).append('.')
                .append((int)addr[startIndex+ 1]).append('.')
                .append((int)addr[startIndex + 2]).append('.')
                .append((int)addr[startIndex + 3]);
        return sb.toString();
    }


    public static String toStringAddr(char[] addr) {
        return toStringAddr(addr, 0);
    }

    /**
     * 字节数组转ip字符串
     * @param addr
     * @param startIndex
     * @return
     */
    public static String toStringAddr(byte[] addr, int startIndex) {
        char[] tmpArr = new char[addr.length - startIndex];
        for (int i = startIndex; i < startIndex + 4; i++) {
            tmpArr[i - startIndex] = (char)(addr[i] & 0xFF);
        }
        return toStringAddr(tmpArr, 0);
    }

    public static String toStringAddr(byte[] addr) {
        char[] tmpArr = new char[addr.length];
        for (int i = 0; i < addr.length; i++) {
            tmpArr[i] = (char)(addr[i] & 0xFF);
        }
        return toStringAddr(tmpArr, 0);
    }

    /**
     * int32位转字符串ip
     * @param intIp
     * @return
     */
    public static String intToIp(Integer intIp) {
        byte[] bytes = IntUtils.intToByteBig(intIp);
        return toStringAddr(bytes);
    }

    /**
     * 字符串ip转int32
     * @param ip
     * @return
     */
    public static Integer ipToInt(String ip) {
        byte[] bytes = toArrayAddr(ip);
        return IntUtils.byteArrayToInt(bytes);
    }


    public static void main(String[] args) {
        String ip  = "10.255.255.255";
        Integer integer = ipToInt(ip);
        System.out.println(integer);
        String s = intToIp(integer);
        System.out.println(s);
    }

}
