package org.net.api;

import java.io.IOException;

/**
 * @Classname SerializationUtil
 * @author: LUOBINGKAI
 * @Description 序列化标准接口
 * @Date 2019/11/14 23:08
 */
public interface SerializationUtil {
    /**
     * 序列化
     *
     * @param out
     * @return
     * @throws IOException
     */
    byte[] serialize(Object out) throws IOException;

    /**
     * 反序列化
     *
     * @param in
     * @return
     * @throws Exception
     */
    Object deserialize(byte[] in) throws Exception;
}
