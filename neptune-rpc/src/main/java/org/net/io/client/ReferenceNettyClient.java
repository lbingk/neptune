package org.net.io.client;


import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.net.constant.TransportTypeEnum;
import org.net.io.handler.ReferenceHandler;
import org.net.io.reference.DefaultFuture;
import org.net.io.reference.NettyChannel;
import org.net.io.reference.Request;
import org.net.transport.RemoteTransporter;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: neptune
 * @description: 消费端调用服务端的netty客户端
 * @author: LUOBINGKAI
 * @create: 2019-11-02 16:56
 */
@Slf4j
public class ReferenceNettyClient {

    private Bootstrap bootstrap;
    private int timeout;
    private int retries;
    private String invokerDirectory;
    private Request request;

    public ReferenceNettyClient(Request request, String invokerDirectory, int timeout, int retries) {
        this.request = request;
        this.invokerDirectory = invokerDirectory;
        this.timeout = timeout;
        this.retries = retries;
    }


    /**
     * 为每一个连接客户端创建 Bootstrap 启动类
     */
    public void doOpen() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ReferenceHandler());

    }

    /**
     * 创建连接通道，在原来的 NettyChannel.CHANNEL_MAP 不存在连接通道的时候，按照配置的重试次数以及超时时间重试
     */
    public Channel dcConnect() throws Exception {
        // 获取旧的 channel
        Channel oldChannel = NettyChannel.CHANNEL_MAP.get(invokerDirectory);
        final int[] currentRetries = {0};
        if (oldChannel == null || !oldChannel.isActive()) {
            // 维护 CHANNEL_MAP
            NettyChannel.CHANNEL_MAP.remove(invokerDirectory);
            // 获取客户端地址
            String[] ipAddrAndPorts = StringUtils.split(invokerDirectory, ":");
            // 建立连接
            final ChannelFuture channelFuture = bootstrap.connect(ipAddrAndPorts[0], Integer.parseInt(ipAddrAndPorts[1]));
            channelFuture.addListener(new InvokerChannelFutureListener(channelFuture, invokerDirectory, timeout, retries, currentRetries));
        }
        for (; ; ) {
            Channel nowChannel = NettyChannel.CHANNEL_MAP.get(invokerDirectory);
            if (nowChannel != null && nowChannel.isActive()) {
                return nowChannel;
            }
            if (currentRetries[0] > retries) {
                break;
            }
        }
        throw new RuntimeException(getErrorMsg());
    }

    /**
     * 发送请求
     *
     * @param channel
     * @return
     * @throws Exception
     */
    public Object doSend(Channel channel) throws Exception {
        // 获取客户端地址
        RemoteTransporter remoteTransporter = RemoteTransporter.create(UUID.randomUUID().toString(), invokerDirectory, JSON.toJSONString(request), TransportTypeEnum.INVOKER.getType());
        channel.writeAndFlush(remoteTransporter);
        return new DefaultFuture(this.request, channel, timeout).get();
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
                            log.error(getErrorMsg());
                            channelFuture.cancel(true);
                        }
                        currentRetries[0]++;
                        try {
                            dcConnect();
                        } catch (Exception e) {
                            log.error(e.getStackTrace().toString());
                        }
                    }
                }, timeout, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 返回错误信息
     *
     * @return
     */
    private String getErrorMsg() {
        return "连接不正常，并超出设定的超时范围以及重试次数" + invokerDirectory + "，" + timeout + "，" + retries;
    }

}
