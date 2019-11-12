package org.net.springextensible.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 15:23
 */
public class ProtocolBeanDefinitionNamespaceParser implements BeanDefinitionParser {

    private Class<?> clz;
    private String id;

    public ProtocolBeanDefinitionNamespaceParser(String id, Class<?> clz) {
        this.clz = clz;
        this.id = id;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(clz);
        beanDef.setLazyInit(Boolean.FALSE);
        beanDef.getPropertyValues().add("ip", element.getAttribute("ip"));
        beanDef.getPropertyValues().add("port", element.getAttribute("port"));
        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();
        beanDefRegistry.registerBeanDefinition(this.id, beanDef);
        return beanDef;
    }
}
