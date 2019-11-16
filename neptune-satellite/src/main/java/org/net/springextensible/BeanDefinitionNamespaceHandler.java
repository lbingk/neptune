package org.net.springextensible;

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
        registerBeanDefinitionParser("server", new BeanDefinitionNamespaceParser(RegistrationBeanDefinition.class));
    }
}
