package com.yechy.tvassserver.util;

import com.google.flatbuffers.FlatBufferBuilder;
import com.yechy.tvassserver.flatbuffers.KeyStatus;
import com.yechy.tvassserver.flatbuffers.SocketMessage;
import com.yechy.tvassserver.net.Constant;

import java.nio.ByteBuffer;

/**
 * Created by yechy on 2017/4/26.
 */
public class FlatUtil {

    public ByteBuffer createSocketMessage() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        SocketMessage.startSocketMessage(builder);
        SocketMessage.addMsgType(builder, Constant.PACKET_TYPE_FIND_DEVICE_REQ_10);
        SocketMessage.addKeyCode(builder, (short) 1);
        SocketMessage.addKeyAction(builder, KeyStatus.ActionDown);
        int end = SocketMessage.endSocketMessage(builder);
        builder.finish(end);

        return builder.dataBuffer();
    }


}
