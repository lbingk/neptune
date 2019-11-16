package org.net.springextensible.beandefinition;

import lombok.Getter;
import lombok.Setter;
import org.net.invoke.InvokerBeanInfo;
import org.net.invoke.ReferenceProxyBeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 15:42
 */
@Setter
@Getter
public class ReferenceBean<T> implements FactoryBean, InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private String interfaceName;
    private int timeout;
    private int retries;
    private Class<?> interfaceClass;
    private Boolean init;
    private T ref;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getObject() throws Exception {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    private Object get() {
        ref = ReferenceProxyBeanFactory.createProxy(this);
        return ref;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        interfaceClass = Class.forName(interfaceName);
        InvokerBeanInfo.addReferenceBeanExport(interfaceClass);
        check();
    }

    /**
     * 检查配置：注册中心，暴露的端口
     *
     * @throws Exception
     */
    private void check() throws Exception {
        RegistryBean registryBean = applicationContext.getBean(RegistryBean.class);
        if (registryBean == null) {
            throw new RuntimeException("registry 属性没有配置");
        }
        ProtocolBean referenceProtocol = (ProtocolBean) applicationContext.getBean("referenceProtocol");
        if (referenceProtocol == null) {
            throw new RuntimeException("referenceProtocol 属性没有配置");
        }
    }

}
