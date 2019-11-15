package org.net.thread.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import org.net.io.handler.ChannelReadHandler;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-15 13:52
 */
@AllArgsConstructor
public class MessageReceiveTask implements Runnable {

    private ChannelHandlerContext ctx;
    private Object msg;

    @Override
    public void run() {
        try {
            new ChannelReadHandler().channelRead(ctx, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
