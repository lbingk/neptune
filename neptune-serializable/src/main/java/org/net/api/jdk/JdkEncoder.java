package org.net.api.jdk;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.net.api.MessageEncoderHandler;

/**
 * @Description 定义编码器：
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class JdkEncoder extends MessageEncoderHandler {

    private static JdkSerialization jdkSerialization = new JdkSerialization();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 先计算数据的大小,并发送实际数据
        byte[] bytes = jdkSerialization.serialize(o);
        int dataLength= bytes.length;
        byteBuf.writeInt(dataLength);
        byteBuf.writeBytes(bytes);
    }
}
