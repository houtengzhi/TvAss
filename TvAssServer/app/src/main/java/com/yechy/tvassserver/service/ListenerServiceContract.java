package com.yechy.tvassserver.service;

import com.yechy.tvassserver.base.BaseContract;

/**
 * Created by yechy on 2017/5/23.
 */

public interface ListenerServiceContract {
    interface IView extends BaseContract.IView {

    }

    interface IPresenetr<T> extends BaseContract.IPresenter<T> {
        void registerUdpMulticast();
        void registerTcpMessage();
        void sendTcpData(byte[] sendBytes);
    }
}
