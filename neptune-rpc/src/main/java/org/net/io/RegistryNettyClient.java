package org.net.io;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.net.springextensible.beandef.ProtocolBeanDef;
import org.net.transport.RemoteTransporter;
import org.net.collection.ServiceBeanInfo;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.beandef.RegistryBeanDef;
import org.net.transport.ServiceBeanExport;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 16:56
 */
@Slf4j
@Component
public class RegistryNettyClient extends BaseCommonClient implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<String> serviceBeanDefinationClassNameList = ServiceBeanInfo.getSingleton().getServiceBeanDefinationClassNameList();
        List<ServiceBeanExport> serviceBeanExportList = new ArrayList<>();
        for (String className : serviceBeanDefinationClassNameList) {
            try {
                Class<?> classInterface = Class.forName(className);
                StringBuilder sb = new StringBuilder(16);
                Method[] methods = classInterface.getMethods();
                for (int i = methods.length - 1; i >= 0; i--) {
                    sb.append(methods[i]);
                    if (i != 0) {
                        sb.append("&");
                    }
                }
                ServiceBeanExport serviceBeanExport = new ServiceBeanExport(classInterface.toString(), sb);
                serviceBeanExportList.add(serviceBeanExport);
            } catch (ClassNotFoundException e) {
                log.error(e.getStackTrace().toString());
            }
        }

        ProtocolBeanDef protocolBeanDef = SpringContextHolder.getBean(ProtocolBeanDef.class);
        String ipAddsAndPort = protocolBeanDef.getIp() + ":" + protocolBeanDef.getPort();
        String transportContent = JSON.toJSONString(serviceBeanExportList);
        RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), ipAddsAndPort, transportContent, TransportTypeEnum.REGISTRY.getType());
        super.run(transporter, SpringContextHolder.getBean(RegistryBeanDef.class));
    }
}
