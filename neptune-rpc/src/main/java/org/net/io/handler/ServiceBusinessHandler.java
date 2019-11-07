package org.net.io.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.constant.TransportTypeEnum;
import org.net.invoke.InvokerBeanInfo;
import org.net.io.common.Response;
import org.net.io.reference.Request;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.springextensible.beandefinition.ServiceBean;
import org.net.transport.RemoteTransporter;
import org.net.util.SpringContextHolder;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @program: neptune
 * @description: 处理具体的业务交接
 * @author: LUOBINGKAI
 * @create: 2019-11-06 10:43
 */
@Slf4j
public class ServiceBusinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);
        if (TransportTypeEnum.INVOKER.getType().equals(readTransporter.getTransType())) {
            Request request = JSON.parseObject(readTransporter.getTransContent(), Request.class);
            Response response = new Response();
            response.setMid(request.getMid());

            String localIpAddrAndPort = null;
            try {
                localIpAddrAndPort = InetAddress.getLocalHost().getHostAddress() + ":" + SpringContextHolder.getBean(ProtocolBean.class).getPort();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            Class interfaceClass = request.getInterfaceClass();
            Method requestMethod = request.getMethod();
            Object[] requestArgs = request.getArgs();

            ServiceBean serviceBean = InvokerBeanInfo.getServiceBean(interfaceClass);
            if (doValidateInvoker(ctx, response, interfaceClass, localIpAddrAndPort, requestMethod, requestArgs, serviceBean)) {
                return;
            }
            doInvoke(ctx, response, localIpAddrAndPort, requestMethod, requestArgs, serviceBean);
        }

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    /**
     * 校验调用参数
     *
     * @param ctx
     * @param response
     * @param interfaceClass
     * @param localIpAddrAndPort
     * @param requestMethod
     * @param requestArgs
     * @param serviceBean
     * @return
     * @throws NoSuchMethodException
     */
    private boolean doValidateInvoker(ChannelHandlerContext ctx, Response response, Class interfaceClass, String localIpAddrAndPort, Method requestMethod, Object[] requestArgs, ServiceBean serviceBean) throws NoSuchMethodException {
        if (serviceBean == null) {
            response.setStatus(Response.NO_SERVER);
            response.setErrorMessage("不存在此服务提供者：" + interfaceClass.getName());
            RemoteTransporter sendTransporter = RemoteTransporter.create(
                    UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(response), TransportTypeEnum.INVOKER_RESULT.getType());
            ctx.writeAndFlush(sendTransporter);
            return true;
        }

        Class[] requestArgsTypes = new Class[requestArgs.length];
        for (int i = 0; i < requestArgs.length; i++) {
            requestArgsTypes[0] = requestArgs.getClass();
        }
        if (serviceBean.getRef().getClass().getMethod(requestMethod.getName(), requestArgsTypes) == null) {
            response.setStatus(Response.NO_METHOD);
            response.setErrorMessage("服务提供者不存在此方法: " + interfaceClass.getName() + "." + requestMethod.getName() + "." + requestArgsTypes[0]);
            RemoteTransporter sendTransporter = RemoteTransporter.create(
                    UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(response), TransportTypeEnum.INVOKER_RESULT.getType());
            ctx.writeAndFlush(sendTransporter);
            return true;
        }
        return false;
    }


    /**
     * 真正调用
     *
     * @param ctx
     * @param response
     * @param localIpAddrAndPort
     * @param requestMethod
     * @param requestArgs
     * @param serviceBean
     */
    private void doInvoke(ChannelHandlerContext ctx, Response response, String localIpAddrAndPort, Method requestMethod, Object[] requestArgs, ServiceBean serviceBean) {
        try {
            Object invoke = requestMethod.invoke(serviceBean.getRef(), requestArgs);
            response.setStatus(Response.OK);
            if (invoke != null) {
                response.setContent(JSON.toJSONString(invoke));
            }
        } catch (Exception e) {
            response.setStatus(Response.EXPTION);
            response.setErrorMessage(e.getMessage());
        } finally {
            RemoteTransporter sendTransporter = RemoteTransporter.create(UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(response), TransportTypeEnum.INVOKER_RESULT.getType());
            ctx.writeAndFlush(sendTransporter);
        }
    }
}
