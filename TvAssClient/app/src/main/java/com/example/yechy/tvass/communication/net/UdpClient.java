package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.UdpMessage;
import com.example.yechy.tvass.flatbuffers.UdpMsgType;
import com.example.yechy.tvass.model.DeviceManager;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.util.AppConfig;
import com.example.yechy.tvass.util.L;
import com.example.yechy.tvass.util.StringUtil;
import com.example.yechy.tvass.util.SystemUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by yechy on 2017/4/15.
 */

public class UdpClient {
    private static final String TAG = UdpClient.class.getSimpleName();

    private static final int RECEIVE_TIME_OUT = 1500;   //接收超时时间
    private static final int RESPONSE_DEVICE_MAX = 5; //响应设备的最大个数，防止UDP广播攻击
    private static final long SEND_TIME_INTERCAL = 1500;   //发送时间间隔
    private static final int SEND_COUNT = 2;

    private MulticastSocket mSocket;
    private InetAddress mInetAddress;
    private DatagramPacket sendPacket, receivePacket;
    private boolean isReceiveMessage = false;

    public UdpClient() {
    }

    /**
     * @throws Exception
     */
    private void createSocket(String host) throws Exception {
        L.d(TAG, "createSocket()");
        mSocket = new MulticastSocket();
        mSocket.setTimeToLive(1);

        mInetAddress = InetAddress.getByName(host);
        if (!mInetAddress.isMulticastAddress()) {
            throw new Exception("不是组播地址");
        }

        mSocket.joinGroup(mInetAddress);
    }

    public boolean sendUdpMessage(byte[] sendBytes) throws IOException {
        if (mSocket != null) {
            closeSocket();
        }
        createSocket(AppConfig.MULTICAST_IP);

        if (sendPacket == null) {
            sendPacket = new DatagramPacket(sendBytes, sendBytes.length,
                    mInetAddress, AppConfig.MULTICAST_PORT);
        }

        if (mSocket != null && !mSocket.isClosed()) {
            L.d(TAG, "Ready to send data...");
            mSocket.send(sendPacket);
            return true;
        }
        return false;
    }

    public Flowable<DatagramPacket> registerUdpMessage() {
        L.d(TAG, "registerUdpMessage()");
        return Flowable.create(new FlowableOnSubscribe<DatagramPacket>() {
            @Override
            public void subscribe(FlowableEmitter<DatagramPacket> emitter) throws Exception {

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                isReceiveMessage = true;
                while (isReceiveMessage) {
                    try {
                        if (mSocket != null && !mSocket.isClosed()) {
                            L.d(TAG, "Ready to receive data...");
                            receivePacket.setData(receiveData);
                            mSocket.receive(receivePacket);
                            emitter.onNext(receivePacket);
                        }

                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                        isReceiveMessage = false;
                        break;
                    }
                }

                closeSocket();
            }
        }, BackpressureStrategy.ERROR);

    }

    /**
     * @return
     */
    public Flowable<Device> sendSearchMulticast() {
        L.d(TAG, "sendSearchMulticast()");
        final byte[] searchBytes = FlatUtil.createUdpMessageBytes(
                UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_REQUEST, AppConfig.MULTICAST_IP, 0,
                SystemUtil.getDeviceName());

        return Flowable.create(new FlowableOnSubscribe<DatagramPacket>() {
            @Override
            public void subscribe(FlowableEmitter<DatagramPacket> emitter) throws Exception {
                L.d(TAG, "debug");
                if (mSocket != null) {
                    closeSocket();
                }
                createSocket(AppConfig.MULTICAST_IP);
                sendUdpMessage(searchBytes);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                isReceiveMessage = true;
                while (isReceiveMessage) {
                    try {
                        if (mSocket != null && !mSocket.isClosed()) {
                            L.d(TAG, "Ready to receive data...");
                            receivePacket.setData(receiveData);
                            mSocket.receive(receivePacket);

                            emitter.onNext(receivePacket);
                        }

                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                        isReceiveMessage = false;
                        break;
                    }
                }

                closeSocket();
            }
        }, BackpressureStrategy.ERROR)
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

    /**
     * @return
     */
    public Flowable<Boolean> closeUdpSocket() {
        L.d(TAG, "closeUdpSocket()");
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                closeSocket();
                emitter.onNext(true);
            }
        }, BackpressureStrategy.DROP);
    }

    private void closeSocket() {
        L.d(TAG, "closeSocket()");
        isReceiveMessage = false;
        if (mSocket != null) {
            mSocket.close();
        }
        mSocket = null;
    }
}
