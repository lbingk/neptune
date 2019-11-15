package org.net.thread.direct;

import io.netty.channel.ChannelHandlerContext;
import org.net.thread.Dispatcher;
import org.net.thread.MessageReceiveTask;

/**
 * @program: neptune
 * @description:  不开启线程池，直接由IO线程运算
 * @create: 2019-11-15 11:46
 */
public class DirectDispatcher implements Dispatcher {

    @Override
    public void receive() {

    }

    @Override
    public void send() {

    }

    @Override
    public void handler(ChannelHandlerContext ctx, Object msg) throws Exception {
       new MessageReceiveTask(ctx, msg).run();
    }
}
