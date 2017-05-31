package com.example.yechy.tvass.communication.net;

import com.example.yechy.tvass.util.AppConfig;
import com.example.yechy.tvass.util.L;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by yechy on 2017/4/15.
 */

public class UdpClient {
    private static final String TAG = UdpClient.class.getSimpleName();

    private static final int RECEIVE_TIME_OUT = 1500;   //接收超时时间

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
        L.d(TAG, "createSocket(), host = " + host);
        if (mSocket == null) {
            mSocket = new MulticastSocket();
        }
        mSocket.setTimeToLive(1);

        mInetAddress = InetAddress.getByName(host);
        if (!mInetAddress.isMulticastAddress()) {
            throw new Exception("不是组播地址");
        }

        mSocket.joinGroup(mInetAddress);
    }

    public boolean sendUdpMessage(byte[] sendBytes) throws Exception {
        L.d(TAG, "sendUdpMessage()");
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
                createSocket(AppConfig.MULTICAST_IP);

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

    public void closeSocket() {
        L.d(TAG, "closeSocket()");
        isReceiveMessage = false;
        if (mSocket != null) {
            mSocket.close();
        }
        mSocket = null;
    }
}
