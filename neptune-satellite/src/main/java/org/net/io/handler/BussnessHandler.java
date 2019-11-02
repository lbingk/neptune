package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.msgpack.MessagePack;
import org.net.RemoteTransporter;
import org.net.constant.ResultEnum;
import org.net.constant.TransportTypeEnum;

/**
 * @Classname BussnessHandler
 * @Description 自定义InboundHandler
 * @Date 2019/11/1 23:15
 * @Created by admin
 */
public class BussnessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);
        if (TransportTypeEnum.REGISTRY.getType().equals(readTransporter.getTransType())) {
            String readTransId = readTransporter.getTransId();
            ctx.writeAndFlush(RemoteTransporter.create(readTransId, "", ResultEnum.IS_SUCESS.getType()));
        }
        System.out.println(readTransporter);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
}
