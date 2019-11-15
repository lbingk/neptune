package org.net.io.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.net.constant.TransportTypeEnum;
import org.net.io.common.Response;
import org.net.io.reference.DefaultFuture;
import org.net.transport.RemoteTransporter;

/**
 * @program: neptune
 * @description: 处理具体的业务交接
 * @author: LUOBINGKAI
 * @create: 2019-11-06 10:43
 */
@Slf4j
public class ReferenceBusinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RemoteTransporter readTransporter = MessagePack.unpack(MessagePack.pack(msg), RemoteTransporter.class);

        if (TransportTypeEnum.INVOKER_RESULT.getType().equals(readTransporter.getTransType())) {
            //返回的远程调用的结果
            Response response = JSON.parseObject(readTransporter.getTransContent(), Response.class);
            DefaultFuture defaultFuture = DefaultFuture.DEFAULT_FUTURE_MAP.get(response.getMid());
            if (defaultFuture != null) {
                defaultFuture.receive(response);
            }
        }
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
