package com.example.yechy.tvass.communication;

import com.example.yechy.tvass.communication.net.TcpClient;
import com.example.yechy.tvass.communication.net.UdpClient;

import io.reactivex.Observable;

/**
 * Created by yechy on 2017/4/22.
 */

public class CommModel implements ICommModel {
    private int mode = Constant.COMMUNICATION_MODE_WIFI;
    private TcpClient tcpClient;
    private UdpClient udpClient;

    public CommModel(TcpClient tcpClient, UdpClient udpClient) {
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
    }

    /**
     * 搜索设备
     * @return
     */
    @Override
    public Observable searchDevice() {
        Observable observable = null;

        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            observable = udpClient.sendUdpMulticast("");

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
            tcpClient.connectDevice(ip, port);

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
            tcpClient.sendAndReceiveData(new byte[1]);

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return observable;
    }
}
