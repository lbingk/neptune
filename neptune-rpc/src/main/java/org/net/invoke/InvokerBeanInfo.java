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

    private static Map<String, ServiceBean<?>> serviceBeanMap = new ConcurrentHashMap<>();

    public static void putServiceBean(String interfaceClassName, ServiceBean serviceBean) {
        serviceBeanMap.put(interfaceClassName, serviceBean);
    }

    public static ServiceBean getServiceBean(String interfaceClassName) {
        return serviceBeanMap.get(interfaceClassName);
    }

    private static List<ReferenceBeanExport> referenceBeanExportList = new ArrayList<>();

    public static List<ReferenceBeanExport> getReferenceBeanExportList() {
        return referenceBeanExportList;
    }

    public static void addReferenceBeanExport(Class<?> interfaceClass) {
        referenceBeanExportList.add(new ReferenceBeanExport(interfaceClass));
    }

}
