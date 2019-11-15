package org.net.springextensible.parser;

import org.apache.commons.lang3.StringUtils;
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
        beanDef.getPropertyValues().add("timeout", element.getAttribute("timeout"));

        String serializeType = element.getAttribute("serializeType");
        if (!StringUtils.isBlank(serializeType)) {
            beanDef.getPropertyValues().add("serializeType", serializeType);
        }
        String executorType = element.getAttribute("executorType");
        if (!StringUtils.isBlank(executorType)) {
            beanDef.getPropertyValues().add("executorType", executorType);
        }
        String threadType = element.getAttribute("threadType");
        if (!StringUtils.isBlank(threadType)) {
            beanDef.getPropertyValues().add("threadType", threadType);
        }
        String threadNum = element.getAttribute("threadNum");
        if (!StringUtils.isBlank(threadNum)) {
            beanDef.getPropertyValues().add("threadNum", threadNum);
        }

        BeanDefinitionRegistry beanDefRegistry = parserContext.getRegistry();
        beanDefRegistry.registerBeanDefinition(this.id, beanDef);
        return beanDef;
    }
}
