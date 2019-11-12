package org.net.io.client;

import lombok.extern.slf4j.Slf4j;
import org.net.springextensible.beandefinition.RegistryBean;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 16:56
 */
@Slf4j
@Component
public class ToSatelliteNettyClient extends ToSatelliteNettyClientBaseClient implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        RegistryBean registryBean = SpringContextHolder.getBean(RegistryBean.class);
        if (registryBean != null) {
            super.run(registryBean);
        }
    }
}
