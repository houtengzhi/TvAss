package com.example.yechy.tvass.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yechy.tvass.App;
import com.example.yechy.tvass.communication.Constant;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.GsonHelper;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by yechy on 2017/5/26.
 */

public class PreferencesHelperImpl implements IPreferencesHelper {
    private static final String SHAREDPREFERENCES_NAME = "tvass_config";
    private static final String COMMUNICATION_MODE = "COMMUNICATION_MODE";
    private static final String CONNECTED_DEVICE = "CONNECTED_DEVICE";


    private final SharedPreferences mSPrefs;
    private Gson mGson;

    @Inject
    public PreferencesHelperImpl() {
        mSPrefs = App.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        mGson = GsonHelper.builderGson();
    }

    @Override
    public int getCommunicationMode() {
        return mSPrefs.getInt(COMMUNICATION_MODE, Constant.COMMUNICATION_MODE_WIFI);
    }

    @Override
    public void setCommunicationMode(int mode) {
        mSPrefs.edit().putInt(COMMUNICATION_MODE, mode).apply();
    }

    @Override
    public boolean isConnect() {
        return false;
    }

    @Override
    public void saveConnectedDeviceInfo(Device device) {
        String e = mGson.toJson(device);
        mSPrefs.edit().putString(CONNECTED_DEVICE, e).apply();
    }

    @Override
    public Device getConnectedDeviceInfo() {
        String e = mSPrefs.getString(CONNECTED_DEVICE, null);
        if (e != null) {
            Device device = mGson.fromJson(e, Device.class);
            return device;
        }
        return null;

    }
}
