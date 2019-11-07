package org.net.invoke;

import org.net.springextensible.beandefinition.ServiceBean;
import org.net.transport.ReferenceBeanExport;
import org.net.transport.ServiceBeanExport;

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

    private static List<ServiceBeanExport> serviceBeanExportList = new ArrayList<>();

    public static List<ServiceBeanExport> getServiceBeanExportList() {
        return serviceBeanExportList;
    }

    public static void addServiceBeanExport(Class<?> interfaceClass) {
        serviceBeanExportList.add(new ServiceBeanExport(interfaceClass));
    }

    private static Map<Class<?>, ServiceBean<?>> classServiceBeanMap = new ConcurrentHashMap<>();

    public static ServiceBean getServiceBean(Class<?> interfaceClass) {
        return classServiceBeanMap.get(interfaceClass);
    }

    private static List<ReferenceBeanExport> referenceBeanExportList = new ArrayList<>();

    public static List<ReferenceBeanExport> getReferenceBeanExportList() {
        return referenceBeanExportList;
    }

    public static void addReferenceBeanExport(Class<?> interfaceClass) {
        referenceBeanExportList.add(new ReferenceBeanExport(interfaceClass));
    }

}
