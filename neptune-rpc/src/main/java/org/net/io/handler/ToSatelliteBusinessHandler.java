package org.net.io.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.constant.TransportTypeEnum;
import org.net.io.reference.InvokerDirectory;
import org.net.transport.RemoteTransporter;
import org.net.transport.SubscribeResult;

/**
 * @Description 定义处理读的处理
 * @Author LUOBINGKAI
 * @Date 2019/7/20 3:09
 * @Version 1.0
 **/
@Slf4j
public class ToSatelliteBusinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);

        if (TransportTypeEnum.SUBSCRIBE_RESULT.getType().equals(readTransporter.getTransType())) {
            //返回的订阅的地址列表信息
            SubscribeResult subscribeResult = JSON.parseObject(readTransporter.getTransContent(), SubscribeResult.class);
            InvokerDirectory.addInvoker(subscribeResult);
        }

        log.info(readTransporter.toString());
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

