package org.net.collection;


import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Classname ChanneDirectory
 * @author: LUOBINGKAI
 * @Description Channe地址维护类
 * @Date 2019/11/5 0:28
 */
public class ChannelDirectory {
    private ChannelDirectory() {
    }

    /**
     * 最外key:为服务接口名，里面的key为IpAndPort
     */
    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 存储
     *
     * @param ipAddrAndPort
     * @param channel
     */
    public static void addChannel(String ipAddrAndPort, Channel channel) {
        channelMap.put(ipAddrAndPort, channel);
    }

    /**
     * 移除
     *
     * @param ipAddrAndPort
     */
    public static void remove(String ipAddrAndPort) {
        channelMap.remove(ipAddrAndPort);
    }

}
