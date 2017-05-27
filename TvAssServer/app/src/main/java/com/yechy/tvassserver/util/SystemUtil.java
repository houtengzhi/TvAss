package com.yechy.tvassserver.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.yechy.tvassserver.App;

/**
 * Created by yechy on 2017/5/23.
 */

public class SystemUtil {
    private static final String TAG = SystemUtil.class.getSimpleName();

    public static String getDeviceName() {
        L.d(TAG, "getDeviceName(), name = " + Build.PRODUCT);
        return Build.PRODUCT;
    }

    public static String getWifiIP() {
        WifiManager wifiManager = (WifiManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            L.w(TAG, "getWifiIP(), wifi is disabled!");
            return null;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        String ip = int2IP(ipInt);
        L.d(TAG, "getWifiIP(), ip = " + ip);
        return ip;
    }

    public static String int2IP(int i) {
        return String.format("%d.%d.%d.%d", i & 0xFF, (i >> 8) & 0xFF, (i >> 16) & 0xFF,
                (i >> 24) & 0xFF);
    }
}
