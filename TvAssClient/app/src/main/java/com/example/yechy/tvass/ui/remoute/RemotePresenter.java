package com.example.yechy.tvass.ui.remoute;

import com.example.yechy.tvass.base.BaseRxPresenter;
import com.example.yechy.tvass.communication.CommModel;
import com.example.yechy.tvass.communication.net.INetModel;
import com.example.yechy.tvass.communication.net.NetModel;
import com.example.yechy.tvass.util.AppCookie;
import com.example.yechy.tvass.util.L;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by yechy on 2017/4/4.
 */

public class RemotePresenter extends BaseRxPresenter<RemoteContract.IView>
        implements RemoteContract.IPresenter<RemoteContract.IView> {
    private static final String TAG= RemotePresenter.class.getSimpleName();

    private CommModel commModel;
    private INetModel netModel;
    private AppCookie appCookie;
    @Inject
    public RemotePresenter(CommModel commModel, NetModel netModel, AppCookie appCookie) {
        this.commModel = commModel;
        this.netModel = netModel;
        this.appCookie = appCookie;
    }

    @Override
    public void sendKeyCode(int keyCode, byte keyStatus) {
        L.d(TAG, "sendKeyCode(), keyCode = " + keyCode + ", keyStatus = " + keyStatus);
        if (!appCookie.isConnect()) {

        } else {
            addSubscribe(netModel.sendKeyCode(keyCode, keyStatus)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResourceSubscriber<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {

                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        }
    }
}
