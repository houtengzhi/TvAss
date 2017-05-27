package com.example.yechy.tvass.ui.device;

import com.example.yechy.tvass.base.BaseRxPresenter;
import com.example.yechy.tvass.communication.CommModel;
import com.example.yechy.tvass.model.DeviceManager;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.L;
import com.example.yechy.tvass.util.LogUtil;
import com.example.yechy.tvass.util.RxUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by yechy on 2017/4/8.
 */

public class DevicePresenter extends BaseRxPresenter<DeviceContract.IView>
        implements DeviceContract.IPresenetr<DeviceContract.IView> {
    private static final String TAG= DevicePresenter.class.getSimpleName();

    private CommModel commModel;

    @Inject
    public DevicePresenter(CommModel commModel) {
        this.commModel = commModel;
    }

    @Override
    public void startSearchDevices() {
        L.d(TAG, "startSearchDevices()");
        DeviceManager.getInstance().removeAllDevices();
        Disposable disposable = commModel.startSearchDevice()
                .compose(RxUtil.<Device>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<Device>() {
                    @Override
                    public void onNext(Device device) {
                        L.d(TAG, " " + device.toString());
                        DeviceManager.getInstance().addDevice(device);
                        mView.refreshDeviceRecyclerView((ArrayList<Device>) DeviceManager
                                .getInstance().getmDeviceList());
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        LogUtil.e(TAG, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void stopSearchDevices() {
        L.d(TAG, "stopSearchDevices()");
        Disposable disposable = commModel.stopSearchDevice()
                .compose(RxUtil.<Boolean>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        LogUtil.e(TAG, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void connectDevice(String ip, int port) {
        L.d(TAG, "connectDevice(), ip = " + ip + ", port = " + port);
        Disposable disposable = commModel.connectDevice(ip, port)
                .compose(RxUtil.<Boolean>rxSchedulerHelper())
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
                });
        addSubscribe(disposable);
    }

    @Override
    public void sendKeyCode(int keyCode, byte keyStatus) {
        L.d(TAG, "sendKeyCode(), keyCode = " + keyCode + ", keyStatus = " + keyStatus);
        Disposable disposable = commModel.sendKeyCode(keyCode, keyStatus)
                .compose(RxUtil.<Boolean>rxSchedulerHelper())
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
                });
                addSubscribe(disposable);
    }
}
