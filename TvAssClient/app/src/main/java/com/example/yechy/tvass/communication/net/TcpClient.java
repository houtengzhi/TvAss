package com.example.yechy.tvass.communication.net;


import com.example.yechy.tvass.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by yechy on 2017/4/22.
 */

public class TcpClient {
    private final static String TAG = TcpClient.class.getSimpleName();

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int INPUTSTREAM_READ_TIMEOUT = 300;
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public TcpClient() {
    }

    /**
     *
     * @param ip
     * @param port
     * @return
     */
    public Observable connectDevice(final String ip, final int port) {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                startConnect(ip, port);
            }
        });
        return observable;
    }

    private void startConnect(String ip, int port) {
        if (mSocket != null) {
            close();
        }

        mSocket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        try {
            mSocket.connect(socketAddress, CONNECT_TIMEOUT);

            if (mSocket.isConnected()) {
                LogUtil.d(TAG, "Connect server success.");

                //设置读流超时时间
                mSocket.setSoTimeout(INPUTSTREAM_READ_TIMEOUT);
                mInputStream = mSocket.getInputStream();
                mOutputStream = mSocket.getOutputStream();
            } else {
                mSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param data
     * @return
     */
    public Observable<byte[]> sendAndReceiveData(final byte[] data) {
        Observable<byte[]> observable = Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> emitter) throws Exception {
                sendTcpData(data);
                emitter.onNext(readTcpData());
            }
        });
        return observable;
    }

    private void sendTcpData(byte[] data) {
        if (mSocket != null && mSocket.isConnected() && mOutputStream != null) {
            try {
                mOutputStream.write(data);
                mOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] readTcpData() {
        if (mSocket != null && mSocket.isConnected() && mInputStream != null) {
            byte[] data = new byte[0];
            byte[] buf = new byte[1024];
            int len;

            try {
                while ((len = mInputStream.read(buf)) != -1) {
                    byte[] temp = new byte[data.length + len];
                    System.arraycopy(data, 0, temp, 0, data.length);
                    System.arraycopy(buf, 0, temp, data.length, len);
                    data = temp;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        return null;
    }

    private void close() {
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
