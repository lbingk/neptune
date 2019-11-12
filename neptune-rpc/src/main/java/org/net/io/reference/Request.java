package org.net.io.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-05 20:06
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {
    public String interfaceClassName;
    public String methodName;
    public Object[] args;
    public Class<?>[] parameterTypes;
    public Class<?> returnType;
    public Object content;
    /**
     * 用来全局唯一的请求Id
     */
    private static final AtomicLong INVOKER_ID = new AtomicLong(0);

    /**
     * 请求唯一id
     */
    public long mid = newMid();

    /**
     * 生成全局的唯一ID
     *
     * @return
     */
    private static long newMid() {
        return INVOKER_ID.getAndIncrement();
    }
}
