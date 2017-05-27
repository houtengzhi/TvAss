package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.communication.Constant;
import com.example.yechy.tvass.model.bean.Device;

import java.net.DatagramPacket;
import java.nio.charset.Charset;

/**
 * Created by yechy on 2017/4/15.
 */

public class UdpUtil {

    /**
     * 打包搜索报文
     * 协议：$ + packType(1) + sendSeq(4) + [deviceIP(n<=15)]
     * @param seq - 发送序列
     * @param packType - 报文类型
     * @param deviceIP - 设备IP，仅确认时携带
     * @return
     */
    public static byte[] packData(int seq, byte packType, String deviceIP) {
        byte[] data = new byte[1024];
        int offset = 0;

        data[offset++] = '$';
        data[offset++] = packType;

        seq = (seq == 3? 1 : ++seq);
        data[offset++] = (byte) seq;
        data[offset++] = (byte) (seq >> 8);
        data[offset++] = (byte) (seq >> 16);
        data[offset++] = (byte) (seq >> 24);

        if (packType == Constant.PACKET_TYPE_FIND_DEVICE_CHK_12) {
            byte[] ips = deviceIP.getBytes(Charset.forName("UTF-8"));
            System.arraycopy(ips, 0, data, offset, ips.length);
            offset += ips.length;
        }

        byte[] result = new byte[offset];
        System.arraycopy(data, 0, result, 0, offset);
        return result;
    }

    /**
     * 解析报文
     * 协议：$ + packType(1) + data(n)
     * data：由n组数据，每组type(1) + length(4) + data(length)
     * type类型中包含name、room类型，但name必须在最前面
     * @param packet
     * @return
     */
    public static Device parsePacket(DatagramPacket packet) {
        if (packet == null || packet.getAddress() == null) {
            return null;
        }

        String ip = packet.getAddress().getHostAddress();
        int port = packet.getPort();
        int dataLen = packet.getLength();
        int offset = 0;
        byte packType;
        byte type;
        int len;
        Device deviceBean = null;

        if (dataLen < 2) {
            return null;
        }

        byte[] data = new byte[dataLen];
        System.arraycopy(packet.getData(), packet.getOffset(), data, 0, dataLen);

        if (data[offset++] != '$') {
            return null;
        }

        packType = data[offset++];
        if (packType != Constant.PACKET_TYPE_FIND_DEVICE_RSP_11) {
            return null;
        }

        while (offset + 5 < dataLen) {
            type = data[offset++];
            len = data[offset++] & 0xFF;
            len |= (data[offset++] << 8);
            len |= (data[offset++] << 16);
            len |= (data[offset++] << 24);

            if (offset + len > dataLen) {
                break;
            }

            switch (type) {
                case Constant.PACKET_DATA_TYPE_DEVICE_NAME:
                    String name = new String(data, offset, len, Charset.forName("UTF-8"));
                    deviceBean = new Device();
                    deviceBean.setName(name);
                    deviceBean.setIp(ip);
                    deviceBean.setPort(port);
                    break;

                case Constant.PACKET_DATA_TYPE_DEVICE_ROOM:
                    String room = new String(data, offset, len, Charset.forName("UTF-8"));
                    if (deviceBean != null) {
                        deviceBean.setRoom(room);
                    }
                    break;

                default:
                    break;
            }

            offset += len;

        }

        return deviceBean;
    }
}
