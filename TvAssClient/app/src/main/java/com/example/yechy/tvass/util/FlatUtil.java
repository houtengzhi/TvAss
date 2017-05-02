package com.example.yechy.tvass.util;

import com.example.yechy.tvass.flatbuffers.KeyStatus;
import com.example.yechy.tvass.flatbuffers.SocketMessage;
import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;

/**
 * Created by yechy on 2017/4/26.
 */
public class FlatUtil {

    public static ByteBuffer createSocketMessage(String msgHead, byte msgType, int ip) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int msgHeadOffset = builder.createString(msgHead);

        SocketMessage.startSocketMessage(builder);
        SocketMessage.addHead(builder, msgHeadOffset);
        SocketMessage.addMsgType(builder, msgType);
        SocketMessage.addIp(builder, ip);

        SocketMessage.addKeyCode(builder, (short) 1);
        SocketMessage.addKeyAction(builder, KeyStatus.ActionDown);
        int end = SocketMessage.endSocketMessage(builder);
        builder.finish(end);
        return builder.dataBuffer();
    }

    public static SocketMessage getSocketMessage(byte[] dataBuffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataBuffer);
        SocketMessage socketMessage = SocketMessage.getRootAsSocketMessage(byteBuffer);
        return socketMessage;
    }

}
