package com.yechy.tvassserver.communication;


import com.yechy.tvassserver.flatbuffers.TcpMessage;

import java.net.DatagramPacket;

import io.reactivex.Flowable;

/**
 * Created by yechy on 2017/4/22.
 */

public interface ICommModel {
    Flowable<DatagramPacket> registerUdpMulticast();
    Flowable<Boolean> closeUdpSocket();

    Flowable<TcpMessage> registerTcpMessage(int port);
    Flowable<Boolean> sendTcpData(byte[] sendBytes);
    Flowable<Boolean> closeTcpSocket();
}
