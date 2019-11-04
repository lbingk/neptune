package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.net.springextensible.beandef.ProtocolBeanDef;
import org.net.transport.RemoteTransporter;
import org.net.constant.TransportTypeEnum;
import org.net.util.SpringContextHolder;

import java.util.UUID;

/**
 * @Description 定义处理chanel激活时的处理逻辑
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:09
 * @Version 1.0
 **/
@Slf4j
public class BaseBussnessHandler extends ChannelInboundHandlerAdapter {
    private static final String ipAddrAndPort;
    static {
        ProtocolBeanDef protocolBeanDef = SpringContextHolder.getBean(ProtocolBeanDef.class);
        ipAddrAndPort = protocolBeanDef.getIp() + ":" + protocolBeanDef.getPort();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.writeAndFlush(RemoteTransporter.create(UUID.randomUUID().toString(), ipAddrAndPort, TransportTypeEnum.REGISTRY_HEART_BEAT.getType()));
                log.info("发送心跳包");
            }
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("BussnessHandler channelInactive");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("BussnessHandler channelActive");
        ctx.fireChannelActive();
    }

}
