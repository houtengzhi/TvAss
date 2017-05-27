package com.example.yechy.tvass.communication.net;


import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.TcpMsgType;
import com.example.yechy.tvass.flatbuffers.TcpResponse;
import com.example.yechy.tvass.util.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by yechy on 2017/4/22.
 */

public class TcpApi {
    private final static String TAG = TcpApi.class.getSimpleName();

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int INPUTSTREAM_READ_TIMEOUT = 300;
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public TcpApi() {
    }

    /**
     * 连接设备
     *
     * @param ip
     * @param port
     * @return
     */
    public Flowable<Boolean> connectDevice(final String ip, final int port) {
        L.d(TAG, "connectDevice(), ip = " + ip + ", port = " + port);
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(startConnect(ip, port));
            }
        }, BackpressureStrategy.BUFFER);
    }

    private boolean startConnect(String ip, int port) {
        L.d(TAG, "startConnect()");
        if (mSocket != null) {
            closeSocket();
        }

        mSocket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        try {
            mSocket.connect(socketAddress, CONNECT_TIMEOUT);

            if (mSocket.isConnected()) {
                L.i(TAG, "Connect device " + ip + ":" + port + " success.");

                //设置读流超时时间
//                mSocket.setSoTimeout(INPUTSTREAM_READ_TIMEOUT);
                mInputStream = mSocket.getInputStream();
                mOutputStream = mSocket.getOutputStream();

                return handleConnect();
            } else {
                mSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
        }
        return false;
    }

    private boolean handleConnect() {
        byte[] requestBytes = FlatUtil.createTcpMessageBytes(TcpMsgType.MESSAGE_TYPE_CONNECT_REQUEST,
                (short) 0, (byte) 0);
        try {
            sendTcpData(requestBytes);
            byte[] responseBytes = readTcpData();
            TcpResponse response = FlatUtil.getTcpResponse(responseBytes);
            if (response != null && response.responseCode() == 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 断开连接
     *
     * @return
     */
    public Flowable<Boolean> disconnectDevice() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                closeSocket();
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * @param sendBytes
     * @return
     */
    public Flowable<byte[]> sendAndReceiveData(final byte[] sendBytes) {
        return Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                sendTcpData(sendBytes);
                emitter.onNext(readTcpData());
            }
        }, BackpressureStrategy.BUFFER);
    }

    private void sendTcpData(byte[] data) throws IOException {
        L.d(TAG, "sendTcpData()");
        if (mSocket != null && mSocket.isConnected() && mOutputStream != null) {
            L.d(TAG, "sendTcpData(), Ready to send data...");
            mOutputStream.write(data);
            mOutputStream.flush();
        }
    }

    private byte[] readTcpData() throws IOException {
        L.d(TAG, "readTcpData()");
        if (mSocket != null && mSocket.isConnected() && mInputStream != null) {
            byte[] data = new byte[0];
            byte[] buf = new byte[1024];
            int len;

            L.d(TAG, "readTcpData(), Waiting for read data...");
            while ((len = mInputStream.read(buf)) != -1) {
                byte[] temp = new byte[data.length + len];
                System.arraycopy(data, 0, temp, 0, data.length);
                System.arraycopy(buf, 0, temp, data.length, len);
                data = temp;
            }
            return data;
        }
        return null;
    }

    private void closeSocket() {
        synchronized (mSocket) {
            if (mInputStream != null) {
                try {
                    mInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mOutputStream != null) {
                try {
                    mOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mSocket != null) {
                try {
                    mSocket.close();
                    mSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
