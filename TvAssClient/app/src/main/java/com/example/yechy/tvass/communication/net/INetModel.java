package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.model.bean.Device;

import io.reactivex.Flowable;

/**
 * Created by yechy on 2017/5/27.
 */

public interface INetModel {
    Flowable<Device> registerSearchMessage();
    Flowable<Boolean> closeUdpSocket();
    Flowable<Boolean> startSearchDevice();

    Flowable<Boolean> connectDevice(String ip, int port);
    Flowable<Boolean> disconnectDevice();
    Flowable<Boolean> sendKeyCode(int keyCode, byte keyStatus);
}
