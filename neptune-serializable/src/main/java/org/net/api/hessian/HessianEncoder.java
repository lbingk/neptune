package org.net.api.hessian;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.net.api.MessageEncoderHandler;

/**
 * @Description 定义编码器：
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class HessianEncoder extends MessageEncoderHandler {

    private static HessianSerialization hessianSerialization = new HessianSerialization();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 先计算数据的大小,并发送
        byte[] bytes = hessianSerialization.serialize(o, byteBuf);
        int dataLength= bytes.length;
        channelHandlerContext.writeAndFlush(dataLength);
        // 正式传送
        channelHandlerContext.writeAndFlush(bytes);
    }
}
