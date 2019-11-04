package org.net.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.net.springextensible.RegistrationBeanDefinition;
import org.net.util.SpringContextHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
     * 用 ConcurrentHashMap 来装载对应的注册服务:key值为暴露的服务
     **/
    public static Map<String, Map<String, String>> serviceBeanInfoMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的订阅服务:key值为需要订阅的服务
     **/
    public static Map<String, Set<String>> referenceBeanInfoMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的注册服务机器心跳连接时的时间
     **/
    public static Map<String, Long> registryHostTimeMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的订阅服务机器心跳连接时的时间
     **/
    public static Map<String, Long> subscribeHostTimeMap = new ConcurrentHashMap<>();

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
        if (registryHostTimeMap.containsKey(ipAddrAndPort)) {
            registryHostTimeMap.put(ipAddrAndPort, System.currentTimeMillis());
        }
    }

    /**
     * 刷新记录订阅心跳时间
     *
     * @param ipAddrAndPort
     */
    private static void refreshSubscribeHostTime(String ipAddrAndPort) {
        if (subscribeHostTimeMap.containsKey(ipAddrAndPort)) {
            subscribeHostTimeMap.put(ipAddrAndPort, System.currentTimeMillis());
        }
    }


    /**
     * 自动刷新连接情况好的注册地址列表:50刷新一次
     */
    public static void refreshBeanInfonMap() {
        final int timeout = SpringContextHolder.getBean(RegistrationBeanDefinition.class).getTimeout();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                doRefreshRegistryHostTimeMap(timeout);
                doRefreshSubscribeHostTimeMap(timeout);
            }
        }, 5, 5 * 10 * 1000, TimeUnit.HOURS);
    }

    private static void doRefreshSubscribeHostTimeMap(int timeout) {
        if (!subscribeHostTimeMap.isEmpty()) {
            Iterator<Map.Entry<String, Long>> entryIterator = subscribeHostTimeMap.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, Long> entry = entryIterator.next();
                if ((System.currentTimeMillis() - entry.getValue()) > timeout) {
                    entryIterator.remove();
                    log.info("服务订阅客户端连接异常,移除:[{}].....", entry.getKey());
                }
            }
        }
    }

    private static void doRefreshRegistryHostTimeMap(int timeout) {
        if (!registryHostTimeMap.isEmpty()) {
            Iterator<Map.Entry<String, Long>> entryIterator = registryHostTimeMap.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, Long> entry = entryIterator.next();
                if ((System.currentTimeMillis() - entry.getValue()) > timeout) {
                    entryIterator.remove();
                    log.info("服务注册客户端连接异常,移除:[{}].....", entry.getKey());
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
}
