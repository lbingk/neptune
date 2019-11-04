package org.net.springextensible.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Classname RefBeanFatory
 * @author: LUOBINGKAI
 * @Description 负责生成消费者动态代理对象
 * @Date 2019/11/3 18:11
 */
public class RefProxyBeanFactory {
    public static <T> T createProxy(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(RefProxyBeanFactory.class.getClassLoader(), new Class<?>[]{interfaceClass}, new RpcProxyBean());
    }

    static class RpcProxyBean implements InvocationHandler {
        public RpcProxyBean() {
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(proxy,args);
        }
    }
}
