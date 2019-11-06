package org.net.io.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.net.handler.MsgpackDecoder;
import org.net.handler.MsgpackEncoder;

/**
 * @program: neptune
 * @description: 处理消费端与服务端的通讯交接
 * @author: luobingkai
 * @create: 2019-11-06 10:32
 */
@Slf4j
public class InvokerHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // 解码与编码
        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
        socketChannel.pipeline().addLast("MessagePack encoder", new MsgpackEncoder());
        socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
        socketChannel.pipeline().addLast("MessagePack Decoder", new MsgpackDecoder());
        // 业务
        channelPipeline.addLast(new InvokerBusinessHandler());
    }
}
