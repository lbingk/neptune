package org.net.api.hessian;

import com.caucho.hessian.io.Hessian2Output;
import io.netty.buffer.ByteBuf;
import org.net.api.SerializationUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @Classname MsgpackSer
 * @author: LUOBINGKAI
 * @Description Hessian序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class HessianSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object o, ByteBuf byteBuf) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        output.writeObject(o);
        byte[] bytes = os.toByteArray();
        byteBuf.writeBytes(bytes);
        output.close();
        return bytes;
    }

    @Override
    public void deserialize(ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);

        ByteArrayInputStream is = new ByteArrayInputStream(array);
        ObjectInputStream in = new ObjectInputStream(is);
        list.add(in.readObject());
    }
}
