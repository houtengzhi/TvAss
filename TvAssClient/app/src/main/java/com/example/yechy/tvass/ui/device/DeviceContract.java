package com.example.yechy.tvass.ui.device;

import com.example.yechy.tvass.base.BaseContract;
import com.example.yechy.tvass.model.bean.Device;

import java.util.ArrayList;

/**
 * Created by yechy on 2017/4/8.
 */

public interface DeviceContract {
    interface IView extends BaseContract.IView {
        void refreshDeviceRecyclerView(ArrayList<Device> deviceArrayList);
        void setSearchButtonClickable(boolean isClickable);
    }

    interface IPresenter<T> extends BaseContract.IPresenter<T> {
        void registerSearchMessage();
        void clearDeviceList();
        void startSearchDevices();
        void stopSearchDevices();

        void connectDevice(Device device, int port);
        void disconnectDevice();
        void sendKeyCode(int keyCode, byte keyStatus);
    }
}
