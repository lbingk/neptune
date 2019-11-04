package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description 定义处理读的处理
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:09
 * @Version 1.0
 **/
public class BussnessHandler extends BaseBussnessHandler {

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

