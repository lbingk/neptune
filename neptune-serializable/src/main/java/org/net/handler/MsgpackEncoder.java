package org.net.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Description 定义编码器：
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    /**
     * 重写方法，负责将 Object 类型的 POJO 对象编码为 byte 数组，然后写入 ByteBuf 中
     *
     * @param channelHandlerContext
     * @param o
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();

        /** 序列化对象*/
        byte[] raw = messagePack.write(o);
        byteBuf.writeBytes(raw);
    }
}
