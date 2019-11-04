package org.net.springextensible.parser;

import org.net.collection.ServiceBeanInfo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @program: neptune
 * @description: 暴露服务的Parser类
 * @author: LUOBINGKAI
 * @create: 2019-11-02 15:23
 */
public class ServiceBeanDefNamespaceParser implements BeanDefinitionParser {

    private Class<?> clz;

    public ServiceBeanDefNamespaceParser(Class<?> clz) {
        this.clz = clz;
    }

    public static ServiceBeanInfo serviceBeanInfo = ServiceBeanInfo.getSingleton();

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(clz);
        beanDef.setLazyInit(Boolean.FALSE);
        beanDef.getPropertyValues().add("service", element.getAttribute("service"));
        beanDef.getPropertyValues().add("ref", element.getAttribute("ref"));
        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();
        beanDefRegistry.registerBeanDefinition(clz.getName(), beanDef);

        serviceBeanInfo.addServiceBeanDefinationClassName(element.getAttribute("service"));

        return beanDef;
    }
}
