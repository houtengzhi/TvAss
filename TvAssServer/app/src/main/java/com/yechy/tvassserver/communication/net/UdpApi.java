package com.yechy.tvassserver.communication.net;


import com.yechy.tvassserver.flatbuffers.FlatUtil;
import com.yechy.tvassserver.flatbuffers.UdpMessage;
import com.yechy.tvassserver.flatbuffers.UdpMsgType;
import com.yechy.tvassserver.util.AppConfig;
import com.yechy.tvassserver.util.L;
import com.yechy.tvassserver.util.LogUtil;
import com.yechy.tvassserver.util.StringUtil;
import com.yechy.tvassserver.util.SystemUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by yechy on 2017/4/15.
 */

public class UdpApi {
    private static final String TAG = UdpApi.class.getSimpleName();
    private MulticastSocket socket = null;
    private boolean isListenerUdp = true;

    private static final int RECEIVE_TIME_OUT = 1500;   //接收超时时间

    public UdpApi() {
    }

    /**
     * 注册接收UDP广播
     * @return
     */
    public Flowable<DatagramPacket> registerUdpMulticast() {
        return Flowable.create(new FlowableOnSubscribe<DatagramPacket>() {
            @Override
            public void subscribe(FlowableEmitter<DatagramPacket> emitter) throws Exception {
                try {
                    socket = new MulticastSocket(AppConfig.MULTICAST_PORT);
                    emitter.setCancellable(getCancellable(socket));

                    InetAddress address = InetAddress.getByName(AppConfig.MULTICAST_IP);
                    if (!address.isMulticastAddress()) {
                        emitter.onError(new Throwable("不是组播地址"));
                        emitter.onComplete();
                    }

                    socket.joinGroup(address);

                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                    isListenerUdp = true;
                    while (isListenerUdp) {
                        //等待主机搜索
                        L.d(TAG, "Waiting for host to search...");
                        socket.receive(receivePacket);
                        if (verifySearchData(receivePacket)) {
                            L.d(TAG, "Check success!");
                            emitter.onNext(receivePacket);

                            //发送回复信息
                            sendResponseData(receivePacket);
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     *校验搜索数据
     */
    private boolean verifySearchData(DatagramPacket packet) {
        L.d(TAG, "verifySearchData(), " + StringUtil.getDatagramPacketInfo(packet));
        if (packet == null || packet.getLength() <= 0) {
            return false;
        }
        byte[] receiveBytes = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), receiveBytes, 0, receiveBytes.length);
        L.d(TAG, StringUtil.toHexString(receiveBytes));
        UdpMessage udpMessage = FlatUtil.getUdpMessage(receiveBytes);
        L.d(TAG, "verifySearchData(), " + FlatUtil.getUdpMessageString(udpMessage));
        if (udpMessage.msgType() == UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_REQUEST) {
            return true;
        }
        return false;
    }

    /**
     * 校验确认数据
     * @param packet
     * @return
     */
    private boolean verifyCheckData(DatagramPacket packet) {
        LogUtil.d(TAG, "verifyCheckData(), " + StringUtil.getDatagramPacketInfo(packet));
        if (packet == null || packet.getLength() <= 0) {
            return false;
        }
        byte[] receiveBytes = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), receiveBytes, 0, receiveBytes.length);
        UdpMessage udpMessage = FlatUtil.getUdpMessage(receiveBytes);
        L.d(TAG, "verifyCheckData(), " + FlatUtil.getUdpMessageString(udpMessage));
        if (udpMessage.msgType() == UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_CHECK) {
            return true;
        }
        return false;
    }

    /**
     * 发送响应
     * @param packet
     * @throws IOException
     */
    private void sendResponseData(DatagramPacket packet) throws IOException {
        L.d(TAG, "sendResponseData(), " + StringUtil.getDatagramPacketInfo(packet));
        byte[] sendBytes = new byte[1024];
        DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length,
                packet.getAddress(), packet.getPort());

        byte[] sendDataBytes = FlatUtil.createUdpMessageBytes(UdpMsgType.MESSAGE_TYPE_FIND_DEVICE_RESPONSE,
                SystemUtil.getWifiIP(), 0, SystemUtil.getDeviceName());

        sendPacket.setData(sendDataBytes);
        socket.send(sendPacket);
    }

    private Cancellable getCancellable(final DatagramSocket socket) {
        return new Cancellable() {
            @Override
            public void cancel() throws Exception {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        };
    }

    public void closeSocket() {
        L.d(TAG, "closeSocket()");
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
    }

}
