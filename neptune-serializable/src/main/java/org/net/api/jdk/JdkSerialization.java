package org.net.api.jdk;

import io.netty.buffer.ByteBuf;
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
    public byte[] serialize(Object o, ByteBuf byteBuf) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        byteBuf.writeBytes(bytes);
        bos.close();
        oos.close();
        return bytes;
    }

    @Override
    public void deserialize(ByteBuf byteBuf, List<Object> list) throws Exception {
        final int length = byteBuf.readableBytes();
        final byte[] b = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), b, 0, length);

        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bis);
        list.add(ois.readObject());
        ois.close();
    }
}
