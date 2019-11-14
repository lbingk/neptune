package org.net.api;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Classname MessageDecoderHander
 * @author: LUOBINGKAI
 * @Description 解码器父类
 * @Date 2019/11/14 23:15
 */
public abstract class MessageDecoderHandler extends ByteToMessageDecoder {

    private SerializationUtil serializationUtil;

    protected MessageDecoderHandler(SerializationUtil serializationUtil) {
        this.serializationUtil = serializationUtil;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.readableBytes() < MessageCoderAdapter.MESSAGE_LENGTH) {
            return;
        }
        buffer.markReaderIndex();
        int length = buffer.readInt();
        if (buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        out.add(bytes);
        serializationUtil.deserialize(buffer, out);
    }
}
