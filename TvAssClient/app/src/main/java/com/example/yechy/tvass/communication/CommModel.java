package com.example.yechy.tvass.communication;

import com.example.yechy.tvass.communication.net.TcpApi;
import com.example.yechy.tvass.communication.net.UdpClient;
import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.TcpMsgType;
import com.example.yechy.tvass.flatbuffers.TcpResponse;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.model.prefs.IPreferencesHelper;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by yechy on 2017/4/22.
 */

public class CommModel implements ICommModel {
    private int mode;
    private TcpApi tcpApi;
    private UdpClient udpClient;
    private IPreferencesHelper mPreferencesHelper;


    @Inject
    public CommModel(TcpApi tcpApi, UdpClient udpClient, IPreferencesHelper preferencesHelper) {
        this.tcpApi = tcpApi;
        this.udpClient = udpClient;
        this.mPreferencesHelper = preferencesHelper;
        mode = mPreferencesHelper.getCommunicationMode();
    }

    /**
     * 开始搜索设备
     * @return
     */
    @Override
    public Flowable<Device> startSearchDevice() {
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            return udpClient.sendSearchMulticast();

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {
        }
        return null;
    }

    /**
     * 停止搜索设备
     * @return
     */
    @Override
    public Flowable<Boolean> stopSearchDevice() {
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            return udpClient.closeUdpSocket();

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {
        }
        return null;
    }

    /**
     * 连接设备
     * @param ip
     * @param port
     * @return
     */
    @Override
    public Flowable<Boolean> connectDevice(String ip, int port) {
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
           return tcpApi.connectDevice(ip, port);

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return null;
    }

    @Override
    public Flowable<Boolean> disconnectDevice() {
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
            return tcpApi.disconnectDevice();
        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return null;
    }

    /**
     * 发送键值
     *
     * @param keyCode
     * @return
     */
    @Override
    public Flowable<Boolean> sendKeyCode(int keyCode, byte keyStatus) {
        byte[] bytes = FlatUtil.createTcpMessageBytes(TcpMsgType.MESSAGE_TYPE_ONKEY_REQUEST,
                (short) keyCode, keyStatus);
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {
           return tcpApi.sendAndReceiveData(bytes)
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
                   });

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return null;
    }
}
