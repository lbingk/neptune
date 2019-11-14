package org.net.api.msgpack;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;
import org.net.api.MessageEncoderHandler;
import org.net.api.SerializationUtil;
import org.net.transport.RemoteTransporter;

import java.util.UUID;

/**
 * @Description 定义编码器：
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class MsgpackEncoder extends MessageEncoderHandler {

    private static SerializationUtil msgpackSerialization = new MsgpackSerialization();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 先计算数据的大小,并发送
        byte[] bytes = msgpackSerialization.serialize(o, byteBuf);
        int dataLength= bytes.length;
        channelHandlerContext.writeAndFlush(dataLength);
        // 正式传送
        channelHandlerContext.writeAndFlush(bytes);
    }
}
