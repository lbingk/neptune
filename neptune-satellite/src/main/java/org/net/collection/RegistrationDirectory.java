package org.net.collection;

import lombok.extern.slf4j.Slf4j;
import org.net.springextensible.RegistrationBeanDef;
import org.net.util.SpringContextHolder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname RegistrationDirectory
 * @Description 用来存储服务的订阅以及注册
 * @Date 2019/11/2 0:54
 * @Created by admin
 */
@Slf4j
public class RegistrationDirectory {

    private static RegistrationDirectory instance = new RegistrationDirectory();

    private RegistrationDirectory() {
    }

    public static RegistrationDirectory getInstance() {
        return instance;
    }

    /**
     * 用 ConcurrentHashMap 来装载对应的注册服务:key值为暴露的服务
     **/
    public static Map<String, Map<String, String>> serviceBeanInfonMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的订阅服务:key值为需要订阅的服务
     **/
    public static Map<String, Set<String>> refBeanInfoMap = new ConcurrentHashMap<>();

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
    public void putServiceBeanInfo(String interfaceName, String ipAddrAndPort, String serviceBeanInfoStr) {
        Map<String, String> serviceProviderMap = serviceBeanInfonMap.get(interfaceName);
        if (serviceProviderMap == null) {
            // 新增的interfaceName,直接添加
            serviceProviderMap = new ConcurrentHashMap<>();
            serviceProviderMap.put(ipAddrAndPort, serviceBeanInfoStr);
        } else {
            // 新增的ipAddrAndPort
            serviceProviderMap.put(ipAddrAndPort, serviceBeanInfoStr);
        }
        serviceBeanInfonMap.put(interfaceName, serviceProviderMap);
    }

    /**
     * 刷新记录注册心跳时间
     *
     * @param ipAddr
     */
    public void refreshRegistryHostTime(String ipAddr) {
        registryHostTimeMap.put(ipAddr, System.currentTimeMillis());
    }

    /**
     * 刷新记录订阅心跳时间
     *
     * @param ipAddr
     */
    public void refreshSubscribeHostTime(String ipAddr) {
        subscribeHostTimeMap.put(ipAddr, System.currentTimeMillis());
    }


    /**
     * 自动刷新连接情况好的注册地址列表:50刷新一次
     */
    public static void refreshBeanInfonMap() {
        final int timeout = SpringContextHolder.getBean(RegistrationBeanDef.class).getTimeout();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                doRefreshRegistryHostTimeMap(timeout);
                doRefreshSubscribeHostTimeMap(timeout);
            }
        }, 0, 5 * 10 * 1000);
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
     * @param ipAddr
     */
    public static void putRefBeanInfo(String interfaceName, String ipAddr) {
        if (refBeanInfoMap.containsKey(interfaceName)) {
            refBeanInfoMap.get(interfaceName).add(ipAddr);
        } else {
            Set<String> ipAddrList = new HashSet<>();
            ipAddrList.add(ipAddr);
            refBeanInfoMap.put(interfaceName, ipAddrList);
        }
    }
}
