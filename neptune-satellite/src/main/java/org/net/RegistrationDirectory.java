package org.net;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname RegistrationDirectory
 * @Description 用来存储服务的订阅以及注册
 * @Date 2019/11/2 0:54
 * @Created by admin
 */
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
    public static Map<String, List<String>> ServiceProviderBeanDefinationMap = new ConcurrentHashMap<>();

    /**
     * 用 ConcurrentHashMap 来装载对应的订阅服务:key值为需要订阅的服务
     **/
    public static Map<String, List<String>> ServiceConsumerBeanDefinationMap = new ConcurrentHashMap<>();

    /**
     * 提供注册服务方法
     *
     * @param interfaceName
     * @param serviceProviderStr
     */
    public static void putServiceProvider(String interfaceName, String serviceProviderStr) {
        if (ServiceProviderBeanDefinationMap.containsKey(interfaceName)) {
            List<String> serviceProviderStrList = ServiceProviderBeanDefinationMap.get(interfaceName);
            if (serviceProviderStrList.contains(serviceProviderStr)) {
                return;
            }
            serviceProviderStrList.add(serviceProviderStr);
        }
    }

    /**
     * 提供订阅服务方法
     *
     * @param interfaceName
     * @param serviceProviderStr
     */
    public static void putServiceConsumer(String interfaceName, String serviceProviderStr) {
        if (ServiceConsumerBeanDefinationMap.containsKey(interfaceName)) {
            List<String> serviceConsumerStrList = ServiceConsumerBeanDefinationMap.get(interfaceName);
            if (serviceConsumerStrList.contains(serviceProviderStr)) {
                return;
            }
            serviceConsumerStrList.add(serviceProviderStr);
        }
    }

    /**
     * 提供取消订阅服务方法
     *
     * @param ipAddrAndPort
     */
    public static void removeServiceConsumer(String ipAddrAndPort) {
        for (Map.Entry<String, List<String>> entry : ServiceConsumerBeanDefinationMap.entrySet()) {
            List<String> serviceConsumerStrList = entry.getValue();
            Iterator<String> iterator = serviceConsumerStrList.iterator();
            for (Iterator i = serviceConsumerStrList.iterator(); i.hasNext(); ) {
                if (StringUtils.split(i.next().toString(), "&")[0].equals(ipAddrAndPort)) {
                    iterator.remove();
                }
            }
            if (serviceConsumerStrList.isEmpty()) {
                ServiceConsumerBeanDefinationMap.clear();
            }
        }
    }

    /**
     * 提供取消注册服务方法
     *
     * @param ipAddrAndPort
     */
    public static void removeServiceProvider(String ipAddrAndPort) {
        for (Map.Entry<String, List<String>> entry : ServiceProviderBeanDefinationMap.entrySet()) {
            List<String> serviceProviderStrList = entry.getValue();
            Iterator<String> iterator = serviceProviderStrList.iterator();
            for (Iterator i = serviceProviderStrList.iterator(); i.hasNext(); ) {
                if (StringUtils.split(i.next().toString(), "&")[0].equals(ipAddrAndPort)) {
                    iterator.remove();
                }
            }
            if (serviceProviderStrList.isEmpty()) {
                ServiceProviderBeanDefinationMap.clear();
            }
        }
    }
}
