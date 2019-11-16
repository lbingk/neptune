package org.net.io.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.constant.TransportTypeEnum;
import org.net.manager.ChannelDirectory;
import org.net.manager.RegistrationDirectory;
import org.net.springextensible.RegistrationBeanDefinition;
import org.net.transport.ReferenceBeanExport;
import org.net.transport.RemoteTransporter;
import org.net.transport.ServiceBeanExport;
import org.net.transport.SubscribeResult;
import org.net.util.SpringContextHolder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @Classname BusinessHandler
 * @Description 自定义InboundHandler
 * @Date 2019/11/1 23:15
 * @Created by admin
 */
@Slf4j
public class BusinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        doRead(ctx, msg);
    }

    private void doRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);
        String remoteIpAddrAndPort = readTransporter.getIpAddrAndPort();
        log.info(readTransporter.toString());

        doRegistry(readTransporter, remoteIpAddrAndPort);

        doSubscriber(ctx, readTransporter, remoteIpAddrAndPort);

        doHeartBeat(readTransporter, remoteIpAddrAndPort);

    }

    /**
     * 处理心跳
     *
     * @param readTransporter
     * @param remoteIpAddrAndPort
     */
    private void doHeartBeat(RemoteTransporter readTransporter, String remoteIpAddrAndPort) {
        if (TransportTypeEnum.HEART_BEAT.getType().equals(readTransporter.getTransType())) {
            // 更新最新的心跳时间
            RegistrationDirectory.refreshHostTime(remoteIpAddrAndPort);
        }
    }

    /**
     * 处理订阅
     *
     * @param ctx
     * @param readTransporter
     * @param remoteIpAddrAndPort
     * @throws UnknownHostException
     */
    private void doSubscriber(ChannelHandlerContext ctx, RemoteTransporter readTransporter, String remoteIpAddrAndPort) {
        if (TransportTypeEnum.SUBSCRIBE.getType().equals(readTransporter.getTransType())) {
            List<ReferenceBeanExport> referenceBeanExportList = JSON.parseArray(readTransporter.getTransContent(), ReferenceBeanExport.class);
            for (ReferenceBeanExport referenceBeanExport : referenceBeanExportList) {
                RegistrationDirectory.putReferenceBeanInfo(referenceBeanExport.getInterfaceClass(), remoteIpAddrAndPort);
                // 维护channel()
                ChannelDirectory.putChannel(remoteIpAddrAndPort, ctx.channel());
                // 推送服务
                RegistrationBeanDefinition registrationBeanDefinition = SpringContextHolder.getBean(RegistrationBeanDefinition.class);
                Set<String> serviceHostSets = RegistrationDirectory.getServiceHost(referenceBeanExport.getInterfaceClass());
                if (serviceHostSets.isEmpty()) {
                    continue;
                }
                String localIpAddrAndPort = null;
                try {

                    localIpAddrAndPort = InetAddress.getLocalHost().getHostAddress() + ":" + registrationBeanDefinition.getPort();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                SubscribeResult subscribeResult = new SubscribeResult();
                subscribeResult.setInterfaceClassName(referenceBeanExport.getInterfaceClass());
                subscribeResult.setIpAddrAndPortSets(serviceHostSets);
                RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), localIpAddrAndPort,
                        JSON.toJSONString(subscribeResult), TransportTypeEnum.SUBSCRIBE_RESULT.getType());
                log.info("transporter:[{}]", transporter);
                ctx.writeAndFlush(transporter);
            }
        }
    }


    /**
     * 处理注册
     *
     * @param readTransporter
     * @param remoteIpAddrAndPort
     */
    private void doRegistry(RemoteTransporter readTransporter, String remoteIpAddrAndPort) {
        if (TransportTypeEnum.REGISTRY.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务注册类型
            List<ServiceBeanExport> serviceBeanExportList = JSON.parseArray(readTransporter.getTransContent(), ServiceBeanExport.class);
            for (ServiceBeanExport serviceBeanExport : serviceBeanExportList) {
                String interfaceClassName = serviceBeanExport.getInterfaceClass();
                String jsonString = JSON.toJSONString(serviceBeanExport);
                RegistrationDirectory.putInvokerBeanInfo(interfaceClassName, remoteIpAddrAndPort, jsonString);
                // 推送服务
                RegistrationDirectory.pushService(interfaceClassName);
            }
        }
    }
}
