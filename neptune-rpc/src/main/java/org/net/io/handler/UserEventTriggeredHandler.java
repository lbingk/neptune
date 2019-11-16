package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.transport.RemoteTransporter;

import java.util.UUID;

/**
 * @program: neptune
 * @description: 处理具体的业务交接
 * @author: LUOBINGKAI
 * @create: 2019-11-06 10:43
 */
@Slf4j
public class UserEventTriggeredHandler extends ChannelInboundHandlerAdapter {

    private String ipAddrAndPort;

    public UserEventTriggeredHandler(ProtocolBean protocolBean) {
        ipAddrAndPort = protocolBean.getIp() + ":" + protocolBean.getPort();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.writeAndFlush(RemoteTransporter.create(UUID.randomUUID().toString(), ipAddrAndPort, TransportTypeEnum.HEART_BEAT.getType()));
                log.info("发送心跳包");
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("BusinessHandler channelInactive");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("BusinessHandler channelActive");
        ctx.fireChannelActive();
    }

}
