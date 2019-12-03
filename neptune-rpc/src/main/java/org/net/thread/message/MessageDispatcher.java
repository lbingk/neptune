package org.net.thread.message;

import io.netty.channel.ChannelHandlerContext;
import org.net.thread.Dispatcher;
import org.net.thread.MessageReceiveTask;
import org.net.thread.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * @program: neptune
 * @description: 只处理业务
 * @create: 2019-11-15 11:46
 */
public class MessageDispatcher implements Dispatcher {

    private ExecutorService executorService = ThreadPoolFactory.getExecutorService();

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
