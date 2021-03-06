package org.net.io.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import org.net.io.client.ToSatelliteNettyClientBaseClient;


/**
 * @Description 定义看门狗重连处理类，当检查到断线时即可触发
 * @Author LUOBINGKAI
 * @Date 2019/7/21 17:08
 * @Version 1.0
 **/

@Setter
@Getter
public class ConnectionWatchDogHandler extends ChannelInboundHandlerAdapter {

    private Bootstrap bootstrap = null;
    private ToSatelliteNettyClientBaseClient client = null;

    public static ConnectionWatchDogHandler createConnectionWatchDogHandler(Bootstrap _bootstrap, ToSatelliteNettyClientBaseClient _client) {
        ConnectionWatchDogHandler connectionWatchDogHandler = new ConnectionWatchDogHandler();
        connectionWatchDogHandler.setBootstrap(_bootstrap);
        connectionWatchDogHandler.setClient(_client);
        return connectionWatchDogHandler;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        client.doConnect();
        ctx.fireChannelInactive();
    }

}
