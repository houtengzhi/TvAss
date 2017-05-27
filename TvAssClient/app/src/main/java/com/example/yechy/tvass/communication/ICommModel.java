package com.example.yechy.tvass.communication;

import com.example.yechy.tvass.model.bean.Device;

import io.reactivex.Flowable;

/**
 * Created by yechy on 2017/4/22.
 */

public interface ICommModel {
    Flowable<Device> startSearchDevice();
    Flowable<Boolean> stopSearchDevice();
    Flowable<Boolean> connectDevice(String ip, int port);
    Flowable<Boolean> disconnectDevice();
    Flowable<Boolean> sendKeyCode(int keyCode, byte keyStatus);
}
