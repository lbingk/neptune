package org.net.io.reference;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: neptune
 * @description: 管理 Channel
 * @author: LUOBINGKAI
 * @create: 2019-11-06 10:56
 */
@Slf4j
public class NettyChannel {

    /**
     * 存储所有的 Channel，便于统一管理：采用单一长连接，key为IP:PORT
     */
    public static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();


}
