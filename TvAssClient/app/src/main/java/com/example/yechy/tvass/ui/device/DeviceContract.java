package com.example.yechy.tvass.ui.device;

import com.example.yechy.tvass.base.BaseContract;

/**
 * Created by yechy on 2017/4/8.
 */

public interface DeviceContract {
    interface IView extends BaseContract.IView {

    }

    interface IPresenetr<T> extends BaseContract.IPresenter<T> {
        void setDevicesListener();
        void searchDevices();
    }
}
