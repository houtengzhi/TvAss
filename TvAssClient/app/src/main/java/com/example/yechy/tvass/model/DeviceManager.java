package com.example.yechy.tvass.model;

import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.L;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by yechy on 2017/5/22.
 */

public class DeviceManager {
    private static final String TAG = DeviceManager.class.getSimpleName();
    private List<Device> mDeviceList;

    private static DeviceManager instance;

    public static DeviceManager getInstance() {
        if (instance == null) {
            synchronized (DeviceManager.class) {
                if (instance == null) {
                    instance = new DeviceManager();
                }
            }
        }
        return instance;
    }

    private DeviceManager() {
        mDeviceList = new ArrayList<>();
    }

    public List<Device> getmDeviceList() {
        return mDeviceList;
    }

    public void addDevice(Device device) {
        L.d(TAG, "addDevice(), " + device.toString());
        mDeviceList.add(device);
    }

    public void removeAllDevices() {
        mDeviceList.clear();
    }

    public boolean containDevice(@NonNull Device device) {
        L.d(TAG, "containDevice()");
        for (Device deviceBean : mDeviceList) {
            if (deviceBean.getIp().equals(device.getIp())) {
                L.d(TAG, "containDevice(), This device has been added.");
                return true;
            }
        }
        return false;
    }
}
