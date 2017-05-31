package com.example.yechy.tvass.flatbuffers;

import com.example.yechy.tvass.util.L;
import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;


/**
 * Created by yechy on 2017/4/26.
 */
public class FlatUtil {
    private static final String TAG = FlatUtil.class.getSimpleName();

    public static ByteBuffer createUdpMessage(byte msgType, String ip, int port, String name) {
        L.d(TAG, "createUdpMessage(), msgType = " + msgType + ", ip = " + ip + ", port = " + port
                + ", name = " + name);
        FlatBufferBuilder builder = new FlatBufferBuilder(1);
        int ipOffset = builder.createString(ip);
        int nameOffset = builder.createString(name);

        UdpMessage.startUdpMessage(builder);
        UdpMessage.addMsgType(builder, msgType);
        UdpMessage.addIp(builder, ipOffset);
        UdpMessage.addPort(builder, port);
        UdpMessage.addName(builder, nameOffset);
        int end = UdpMessage.endUdpMessage(builder);
        UdpMessage.finishUdpMessageBuffer(builder, end);

        return builder.dataBuffer();
    }

    public static byte[] createUdpMessageBytes(byte msgType, String ip, int port, String name) {
        L.d(TAG, "createUdpMessageBytes(), msgType = " + msgType + ", ip = " + ip + ", port = " + port
                + ", name = " + name);
        FlatBufferBuilder builder = new FlatBufferBuilder(1);
        int ipOffset = builder.createString(ip);
        int nameOffset = builder.createString(name);

        UdpMessage.startUdpMessage(builder);
        UdpMessage.addMsgType(builder, msgType);
        UdpMessage.addIp(builder, ipOffset);
        UdpMessage.addPort(builder, port);
        UdpMessage.addName(builder, nameOffset);
        int end = UdpMessage.endUdpMessage(builder);
        UdpMessage.finishUdpMessageBuffer(builder, end);

        return builder.sizedByteArray();
    }

    public static UdpMessage getUdpMessage(byte[] bytes) {
        L.d(TAG, "getUdpMessage(), dataBytes length = " + bytes.length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        UdpMessage udpMessage = UdpMessage.getRootAsUdpMessage(byteBuffer);
        return udpMessage;
    }

    public static UdpMessage getUdpMessage(ByteBuffer byteBuffer) {
        L.d(TAG, "getUdpMessage(), byteBuffer length = " + byteBuffer.array().length);
        UdpMessage udpMessage = UdpMessage.getRootAsUdpMessage(byteBuffer);
        return udpMessage;
    }


    public static ByteBuffer createTcpMessage(byte msgType, short keyCode, byte keyAction) {
        L.d(TAG, "createTcpMessageBytes(), msgType = " + msgType + ", keyCode = " + keyCode
                + ", keyAction = " + keyAction);
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        TcpMessage.startTcpMessage(builder);
        TcpMessage.addMsgType(builder, msgType);
        TcpMessage.addKeyCode(builder, keyCode);
        TcpMessage.addKeyAction(builder, keyAction);

        builder.finish(TcpMessage.endTcpMessage(builder));

        return builder.dataBuffer();
    }

    public static byte[] createTcpMessageBytes(byte msgType, short keyCode, byte keyAction) {
        L.d(TAG, "createTcpMessageBytes(), msgType = " + msgType + ", keyCode = " + keyCode
         + ", keyAction = " + keyAction);
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        TcpMessage.startTcpMessage(builder);
        TcpMessage.addMsgType(builder, msgType);
        TcpMessage.addKeyCode(builder, keyCode);
        TcpMessage.addKeyAction(builder, keyAction);

        builder.finish(TcpMessage.endTcpMessage(builder));

        return builder.sizedByteArray();
    }

    public static byte[] createTcpResponseBytes(byte msgType, byte responseCode, byte[] content) {
        L.d(TAG, "createTcpResponseBytes(), msgType = " + msgType + ", responseCode = " + responseCode
        + ", content length = " + content.length);
        FlatBufferBuilder builder = new FlatBufferBuilder(1);
        int contentOffset = TcpResponse.createMsgContentVector(builder, content);

        TcpResponse.startTcpResponse(builder);
        TcpResponse.addMsgType(builder, msgType);
        TcpResponse.addResponseCode(builder, responseCode);
        TcpResponse.addMsgContent(builder, contentOffset);

        builder.finish(TcpResponse.endTcpResponse(builder));
        return builder.sizedByteArray();
    }

    public static TcpResponse getTcpResponse(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        TcpResponse tcpResponse = TcpResponse.getRootAsTcpResponse(byteBuffer);
        return tcpResponse;
    }

    public static TcpMessage getTcpMessage(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        TcpMessage tcpMessage = TcpMessage.getRootAsTcpMessage(byteBuffer);
        return tcpMessage;
    }

    public static String getUdpMessageString(UdpMessage message) {
        if (message == null) {
            return null;
        }
        return "UdpMessage{" + "msgType = " + message.msgType() + "\'"
                + ", ip = " + message.ip() + "\'"
                + ", port = " + message.port() + "\'"
                + ", name = " + message.name() + "\'"
                + "}";
    }

    public static String getTcpMessageString(TcpMessage message) {
        if (message == null) {
            return null;
        }
        return "TcpMessage{" + "msgType = " + message.msgType()
                + ", keyCode = " + message.keyCode()
                + ", keyAction = " + message.keyAction();
    }

    public static String getTcpResponseString(TcpResponse response) {
        if (response == null) {
            return null;
        }
        return "TcpResponse {" + "msgType = " + response.msgType()
                + ", responseCode = " + response.responseCode()
                + ", msgContent length = " + response.msgContentLength();
    }
}
