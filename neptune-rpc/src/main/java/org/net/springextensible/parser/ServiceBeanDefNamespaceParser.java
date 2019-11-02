package org.net.springextensible.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @program: neptune
 * @description: 暴露服务的Parser类
 * @author: luobingkai
 * @create: 2019-11-02 15:23
 */
public class ServiceBeanDefNamespaceParser implements BeanDefinitionParser {

    private Class<?> clz;

    public ServiceBeanDefNamespaceParser(Class<?> clz) {
        this.clz = clz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(clz);
        beanDef.setLazyInit(Boolean.FALSE);
        beanDef.getPropertyValues().add("service", element.getAttribute("service"));
        beanDef.getPropertyValues().add("ref", element.getAttribute("ref"));
        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();
        beanDefRegistry.registerBeanDefinition(clz.getName(), beanDef);
        return beanDef;
    }
}
