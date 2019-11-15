package org.net.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.net.thread.Dispatcher;
import org.net.thread.DispatcherAdapter;

/**
 * @program: neptune
 * @description: 处理具体的业务交接
 * @author: LUOBINGKAI
 * @create: 2019-11-06 10:43
 */
@Slf4j
public class ServiceBusinessHandler extends ChannelInboundHandlerAdapter {

    private Dispatcher dispatcher = DispatcherAdapter.getInstance().getDispatcher();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        dispatcher.handler(ctx, msg);
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
