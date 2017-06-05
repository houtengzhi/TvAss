package com.example.yechy.tvass.communication.net;


import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.TcpMsgType;
import com.example.yechy.tvass.flatbuffers.TcpResponse;
import com.example.yechy.tvass.util.DataTool;
import com.example.yechy.tvass.util.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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

    public boolean startConnect(String ip, int port) {
        L.d(TAG, "startConnect(), ip = " + ip + ", port = " + port);
        if (mSocket != null) {
            closeSocket();
        }

        mSocket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        try {
            mSocket.connect(socketAddress, CONNECT_TIMEOUT);

            if (mSocket.isConnected()) {
                //设置读流超时时间
//                mSocket.setSoTimeout(INPUTSTREAM_READ_TIMEOUT);
                mOutputStream = mSocket.getOutputStream();
                mInputStream = mSocket.getInputStream();

                if (processConnectInfo()) {
                    L.i(TAG, "Connect device " + ip + ":" + port + " success.");
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        closeSocket();
        return false;
    }

    private boolean processConnectInfo() {
        L.d(TAG, "processConnectInfo()");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendTcpData(byte[] data) throws IOException {
        L.d(TAG, "sendTcpData(), sendData size = " + data.length);
        if (mSocket != null && mSocket.isConnected() && mOutputStream != null) {
            L.d(TAG, "sendTcpData(), Ready to send data...");
            mOutputStream.write(DataTool.intToBytes(data.length));
            mOutputStream.write(data);
            mOutputStream.flush();
        }
    }

    public byte[] readTcpData() throws Exception {
        L.d(TAG, "readTcpData()");
        if (mSocket != null && mSocket.isConnected() && mInputStream != null) {
            int dataLen;
            byte[] lenBytes = new byte[4];
            byte[] data;

            //读取数据长度，定义为int
            L.d(TAG, "readTcpData(), Waiting to read data...");
            mInputStream.read(lenBytes, 0, lenBytes.length);
            dataLen = DataTool.bytesToInt(lenBytes, 0);

            if (dataLen >= 0) {
                data = new byte[dataLen];
                mInputStream.read(data, 0, data.length);
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
                    mSocket.shutdownInput();
                    mSocket.shutdownOutput();
                    mSocket.close();
                    mSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
