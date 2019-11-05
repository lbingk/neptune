package org.net.invoke;

import org.net.transport.InvokerBeanExport;

import java.util.ArrayList;
import java.util.List;

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
}
