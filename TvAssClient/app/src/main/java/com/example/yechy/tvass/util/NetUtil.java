package com.example.yechy.tvass.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by yechy on 2017/4/27.
 */

public class NetUtil {

    public static String getWifiIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        String ip = int2IP(ipInt);
        return ip;
    }

    public static String int2IP(int i) {
        return String.format("%d.%d.%d.%d", i & 0xFF, (i >> 8) & 0xFF, (i >> 16) & 0xFF,
                (i >> 24) & 0xFF);
    }
}
