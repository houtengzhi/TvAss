// automatically generated by the FlatBuffers compiler, do not modify

package com.example.yechy.tvass.flatbuffers;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class TcpResponse extends Table {
  public static TcpResponse getRootAsTcpResponse(ByteBuffer _bb) { return getRootAsTcpResponse(_bb, new TcpResponse()); }
  public static TcpResponse getRootAsTcpResponse(ByteBuffer _bb, TcpResponse obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public TcpResponse __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public byte msgType() { int o = __offset(4); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public byte responseCode() { int o = __offset(6); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public byte msgContent(int j) { int o = __offset(8); return o != 0 ? bb.get(__vector(o) + j * 1) : 0; }
  public int msgContentLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer msgContentAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }

  public static int createTcpResponse(FlatBufferBuilder builder,
      byte msgType,
      byte responseCode,
      int msgContentOffset) {
    builder.startObject(3);
    TcpResponse.addMsgContent(builder, msgContentOffset);
    TcpResponse.addResponseCode(builder, responseCode);
    TcpResponse.addMsgType(builder, msgType);
    return TcpResponse.endTcpResponse(builder);
  }

  public static void startTcpResponse(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addMsgType(FlatBufferBuilder builder, byte msgType) { builder.addByte(0, msgType, 0); }
  public static void addResponseCode(FlatBufferBuilder builder, byte responseCode) { builder.addByte(1, responseCode, 0); }
  public static void addMsgContent(FlatBufferBuilder builder, int msgContentOffset) { builder.addOffset(2, msgContentOffset, 0); }
  public static int createMsgContentVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startMsgContentVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static int endTcpResponse(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
