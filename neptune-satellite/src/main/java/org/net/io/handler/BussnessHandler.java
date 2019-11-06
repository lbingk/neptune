package org.net.io.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.constant.TransportTypeEnum;
import org.net.manager.ChannelDirectory;
import org.net.manager.RegistrationDirectory;
import org.net.transport.InvokerBeanExport;
import org.net.transport.RemoteTransporter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

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

        doRegistry(ctx, readTransporter, remoteIpAddrAndPort);

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
    private void doSubscriber(ChannelHandlerContext ctx, RemoteTransporter readTransporter, String remoteIpAddrAndPort) throws UnknownHostException {
        if (TransportTypeEnum.SUBSCRIBE.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务订阅类型
            List<InvokerBeanExport> invokerBeanExportList = JSON.parseArray(readTransporter.getTransContent(), InvokerBeanExport.class);
            // 维护channel()
            ChannelDirectory.putChannel(remoteIpAddrAndPort, ctx.channel());
        }
    }

    /**
     * 处理注册
     *
     * @param ctx
     * @param readTransporter
     * @param remoteIpAddrAndPort
     */
    private void doRegistry(ChannelHandlerContext ctx, RemoteTransporter readTransporter, String remoteIpAddrAndPort) {
        if (TransportTypeEnum.REGISTRY.getType().equals(readTransporter.getTransType())) {
            // 当收到的是服务注册类型
            List<InvokerBeanExport> invokerBeanExportList = JSON.parseArray(readTransporter.getTransContent(), InvokerBeanExport.class);
            for (InvokerBeanExport invokerBeanExport : invokerBeanExportList) {
                String interfaceClassName = invokerBeanExport.getInterfaceClass();
                String jsonString = JSON.toJSONString(invokerBeanExport);
                RegistrationDirectory.putInvokerBeanInfo(interfaceClassName, remoteIpAddrAndPort, jsonString);
                // 维护channel()
                ChannelDirectory.putChannel(remoteIpAddrAndPort, ctx.channel());
                // 推送服务
                RegistrationDirectory.pushService(interfaceClassName);
            }
        }
    }
}
