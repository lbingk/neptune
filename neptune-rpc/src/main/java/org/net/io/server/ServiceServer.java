package org.net.io.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.net.io.handler.ServiceHandler;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: neptune
 * @description: 服务提供者的服务器
 * @author: LUOBINGKAI
 * @create: 2019-11-05 19:58
 */
@Component
public class ServiceServer implements ApplicationListener<ContextRefreshedEvent> {
    public static ProtocolBean protocolBean = null;
    public static ServerBootstrap bootstrap;
    public static EventLoopGroup bossGroup;
    public static EventLoopGroup workerGroup;
    /**
     * <IP:PORT,CHANNEL>
     */
    public static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        protocolBean = SpringContextHolder.getBean("serviceProtocol");
        if (protocolBean == null) {
            return;
        }
        doOpen();
        doConnect();
    }

    /**
     * 创建启动类：ServerBootstrap
     */
    private void doOpen() {
        // 创建Boss：作用于客户端的连接
        bossGroup = new NioEventLoopGroup();
        // 创建woker：作用于迭代器可用的连接
        workerGroup = new NioEventLoopGroup();
        // 创建 ServerBootstrap：启动类
        bootstrap = new ServerBootstrap();
        // 配置参数
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                childHandler(new ServiceHandler()).option(ChannelOption.SO_BACKLOG, 2048 * 2048 * 2048);
    }

    /**
     * 打开连接端口
     */
    private void doConnect() {
        try {
            bootstrap.bind(protocolBean.getPort()).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
