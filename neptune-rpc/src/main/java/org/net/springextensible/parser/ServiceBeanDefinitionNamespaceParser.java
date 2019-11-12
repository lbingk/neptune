package org.net.springextensible.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
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
public class ServiceBeanDefinitionNamespaceParser implements BeanDefinitionParser {

    private Class<?> clz;

    public ServiceBeanDefinitionNamespaceParser(Class<?> clz) {
        this.clz = clz;
    }


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(clz);
        beanDefinition.setLazyInit(Boolean.FALSE);

        String generatedBeanId = element.getAttribute("interface");

        // 每个配置ServiceBean对象的注册到容器BeanDefinitionMap的时候需要用Id来区分
        String id = generatedBeanId;
        int counter = 2;
        while (parserContext.getRegistry().containsBeanDefinition(id)) {
            id = generatedBeanId + (counter++);
        }
        if (generatedBeanId != null && generatedBeanId.length() > 0) {
            if (parserContext.getRegistry().containsBeanDefinition(generatedBeanId)) {
                throw new IllegalStateException("Duplicate spring bean id " + generatedBeanId);
            }
        }
        beanDefinition.getPropertyValues().add("interfaceName", generatedBeanId);

        String ref = element.getAttribute("ref");
        beanDefinition.getPropertyValues().add("ref", new RuntimeBeanReference(ref));

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        return beanDefinition;
    }
}
