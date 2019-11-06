package org.net.springextensible.beandefinition;

import lombok.Getter;
import lombok.Setter;
import org.net.invoke.InvokerBeanInfo;
import org.net.invoke.ReferenceProxyBeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 15:42
 */
@Setter
@Getter
public class ReferenceBean<T> implements FactoryBean, InitializingBean {
    private String interfaceName;
    private int timeout;
    private int reties;
    private Class<?> interfaceClass;
    private Boolean init;
    private T ref;

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
        InvokerBeanInfo.addInvokerBeanExport(interfaceClass);
    }
}
