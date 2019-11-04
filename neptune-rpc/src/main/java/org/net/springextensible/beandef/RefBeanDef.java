package org.net.springextensible.beandef;

import lombok.Getter;
import lombok.Setter;
import org.net.collection.RefBeanInfo;
import org.net.springextensible.factory.RefProxyBeanFactory;
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
public class RefBeanDef<T> implements FactoryBean, InitializingBean {
    private String interfaceName;
    private int timeout;
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
        ref = RefProxyBeanFactory.createProxy(interfaceClass);
        return ref;
    }

    private Class<?> getInterfaceClass() {
        try {
            if (interfaceClass == null) {
                interfaceClass = Class.forName(interfaceName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return interfaceClass;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RefBeanInfo.getSingleton().addRefBeanDefinationClassName(interfaceName);
//        this.getObject();
    }
}
