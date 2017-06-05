package com.yechy.tvassserver.communication.net;

import com.yechy.tvassserver.util.DataTool;
import com.yechy.tvassserver.util.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yechy on 2017/4/22.
 */

public class TcpApi {
    private final static String TAG = TcpApi.class.getSimpleName();

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int INPUTSTREAM_READ_TIMEOUT = 300;
    private ServerSocket mServerSocket;
    private Socket mSocket;

    public TcpApi() {
    }

    public Flowable<byte[]> registerTcpMessage(final int port) {
        L.d(TAG, "registerTcpMessage()");
        return Flowable.create(new FlowableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(FlowableEmitter<byte[]> emitter) throws Exception {
                if (mServerSocket != null) {
                    closeSocket();
                }

                mServerSocket = new ServerSocket(port);
                while (mServerSocket != null && !mServerSocket.isClosed()) {
                    L.d(TAG, "Waiting for host to connect...");
                    try {
                        mSocket = mServerSocket.accept();
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    if (mSocket.isConnected()) {
//                        mSocket.setSoTimeout(INPUTSTREAM_READ_TIMEOUT);
                        InputStream inputStream = mSocket.getInputStream();
                        while (mSocket != null && mSocket.isConnected()) {
                            try {
                                //读取流
                                byte[] receBytes = readTcpData(inputStream);
                                emitter.onNext(receBytes);
                            }catch (SocketTimeoutException e) {
                                e.printStackTrace();
                                continue;
                            } catch (IOException e) {
                                e.printStackTrace();
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io());
    }

    /**
     * @param dataBytes
     * @return
     */
    public Flowable<Boolean> sendTcpMessage(final byte[] dataBytes) {
        L.d(TAG, "sendTcpMessage()");
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(sendTcpData(dataBytes));
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io());
    }

    private boolean sendTcpData(byte[] data) throws IOException {
        L.d(TAG, "sendTcpData(), sendData size = " + data.length);
        if (mSocket != null && mSocket.isConnected()) {
            OutputStream out = mSocket.getOutputStream();
            L.d(TAG, "sendTcpData(), Ready to send data...");
            out.write(DataTool.intToBytes(data.length));
            out.write(data);
            out.flush();
            return true;
        }
        return false;
    }

    private byte[] readTcpData(InputStream inputStream) throws Exception {
        L.d(TAG, "readTcpData()");
        if (mSocket != null && mSocket.isConnected()) {
            int dataLen;
            byte[] lenBytes = new byte[4];
            byte[] data;

            //读取数据长度，定义为int
            L.d(TAG, "readTcpData(), Waiting to read data...");
            inputStream.read(lenBytes, 0, lenBytes.length);
            dataLen = DataTool.bytesToInt(lenBytes, 0);

            if (dataLen >= 0) {
                data = new byte[dataLen];
                inputStream.read(data, 0, data.length);
                L.d(TAG, "readTcpData(), return data size = " + data.length);
                return data;
            } else {
                L.w(TAG, "readTcpData(), return data is null");
            }
            return null;
        } else {
            throw new Exception("Socket is null or disconnect");
        }

    }

    public void closeSocket() {
        L.d(TAG, "closeSocket()");
        synchronized (mSocket) {
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
