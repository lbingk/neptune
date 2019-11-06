package org.net.io.reference;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-05 20:06
 */
@Setter
@Getter
public class Request<T> implements Serializable {
    public Class<T> interfaceClass;
    public Method method;
    public Object[] args;
    public Object content;
    /**
     * 用来全局唯一的请求Id
     */
    private static final AtomicLong INVOKER_ID = new AtomicLong(0);

    /**
     * 请求唯一id
     */
    public final long mid;

    public Request() {
        mid = newMid();
    }

    /**
     * 生成全局的唯一ID
     *
     * @return
     */
    private static long newMid() {
        return INVOKER_ID.getAndIncrement();
    }
}
