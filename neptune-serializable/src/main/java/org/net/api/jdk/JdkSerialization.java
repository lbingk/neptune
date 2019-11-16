package org.net.api.jdk;

import io.netty.buffer.ByteBuf;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.net.api.SerializationUtil;

import java.io.*;
import java.util.List;

/**
 * @Classname MsgpackSer
 * @author: LUOBINGKAI
 * @Description JDK序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class JdkSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(o);
        objOut.close();
        return byteOut.toByteArray();
    }


    @Override
    public void deserialize(ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(array);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Object obj = objIn.readObject();
        objIn.close();
        list.add(obj);
    }
}
