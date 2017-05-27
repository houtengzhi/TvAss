package com.example.yechy.tvass.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yechy.tvass.App;
import com.example.yechy.tvass.communication.Constant;

import javax.inject.Inject;

/**
 * Created by yechy on 2017/5/26.
 */

public class PreferencesHelperImpl implements IPreferencesHelper {
    private static final String SHAREDPREFERENCES_NAME = "tvass_config";
    private static final String COMMUNICATION_MODE = "COMMUNICATION_MODE";


    private final SharedPreferences mSPrefs;

    @Inject
    public PreferencesHelperImpl() {
        mSPrefs = App.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int getCommunicationMode() {
        return mSPrefs.getInt(COMMUNICATION_MODE, Constant.COMMUNICATION_MODE_WIFI);
    }

    @Override
    public void setCommunicationMode(int mode) {
        mSPrefs.edit().putInt(COMMUNICATION_MODE, mode).apply();
    }
}
