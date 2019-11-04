package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.collection.RegistrationDirectory;
import org.net.transport.RemoteTransporter;
import org.net.constant.TransportTypeEnum;
import org.net.transport.ServiceBeanExport;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.List;

/**
 * @Classname BussnessHandler
 * @Description 自定义InboundHandler
 * @Date 2019/11/1 23:15
 * @Created by admin
 */
@Slf4j
public class BussnessHandler extends ChannelInboundHandlerAdapter {

    public static RegistrationDirectory registrationDirectory = RegistrationDirectory.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        doParse(msg);
    }

    private void doParse(Object msg) throws IOException {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);
        String ipAddrAndPort = readTransporter.getIpAddrAndPort();
        log.info(readTransporter.toString());

        if (TransportTypeEnum.REGISTRY.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务注册类型
            List<ServiceBeanExport> serviceBeanExportList = JSON.parseArray(readTransporter.getTransContent(), ServiceBeanExport.class);
            for (ServiceBeanExport serviceBeanExport : serviceBeanExportList) {
                String interfaceClassNmae = serviceBeanExport.getInterfaceClass().toString();
                String jsonString = JSON.toJSONString(serviceBeanExport);
                registrationDirectory.putServiceBeanInfo(interfaceClassNmae, ipAddrAndPort, jsonString);
            }
        }

        if (TransportTypeEnum.SUBSCRIBE.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务订阅类型
            List<String> transContentList = JSON.parseArray(readTransporter.getTransContent(), String.class);
            for (String interfaceClassNmae : transContentList) {
                registrationDirectory.putRefBeanInfo(interfaceClassNmae, ipAddrAndPort);
            }
        }

        if (TransportTypeEnum.REGISTRY_HEART_BEAT.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务提供者心跳的检测,更新维护心跳时间
            registrationDirectory.refreshRegistryHostTime(ipAddrAndPort);
        }

        if (TransportTypeEnum.SUBSCRIBE_HEART_BEAT.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务订阅者心跳的检测,更新维护心跳时间
            registrationDirectory.refreshSubscribeHostTime(ipAddrAndPort);
        }
    }
}
