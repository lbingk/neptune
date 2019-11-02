package org.net.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.net.RemoteTransporter;
import org.net.handler.MsgpackDecoder;
import org.net.handler.MsgpackEncoder;
import org.net.io.handler.BussnessHandler;
import org.net.springextensible.beandef.RegistryBeanDef;

import java.util.concurrent.TimeUnit;

/**
 * @Description 抽取客户端相同的代码
 * @Author luobingkai
 * @Date 2019/7/23 12:13
 * @Version 1.0
 **/
public abstract class BaseCommonClient {
    private Bootstrap bootstrap = null;
    private Channel channel = null;
    private RemoteTransporter remoteTransporterProvider = null;
    private RegistryBeanDef registryBeanDefination = null;

    public void run(RemoteTransporter remoteTransporterProvider,RegistryBeanDef registryBeanDefination) {

        this.remoteTransporterProvider=remoteTransporterProvider;
        this.registryBeanDefination=registryBeanDefination;


        // 配置 netty 的启动引导类
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                // 心跳机制:4s周期没有发生事件，就触发userEventTriggered方法
                socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                socketChannel.pipeline().addLast("MessagePack encoder", new MsgpackEncoder());
                socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                socketChannel.pipeline().addLast("MessagePack Decoder", new MsgpackDecoder());
                // 调用自定义的看门狗重连处理类，主要处理正常连接以及异常重连
//                channelPipeline.addLast(ConnectionWatchDogHandler.createConnectionWatchDogHandler(bootstrap, BaseCommonClient.this));
                channelPipeline.addLast(new BussnessHandler());
            }
        });
        doConnect();
    }

    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture channelFuture = bootstrap.connect(registryBeanDefination.getIp(), registryBeanDefination.getPort());
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    Channel localChanel = future.channel();
                    // 发送消息给配置中心
                    if (remoteTransporterProvider != null) {
                        localChanel.writeAndFlush(remoteTransporterProvider);
                        System.out.println("注册消息" + remoteTransporterProvider.toString());
                    }
                    System.out.println("链路连接成功");
                } else {
                    future.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 5, TimeUnit.SECONDS);
                    System.out.println("正在尝试重连链路中......");
                }
            }
        });
    }

}
