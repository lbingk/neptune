package org.net.api.msgpack;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.net.api.MessageEncoderHandler;
import org.net.api.SerializationUtil;

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
        byte[] bytes = msgpackSerialization.serialize(o);
        int dataLength= bytes.length;
        byteBuf.writeInt(dataLength);
        byteBuf.writeBytes(bytes);
    }
}
