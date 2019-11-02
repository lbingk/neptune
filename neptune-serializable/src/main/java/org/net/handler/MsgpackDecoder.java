package org.net.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import java.util.List;

/**
 * @Description 定义解码器
 * @Author luobingkai
 * @Date 2019/7/20 3:07
 * @Version 1.0
 **/
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 重写方法，首先从数据报 byteBuf 中获取需要解码的 byte 数组，
     * 然后调用 MessagePack 的 read 方法将其反序列化为 Object 对象，将解码后的对象加入到解码列表 list 中，
     * 这样就完成了 MessagePack 的解码操作
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        Value read = messagePack.read(array);
        list.add(read);
    }
}