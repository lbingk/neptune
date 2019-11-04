package org.net.springextensible.beandefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.net.springextensible.invoke.InvokerBeanInfo;
import org.springframework.beans.factory.InitializingBean;

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
public class ServiceBean<T> implements InitializingBean {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        interfaceClass = Class.forName(interfaceName);
        InvokerBeanInfo.addInvokerBeanExport(interfaceClass);
    }
}