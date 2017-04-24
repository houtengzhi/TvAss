package com.yechy.tvassserver.net;

/**
 * Created by yechy on 2017/4/15.
 */

public final class Constant {
    private Constant() {
    }

    public static final byte PACKET_TYPE_FIND_DEVICE_REQ_10 = 0x10; //搜索请求
    public static final byte PACKET_TYPE_FIND_DEVICE_RSP_11 = 0x11; //搜索响应
    public static final byte PACKET_TYPE_FIND_DEVICE_CHK_12 = 0x12; //搜索确认

    public static final byte PACKET_DATA_TYPE_DEVICE_NAME = 0x20; //
    public static final byte PACKET_DATA_TYPE_DEVICE_ROOM = 0x21; //
}
