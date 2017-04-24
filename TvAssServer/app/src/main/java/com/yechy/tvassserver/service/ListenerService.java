package com.yechy.tvassserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by yechy on 2017/4/22.
 */

public class ListenerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
