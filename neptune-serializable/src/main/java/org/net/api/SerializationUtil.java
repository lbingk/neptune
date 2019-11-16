package org.net.api;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

/**
 * @Classname SerializationUtil
 * @author: LUOBINGKAI
 * @Description 序列化标准接口
 * @Date 2019/11/14 23:08
 */
public interface SerializationUtil {
    /**
     * 序列化
     *  @param o
     */
    byte[] serialize(Object o) throws IOException;

    void deserialize(ByteBuf msg, List<Object> out) throws  Exception;
}
