package org.net.springextensible.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @program: neptune
 * @description: 订阅服务的Parser类
 * @author: LUOBINGKAI
 * @create: 2019-11-02 15:23
 */
public class ReferenceBeanDefinitionNamespaceParser implements BeanDefinitionParser {

    private Class<?> clz;

    public ReferenceBeanDefinitionNamespaceParser(Class<?> clz) {
        this.clz = clz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(clz);
        beanDefinition.setLazyInit(Boolean.FALSE);

        String interfaceName = element.getAttribute("interface");
        beanDefinition.getPropertyValues().add("interfaceName", interfaceName);

        // 每个配置ServiceBean对象的注册到容器BeanDefinitionMap的时候需要用Id来区分

        if (interfaceName != null && interfaceName.length() > 0) {
            if (parserContext.getRegistry().containsBeanDefinition(interfaceName)) {
                throw new IllegalStateException("Duplicate spring bean id " + interfaceName);
            }
        }

        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();

        beanDefRegistry.registerBeanDefinition(interfaceName, beanDefinition);
        return beanDefinition;
    }
}
