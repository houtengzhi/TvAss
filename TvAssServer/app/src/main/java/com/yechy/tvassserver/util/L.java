package com.yechy.tvassserver.util;

import android.util.Log;

import com.yechy.tvassserver.BuildConfig;


public class L {
    private static boolean debug = BuildConfig.DEBUG;
    private static String version = "TV_ASS_v1.0_";

    public static void e(String tag, String msg) {
        if (debug) {
            Log.e(version + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (debug) {
            Log.d(version + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (debug) {
            Log.w(version + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (debug) {
            Log.i(version + tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (debug) {
            Log.v(version + tag, msg);
        }
    }

    private static int LOG_MAXLENGTH = 2000;

    public static void LongString(String msg) {
        if (debug) {

            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e("shitou___" + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e("shitou___" + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void LongString(String tag, String msg) {

        if (debug) {

            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e(tag + "___" + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e(tag + "___" + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }
}
