package org.net.springextensible.handler;

import org.net.springextensible.beandef.ProtocolBeanDef;
import org.net.springextensible.beandef.RefBeanDef;
import org.net.springextensible.beandef.RegistryBeanDef;
import org.net.springextensible.beandef.ServiceBeanDef;
import org.net.springextensible.parser.ProtocolBeanDefNamespaceParser;
import org.net.springextensible.parser.RefBeanDefNamespaceParser;
import org.net.springextensible.parser.RegistryBeanDefNamespaceParser;
import org.net.springextensible.parser.ServiceBeanDefNamespaceParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 14:51
 */
public class BeanDefNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefNamespaceParser(RegistryBeanDef.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefNamespaceParser(ServiceBeanDef.class));
        registerBeanDefinitionParser("protocol", new ProtocolBeanDefNamespaceParser(ProtocolBeanDef.class));
        registerBeanDefinitionParser("ref", new RefBeanDefNamespaceParser(RefBeanDef.class));
    }
}
