package org.net.api.hessian;

import com.caucho.hessian.io.Hessian2Output;
import org.net.api.SerializationUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @Classname MsgpackSer
 * @author: LUOBINGKAI
 * @Description Hessian序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class HessianSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        output.writeObject(o);
        byte[] bytes = os.toByteArray();
//        byteBuf.writeBytes(bytes);
        output.close();
        return bytes;
    }

    @Override
    public Object deserialize(byte[] ins) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(ins);
        ObjectInputStream in = new ObjectInputStream(is);
        return in;
    }
}
