package org.net.api;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Classname MessageEncoderHandler
 * @author: LUOBINGKAI
 * @Description 编码器父类
 * @Date 2019/11/14 23:16
 */
public abstract class MessageEncoderHandler extends MessageToByteEncoder<Object> {

    @Override
    protected abstract void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception;
}
