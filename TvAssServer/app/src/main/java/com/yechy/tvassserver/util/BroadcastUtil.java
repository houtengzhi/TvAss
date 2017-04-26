package com.yechy.tvassserver.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by yechy on 2016/12/16.
 */
public class BroadcastUtil {
    private final static String TAG = "BroadcastUtil";

    /**
     * 注册广播
     */
    public static void registerBroadcastReceiver(Context context, BroadcastReceiver broadcastReceiver, String... actions) {
        IntentFilter mFilter = new IntentFilter();
        if (actions.length > 0) {
            for (String action : actions) {
                mFilter.addAction(action);
            }
        }
        context.registerReceiver(broadcastReceiver, mFilter);
    }

    /**
     *注销广播
     */
    public static void unRegisterBroadcastReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver == null || context == null) {
            return;
        }
        context.unregisterReceiver(broadcastReceiver);
    }
    
}
