package org.net.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.RegistrationBeanDefinition;
import org.net.transport.RemoteTransporter;
import org.net.util.SpringContextHolder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Classname RegistrationDirectory
 * @Description 用来存储服务的订阅以及注册
 * @Date 2019/11/2 0:54
 * @Created by admin
 */
@Slf4j
public class RegistrationDirectory {

    private RegistrationDirectory() {
    }


    /**
     * 用 ConcurrentHashMap 来装载对应的注册服务:key值为暴露的服务接口名,InvokerBeanExport的 JSON 字符串
     **/
    public static Map<String, Map<String, String>> serviceBeanInfoMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的订阅服务:key值为需要订阅的服务接口名
     **/
    public static Map<String, Set<String>> referenceBeanInfoMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的注册服务机器心跳连接时的时间
     **/
    private static Map<String, Long> registryHostTimeMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的订阅服务机器心跳连接时的时间
     **/
    private static Map<String, Long> subscribeHostTimeMap = new ConcurrentHashMap<>();

    /**
     * 提供注册服务
     *
     * @param interfaceName
     * @param serviceBeanInfoStr
     */
    public static void putInvokerBeanInfo(String interfaceName, String ipAddrAndPort, String serviceBeanInfoStr) {
        Map<String, String> serviceProviderMap = serviceBeanInfoMap.get(interfaceName);
        if (serviceProviderMap == null) {
            // 新增的interfaceName,直接添加
            serviceProviderMap = new ConcurrentHashMap<>();
            serviceProviderMap.put(ipAddrAndPort, serviceBeanInfoStr);
        } else {
            // 新增的ipAddrAndPort
            serviceProviderMap.put(ipAddrAndPort, serviceBeanInfoStr);
        }
        serviceBeanInfoMap.put(interfaceName, serviceProviderMap);
        registryHostTimeMap.put(ipAddrAndPort, System.currentTimeMillis());
    }

    /**
     * 返回订阅的结果
     *
     * @param interfaceName
     * @return
     */
    public static Map<String, String> subscribeResult(String interfaceName) {
        return serviceBeanInfoMap.get(interfaceName);
    }


    /**
     * 刷新记录心跳时间
     *
     * @param ipAddrAndPort
     */
    public static void refreshHostTime(String ipAddrAndPort) {
        refreshRegistryHostTime(ipAddrAndPort);
        refreshSubscribeHostTime(ipAddrAndPort);
    }

    /**
     * 刷新记录注册心跳时间
     *
     * @param ipAddrAndPort
     */
    private static void refreshRegistryHostTime(String ipAddrAndPort) {
        registryHostTimeMap.put(ipAddrAndPort, System.currentTimeMillis());
    }

    /**
     * 刷新记录订阅心跳时间
     *
     * @param ipAddrAndPort
     */
    private static void refreshSubscribeHostTime(String ipAddrAndPort) {
        subscribeHostTimeMap.put(ipAddrAndPort, System.currentTimeMillis());
    }

    /**
     * 自动刷新连接情况好的注册地址列表:50刷新一次
     */
    public static void refreshBeanInfoMap() {
        final int timeout = SpringContextHolder.getBean(RegistrationBeanDefinition.class).getTimeout();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("refreshBeanInfoMap-schedule-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                doRefreshRegistryHostTimeMap(timeout);
                doRefreshSubscribeHostTimeMap(timeout);
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    public static void refreshBeanInfo() {
        final int timeout = SpringContextHolder.getBean(RegistrationBeanDefinition.class).getTimeout();
        doRefreshRegistryHostTimeMap(timeout);
        doRefreshSubscribeHostTimeMap(timeout);
    }


    /**
     * 刷新订阅客户端
     *
     * @param timeout
     */
    private static void doRefreshSubscribeHostTimeMap(int timeout) {
        if (!subscribeHostTimeMap.isEmpty()) {
            Iterator<Map.Entry<String, Long>> entryIterator = subscribeHostTimeMap.entrySet().iterator();
            Collection<Set<String>> values = referenceBeanInfoMap.values();
            Iterator<Set<String>> referenceBeanInfoMapIterator = values.iterator();

            while (entryIterator.hasNext()) {
                Map.Entry<String, Long> entry = entryIterator.next();
                if ((System.currentTimeMillis() - entry.getValue()) > timeout) {
                    while (referenceBeanInfoMapIterator.hasNext()) {
                        Set<String> sets = referenceBeanInfoMapIterator.next();
                        sets.remove(entry.getKey());
                    }
                    entryIterator.remove();
                    // 同时需要维护Chanel
                    ChannelDirectory.removeChannel(entry.getKey());
                    log.info("服务订阅客户端连接异常,移除:[{}].....", entry.getKey());
                }
            }
        }
    }

    /**
     * 刷新注册客户端
     *
     * @param timeout
     */
    private static void doRefreshRegistryHostTimeMap(int timeout) {
        if (!registryHostTimeMap.isEmpty()) {
            Iterator<Map.Entry<String, Long>> registryHostTimeMapIterator = registryHostTimeMap.entrySet().iterator();
            Iterator<Map<String, String>> serviceBeanInfoMapIterator = serviceBeanInfoMap.values().iterator();
            Set<String> serviceBeanInfoSets = serviceBeanInfoMap.keySet();

            String localIpAddrAndPort = null;
            try {
                RegistrationBeanDefinition registrationBeanDefinition = SpringContextHolder.getBean(RegistrationBeanDefinition.class);
                localIpAddrAndPort = InetAddress.getLocalHost().getHostAddress() + ":" + registrationBeanDefinition.getPort();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            List<String> pushInterfaceNameList = new ArrayList<>();

            while (registryHostTimeMapIterator.hasNext()) {
                Map.Entry<String, Long> entry = registryHostTimeMapIterator.next();
                if ((System.currentTimeMillis() - entry.getValue()) > timeout) {
                    String ipAddrAndPort = entry.getKey();
                    while (serviceBeanInfoMapIterator.hasNext()) {
                        Map<String, String> map = serviceBeanInfoMapIterator.next();
                        // 收集需要更新注册地址的interface
                        for (String interfaceName : serviceBeanInfoSets) {
                            if (map.equals(serviceBeanInfoMap.get(interfaceName))) {
                                // 推送服务
                                pushInterfaceNameList.add(interfaceName);
                            }
                        }
                        map.remove(ipAddrAndPort);
                    }
                    registryHostTimeMapIterator.remove();
                    // 推送服务
                    for (String interfaceName : pushInterfaceNameList) {
                        Set<String> referenceAddrSets = referenceBeanInfoMap.get(interfaceName);
                        for (String referenceAddr : referenceAddrSets) {
                            Channel channel = ChannelDirectory.getChannel(referenceAddr);
                            if (channel == null) {
                                continue;
                            }
                            try {
                                RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), localIpAddrAndPort, TransportTypeEnum.SUBSCRIBE_RESULT.getType());
                                log.info("transporter:[{}]", transporter);
                                channel.writeAndFlush(transporter);
                            } finally {
                                continue;
                            }
                        }
                    }
                    // 同时需要维护Chanel
                    ChannelDirectory.removeChannel(ipAddrAndPort);
                    log.info("服务注册客户端连接异常,移除:[{}].....", ipAddrAndPort);
                }
            }
        }
    }

    /**
     * 提供订阅地址存储
     *
     * @param interfaceName
     * @param ipAddrAndPort
     */
    public static void putReferenceBeanInfo(String interfaceName, String ipAddrAndPort) {
        if (referenceBeanInfoMap.containsKey(interfaceName)) {
            referenceBeanInfoMap.get(interfaceName).add(ipAddrAndPort);
        } else {
            Set<String> ipAddrAndPortList = new HashSet<>();
            ipAddrAndPortList.add(ipAddrAndPort);
            referenceBeanInfoMap.put(interfaceName, ipAddrAndPortList);
        }
    }

    /**
     * 推送服务
     *
     * @param interfaceClassName
     */
    public static void pushService(String interfaceClassName) {
        if (serviceBeanInfoMap.containsKey(interfaceClassName) && referenceBeanInfoMap.containsKey(interfaceClassName)) {
            // 获取该服务下的注册信息：ipAddrAndPort,serviceBeanInfo
            Map<String, String> serviceMap = serviceBeanInfoMap.get(interfaceClassName);
            // 获取该服务下的订阅者
            Set<String> referenceAddrSets = referenceBeanInfoMap.get(interfaceClassName);
            // 拼接ipAndPort
            RegistrationBeanDefinition registrationBeanDefinition = SpringContextHolder.getBean(RegistrationBeanDefinition.class);
            String localIpAddrAndPort = null;
            try {
                localIpAddrAndPort = InetAddress.getLocalHost().getHostAddress() + ":" + registrationBeanDefinition.getPort();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            // 推送服务，按照服务来
            for (String referenceAddr : referenceAddrSets) {
                if (serviceMap.containsKey(referenceAddr)) {
                    String serviceBeanStr = serviceMap.get(referenceAddr);
                    Channel channel = ChannelDirectory.getChannel(referenceAddr);
                    if (channel == null) {
                        continue;
                    }
                    try {
                        RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), localIpAddrAndPort, serviceBeanStr, TransportTypeEnum.SUBSCRIBE_RESULT.getType());
                        log.info("transporter:[{}]", transporter);
                        channel.writeAndFlush(transporter);
                    } finally {
                        continue;
                    }
                }
            }
        }
    }
}
