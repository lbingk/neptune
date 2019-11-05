package org.net.invoke;

import lombok.*;

import java.lang.reflect.Method;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-04 20:38
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Invoker<T> {
    private Class<T> interfaceClass;

    /**
     * 实现远程调用
     *
     * @param method
     * @param args
     * @return
     */
    public Result invoke(Method method, Object[] args) throws Exception {
        return null;
    }
}
