package org.net.invoke;

import org.net.springextensible.beandefinition.ServiceBean;
import org.net.transport.InvokerBeanExport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname InvokerBeanInfo
 * @author: LUOBINGKAI
 * @Description 存储暴露的服务
 * @Date 2019/11/2 23:39
 */
public class InvokerBeanInfo {

    private InvokerBeanInfo() {
    }

    private static List<InvokerBeanExport> invokerBeanExportList = new ArrayList<>();

    public static List<InvokerBeanExport> getInvokerBeanExportList() {
        return invokerBeanExportList;
    }

    public static void addInvokerBeanExport(Class<?> interfaceClass) {
        invokerBeanExportList.add(new InvokerBeanExport(interfaceClass));
    }

    private static Map<Class<?>, ServiceBean<?>> classServiceBeanMap = new ConcurrentHashMap<>();

    public static ServiceBean getServiceBean(Class<?> interfaceClass) {
        return classServiceBeanMap.get(interfaceClass);
    }

}
