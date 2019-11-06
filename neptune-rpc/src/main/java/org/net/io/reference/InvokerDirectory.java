package org.net.io.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-05 16:12
 */
@Setter
@Getter
@ToString
public class InvokerDirectory {

    private InvokerDirectory() {
    }

    /**
     * 存在注册中心返回的地址列表信息：key为interfaceName,value为地址列表
     */
    private static Map<String, Set<String>> invokerDirectoryMap = new ConcurrentHashMap<>();

    /**
     * 保存服务地址信息
     *
     * @param interfaceName
     * @param ipAddrAndPort
     */
    public static void addInvoker(String interfaceName, String ipAddrAndPort) {
        if (invokerDirectoryMap.containsKey(interfaceName)) {
            invokerDirectoryMap.get(interfaceName).add(ipAddrAndPort);
        } else {
            Set<String> invokerDirectorySet = new CopyOnWriteArraySet<>();
            invokerDirectorySet.add(ipAddrAndPort);
            invokerDirectoryMap.put(interfaceName, invokerDirectorySet);
        }
    }

    /**
     * 随机算法获取地址
     *
     * @param interfaceName
     * @return
     */
    public static String getRandom(String interfaceName) {
        Set<String> invokerDirectorySet = invokerDirectoryMap.get(interfaceName);
        if (invokerDirectorySet == null) {
            return null;
        }
        Random random = new Random();
        int rn = random.nextInt(invokerDirectorySet.size());
        int i = 0;
        for (String e : invokerDirectorySet) {
            if (i == rn) {
                return e;
            }
            i++;
        }
        return null;
    }

}
