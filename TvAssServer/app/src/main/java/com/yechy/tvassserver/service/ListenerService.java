package com.yechy.tvassserver.service;

import android.content.Intent;
import android.os.IBinder;

import com.yechy.tvassserver.base.BaseRxService;
import com.yechy.tvassserver.util.L;

/**
 * Created by yechy on 2017/4/22.
 */

public class ListenerService extends BaseRxService<ListenerServicePresenter>
        implements ListenerServiceContract.IView {

    private static final String TAG = ListenerService.class.getSimpleName();

    @Override
    protected void initInject() {
        getServiceComponent().inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.d(TAG, "onCreate()");
        mPresenter.registerUdpMulticast();
        mPresenter.registerTcpMessage();
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
