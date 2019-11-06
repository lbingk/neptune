package org.net.io.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.net.io.handler.InvokerHandler;
import org.net.io.reference.DefaultFuture;
import org.net.io.reference.NettyChannel;
import org.net.io.reference.Request;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @program: neptune
 * @description: 消费端调用服务端的netty客户端
 * @author: LUOBINGKAI
 * @create: 2019-11-02 16:56
 */
@Slf4j
public class InvokerNettyClient {

    private Bootstrap bootstrap;

    /**
     * 为每一个连接客户端创建 Bootstrap 启动类
     */
    public void doOpen() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new InvokerHandler());
    }

    /**
     * 创建连接通道，在原来的 NettyChannel.CHANNEL_MAP 不存在连接通道的时候，按照配置的重试次数以及超时时间重试
     *
     * @param invokerDirectory
     * @param timeout
     * @param retries
     */
    public Channel dcConnect(final String invokerDirectory, final int timeout, final int retries) {
        // 获取旧的 channel
        Channel oldChannel = NettyChannel.CHANNEL_MAP.get(invokerDirectory);
        if (oldChannel == null || !oldChannel.isActive()) {
            // 维护 CHANNEL_MAP
            NettyChannel.CHANNEL_MAP.remove(invokerDirectory);
            // 获取客户端地址
            String[] ipAddrAndPorts = StringUtils.split(invokerDirectory, ":");
            // 建立连接
            final int[] currentRetries = {0};
            final ChannelFuture channelFuture = bootstrap.connect(ipAddrAndPorts[0], Integer.parseInt(ipAddrAndPorts[1]));
            channelFuture.addListener(new InvokerChannelFutureListener(channelFuture, invokerDirectory, timeout, retries, currentRetries));
        }
        return NettyChannel.CHANNEL_MAP.get(invokerDirectory);
    }

    /**
     * 发送请求
     *
     * @param request
     * @param channel
     * @param timeout
     * @param retries
     * @return
     * @throws Exception
     */
    public Object doSend(Request request, Channel channel, final int timeout, final int retries) throws Exception {
        channel.writeAndFlush(request);
        return new DefaultFuture(request,channel).get(timeout);
    }


    /**
     * 内部类定义:ChannelFutureListener
     */
    class InvokerChannelFutureListener implements ChannelFutureListener {
        private String invokerDirectory;
        private int timeout;
        private int retries;
        private ChannelFuture channelFuture;
        private int[] currentRetries;

        InvokerChannelFutureListener(ChannelFuture channelFuture, final String invokerDirectory, final int timeout, final int retries, int[] currentRetries) {
            this.invokerDirectory = invokerDirectory;
            this.timeout = timeout;
            this.retries = retries;
            this.channelFuture = channelFuture;
            this.currentRetries = currentRetries;
        }

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                Channel newChannel = future.channel();
                // 维护 CHANNEL_MAP
                NettyChannel.CHANNEL_MAP.put(invokerDirectory, newChannel);
                channelFuture.removeListener(this);
            } else {
                future.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (currentRetries[0] > retries) {
                            log.error("连接不正常，并超出设定的超时范围以及重试次数" + invokerDirectory + "，" + timeout + "，" + retries);
                            channelFuture.cancel(true);
                        }
                        currentRetries[0]++;
                        dcConnect(invokerDirectory, timeout, retries);
                    }
                }, timeout, TimeUnit.SECONDS);
            }
        }
    }

}
