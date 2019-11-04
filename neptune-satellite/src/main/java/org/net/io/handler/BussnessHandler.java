package org.net.io.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.collection.RegistrationDirectory;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.RegistrationBeanDefinition;
import org.net.transport.InvokerBeanExport;
import org.net.transport.RemoteTransporter;
import org.net.util.SpringContextHolder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Classname BussnessHandler
 * @Description 自定义InboundHandler
 * @Date 2019/11/1 23:15
 * @Created by admin
 */
@Slf4j
public class BussnessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        doParse(ctx, msg);
    }

    private void doParse(ChannelHandlerContext ctx, Object msg) throws IOException {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);
        String remoteIpAddrAndPort = readTransporter.getIpAddrAndPort();
        log.info(readTransporter.toString());
        List<InvokerBeanExport> invokerBeanExportList = JSON.parseArray(readTransporter.getTransContent(), InvokerBeanExport.class);

        if (TransportTypeEnum.REGISTRY.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务注册类型
            for (InvokerBeanExport invokerBeanExport : invokerBeanExportList) {
                String interfaceClassName = invokerBeanExport.getInterfaceClass();
                String jsonString = JSON.toJSONString(invokerBeanExport);
                RegistrationDirectory.putInvokerBeanInfo(interfaceClassName, remoteIpAddrAndPort, jsonString);
            }
        }

        if (TransportTypeEnum.SUBSCRIBE.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务订阅类型
            for (InvokerBeanExport invokerBeanExport : invokerBeanExportList) {
                RegistrationDirectory.putReferenceBeanInfo(invokerBeanExport.getInterfaceClass(), remoteIpAddrAndPort);
                //返回订阅的结果
                Map<String, String> directoryMap = RegistrationDirectory.subscribeResult(invokerBeanExport.getInterfaceClass());
                RegistrationBeanDefinition registrationBeanDefinition = SpringContextHolder.getBean(RegistrationBeanDefinition.class);
                String localIpAddrAndPort = InetAddress.getLocalHost().getHostAddress() + ":" + registrationBeanDefinition.getPort();
                RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), localIpAddrAndPort, JSON.toJSONString(directoryMap), TransportTypeEnum.SUBSCRIBE_RESULT.getType());
                ctx.writeAndFlush(transporter);
            }
        }

        if (TransportTypeEnum.HEART_BEAT.getType().equals(readTransporter.getTransType())) {
            // 更新最新的心跳时间
            RegistrationDirectory.refreshHostTime(remoteIpAddrAndPort);
        }

    }
}
