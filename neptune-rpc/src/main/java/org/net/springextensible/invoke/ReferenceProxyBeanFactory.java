package org.net.springextensible.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Classname RefBeanFatory
 * @author: LUOBINGKAI
 * @Description 负责生成消费者动态代理对象
 * @Date 2019/11/3 18:11
 */
public class ReferenceProxyBeanFactory {
    public static <T> T createProxy(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(ReferenceProxyBeanFactory.class.getClassLoader(), new Class<?>[]{interfaceClass}, new ReferenceProxyBean(new Invoker(interfaceClass)));
    }

    static class ReferenceProxyBean<T> implements InvocationHandler {
        Invoker<T> handler;

        public ReferenceProxyBean(Invoker<T> handler) {
            this.handler = handler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(handler, args);
            }
            if ("toString".equals(methodName) && parameterTypes.length == 0) {
                return handler.toString();
            }
            if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
                return handler.hashCode();
            }
            if ("equals".equals(methodName) && parameterTypes.length == 1) {
                return handler.equals(args[0]);
            }
            return handler.invoke(method, args);
        }
    }
}
