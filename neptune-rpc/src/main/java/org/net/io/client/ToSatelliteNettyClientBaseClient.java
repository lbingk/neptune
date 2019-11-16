package org.net.io.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.net.api.msgpack.MsgpackDecoder;
import org.net.api.msgpack.MsgpackEncoder;
import org.net.constant.TransportTypeEnum;
import org.net.invoke.InvokerBeanInfo;
import org.net.io.handler.ConnectionWatchDogHandler;
import org.net.io.handler.ToSatelliteBusinessHandler;
import org.net.io.handler.UserEventTriggeredHandler;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.springextensible.beandefinition.RegistryBean;
import org.net.transport.RemoteTransporter;
import org.net.util.SpringContextHolder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description 抽取客户端相同的代码
 * @Author LUOBINGKAI
 * @Date 2019/7/23 12:13
 * @Version 1.0
 **/
@Slf4j
public abstract class ToSatelliteNettyClientBaseClient {

    private Bootstrap bootstrap = null;
    private Channel channel = null;
    private RemoteTransporter registerRemoteTransporter = null;
    private RemoteTransporter subscribeRemoteTransporter = null;
    private RegistryBean registryBean = null;
    private ProtocolBean serviceProtocol = null;
    private ProtocolBean referenceProtocol = null;

    public void run(RegistryBean registryBean) {
        doInit(registryBean);
        doOpen();
        doConnect();
    }

    /**
     * 初始化
     *
     * @param registryBean
     */
    private void doInit(RegistryBean registryBean) {
        this.registryBean = registryBean;

        if (!InvokerBeanInfo.getServiceBeanExportList().isEmpty()) {
            serviceProtocol = SpringContextHolder.getBean("serviceProtocol");
            String serviceIpAddsAndPort = serviceProtocol.getIp() + ":" + serviceProtocol.getPort();
            String registerTransportContent = JSON.toJSONString(InvokerBeanInfo.getServiceBeanExportList());
            registerRemoteTransporter = RemoteTransporter.create(UUID.randomUUID().toString(), serviceIpAddsAndPort, registerTransportContent, TransportTypeEnum.REGISTRY.getType());
        }

        if (!InvokerBeanInfo.getReferenceBeanExportList().isEmpty()) {
            referenceProtocol = SpringContextHolder.getBean("referenceProtocol");
            String referenceIpAddsAndPort = referenceProtocol.getIp() + ":" + referenceProtocol.getPort();
            String subscribeTransportContent = JSON.toJSONString(InvokerBeanInfo.getReferenceBeanExportList());
            subscribeRemoteTransporter = RemoteTransporter.create(UUID.randomUUID().toString(), referenceIpAddsAndPort, subscribeTransportContent, TransportTypeEnum.SUBSCRIBE.getType());
        }
    }

    /**
     * 启动类
     */
    private void doOpen() {
        // 配置 netty 的启动引导类
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ToSatelliteHandler());
    }

    /**
     * 配置handler
     */
    class ToSatelliteHandler extends ChannelInitializer {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline channelPipeline = ch.pipeline();
            // 心跳机制:4s周期没有发生事件，就触发userEventTriggered方法
            ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
            ch.pipeline().addLast("encoder", new MsgpackEncoder());
            ch.pipeline().addLast("decoder", new MsgpackDecoder());
            // 调用自定义的看门狗重连处理类，主要处理异常重连
            channelPipeline.addLast(ConnectionWatchDogHandler.createConnectionWatchDogHandler(bootstrap, ToSatelliteNettyClientBaseClient.this));
            channelPipeline.addLast(new ToSatelliteBusinessHandler());
            // 心跳
            if (serviceProtocol != null) {
                channelPipeline.addLast(new UserEventTriggeredHandler(serviceProtocol));
            }
            if (referenceProtocol != null) {
                channelPipeline.addLast(new UserEventTriggeredHandler(referenceProtocol));
            }
        }
    }

    /**
     * 建立连接
     */
    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture channelFuture = bootstrap.connect(registryBean.getIp(), registryBean.getPort());
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    Channel localChanel = future.channel();
                    if (registerRemoteTransporter != null) {
                        localChanel.writeAndFlush(registerRemoteTransporter);
                        log.info("注册：[{}]", registerRemoteTransporter);
                    }
                    if (subscribeRemoteTransporter != null) {
                        localChanel.writeAndFlush(subscribeRemoteTransporter);
                        log.info("订阅：[{}]", subscribeRemoteTransporter.getTransType());
                    }
                    log.info("链路连接成功");
                } else {
                    future.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 5, TimeUnit.SECONDS);
                    log.info("正在尝试重连链路中......");
                }
            }
        });
    }

}
