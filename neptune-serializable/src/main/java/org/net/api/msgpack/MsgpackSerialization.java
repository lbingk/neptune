package org.net.api.msgpack;

import io.netty.buffer.ByteBuf;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.net.api.SerializationUtil;

import java.io.IOException;
import java.util.List;

/**
 * @Classname MsgpackSer
 * @author: LUOBINGKAI
 * @Description Msgpack序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class MsgpackSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object o, ByteBuf byteBuf) throws IOException {
        MessagePack messagePack = new MessagePack();
        /** 序列化对象*/
        byte[] raw = messagePack.write(o);
        byteBuf.writeBytes(raw);
        return raw;
    }

    @Override
    public void deserialize(ByteBuf byteBuf, List<Object> list) throws IOException {
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        Value read = messagePack.read(array);
        list.add(read);
    }
}
