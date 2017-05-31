package com.example.yechy.tvass.util;

import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.model.prefs.IPreferencesHelper;

import javax.inject.Inject;

/**
 * Created by yechy on 2017/5/31.
 */

public class AppCookie {
    private boolean isConnect = false;
    private IPreferencesHelper mPreferencesHelper;

    @Inject
    public AppCookie(IPreferencesHelper preferencesHelper) {
        this.mPreferencesHelper = preferencesHelper;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public void saveConnectedDeviceInfo(Device device) {
        mPreferencesHelper.saveConnectedDeviceInfo(device);
    }

    public Device getConnectedDeviceInfo() {
        return mPreferencesHelper.getConnectedDeviceInfo();
    }
}
