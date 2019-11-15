package org.net.thread;

import io.netty.channel.ChannelHandlerContext;

/**
 * @program: neptune
 * @description: 线程模型接口
 * @author: luobingkai
 * @create: 2019-11-15 11:43
 */
public interface Dispatcher {
    /**
     * 获取消息
     */
    void receive();

    /**
     * 发送消息
     */
    void send();

    /**
     * 处理业务
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    void handler(ChannelHandlerContext ctx, Object msg) throws Exception;
}
