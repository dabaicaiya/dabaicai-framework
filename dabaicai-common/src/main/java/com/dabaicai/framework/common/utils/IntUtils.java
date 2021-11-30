package com.dabaicai.framework.common.utils;

/**
 * @author zhangyanbing
 * @Description: 整型工具类
 * @date 2021/5/19 21:15
 */
public class IntUtils {

    public static byte[] intToByteBig(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n >> 24 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[3] = (byte) (n & 0xff);
        return b;
    }

    public static byte[] intToByteLittle(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes, boolean isLittle) {
        int value = byteArrayToInt(bytes);
        if (isLittle) {
            byte[] valueBytes = intToByteLittle(value);
            return byteArrayToInt(valueBytes);
        }
        return value;
    }


    void decoderIpHeader(byte[] input) {

    }
    /**
     * 转换byte数组为int（小端）
     * @return
     * @note 数组长度至少为4，按小端方式转换,即传入的bytes是小端的，按这个规律组织成int
     */
    public static int Bytes2Int_LE(byte[] bytes){
        if(bytes.length < 4) {
            return -1;
        }
        int iRst = (bytes[0] & 0xFF);
        iRst |= (bytes[1] & 0xFF) << 8;
        iRst |= (bytes[2] & 0xFF) << 16;
        iRst |= (bytes[3] & 0xFF)<< 24;

        return iRst;
    }
}
