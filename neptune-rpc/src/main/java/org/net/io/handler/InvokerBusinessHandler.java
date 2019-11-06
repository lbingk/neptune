package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: neptune
 * @description: 处理具体的业务交接
 * @author: luobingkai
 * @create: 2019-11-06 10:43
 */
@Slf4j
public class InvokerBusinessHandler extends BaseBusinessHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
