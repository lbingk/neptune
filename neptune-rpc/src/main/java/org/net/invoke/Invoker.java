package org.net.invoke;

import io.netty.channel.Channel;
import lombok.*;
import org.net.io.client.ReferenceNettyClient;
import org.net.io.reference.InvokerDirectory;
import org.net.io.reference.Request;
import org.net.springextensible.beandefinition.ReferenceBean;
import org.springframework.util.StringUtils;

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
    private ReferenceBean<T> referenceBean;

    /**
     * 实现远程调用
     *
     * @param method
     * @param args
     * @return
     */
    public Object invoke(Method method, Object[] args) throws Exception {
        String invokerDirectory = InvokerDirectory.getRandom(referenceBean.getInterfaceName());
        if (StringUtils.isEmpty(invokerDirectory)) {
            throw new RuntimeException("不存在此服务提供者：" + referenceBean.getInterfaceName());
        }
        Request request = new Request();
        request.setInterfaceClassName(referenceBean.getInterfaceClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setReturnType(method.getReturnType());
        request.setArgs(args);
        return doInvoke(invokerDirectory, request);
    }

    /**
     * 真正干活：开启netty,发送调用信息，获取结果
     *
     * @param invokerDirectory
     * @param request
     * @return
     * @throws Exception
     */
    private Object doInvoke(String invokerDirectory, Request request) throws Exception {
        ReferenceNettyClient invokerNettyClient = new ReferenceNettyClient(request, invokerDirectory, referenceBean.getTimeout(), referenceBean.getRetries());
        invokerNettyClient.doOpen();
        Channel channel = invokerNettyClient.dcConnect();
        return invokerNettyClient.doSend(channel);
    }
}
