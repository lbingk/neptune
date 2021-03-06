package org.net.api.hessian;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.net.api.MessageDecoderHandler;
import org.net.api.SerializationUtil;

import java.util.List;

/**
 * @Description 定义解码器
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class HessianDecoder extends MessageDecoderHandler {

    private static SerializationUtil serializationUtil = new HessianSerialization();

    public HessianDecoder() {
        super(serializationUtil);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        super.decode(channelHandlerContext, byteBuf, list);
    }
}