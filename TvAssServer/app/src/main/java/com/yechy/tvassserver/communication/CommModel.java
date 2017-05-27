package com.yechy.tvassserver.communication;


import com.yechy.tvassserver.communication.net.TcpApi;
import com.yechy.tvassserver.communication.net.UdpApi;
import com.yechy.tvassserver.flatbuffers.FlatUtil;
import com.yechy.tvassserver.flatbuffers.TcpMessage;
import com.yechy.tvassserver.util.L;

import java.net.DatagramPacket;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by yechy on 2017/4/22.
 */

public class CommModel implements ICommModel {
    private static final String TAG= CommModel.class.getSimpleName();
    private int mode = Constant.COMMUNICATION_MODE_WIFI;
    private TcpApi tcpApi;
    private UdpApi udpApi;

    @Inject
    public CommModel(TcpApi tcpApi, UdpApi udpApi) {
        this.tcpApi = tcpApi;
        this.udpApi = udpApi;
    }

    @Override
    public Flowable<DatagramPacket> registerUdpMulticast() {
        L.d(TAG, "registerUdpMulticast()");
        return udpApi.registerUdpMulticast();
    }

    @Override
    public Flowable<TcpMessage> registerTcpMessage(int port) {
        L.d(TAG, "registerTcpMessage(), port = " + port);
        return tcpApi.registerTcpMessage(port)
                .map(new Function<byte[], TcpMessage>() {
                    @Override
                    public TcpMessage apply(@NonNull byte[] bytes) throws Exception {
                        TcpMessage tcpMessage = FlatUtil.getTcpMessage(bytes);
                        return tcpMessage;
                    }
                });

    }

    @Override
    public Flowable<Boolean> sendTcpData(byte[] sendBytes) {
        L.d(TAG, "sendTcpData()");
        return tcpApi.sendTcpData(sendBytes);
    }
}
