package com.example.yechy.tvass.ui.device;

import com.example.yechy.tvass.base.BaseRxPresenter;
import com.example.yechy.tvass.communication.net.UdpClient;

import java.net.DatagramPacket;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yechy on 2017/4/8.
 */

public class DevicePresenter extends BaseRxPresenter<DeviceContract.IView>
        implements DeviceContract.IPresenetr<DeviceContract.IView> {
    private UdpClient udpModel;

    @Inject
    public DevicePresenter() {
        udpModel = new UdpClient();
    }

    @Override
    public void setDevicesListener() {
        Disposable disposable = udpModel.receiveUdpMulticast(1024)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DatagramPacket>() {
                    @Override
                    public void accept(@NonNull DatagramPacket datagramPacket) throws Exception {
                        datagramPacket.getData();
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void searchDevices() {
        Disposable disposable = udpModel.sendUdpMulticast("")
                .subscribeOn(Schedulers.io())
                .subscribe();
        addSubscribe(disposable);
    }
}
