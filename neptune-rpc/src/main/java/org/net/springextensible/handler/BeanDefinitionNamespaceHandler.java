package org.net.springextensible.handler;

        import org.net.springextensible.beandefinition.ProtocolBean;
        import org.net.springextensible.beandefinition.ReferenceBean;
        import org.net.springextensible.beandefinition.RegistryBean;
        import org.net.springextensible.beandefinition.ServiceBean;
        import org.net.springextensible.parser.ProtocolBeanDefinitionNamespaceParser;
        import org.net.springextensible.parser.ReferenceBeanDefinitionNamespaceParser;
        import org.net.springextensible.parser.RegistryBeanDefinitionNamespaceParser;
        import org.net.springextensible.parser.ServiceBeanDefinitionNamespaceParser;
        import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 14:51
 */
public class BeanDefinitionNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionNamespaceParser(RegistryBean.class));
        registerBeanDefinitionParser("serviceProtocol", new ProtocolBeanDefinitionNamespaceParser("serviceProtocol", ProtocolBean.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionNamespaceParser(ServiceBean.class));
        registerBeanDefinitionParser("referenceProtocol", new ProtocolBeanDefinitionNamespaceParser("referenceProtocol", ProtocolBean.class));
        registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionNamespaceParser(ReferenceBean.class));
    }
}
