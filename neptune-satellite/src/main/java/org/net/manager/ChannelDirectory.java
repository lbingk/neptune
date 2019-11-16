package org.net.manager;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-05 11:01
 */
public class ChannelDirectory {

    private ChannelDirectory() {
    }

    /**
     * key为ipAddrAndPort
     */
    public static final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 存储Channel
     *
     * @param ipAddrAndPort
     * @param channel
     */
    public static void putChannel(String ipAddrAndPort, Channel channel) {
        channelMap.put(ipAddrAndPort, channel);
    }

    /**
     * 移除Channel
     *
     * @param ipAddrAndPort
     */
    public static void removeChannel(String ipAddrAndPort) {
        // 找出相关的订阅者
        channelMap.remove(ipAddrAndPort);
    }

    /**
     * 返回相关的Channel
     *
     * @param ipAddrAndPort
     * @return
     */
    public static Channel getChannel(String ipAddrAndPort) {
        return channelMap.get(ipAddrAndPort);
    }

}
