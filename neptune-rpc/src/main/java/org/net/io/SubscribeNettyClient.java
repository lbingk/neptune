package org.net.io;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.net.collection.RefBeanInfo;
import org.net.collection.ServiceBeanInfo;
import org.net.constant.TransportTypeEnum;
import org.net.springextensible.beandef.ProtocolBeanDef;
import org.net.springextensible.beandef.RegistryBeanDef;
import org.net.transport.RemoteTransporter;
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
public class SubscribeNettyClient extends BaseCommonClient implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    }
}
