package org.net.io;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.net.springextensible.invoke.InvokerBeanInfo;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.springextensible.beandefinition.RegistryBean;
import org.net.transport.RemoteTransporter;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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
        ProtocolBean protocolBeanDef = SpringContextHolder.getBean(ProtocolBean.class);
        String ipAddsAndPort = protocolBeanDef.getIp() + ":" + protocolBeanDef.getPort();
        String transportContent = JSON.toJSONString(InvokerBeanInfo.getInvokerBeanExportList());
        RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), ipAddsAndPort, transportContent, TransportTypeEnum.REGISTRY.getType());
        super.run(transporter, SpringContextHolder.getBean(RegistryBean.class));
    }
}
