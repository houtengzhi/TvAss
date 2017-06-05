package com.example.yechy.tvass.util;

import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.model.prefs.IPreferencesHelper;

import javax.inject.Inject;

/**
 * Created by yechy on 2017/5/31.
 */

public class AppCookie {
    private static final String TAG = AppCookie.class.getSimpleName();
    private boolean isConnect = false;
    private IPreferencesHelper mPreferencesHelper;

    @Inject
    public AppCookie(IPreferencesHelper preferencesHelper) {
        this.mPreferencesHelper = preferencesHelper;
    }

    public boolean isConnect() {
        L.d(TAG, "isConnect(), return " + isConnect);
        return isConnect;
    }

    public void setConnect(boolean connect) {
        L.d(TAG, "setConnect(), connect = " + connect);
        isConnect = connect;
    }

    public void saveConnectedDeviceInfo(Device device) {
        L.d(TAG, "saveConnectedDeviceInfo(),");
        mPreferencesHelper.saveConnectedDeviceInfo(device);
    }

    public Device getConnectedDeviceInfo() {
        L.d(TAG, "getConnectedDeviceInfo()");
        return mPreferencesHelper.getConnectedDeviceInfo();
    }
}
