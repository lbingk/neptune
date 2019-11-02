package org.net.springextensible.handler;

import org.net.springextensible.beandef.RegistryBeanDef;
import org.net.springextensible.beandef.ServiceBeanDef;
import org.net.springextensible.parser.RegistryBeanDefNamespaceParser;
import org.net.springextensible.parser.ServiceBeanDefNamespaceParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-02 14:51
 */
public class BeanDefNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefNamespaceParser(RegistryBeanDef.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefNamespaceParser(ServiceBeanDef.class));
    }
}
