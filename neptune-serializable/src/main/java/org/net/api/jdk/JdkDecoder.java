package org.net.api.jdk;


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
public class  JdkDecoder extends MessageDecoderHandler {

    private static SerializationUtil serializationUtil = new JdkSerialization();

    public JdkDecoder() {
        super(serializationUtil);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        super.decode(channelHandlerContext, byteBuf, list);
    }
}