package org.net.thread.message;

import io.netty.channel.ChannelHandlerContext;
import org.net.thread.Dispatcher;
import org.net.thread.TreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * @program: neptune
 * @description: 自定义的线程池:只处理业务，不处理IO事件
 * @create: 2019-11-15 11:46
 */
public class MessageDispatcher implements Dispatcher {

    private ExecutorService executorService = TreadPoolFactory.getExecutorService();

    @Override
    public void receive() {

    }

    @Override
    public void send() {

    }

    @Override
    public void handler(ChannelHandlerContext ctx, Object msg) throws Exception {
        executorService.submit(new MessageReceiveTask(ctx, msg));
    }
}
