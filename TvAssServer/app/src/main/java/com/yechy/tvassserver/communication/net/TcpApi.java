package com.yechy.tvassserver.communication.net;

import com.yechy.tvassserver.util.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
    private ServerSocket mServerSocket;
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public TcpApi() {
    }

    public Flowable<byte[]> registerTcpMessage(final int port) {
        return Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                if (mServerSocket != null) {
                    closeSocket();
                }

                mServerSocket = new ServerSocket(port);
                while (mServerSocket != null && !mServerSocket.isClosed()) {
                    L.d(TAG, "Waiting for host to connect...");
                    mSocket = mServerSocket.accept();
                    if (mSocket.isConnected()) {
                        mSocket.setSoTimeout(INPUTSTREAM_READ_TIMEOUT);
                        mInputStream = mSocket.getInputStream();
                        mOutputStream = mSocket.getOutputStream();
                        while (mSocket != null && mSocket.isConnected()) {
                            //读取流
                            emitter.onNext(readData());
                        }
                    }
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * @param dataBytes
     * @return
     */
    public Flowable<Boolean> sendTcpData(final byte[] dataBytes) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(sendData(dataBytes));
            }
        }, BackpressureStrategy.BUFFER);
    }

    private boolean sendData(byte[] data) {
        if (mSocket != null && mSocket.isConnected() && mOutputStream != null) {
            try {
                mOutputStream.write(data);
                mOutputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private byte[] readData() {
        L.d(TAG, "readData()");
        byte[] data = new byte[0];
        byte[] buf = new byte[1024];
        int len;
        try {
            L.d(TAG, "Waiting to read data...");
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

    private void closeSocket() {
        L.d(TAG, "closeSocket()");
        synchronized (mServerSocket) {
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
            if (mServerSocket != null) {
                try {
                    mServerSocket.close();
                    mServerSocket = null;
                } catch (IOException e) {

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
