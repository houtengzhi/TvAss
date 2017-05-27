package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.model.bean.Device;

import io.reactivex.Flowable;

/**
 * Created by yechy on 2017/5/27.
 */

public interface INetModel {
    Flowable<Device> startSearchDevice();
}
