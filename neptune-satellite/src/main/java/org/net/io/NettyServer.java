package org.net.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.net.manager.RegistrationDirectory;
import org.net.handler.MsgpackDecoder;
import org.net.handler.MsgpackEncoder;
import org.net.io.handler.BusinessHandler;
import org.net.springextensible.RegistrationBeanDefinition;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Classname NettyServer
 * @Description
 * @Date 2019/11/1 22:49
 * @Created by admin
 */
@Component
public class NettyServer implements ApplicationListener<ContextRefreshedEvent> {

    public void run() {
        // 获取配置的参数：port 以及 timeout
        RegistrationBeanDefinition registrationBeanDefinition = SpringContextHolder.getBean(RegistrationBeanDefinition.class);
        // 创建Boss：作用于客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 创建woker：作用于迭代器可用的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建 ServerBootstrap：启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 配置参数
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 解码，编码以及业务逻辑处理链，10秒没有发生写事件，就触发userEventTriggered
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        socketChannel.pipeline().addLast(new IdleStateHandler(0, 10, 0, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        socketChannel.pipeline().addLast("MessagePack Decoder", new MsgpackDecoder());
                        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast("MessagePack encoder", new MsgpackEncoder());
                        pipeline.addLast(new BusinessHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 2048 * 2048 * 2048);
        try {
             bootstrap.bind(registrationBeanDefinition.getPort()).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 异常情况优雅关闭
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.run();
        RegistrationDirectory.refreshBeanInfoMap();
    }
}
