package com.yechy.tvassserver.service;

import com.yechy.tvassserver.base.BaseRxPresenter;
import com.yechy.tvassserver.communication.CommModel;
import com.yechy.tvassserver.communication.ICommModel;
import com.yechy.tvassserver.flatbuffers.FlatUtil;
import com.yechy.tvassserver.flatbuffers.TcpMessage;
import com.yechy.tvassserver.flatbuffers.TcpMsgType;
import com.yechy.tvassserver.util.AppConfig;
import com.yechy.tvassserver.util.L;
import com.yechy.tvassserver.util.RxUtil;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by yechy on 2017/5/23.
 */

public class ListenerServicePresenter extends BaseRxPresenter<ListenerServiceContract.IView>
        implements ListenerServiceContract.IPresenetr<ListenerServiceContract.IView> {
    private static final String TAG = ListenerServicePresenter.class.getSimpleName();

    private ICommModel commModel;

    @Inject
    public ListenerServicePresenter(CommModel commModel) {
        this.commModel = commModel;
    }

    @Override
    public void registerUdpMulticast() {
        Disposable disposable = commModel.registerUdpMulticast()
                .compose(RxUtil.<DatagramPacket>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<DatagramPacket>() {
                    @Override
                    public void onNext(DatagramPacket packet) {
                        InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
                        L.d(TAG, "On-Line, host : " + socketAddress.getAddress().getHostAddress() +
                        ":" + socketAddress.getPort());
                    }

                    @Override
                    public void onError(Throwable t) {
                        L.e(TAG, "Off-Line, " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void registerTcpMessage() {
        Disposable disposable = commModel.registerTcpMessage(AppConfig.TCP_PORT)
                .compose(RxUtil.<TcpMessage>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<TcpMessage>() {
                    @Override
                    public void onNext(TcpMessage tcpMessage) {
                        processTcpMessage(tcpMessage);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscribe(disposable);
    }

    private void processTcpMessage(TcpMessage tcpMessage) {
        if (tcpMessage == null) {

        } else if (tcpMessage.msgType() == TcpMsgType.MESSAGE_TYPE_CONNECT_REQUEST) {
            byte[] sendBytes = FlatUtil.createTcpResponseBytes(TcpMsgType.MESSAGE_TYPE_CONNECT_RESPONSE,
                    (byte)0, new byte[0]);
            L.d(TAG, "processTcpMessage(), Receive connect request.");
            sendTcpData(sendBytes);

        } else if (tcpMessage.msgType() == TcpMsgType.MESSAGE_TYPE_ONKEY_REQUEST) {
            byte[] sendBytes = FlatUtil.createTcpResponseBytes(TcpMsgType.MESSAGE_TYPE_ONKEY_RESPONSE,
                    (byte)0, new byte[0]);
            L.d(TAG, "processTcpMessage(), Receive onkey request.");
            sendTcpData(sendBytes);

        } else if (tcpMessage.msgType() == TcpMsgType.MESSAGE_TYPE_FUNCTION_REQUEST) {
            byte[] sendBytes = FlatUtil.createTcpResponseBytes(TcpMsgType.MESSAGE_TYPE_FUNCTION_RESPONSE,
                    (byte)0, new byte[0]);
            L.d(TAG, "processTcpMessage(), Receive function request.");
            sendTcpData(sendBytes);
        } else {

        }

    }

    @Override
    public void sendTcpData(byte[] sendBytes) {
        Disposable disposable = commModel.sendTcpData(sendBytes)
                .compose(RxUtil.<Boolean>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void closeSocket() {
        addSubscribe(commModel.closeUdpSocket().subscribe());
        addSubscribe(commModel.closeTcpSocket().subscribe());
    }
}
