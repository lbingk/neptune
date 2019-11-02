package org.net.springextensible;

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
        registerBeanDefinitionParser("server", new BeanDefNamespaceParser(RegistrationBeanDef.class));
    }
}
