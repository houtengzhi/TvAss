package com.example.yechy.tvass.util;

import com.example.yechy.tvass.BuildConfig;
import com.orhanobut.logger.Logger;

import java.net.PortUnreachableException;


/**
 * Created by yechy on 2017/4/15.
 */

public class LogUtil {

    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "TvAss_";

    public static void e(String tag,Object o) {
        if(isDebug) {
            Logger.e(TAG + tag, o);
        }
    }

    public static void e(Object o) {
        LogUtil.e(TAG,o);
    }

    public static void w(String tag,Object o) {
        if(isDebug) {
            Logger.w(TAG + tag, o);
        }
    }

    public static void w(Object o) {
        LogUtil.w(TAG,o);
    }

    public static void d(String msg) {
        if(isDebug) {
            Logger.d(msg);
        }
    }

    public static void d(String tag, Object o) {
        if (isDebug) {
            Logger.d(TAG + tag, o);
        }
    }

    public static void i(String msg) {
        if(isDebug) {
            Logger.i(msg);
        }
    }

    public static void i(String tag, Object o) {
        if (isDebug) {
            Logger.i(TAG + tag, o);
        }
    }
}

