package org.net.api.msgpack;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.net.api.MessageDecoderHandler;

import java.util.List;

/**
 * @Description 定义解码器
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class MsgpackDecoder extends MessageDecoderHandler {

    private static MsgpackSerialization msgpackSerialization = new MsgpackSerialization();

    public MsgpackDecoder() {
        super(msgpackSerialization);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        super.decode(channelHandlerContext,byteBuf,list);
    }
}