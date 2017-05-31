package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.TcpMsgType;
import com.example.yechy.tvass.flatbuffers.TcpResponse;
import com.example.yechy.tvass.flatbuffers.UdpMessage;
import com.example.yechy.tvass.flatbuffers.UdpMsgType;
import com.example.yechy.tvass.model.DeviceManager;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.AppConfig;
import com.example.yechy.tvass.util.L;
import com.example.yechy.tvass.util.StringUtil;
import com.example.yechy.tvass.util.SystemUtil;

import java.net.DatagramPacket;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yechy on 2017/5/27.
 */

public class NetModel implements INetModel {
    private static final String TAG = NetModel.class.getSimpleName();
    private UdpClient udpClient;
    private TcpClient tcpClient;
    private static final long SEND_TIME_INTERVAL = 1500;   //发送时间间隔
    private static final int SEND_COUNT = 2;

    @Inject
    public NetModel(UdpClient udpClient, TcpClient tcpClient) {
        this.udpClient = udpClient;
        this.tcpClient = tcpClient;
    }

    @Override
    public Flowable<Device> registerSearchMessage() {
        return udpClient.registerUdpMessage()
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
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<Boolean> closeUdpSocket() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                udpClient.closeSocket();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<Boolean> startSearchDevice() {
        final byte[] searchBytes = FlatUtil.createUdpMessageBytes(
                UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_REQUEST, AppConfig.MULTICAST_IP, 0,
                SystemUtil.getDeviceName());

        return Flowable.intervalRange(0, SEND_COUNT, 0, SEND_TIME_INTERVAL, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Long aLong) throws Exception {
                        return udpClient.sendUdpMessage(searchBytes);
                    }
                })
                .subscribeOn(Schedulers.io());
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

    @Override
    public Flowable<Boolean> connectDevice(final String ip, final int port) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(tcpClient.startConnect(ip, port));
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<Boolean> disconnectDevice() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                tcpClient.closeSocket();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<Boolean> sendKeyCode(int keyCode, byte keyStatus) {
        final byte[] sendBytes = FlatUtil.createTcpMessageBytes(TcpMsgType.MESSAGE_TYPE_ONKEY_REQUEST,
                (short) keyCode, keyStatus);

        return Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                tcpClient.sendTcpData(sendBytes);
                emitter.onNext(tcpClient.readTcpData());
            }
        }, BackpressureStrategy.ERROR)
                .map(new Function<byte[], Boolean>() {
                    @Override
                    public Boolean apply(@NonNull byte[] bytes) throws Exception {
                        if (bytes != null) {
                            TcpResponse tcpResponse = FlatUtil.getTcpResponse(bytes);
                            if (tcpResponse.msgType() == TcpMsgType.MESSAGE_TYPE_ONKEY_RESPONSE) {
                                return tcpResponse.responseCode() == 0;
                            }
                        }
                        return false;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
