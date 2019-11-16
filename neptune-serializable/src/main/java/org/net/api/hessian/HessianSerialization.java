package org.net.api.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.net.api.SerializationUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Classname HessianSerialization
 * @author: LUOBINGKAI
 * @Description Hessian序列化与反序列化
 * @Date 2019/11/14 23:32
 */
public class HessianSerialization implements SerializationUtil {

    @Override
    public byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(os);
        output.writeObject(o);
        byte[] bytes = os.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public Object deserialize(byte[] ins) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(ins);
        HessianInput hi = new HessianInput(is);
        Object  o= hi.readObject();
        hi.close();
        is.close();
        return o;
    }
}
