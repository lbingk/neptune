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

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 16:56
 */
@Slf4j
public class InvokerNettyClient {

    private static Bootstrap bootstrap;

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
     * 建立连接，发送请求
     *
     * @param request
     * @param ipAddrAndPorts
     * @param timeout
     * @return
     * @throws Exception
     */
    public static Object invoke(Request request, String[] ipAddrAndPorts, int timeout) throws Exception {
        ChannelFuture channelFuture = bootstrap.connect(ipAddrAndPorts[0], Integer.parseInt(ipAddrAndPorts[1]));
        channelFuture.channel().writeAndFlush(request);
        return new DefaultFuture(request).get(timeout);
    }
}
