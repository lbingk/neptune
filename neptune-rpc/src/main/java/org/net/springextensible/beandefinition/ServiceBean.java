package org.net.springextensible.beandefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.net.invoke.InvokerBeanInfo;
import org.springframework.beans.BeansException;
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
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBean<T> implements InitializingBean, ApplicationContextAware {
    /**
     * 暴露的接口
     */
    private String interfaceName;
    private Class<?> interfaceClass;
    /**
     * 暴露的接口实现类
     */
    private T ref;
    private String timeout;
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        interfaceClass = Class.forName(interfaceName);
        InvokerBeanInfo.addServiceBeanExport(interfaceClass);
        // 需要存储在自定义的Map里面，无法从Spring上下文里面获取，因为不确定对应的Key是什么
        InvokerBeanInfo.putServiceBean(interfaceClass.getName(),this);
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
        ProtocolBean serviceProtocol = (ProtocolBean) applicationContext.getBean("serviceProtocol");
        if (serviceProtocol == null) {
            throw new RuntimeException("serviceProtocol 属性没有配置");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}