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
    }

    interface IPresenetr<T> extends BaseContract.IPresenter<T> {
        void startSearchDevices();
        void stopSearchDevices();
        void connectDevice(String ip, int port);
        void sendKeyCode(int keyCode, byte keyStatus);
    }
}
