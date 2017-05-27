package com.yechy.tvassserver.util;

import java.net.DatagramPacket;

/**
 * Created by yechy on 2017/5/24.
 */

public class StringUtil {

    public static String getDatagramPacketInfo(DatagramPacket packet) {
        if (packet == null) {
            return null;
        }
        return "DatagramPacket: packet length = "
                + packet.getLength() + ", HostAddress = " + packet.getAddress().getHostAddress()
                + ", port = " + packet.getPort() + ", data length = " + packet.getData().length
                + ", offset = " + packet.getOffset();
    }

    /**
     * 字节数组转十六进制字符串
     * @param byteArray
     * @return
     */
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    /**
     * 把byte 转化为两位十六进制数
     */
    public static String toHex(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        if (result.length() == 1) {
            result = '0' + result;
        }
        return result;
    }
}
