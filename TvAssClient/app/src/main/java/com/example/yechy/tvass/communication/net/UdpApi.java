package com.example.yechy.tvass.communication.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;

/**
 * Created by yechy on 2017/4/15.
 */

public class UdpApi {
    private static final String TAG = UdpApi.class.getSimpleName();
    private DatagramSocket datagramSocket;
    private boolean isListenerUdp = true;
    private String groupIP = "224.1.1.1";
    private int groupPort = 8000;
    private static final int DEVICE_FIND_PORT = 9000;
    private static final int RECEIVE_TIME_OUT = 1500;   //接收超时时间
    private static final int RESPONSE_DEVICE_MAX = 200; //响应设备的最大个数，防止UDP广播攻击

    private String mDeviceIP;


    public UdpApi() {
    }

    /**
     * 发送组播
     * @param sendBuffer
     * @return
     */
    public Observable sendUdpMulticast(final ByteBuffer sendBuffer) {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);
                SocketAddress socketAddress = new InetSocketAddress(groupIP, groupPort);
                datagramChannel.connect(socketAddress);

                Selector selector = Selector.open();
                datagramChannel.register(selector, SelectionKey.OP_READ);
                datagramChannel.write(sendBuffer);

                while (selector.select() > 0) {
                    Iterator iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = (SelectionKey) iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {

                        }

                        if (selectionKey.isWritable()) {

                        }

                    }
                }
//                MulticastSocket socket;
//                socket = new MulticastSocket();
//                socket.setTimeToLive(1);
//
//                //设置超时时间
//                socket.setSoTimeout(RECEIVE_TIME_OUT);
//
//                String deviceIP = null;
//                InetAddress address = InetAddress.getByName(groupIP);
//                if (!address.isMulticastAddress()) {
//                    emitter.onError(new Throwable("不是组播地址"));
//                    emitter.onComplete();
//                }
//
//                socket.joinGroup(address);
//
//                byte[] sendDataBytes = new byte[1024];
//                DatagramPacket sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length,
//                        address, DEVICE_FIND_PORT);
//
//                for (int i = 0; i < 3; i++) {
//                    //发送搜索广播
//                    byte packType = Constant.PACKET_TYPE_FIND_DEVICE_REQ_10;
//                    sendPacket.setData(UdpUtil.packData(i+1, packType, deviceIP));
//                    socket.send(sendPacket);
//
//                    byte[] receiveData = new byte[1024];
//                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                    try {
//                        int rspCount = RESPONSE_DEVICE_MAX;
//                        while (rspCount-- > 0) {
//                            receivePacket.setData(receiveData);
//                            socket.receive(receivePacket);
//                            if (receivePacket.getLength() > 0) {
//                                deviceIP = receivePacket.getAddress().getHostAddress();
//                                if (UdpUtil.parsePacket(receivePacket) != null) {
//                                    LogUtil.d(TAG, "Device is online: " + deviceIP);
//
//                                    //发送一对一的确认消息。使用接收报，因为接收报中有对方的实际IP
//                                    packType = Constant.PACKET_TYPE_FIND_DEVICE_CHK_12;
//                                    receivePacket.setData(UdpUtil.packData(rspCount, packType, deviceIP));
//                                    socket.send(receivePacket);
//                                }
//                            }
//                        }
//                    } catch (SocketTimeoutException e) {
//
//                    }
//                }
//
//                if (socket != null) {
//                    socket.close();
//                }
            }
        });
        return observable;
    }

    public Observable<ByteBuffer> registerUdpMulticast() {
        Observable<ByteBuffer> observable = Observable.create(new ObservableOnSubscribe<ByteBuffer>() {
            @Override
            public void subscribe(ObservableEmitter<ByteBuffer> emitter) throws Exception {
                NetworkInterface networkInterface = NetworkInterface.getByName("eth1");
                InetSocketAddress socketAddress = new InetSocketAddress(groupPort);
                DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET)
                        .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                        .bind(socketAddress)
                        .setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);

                InetAddress inetAddress = InetAddress.getByName(groupIP);
            }
        });
        return observable;
     }



    public Observable<DatagramPacket> receiveUdpMulticast(final int bufferSizeInBytes) {
        Observable<DatagramPacket> observable = Observable.create(new ObservableOnSubscribe<DatagramPacket>() {
            @Override
            public void subscribe(ObservableEmitter<DatagramPacket> emitter) throws Exception {
                MulticastSocket socket = null;
                try {
                    socket = new MulticastSocket(groupPort);
                    emitter.setCancellable(getCancellable(socket));

                    InetAddress address = InetAddress.getByName(groupIP);
                    if (!address.isMulticastAddress()) {
                        emitter.onError(new Throwable("不是组播地址"));
                        return;
                    }

                    socket.joinGroup(address);

                    byte[] receiveBuffer = new byte[bufferSizeInBytes];
                    DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                    isListenerUdp = true;
                    while (isListenerUdp) {
                        socket.receive(packet);
                        emitter.onNext(packet);
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        });
        return observable;
    }

    /**
     * 发送广播
     * @param data
     * @param port
     * @return
     */
    public Completable sendUdpBroadcast(final String data, final int port) {
        Completable completable = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (datagramSocket == null) {
                    datagramSocket = new DatagramSocket(port);
                    datagramSocket.setBroadcast(true);
                }

                byte[] dataBytes = data.getBytes();
                InetAddress inetAddress = Inet4Address.getByName("127.0.0.1");
                DatagramPacket datagramPacket = new DatagramPacket(dataBytes, dataBytes.length,
                        inetAddress, port);
                datagramSocket.send(datagramPacket);
            }
        });


        return completable;
    }

    public Observable<DatagramPacket> receiveUdpBroadcast(final int port, final int bufferSizeInBytes) {
        Observable<DatagramPacket> observable = Observable.create(new ObservableOnSubscribe<DatagramPacket>() {
            @Override
            public void subscribe(ObservableEmitter<DatagramPacket> emitter) throws Exception {
                DatagramSocket datagramSocket = null;
                try {
                    datagramSocket = new DatagramSocket(port);
                    emitter.setCancellable(getCancellable(datagramSocket));

                    byte[] receiveBuffer = new byte[bufferSizeInBytes];
                    DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                    isListenerUdp = true;
                    while (isListenerUdp) {
                        datagramSocket.receive(packet);
                        emitter.onNext(packet);
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                } finally {
                    if (datagramSocket != null) {
                        datagramSocket.close();
                    }
                }
            }
        });
        return observable;
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
}
