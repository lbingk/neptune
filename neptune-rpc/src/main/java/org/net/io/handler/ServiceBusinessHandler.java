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

            String requestInterfaceClassName = request.getInterfaceClassName();
            String requestMethodName = request.getMethodName();
            Class<?>[] requestParameterTypes = request.getParameterTypes();

            Object[] requestArgs = request.getArgs();

            ServiceBean serviceBean = null;
            Object invokerObj = null;
            Method invokerMethod = null;
            Class[] requestArgsTypes = new Class[requestArgs.length];
            for (int i = 0; i < requestArgs.length; i++) {
                requestArgsTypes[i] = requestArgs.getClass();
            }

            // doValidateInvoker
            DoValidateInvoker doValidateInvoker = new DoValidateInvoker(ctx, response, localIpAddrAndPort, requestInterfaceClassName, requestMethodName, requestParameterTypes, invokerMethod).invoke();
            if (doValidateInvoker.is()){
                return;
            }
            serviceBean = doValidateInvoker.getServiceBean();
            invokerMethod = doValidateInvoker.getInvokerMethod();

            // doInvoke
            doInvoke(ctx, response, localIpAddrAndPort, requestArgs, serviceBean, invokerMethod);
        }

    }

    private void doInvoke(ChannelHandlerContext ctx, Response response, String localIpAddrAndPort, Object[] requestArgs, ServiceBean serviceBean, Method invokerMethod) {
        try {
            Object invokeResult = invokerMethod.invoke(serviceBean.getRef(), requestArgs);
            response.setStatus(Response.OK);
            if (invokeResult != null) {
                response.setContent(JSON.toJSONString(invokeResult));
            }
        } catch (Exception e) {
            response.setStatus(Response.EXPTION);
            response.setErrorMessage(e.getMessage());
        } finally {
            RemoteTransporter sendTransporter = RemoteTransporter.create(UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(response), TransportTypeEnum.INVOKER_RESULT.getType());
            ctx.writeAndFlush(sendTransporter);
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

    private class DoValidateInvoker {
        private boolean myResult;
        private ChannelHandlerContext ctx;
        private Response response;
        private String localIpAddrAndPort;
        private String requestInterfaceClassName;
        private String requestMethodName;
        private Class<?>[] requestParameterTypes;
        private Method invokerMethod;
        private ServiceBean serviceBean;

        public DoValidateInvoker(ChannelHandlerContext ctx, Response response, String localIpAddrAndPort, String requestInterfaceClassName, String requestMethodName, Class<?>[] requestParameterTypes, Method invokerMethod) {
            this.ctx = ctx;
            this.response = response;
            this.localIpAddrAndPort = localIpAddrAndPort;
            this.requestInterfaceClassName = requestInterfaceClassName;
            this.requestMethodName = requestMethodName;
            this.requestParameterTypes = requestParameterTypes;
            this.invokerMethod = invokerMethod;
        }

        boolean is() {
            return myResult;
        }

        public ServiceBean getServiceBean() {
            return serviceBean;
        }

        public Method getInvokerMethod() {
            return invokerMethod;
        }

        public DoValidateInvoker invoke() throws NoSuchMethodException {
            Object invokerObj;
            if ((serviceBean = InvokerBeanInfo.getServiceBean(requestInterfaceClassName)) == null || (invokerObj = serviceBean.getRef()) == null) {
                response.setStatus(Response.NO_SERVER);
                response.setErrorMessage("不存在此服务提供者：" + requestInterfaceClassName);
                RemoteTransporter sendTransporter = RemoteTransporter.create(
                        UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(response), TransportTypeEnum.INVOKER_RESULT.getType());
                ctx.writeAndFlush(sendTransporter);
                myResult = true;
                return this;
            }

            try {
                invokerMethod = invokerObj.getClass().getMethod(requestMethodName, requestParameterTypes);
            } finally {
                if ((invokerMethod == null)) {
                    response.setStatus(Response.NO_METHOD);
                    response.setErrorMessage("服务提供者不存在此方法: " + requestInterfaceClassName + "." + requestMethodName + "." + requestParameterTypes);
                    RemoteTransporter sendTransporter = RemoteTransporter.create(
                            UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(response), TransportTypeEnum.INVOKER_RESULT.getType());
                    ctx.writeAndFlush(sendTransporter);
                    myResult = true;
                    return this;
                }
            }
            myResult = false;
            return this;
        }
    }
}
