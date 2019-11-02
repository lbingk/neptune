package org.net.io;

import com.alibaba.fastjson.JSONObject;
import org.net.RemoteTransporter;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.beandef.RegistryBeanDef;
import org.net.springextensible.beandef.ServiceBeanDef;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-02 16:56
 */
@Component
public class RegistryNettyClient extends BaseCommonClient implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String transportContent = JSONObject.toJSONString(SpringContextHolder.getBean(ServiceBeanDef.class));
        RemoteTransporter transporter = RemoteTransporter.create(UUID.randomUUID().toString(), transportContent, TransportTypeEnum.REGISTRY.getType());
        super.run(transporter, SpringContextHolder.getBean(RegistryBeanDef.class));
    }
}
