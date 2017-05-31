package com.example.yechy.tvass.model.prefs;

import com.example.yechy.tvass.model.bean.Device;

/**
 * Created by yechy on 2017/5/26.
 */

public interface IPreferencesHelper {
    int getCommunicationMode();
    void setCommunicationMode(int mode);

    boolean isConnect();
    void saveConnectedDeviceInfo(Device device);
    Device getConnectedDeviceInfo();
}
