package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.UdpMessage;
import com.example.yechy.tvass.flatbuffers.UdpMsgType;
import com.example.yechy.tvass.model.DeviceManager;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.AppConfig;
import com.example.yechy.tvass.util.L;
import com.example.yechy.tvass.util.RxUtil;
import com.example.yechy.tvass.util.StringUtil;
import com.example.yechy.tvass.util.SystemUtil;

import java.net.DatagramPacket;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by yechy on 2017/5/27.
 */

public class NetModel implements INetModel {
    private static final String TAG = NetModel.class.getSimpleName();
    private UdpClient udpClient;

    public NetModel(UdpClient udpClient) {
        this.udpClient = udpClient;
    }

    @Override
    public Flowable<Device> startSearchDevice() {

        Flowable receiveFlowable = udpClient.registerUdpMessage()
                .map(new Function<DatagramPacket, Device>() {
                    @Override
                    public Device apply(DatagramPacket packet) throws Exception {
                        return verifyResponseData(packet);
                    }
                })
                .filter(new Predicate<Device>() {
                    @Override
                    public boolean test(Device device) throws Exception {
                        return device != null;
                    }
                })
                .filter(new Predicate<Device>() {
                    @Override
                    public boolean test(@NonNull Device device) throws Exception {
                        return !DeviceManager.getInstance().containDevice(device);
                    }
                });

        final byte[] searchBytes = FlatUtil.createUdpMessageBytes(
                UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_REQUEST, AppConfig.MULTICAST_IP, 0,
                SystemUtil.getDeviceName());

        Flowable.intervalRange(0, 2, 0, 1500, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Long aLong) throws Exception {

                        return udpClient.sendUdpMessage(searchBytes);
                    }
                })
                .compose(RxUtil.<Boolean>rxSchedulerHelper())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                    }
                });


    }

    private Device verifyResponseData(DatagramPacket packet) {
        L.d(TAG, "verifyResponseData(), " + StringUtil.getDatagramPacketInfo(packet));
        if (packet == null || packet.getLength() <= 0) {
            L.e(TAG, "verifyResponseData(), packet data length <= 0");
            return null;
        }
        String deviceIP = packet.getAddress().getHostAddress();
        int devicePort = packet.getPort();

        byte[] receiveBytes = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), receiveBytes, 0, receiveBytes.length);
        UdpMessage message = FlatUtil.getUdpMessage(receiveBytes);

        L.d(TAG, "verifyResponseData(), " + FlatUtil.getUdpMessageString(message));
        byte msgType = message.msgType();
        if (msgType == UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_RESPONSE) {
            L.d(TAG, "verifyResponseData() success");
            Device device = new Device();
            device.setIp(deviceIP);
            device.setName(message.name());
            device.setPort(devicePort);
            return device;

        }
        L.w(TAG, "verifyResponseData(), msgType = " + msgType);
        return null;
    }
}
