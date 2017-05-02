package com.example.yechy.tvass.communication;

import com.example.yechy.tvass.communication.net.TcpApi;
import com.example.yechy.tvass.communication.net.UdpApi;
import com.example.yechy.tvass.flatbuffers.SocketMsgType;
import com.example.yechy.tvass.util.FlatUtil;

import java.nio.ByteBuffer;

import io.reactivex.Observable;

/**
 * Created by yechy on 2017/4/22.
 */

public class CommModel implements ICommModel {
    private int mode = Constant.COMMUNICATION_MODE_WIFI;
    private static final String HEAD = "TVA";
    private TcpApi tcpApi;
    private UdpApi udpApi;

    public CommModel(TcpApi tcpApi, UdpApi udpApi) {
        this.tcpApi = tcpApi;
        this.udpApi = udpApi;
    }

    /**
     * 搜索设备
     * @return
     */
    @Override
    public Observable searchDevice(int ip) {
        Observable observable = null;

        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            ByteBuffer byteBuffer = FlatUtil.createSocketMessage(HEAD,
                    SocketMsgType.MESSAGE_TYPE_FIND_DEVICE_REQUEST, 1);
            observable = udpApi.sendUdpMulticast(byteBuffer);

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return observable;
    }

    /**
     * 连接设备
     * @param ip
     * @param port
     * @return
     */
    @Override
    public Observable connectDevice(String ip, int port) {
        Observable observable = null;
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            tcpApi.connectDevice(ip, port);

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return observable;
    }

    /**
     * 发送键值
     * @param keyCode
     * @return
     */
    @Override
    public Observable sendKeyCode(int keyCode) {
        Observable observable = null;

        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            tcpApi.sendAndReceiveData(new byte[1]);

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return observable;
    }
}
