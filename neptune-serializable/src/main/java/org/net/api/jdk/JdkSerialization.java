package org.net.api.jdk;

import org.net.api.SerializationUtil;

import java.io.*;

/**
 * @Classname JdkSerialization
 * @author: LUOBINGKAI
 * @Description JDK序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class JdkSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object out) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(out);
        objOut.close();
        return byteOut.toByteArray();
    }


    @Override
    public Object  deserialize(byte[] in) throws Exception {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(in);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Object obj = objIn.readObject();
        objIn.close();
        return obj;
    }
}
