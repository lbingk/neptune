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
public class BeanDefinitionNamespaceParser implements BeanDefinitionParser {

    private Class<?> clz;

    public BeanDefinitionNamespaceParser(Class<?> clz) {
        this.clz = clz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(clz);
        beanDef.setLazyInit(Boolean.FALSE);
        beanDef.getPropertyValues().add("port", element.getAttribute("port"));
        beanDef.getPropertyValues().add("timeout", element.getAttribute("timeout"));
        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();
        beanDefRegistry.registerBeanDefinition(clz.getName(), beanDef);
        return beanDef;
    }
}
