package org.net.io.reference;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.net.handler.MsgpackDecoder;
import org.net.handler.MsgpackEncoder;
import org.net.io.handler.BussnessHandler;
import org.net.transport.RemoteTransporter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 16:56
 */
@Slf4j
public class InvokerNettyClient {

    private static Bootstrap bootstrap = null;
    private Channel channel = null;
    private RemoteTransporter remoteTransporter = null;

    static {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                socketChannel.pipeline().addLast("MessagePack encoder", new MsgpackEncoder());
                socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                socketChannel.pipeline().addLast("MessagePack Decoder", new MsgpackDecoder());
                channelPipeline.addLast(new BussnessHandler());
            }
        });
    }

    /**
     * netty远程调用客户端
     *
     * @param interfaceClass
     * @param method
     * @param args
     * @return
     */
    public void invoke(Class<?> interfaceClass, Method method, Object[] args) throws Exception {
        String invokerDirectory = InvokerDirectory.getRandom(interfaceClass.getName());
        if (StringUtils.isEmpty(invokerDirectory)) {
            throw new Exception("不存在此服务提供者：" + interfaceClass);
        }
        String[] ipAddrAndPorts = StringUtils.split(invokerDirectory, ":");
        ChannelFuture channelFuture = bootstrap.connect(ipAddrAndPorts[0], Integer.parseInt(ipAddrAndPorts[1]));


        method.invoke(interfaceClass, args);
    }
}
