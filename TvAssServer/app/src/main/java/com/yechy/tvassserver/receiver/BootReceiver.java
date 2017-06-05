package com.yechy.tvassserver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yechy.tvassserver.util.L;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        L.d(TAG, "Receive " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
//            context.startService(new Intent(context, ListenerService.class));
        }
    }
}
