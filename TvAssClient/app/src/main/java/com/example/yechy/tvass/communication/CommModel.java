package com.example.yechy.tvass.communication;

import com.example.yechy.tvass.communication.net.TcpClient;
import com.example.yechy.tvass.communication.net.UdpClient;
import com.example.yechy.tvass.flatbuffers.FlatUtil;
import com.example.yechy.tvass.flatbuffers.TcpMsgType;
import com.example.yechy.tvass.model.bean.Device;
import com.example.yechy.tvass.model.prefs.IPreferencesHelper;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by yechy on 2017/4/22.
 */

public class CommModel implements ICommModel {
    private int mode;
    private TcpClient tcpClient;
    private UdpClient udpClient;
    private IPreferencesHelper mPreferencesHelper;


    @Inject
    public CommModel(TcpClient tcpClient, UdpClient udpClient, IPreferencesHelper preferencesHelper) {
        this.tcpClient = tcpClient;
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

        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return null;
    }

    @Override
    public Flowable<Boolean> disconnectDevice() {
        if (mode == Constant.COMMUNICATION_MODE_WIFI) {

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


        } else if (mode == Constant.COMMUNICATION_MODE_BLUETOOTH) {

        }
        return null;
    }
}
