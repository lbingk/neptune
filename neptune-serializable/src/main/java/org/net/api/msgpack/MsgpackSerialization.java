package org.net.api.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.net.api.SerializationUtil;

import java.io.IOException;

/**
 * @Classname MsgpackSer
 * @author: LUOBINGKAI
 * @Description Msgpack序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class MsgpackSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object out) throws IOException {
        MessagePack messagePack = new MessagePack();
        return messagePack.write(out);
    }

    @Override
    public Object deserialize(byte[] ins) throws IOException {
        MessagePack messagePack = new MessagePack();
        Value read = messagePack.read(ins);
        return read;
    }
}
