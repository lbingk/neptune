package org.net.io.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.net.api.MessageCoderAdapter;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.util.SpringContextHolder;

/**
 * @program: neptune
 * @description: 处理消费端与服务端的通讯交接
 * @author: LUOBINGKAI
 * @create: 2019-11-06 10:32
 */
@Slf4j
public class ReferenceHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        MessageCoderAdapter messageCoderAdapter = new MessageCoderAdapter(getProtocolType());
        // 解码与编码
        socketChannel.pipeline().addLast("Encoder", messageCoderAdapter.encode());
        socketChannel.pipeline().addLast("Decoder", messageCoderAdapter.decode());
        // 业务
        channelPipeline.addLast(new ReferenceBusinessHandler());
    }

    /**
     * 获取序列化协议类型
     *
     * @return
     */
    private String getProtocolType() {
        ProtocolBean referenceProtocol = SpringContextHolder.getBean("referenceProtocol");
        return referenceProtocol.getSerializeType();
    }
}
