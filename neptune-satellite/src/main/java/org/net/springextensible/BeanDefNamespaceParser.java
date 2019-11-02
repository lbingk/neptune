package org.net.springextensible;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


/**
 * spring 扩展：自定义xml配置解析类
 */
public class BeanDefNamespaceParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(RegistrationBeanDef.class);
        beanDef.setLazyInit(Boolean.FALSE);
        beanDef.getPropertyValues().add("ip", element.getAttribute("ip"));
        beanDef.getPropertyValues().add("port", element.getAttribute("port"));
        beanDef.getPropertyValues().add("timeout", element.getAttribute("timeout"));
        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();
        beanDefRegistry.registerBeanDefinition(RegistrationBeanDef.class.getName(), beanDef);
        return beanDef;
    }
}
