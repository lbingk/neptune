package org.net.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ServiceBeanInfo
 * @author: LUOBINGKAI
 * @Description 存储暴露的服务
 * @Date 2019/11/2 23:39
 */
public class ServiceBeanInfo {
    private static ServiceBeanInfo singleton = new ServiceBeanInfo();

    private ServiceBeanInfo() {
    }

    public static ServiceBeanInfo getSingleton() {
        return singleton;
    }

    private List<String> serviceBeanDefinationClassNameList = new ArrayList<>();

    public List<String> getServiceBeanDefinationClassNameList() {
        return serviceBeanDefinationClassNameList;
    }

    public void addServiceBeanDefinationClassName(String serviceBeanDefinationClassName)  {
        serviceBeanDefinationClassNameList.add(serviceBeanDefinationClassName);
    }
}
