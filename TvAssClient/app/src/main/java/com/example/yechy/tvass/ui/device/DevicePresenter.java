package com.example.yechy.tvass.ui.device;

import com.example.yechy.tvass.base.BaseRxPresenter;
import com.example.yechy.tvass.communication.CommModel;
import com.example.yechy.tvass.communication.net.INetModel;
import com.example.yechy.tvass.communication.net.NetModel;
import com.example.yechy.tvass.model.DeviceManager;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.AppCookie;
import com.example.yechy.tvass.util.L;
import com.example.yechy.tvass.util.LogUtil;

import org.reactivestreams.Subscription;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by yechy on 2017/4/8.
 */

public class DevicePresenter extends BaseRxPresenter<DeviceContract.IView>
        implements DeviceContract.IPresenetr<DeviceContract.IView> {
    private static final String TAG= DevicePresenter.class.getSimpleName();

    private CommModel commModel;
    private INetModel netModel;
    private AppCookie appCookie;
    private Disposable searchDisposable;

    @Inject
    public DevicePresenter(CommModel commModel, NetModel netModel, AppCookie appCookie) {
        this.commModel = commModel;
        this.netModel = netModel;
        this.appCookie = appCookie;
    }

    @Override
    public void registerSearchMessage() {
        Disposable disposable = netModel.registerSearchMessage()
                .observeOn(AndroidSchedulers.mainThread())
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
    public void clearDeviceList() {
        L.d(TAG, "clearDeviceList()");
        DeviceManager.getInstance().removeAllDevices();
        mView.refreshDeviceRecyclerView((ArrayList<Device>) DeviceManager
                .getInstance().getmDeviceList());
    }

    @Override
    public void startSearchDevices() {
        L.d(TAG, "startSearchDevices()");
        if (searchDisposable != null && !searchDisposable.isDisposed()) {
            searchDisposable.dispose();
        }
        searchDisposable = netModel.startSearchDevice()
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(@NonNull Subscription subscription) throws Exception {
                        mView.setSearchButtonClickable(false);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
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
                        mView.setSearchButtonClickable(true);
                    }
                });
        addSubscribe(searchDisposable);
    }

    @Override
    public void stopSearchDevices() {
        L.d(TAG, "stopSearchDevices()");
        Disposable disposable = netModel.closeUdpSocket()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void connectDevice(final Device device, int port) {
        L.d(TAG, "connectDevice(), device = " + device.toString() + ", port = " + port);
        Disposable disposable =netModel.connectDevice(device.getIp(), port)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                       appCookie.setConnect(aBoolean.booleanValue());
                        if (aBoolean.booleanValue()) {
                            appCookie.saveConnectedDeviceInfo(device);
                        }
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
    public void disconnectDevice() {
        Disposable disposable = netModel.disconnectDevice()
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
                });
        addSubscribe(disposable);
    }

    @Override
    public void sendKeyCode(int keyCode, byte keyStatus) {
        L.d(TAG, "sendKeyCode(), keyCode = " + keyCode + ", keyStatus = " + keyStatus);
        Disposable disposable = netModel.sendKeyCode(keyCode, keyStatus)
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
                });
                addSubscribe(disposable);
    }
}
