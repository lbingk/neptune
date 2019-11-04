package org.net.springextensible.invoke;

import lombok.*;

import java.lang.reflect.Method;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-04 20:38
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Invoker<T> {
    private Class<T> interfaceClass;

    public Result invoke(Method method, Object[] args) {
        return new Result();
    }
}
